package com.churchofphilippi.webserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "department_budget")
@Getter
@Setter
@NoArgsConstructor
public class DepartmentBudget {

    @Id
    @Column(nullable = false)
    private int year;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BudgetCode budgetCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Dept department;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private String remrark;
}
