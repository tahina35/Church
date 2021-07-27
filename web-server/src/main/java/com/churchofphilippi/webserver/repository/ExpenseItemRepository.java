package com.churchofphilippi.webserver.repository;

import com.churchofphilippi.webserver.model.ExpenseItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ExpenseItemRepository extends JpaRepository<ExpenseItem, Long> {

}
