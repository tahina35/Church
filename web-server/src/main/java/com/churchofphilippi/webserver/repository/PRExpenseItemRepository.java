package com.churchofphilippi.webserver.repository;

import com.churchofphilippi.webserver.model.PrExpenseItem;
import com.churchofphilippi.webserver.model.keys.PrExpenseItemKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PRExpenseItemRepository extends JpaRepository<PrExpenseItem, PrExpenseItemKey> {
}
