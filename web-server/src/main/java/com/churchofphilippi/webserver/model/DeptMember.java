package com.churchofphilippi.webserver.model;

import com.churchofphilippi.webserver.model.keys.DeptMemberKey;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "dept_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeptMember {

    @EmbeddedId
    private DeptMemberKey id = new DeptMemberKey();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("deptId")
    @JoinColumn(
            name = "dept_id",
            nullable = false
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Dept department;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("memberId")
    @JoinColumn(
            name = "member_id",
            nullable = false
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AllMembers member;

    @Column(
            nullable = false
    )
    private LocalDate startDate;

    private LocalDate endDate;
    private String remark;

}
