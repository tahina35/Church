package com.churchofphilippi.webserver.model;

import com.churchofphilippi.webserver.model.keys.PrExpenseItemKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "pr_expense_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrExpenseItem {

    @EmbeddedId
    private PrExpenseItemKey prExpenseItemKey = new PrExpenseItemKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("expenseItemID")
    @JoinColumn(
            name = "expense_item_id",
            nullable = false
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ExpenseItem item;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("paymentRequestId")
    @JoinColumn(
            name = "payment_request_id",
            nullable = false
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PaymentRequest paymentRequest;
}
