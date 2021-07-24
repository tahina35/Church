package com.churchofphilippi.webserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "check_details")
@Getter
@Setter
@NoArgsConstructor
public class CheckDetails {

    @Id
    @SequenceGenerator(
            name = "check_details_sequence",
            sequenceName = "check_details_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "check_details_sequence"
    )
    private Long checkDetailsID;

    @Column(nullable = false)
    private String imageLink;

    @Column(nullable = false)
    private String checkNumber;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDate issuingDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Member issuedBy;
}
