package com.churchofphilippi.webserver.controller;

import com.churchofphilippi.webserver.model.customModels.PaymentRequestData;
import com.churchofphilippi.webserver.service.BudgetCodeService;
import com.churchofphilippi.webserver.service.PaymentRequestService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/payment-request")
public class PaymentRequestController {

    private final PaymentRequestService paymentRequestService;
    private final BudgetCodeService budgetCodeService;

    @GetMapping("/budget-code")
    public ResponseEntity<?> getBudgetCode() {
        return ResponseEntity.ok(budgetCodeService.getAll());
    }

    @PostMapping("/new-form")
    public void createNewPaymentRequest(@RequestBody PaymentRequestData data) {
        paymentRequestService.createPR(data);
    }
}
