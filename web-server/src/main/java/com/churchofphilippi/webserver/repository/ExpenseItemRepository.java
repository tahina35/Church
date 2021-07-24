package com.churchofphilippi.webserver.repository;

import com.churchofphilippi.webserver.model.ExpenseItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseItemRepository extends JpaRepository<ExpenseItem, Long> {
}
