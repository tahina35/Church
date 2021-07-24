package com.churchofphilippi.webserver.service;

import com.churchofphilippi.webserver.config.GeneralConfig;
import com.churchofphilippi.webserver.config.PaymentRequestConfig;
import com.churchofphilippi.webserver.exception.exceptionModel.NewFormException;
import com.churchofphilippi.webserver.model.*;
import com.churchofphilippi.webserver.model.customModels.PaymentRequestData;
import com.churchofphilippi.webserver.model.keys.PrExpenseItemKey;
import com.churchofphilippi.webserver.model.keys.SignatureKey;
import com.churchofphilippi.webserver.repository.*;
import lombok.AllArgsConstructor;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class PaymentRequestService implements BaseService<PaymentRequest> {

    private final PaymentRequestRepository paymentRequestRepository;
    private final MemberRepository memberRepository;
    private final ExpenseItemRepository expenseItemRepository;
    private final PRExpenseItemRepository prExpenseItemRepository;
    private final ReceiptRepository receiptRepository;
    private final SignatureRepository signatureRepository;
    private final GeneralConfig generalConfig;
    private final PaymentRequestConfig paymentRequestConfig;

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

    @Transactional
    public void createPR(PaymentRequestData data) {
        try {
            PaymentRequest request = data.getPaymentRequest();
            List<Member> signers = data.getSigners();
            List<Receipt> receipts = data.getReceipts();
            memberRepository.save(request.getRequestor());
            request.setStatus(paymentRequestConfig.getNeedSignature());
            request = paymentRequestRepository.save(request);
            saveExpenseItems(data.getItems(), request);
            saveReceipts(receipts, request);
            addSignatures(signers, request);
        } catch (Exception e) {
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

    public void addSignatures(List<Member> signers, PaymentRequest request) {
        Signature requestorSignature = new Signature(new SignatureKey(), request.getRequestor(), request, true, LocalDate.now(), 1);
        Signature directorSignature = new Signature(new SignatureKey(), signers.get(0), request, false, null, 2);
        Signature pastorSignature = new Signature(new SignatureKey(), signers.get(1), request, false, null, 3);
        List<Signature> signatures = new ArrayList<Signature>(Arrays.asList(requestorSignature, directorSignature, pastorSignature));
        for(Signature s : signatures) {
            signatureRepository.save(s);
        }
    }
}
