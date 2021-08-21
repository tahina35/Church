package com.churchofphilippi.webserver.repository;

import com.churchofphilippi.webserver.model.Member;
import com.churchofphilippi.webserver.model.PaymentRequest;
import com.churchofphilippi.webserver.model.Signature;
import com.churchofphilippi.webserver.model.keys.SignatureKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SignatureRepository extends JpaRepository<Signature, SignatureKey> {

    @Query(value = "SELECT COUNT(s) FROM Signature s WHERE s.member.memberId = ?1 AND s.hasSigned = false ")
    int numberOfPrWaitingForSignature(Long id);

    @Query(value = "SELECT COUNT(s) FROM Signature s WHERE s.paymentRequest.paymentRequestId = ?1 ")
    int numberOfSignaturesNeeded(Long id);

    List<Signature> getSignatureByPaymentRequest(PaymentRequest paymentRequest);


}
