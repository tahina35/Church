package com.churchofphilippi.webserver.repository;

import com.churchofphilippi.webserver.model.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReceiptRepository extends JpaRepository<Receipt, Long> {
}
