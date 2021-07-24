package com.churchofphilippi.webserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transaction")
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @SequenceGenerator(
            name = "transaction_sequence",
            sequenceName = "transaction_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "transaction_sequence"
    )
    private Long transactionID;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private double expenseAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_request_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PaymentRequest paymentRequest;
}
