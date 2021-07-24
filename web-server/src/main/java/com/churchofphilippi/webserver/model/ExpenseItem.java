package com.churchofphilippi.webserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "expense_item")
@Getter
@Setter
@NoArgsConstructor
public class ExpenseItem {

    @Id
    @SequenceGenerator(
            name = "expense_item_sequence",
            sequenceName = "expense_item_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "expense_item_sequence"
    )
    private Long expenseItemID;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String detailedPurpose;

    @Column(nullable = false)
    private double amount;

    private String remarks;

    @Column(nullable = false)
    private LocalDate purchaseDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "code")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BudgetCode budgetCode;
}
