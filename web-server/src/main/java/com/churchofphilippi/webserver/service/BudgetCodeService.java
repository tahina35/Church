package com.churchofphilippi.webserver.service;

import com.churchofphilippi.webserver.model.BudgetCode;
import com.churchofphilippi.webserver.repository.BudgetCodeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BudgetCodeService implements BaseService<BudgetCode>{

    private final BudgetCodeRepository budgetCodeRepository;

    @Override
    public List<BudgetCode> getAll() {
        return budgetCodeRepository.findAll();
    }

    @Override
    public BudgetCode save(BudgetCode entity) {
        return budgetCodeRepository.save(entity);
    }

    @Override
    public void delete(BudgetCode entity) {
        budgetCodeRepository.delete(entity);
    }
}
