package com.churchofphilippi.webserver.model;

import com.churchofphilippi.webserver.model.keys.RoleKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {

    @EmbeddedId
    private RoleKey id = new RoleKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(
            name = "member_id",
            nullable = false
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AllMembers member;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("positionId")
    @JoinColumn(
            name = "position_id",
            nullable = false
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Position position;

    @Column(nullable = false)
    private LocalDate startDate;
    
    private LocalDate endDate;

    private String remark;

}
