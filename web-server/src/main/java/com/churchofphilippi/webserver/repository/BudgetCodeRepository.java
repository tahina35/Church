package com.churchofphilippi.webserver.repository;

import com.churchofphilippi.webserver.model.BudgetCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BudgetCodeRepository extends JpaRepository<BudgetCode, Integer> {
}
