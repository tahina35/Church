package com.churchofphilippi.webserver.repository;

import com.churchofphilippi.webserver.model.ExpenseItem;
import com.churchofphilippi.webserver.model.PrExpenseItem;
import com.churchofphilippi.webserver.model.keys.PrExpenseItemKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PRExpenseItemRepository extends JpaRepository<PrExpenseItem, PrExpenseItemKey> {

    @Query(value = "SELECT prei.item FROM PrExpenseItem prei WHERE prei.paymentRequest.paymentRequestId = ?1")
    List<ExpenseItem> getExpenseItems(long prId);
}
