package com.churchofphilippi.webserver.repository;

import com.churchofphilippi.webserver.model.PaymentRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRequestRepository extends JpaRepository<PaymentRequest, Long> {
}
