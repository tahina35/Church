package com.churchofphilippi.webserver.model;

import com.churchofphilippi.webserver.model.keys.SignatureKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "signature")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Signature {

    @EmbeddedId
    private SignatureKey id = new SignatureKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(name = "member_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AllMembers member;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("paymentRequestId")
    @JoinColumn(
            name = "payment_request_id",
            nullable = false
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private PaymentRequest paymentRequest;

    @Column(nullable = false)
    private boolean hasSigned;

    private LocalDate signedDate;

}
