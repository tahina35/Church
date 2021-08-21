package com.churchofphilippi.webserver.controller;

import com.churchofphilippi.webserver.model.Signature;
import com.churchofphilippi.webserver.model.customModels.PaymentRequestData;
import com.churchofphilippi.webserver.service.BudgetCodeService;
import com.churchofphilippi.webserver.service.PaymentRequestService;
import com.churchofphilippi.webserver.service.SignatureService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/payment-request")
public class PaymentRequestController {

    private final PaymentRequestService paymentRequestService;
    private final BudgetCodeService budgetCodeService;
    private final SignatureService signatureService;

    @GetMapping("/budget-code")
    public ResponseEntity<?> getBudgetCode() {
        return ResponseEntity.ok(budgetCodeService.getAll());
    }

    @PostMapping("/new-form")
    public void createNewPaymentRequest(@RequestBody PaymentRequestData data) {
        paymentRequestService.createPR(data);
    }

    @PostMapping("/upload-documents")
    public ResponseEntity<?> uploadDocuments(@RequestParam("files") List<MultipartFile> files) {
        paymentRequestService.uploadFiles(files);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/number-of-pr-waiting-for-signature/member/{id}")
    public ResponseEntity<?> getNbOfPRWaitingForSignature(@PathVariable("id")Long id) {
        return ResponseEntity.ok(signatureService.numberOfPrWaitingForSignature(id));
    }

    @GetMapping("/number-of-pr-waiting-for-payment")
    public ResponseEntity<?> getNbOfPRWaitingForPayment() {
        return ResponseEntity.ok(paymentRequestService.getNoPrWaitingForPayment());
    }

    @GetMapping("/pr-waiting-for-signature/member/{id}")
    public ResponseEntity<?> getPRWaitingForSignature(@PathVariable("id")Long id) {
        return ResponseEntity.ok(paymentRequestService.getPrWaitingForSignature(id));
    }

    @GetMapping("/pr-waiting-for-payment")
    public ResponseEntity<?> getPrWaitingForPayment() {
        return ResponseEntity.ok(paymentRequestService.getPrWaitingForPayment());
    }

    @GetMapping("/pr-{id}")
    public ResponseEntity<?> getPrById(@PathVariable("id") Long prId) {
        return ResponseEntity.ok(paymentRequestService.getPaymentRequestById(prId));
    }

    @PostMapping("/sign")
    public ResponseEntity<?> signPR(@RequestBody Signature signature) {
        return ResponseEntity.ok(paymentRequestService.signPR(signature));
    }
}
