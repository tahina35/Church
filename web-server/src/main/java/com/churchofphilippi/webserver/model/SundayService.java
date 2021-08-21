package com.churchofphilippi.webserver.model;

import com.churchofphilippi.webserver.model.keys.SundayServicekey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sunday_service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SundayServicekey.class)
public class SundayService {

    @Id
    @Column(nullable = false)
    private int serviceType;

    @Id
    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String worshipAndPraise;

    @Column(nullable = false)
    private String confessionOfFaith;

    @Column(nullable = false)
    private String churchNews;

    @Column(nullable = false)
    private String offering;

    @Column(
            nullable = false,
            length = 150
    )
    private String hymn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AllMembers prayer;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="sermon_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Sermon sermon;

}
