package com.churchofphilippi.webserver.repository;

import com.churchofphilippi.webserver.model.PaymentRequest;
import com.churchofphilippi.webserver.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    List<Receipt> getReceiptByPaymentRequest(PaymentRequest paymentRequest);
}
