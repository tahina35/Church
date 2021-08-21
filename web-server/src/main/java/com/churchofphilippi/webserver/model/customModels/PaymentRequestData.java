package com.churchofphilippi.webserver.model.customModels;

import com.churchofphilippi.webserver.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestData {

    private PaymentRequest paymentRequest;
    private List<ExpenseItem> items;
    private List<Receipt> receipts;
    private List<Signature> signatures;
}
