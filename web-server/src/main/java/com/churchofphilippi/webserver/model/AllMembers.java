package com.churchofphilippi.webserver.model;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "all_members")
@Getter
@Setter
@NoArgsConstructor
public class AllMembers implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "all_members_sequence",
            sequenceName = "all_members_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "all_members_sequence"
    )
    private long memberId;

    @Column(
            length = 100,
            name ="efname"
    )
    private String efname;

    @Column(
            length = 100,
            name ="elname"
    )
    private String elname;

    @Column(
            length = 100,
            name ="emname"
    )
    private String emname;

    @Column(
            length = 100,
            name ="kfname"
    )
    private String kfname;

    @Column(
            length = 100,
            name ="klname"
    )
    private String klname;

    private String firstname;
    private String lastname;
    private String birthdate;
    private String anniversary;

    @Column(length = 1)
    private String gender;

    private String grade;
    private String school;
    private String medicalnotes;
    private String child;
    private String maritalstatus;
    private String status;
    private String membership;
    private String inactivereason;
    private String inactivedate;
    private String campusname;

    private String homeaddressstreetline1;
    private String homeaddressstreetline2;
    private String homeaddresscity;
    private String homeaddressstate;
    private String homeaddresszipcode;
    private String mobilephonenumber;
    private String homephonenumber;
    private String homeemail;

    private LocalDate memberDate;
    private Boolean active = true;
    private Boolean admin = false;
    private String password;

    @Column(columnDefinition="TEXT")
    private String signature;

    private String accessToken;

    private String householdid;
    private String householdname;
    private String householdprimarycontact;
    private String communityRegistrationdate;
    private String communityLevel;
    private String communityConnected;
    private String communityOutofstate;
    private String communityBaptism;
    private String community_직업;
    private String communityBusinessnamecard;
    private String koreancongregation_목장;
    private String koreancongregationSrbleader;
    private String koreancongregation_사랑방현재;
    private String koreancongregation_사랑방활동;
    private String koreancongregation_사랑방연결날짜;
    private String koreancongregation_연결가능성;
    private String koreancongregation_기타정보;
    private String k_discipleship_바나바;
    private String k_discipleship_디스커버리discovery;
    private String k_discipleship_파운데이션foundation;
    private String k_discipleship_만사mansa;
    private String k_discipleship_만사섬김이;
    private String k_discipleship_파노라마panorama;
    private String k_discipleship_코너스톤cornerstone;
    private String k_discipleship_파운더리foundry;
    private String k_discipleship_파트너십partnership;
    private String k_discipleship_컴미션commission;
    private String k_discipleship_성장반;
    private String k_discipleship_제자반;
    private String k_discipleship_컴미션2;
    private String k_discipleship_파노라마2_선지서_;
    private String k_discipleship_다이아몬드복음전도_전도폭발;
    private String k_discipleship_온라인1기_may_june2020_;
    private String k_discipleship_온라인2기_july_august2020_;
    private String k_discipleship_직위;
    private String k_discipleship_진행중만사형제_자매이름;
    private String k_discipleship_진행중만사시작일;
    private String k_discipleship_진행중만사진도;
    private String k_discipleship_진행중만사종료일;
    private String k_5ministries_새가족연결;
    private String k_5ministries_전도팀;
    private String k_5ministries_시작일;
    private String k_5ministries_직위;
    private String k_5ministries_종료일;
    private String k_otherministries_행정직위;
    private String k_otherministries_업무시작일;
    private String k_otherministries_업무종료일;
    private String k_otherministries_기타정보;
    private String k_otherministries_특별위원회;
    private String k_otherministries_위원장;
    private String k_otherministries_업무시작일_2_;
    private String k_otherministries_업무종료일_2_;
    private String k_otherministries_taskforces;
    private String k_otherministries_사역팀장;
    private String k_otherministries_업무시작일_2__3_;
    private String k_otherministries_업무종료일_2__3_;
    private String k_목자_목장목자;
    private String k_목자_목장목자임명일;
    private String k_목자_목양현재상태;
    private String k_목자_목장목자사역종료일;
    private String k_목자_사랑방목자;
    private String k_목자_사랑방목자임명일;
    private String k_목자_목양현재상태_2_;
    private String k_목자_reasoninactive;
    private String k_목자_사랑방목자사역종료일;
    private String englishcongregationSrbSmallgroup;
    private String englishcongregationDiscoveryclassDatecompleted;
    private String englishcongregationFoundationclassDatecompleted;
    private String englishcongregationMansaDatecompleted;
    private String englishcongregationMembershipcovenant;
    private String englishcongregationMiscclasses;
    private String 청년부_smallgroupleader;
    private String child_prek_12__smallgroupleader;
    private String child_prek_12__grade;
    private String churchstaff_직임;
    private String churchstaff_직급;
    private String churchstaff_사역시작일;
    private String churchstaff_사역종료일;
    private String churchstaff_부서;
    private String churchstaff_기타정보;
    private String ename1;
    private String ename2;

    public AllMembers(String kfname, String klname, String gender, LocalDate memberDate, String homeaddressstreetline1, String homeaddressstreetline2, String city, String state, String zipCode, String email, String phoneNumber, String password) {
        this.kfname = kfname;
        this.klname = klname;
        this.gender = gender;
        this.memberDate = memberDate;
        this.homeaddressstreetline1 = homeaddressstreetline1;
        this.homeaddressstreetline2 = homeaddressstreetline2;
        this.homeaddresscity = city;
        this.homeaddressstate = state;
        this.homeaddresszipcode = zipCode;
        this.homeemail = email;
        this.homephonenumber = phoneNumber;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new HashSet<GrantedAuthority>();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return homeemail;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
