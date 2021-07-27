package com.churchofphilippi.webserver.repository;

import com.churchofphilippi.webserver.model.Member;
import com.churchofphilippi.webserver.model.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Long> {

    @Query(value = "SELECT s.paymentRequest FROM Signature s WHERE s.member.memberId = ?1 AND s.hasSigned = false ")
    List<PaymentRequest> getPrWaitingForSignature(Long id);
}
