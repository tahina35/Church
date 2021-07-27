package com.churchofphilippi.webserver.controller;

import com.churchofphilippi.webserver.model.customModels.PaymentRequestData;
import com.churchofphilippi.webserver.service.BudgetCodeService;
import com.churchofphilippi.webserver.service.PaymentRequestService;
import com.churchofphilippi.webserver.service.SignatureService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/number-of-pr-waiting-for-signature/member/{id}")
    public ResponseEntity<?> getNbOfPRWaitingForSignature(@PathVariable("id")Long id) {
        return ResponseEntity.ok(signatureService.numberOfPrWaitingForSignature(id));
    }

    @GetMapping("/pr-waiting-for-signature/member/{id}")
    public ResponseEntity<?> getPRWaitingForSignature(@PathVariable("id")Long id) {
        return ResponseEntity.ok(paymentRequestService.getPrWaitingForSignature(id));
    }
}
