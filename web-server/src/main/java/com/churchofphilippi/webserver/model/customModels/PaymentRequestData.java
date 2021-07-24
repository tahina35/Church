package com.churchofphilippi.webserver.model.customModels;

import com.churchofphilippi.webserver.model.ExpenseItem;
import com.churchofphilippi.webserver.model.Member;
import com.churchofphilippi.webserver.model.PaymentRequest;
import com.churchofphilippi.webserver.model.Receipt;
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
    private List<Member> signers;
    private List<ExpenseItem> items;
    private List<Receipt> receipts;
}
