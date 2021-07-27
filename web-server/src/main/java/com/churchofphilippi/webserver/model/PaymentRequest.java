package com.churchofphilippi.webserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payment_request")
@Getter
@Setter
@NoArgsConstructor
public class PaymentRequest {

    @Id
    @SequenceGenerator(
            name = "pr_sequence",
            sequenceName = "pr_sequence",
                allocationSize = 1
        )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "pr_sequence"
    )
    private Long paymentRequestId;

    @Column(nullable = false)
    private int category;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(nullable = false)
    private String requestorAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Member requestor;

    @Column(nullable = false)
    private String status;

    private LocalDate paymentDate;

    private boolean paidByCreditCard = false;

    private boolean paidByCheck = false;

    private boolean paidByCash = false;

    @Column(nullable = false)
    private String payableTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Dept department;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="checkDetails_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private CheckDetails checkDetails;
}
