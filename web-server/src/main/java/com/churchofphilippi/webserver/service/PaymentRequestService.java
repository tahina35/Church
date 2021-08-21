package com.churchofphilippi.webserver.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.churchofphilippi.webserver.config.GeneralConfig;
import com.churchofphilippi.webserver.config.PaymentRequestConfig;
import com.churchofphilippi.webserver.config.StorageConfig;
import com.churchofphilippi.webserver.exception.exceptionModel.NewFormException;
import com.churchofphilippi.webserver.model.*;
import com.churchofphilippi.webserver.model.customModels.PaymentRequestData;
import com.churchofphilippi.webserver.model.keys.PrExpenseItemKey;
import com.churchofphilippi.webserver.model.keys.SignatureKey;
import com.churchofphilippi.webserver.repository.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.juli.logging.Log;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class PaymentRequestService implements BaseService<PaymentRequest> {

    private final PaymentRequestRepository paymentRequestRepository;
    private final AllMembersRepository memberRepository;
    private final ExpenseItemRepository expenseItemRepository;
    private final PRExpenseItemRepository prExpenseItemRepository;
    private final ReceiptRepository receiptRepository;
    private final SignatureRepository signatureRepository;
    private final GeneralConfig generalConfig;
    private final PaymentRequestConfig paymentRequestConfig;
    private final StorageConfig storageConfig;
    private final AmazonS3 s3Client;

    @Override
    public List<PaymentRequest> getAll() {
        return paymentRequestRepository.findAll();
    }

    @Override
    public PaymentRequest save(PaymentRequest entity) {
        return paymentRequestRepository.save(entity);
    }

    @Override
    public void delete(PaymentRequest entity) {
        paymentRequestRepository.delete(entity);
    }

    public PaymentRequest getById(Long prID) {
        return paymentRequestRepository.findByPaymentRequestId(prID).orElseThrow();
    }

    public PaymentRequestData getPaymentRequestById(Long prID) {
        PaymentRequestData prData = new PaymentRequestData();
        PaymentRequest pr = getById(prID);
        prData.setPaymentRequest(pr);
        prData.setSignatures(signatureRepository.getSignatureByPaymentRequest(pr));
        prData.setItems(prExpenseItemRepository.getExpenseItems(prID));
        prData.setReceipts(receiptRepository.getReceiptByPaymentRequest(pr));
        return  prData;
    }

    @Transactional
    public void createPR(PaymentRequestData data) {
        try {
            PaymentRequest request = data.getPaymentRequest();
            List<Signature> signatures = data.getSignatures();
            memberRepository.save(request.getRequestor());
            request.setStatus(paymentRequestConfig.getNeedSignature());
            paymentRequestRepository.save(request);
            saveExpenseItems(data.getItems(), request);
            saveReceipts(data.getReceipts(), request);
            addSignatures(signatures, request);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NewFormException("An unexpected error happened. The payment request was not saved");
        }
    }

    public void saveExpenseItems(List<ExpenseItem> items, PaymentRequest request) {
        for (int i = 0; i < items.size(); i++) {
            ExpenseItem item = expenseItemRepository.save(items.get(i));
            prExpenseItemRepository.save(new PrExpenseItem(new PrExpenseItemKey(), item, request));
        }
    }

    public void saveReceipts(List<Receipt> receipts, PaymentRequest request) {
        for (int i = 0; i < receipts.size(); i++) {
            receipts.get(i).setPaymentRequest(request);
            receipts.get(i).setUrl(generalConfig.getWebAppBaseUrl() + "/receipts/" + receipts.get(i).getName());
            receiptRepository.save(receipts.get(i));
        }
    }

    public void uploadFiles(List<MultipartFile> files) {
        for(MultipartFile f : files) {
            File fileObj = convertMultipartFile(f);
            String fileName = storageConfig.getFolder() + System.currentTimeMillis() + "_" + f.getOriginalFilename();
            s3Client.putObject(new PutObjectRequest(storageConfig.getBucketName(), fileName, fileObj));
            fileObj.delete();
        }

    }

    public File convertMultipartFile(MultipartFile file) {
        File convertedFile = new File(file.getOriginalFilename());
        try {
            FileOutputStream fos = new FileOutputStream(convertedFile);
            fos.write(file.getBytes());
        } catch (IOException e) {
            log.error("Error converting multipartFile to file", e);
        }

        return convertedFile;
    }

    public void addSignatures(List<Signature> signatures, PaymentRequest request) {
        Signature requestorSignature = new Signature(new SignatureKey(null, null, 1), request.getRequestor(), request, true, LocalDate.now());
        Signature directorSignature = new Signature(new SignatureKey(null, null, 2), signatures.get(0).getMember(), request, false, null);
        Signature pastorSignature = new Signature(new SignatureKey(null, null, 3), signatures.get(1).getMember(), request, false, null);
        List<Signature> res = new ArrayList<Signature>(Arrays.asList(requestorSignature, directorSignature, pastorSignature));
        Signature teamLeaderSignature = new Signature();
        if(signatures.get(2).getMember() != null) {
            teamLeaderSignature = new Signature(new SignatureKey(null, null, 4), signatures.get(2).getMember(), request, false, null);
            res.add(teamLeaderSignature);
        }
        for(Signature s : res) {
            signatureRepository.save(s);
        }
    }

    public List<PaymentRequestData> getPrWaitingForSignature(Long memberId) {
        List<PaymentRequestData> data = new ArrayList<PaymentRequestData>();
        List<PaymentRequest> paymentRequests = paymentRequestRepository.getPrWaitingForSignature(memberId);
        for(PaymentRequest pr : paymentRequests) {
            PaymentRequestData prData = new PaymentRequestData();
            prData.setPaymentRequest(pr);
            List<ExpenseItem> items = prExpenseItemRepository.getExpenseItems(pr.getPaymentRequestId());
            prData.setItems(items);
            data.add(prData);
        }
        return data;
    }

    public List<PaymentRequestData> getPrWaitingForPayment() {
        List<PaymentRequestData> data = new ArrayList<PaymentRequestData>();
        List<PaymentRequest> paymentRequests = paymentRequestRepository.getAllByStatusEquals(paymentRequestConfig.getNeedPayment());
        for(PaymentRequest pr : paymentRequests) {
            PaymentRequestData prData = new PaymentRequestData();
            prData.setPaymentRequest(pr);
            List<ExpenseItem> items = prExpenseItemRepository.getExpenseItems(pr.getPaymentRequestId());
            prData.setItems(items);
            data.add(prData);
        }
        return data;
    }

    @Transactional
    public PaymentRequest signPR(Signature signature) {
        PaymentRequest pr = signature.getPaymentRequest();
        signatureRepository.save(signature);
        memberRepository.save(signature.getMember());
        List<Signature> signatures = signatureRepository.getSignatureByPaymentRequest(signature.getPaymentRequest());
        int count = 0;
        for ( Signature s : signatures)  {
            if(s.isHasSigned()) {
                count++;
            }
        }

        if (count == signatures.size()) {
            pr.setStatus(paymentRequestConfig.getNeedPayment());
            paymentRequestRepository.save(pr);
        }

        return pr;
    }

    public int getNoPrWaitingForPayment() {
        return paymentRequestRepository.getPrWaitingForPayment();
    }





}
