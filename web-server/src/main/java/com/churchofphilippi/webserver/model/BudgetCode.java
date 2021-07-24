package com.churchofphilippi.webserver.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "budget_code")
@Getter
@Setter
@NoArgsConstructor
public class BudgetCode {

    @Id
    @Column(nullable = false)
    private int code;

    @Column(nullable = false)
    private String description;
}
