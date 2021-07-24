package com.churchofphilippi.webserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dept")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dept {
    @Id
    @SequenceGenerator(
            name = "dept_sequence",
            sequenceName = "dept_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "dept_sequence"
    )
    @Column(
            name = "dept_id",
            updatable = false
    )
    private Long deptId;

    @Column(
            nullable = false,
            length = 50
    )
    private String name;

    @Column(nullable = false)
    private LocalDate creationDate;

    @Column(nullable = true)
    private int deptCode;

    @Column(name = "is_parent")
    private boolean isParent;

    @ManyToOne
    @JoinColumn(name="parent_dept_id")
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private Dept parentDept;

    @ManyToOne
    @JoinColumn(name="member_id")
    @JsonIgnoreProperties(value = {"applications", "hibernateLazyInitializer"})
    private Member leader;
    
}
