package com.churchofphilippi.webserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "member")
@Getter
@Setter
@NoArgsConstructor
public class Member implements UserDetails {

    @Id
    @SequenceGenerator(
            name = "member_sequence",
            sequenceName = "member_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "member_sequence"
    )
    private Long memberId;

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

    @Column(length = 1)
    private String gender;

    private LocalDate memberDate;

    private Boolean active = true;

    @Column(length = 150)
    private String streetName;

    @Column(length = 150)
    private String streetAddress;

    @Column(length = 5)
    private String aptNumber;

    @Column(length = 50)
    private String city;

    @Column(length = 2)
    private String state;

    private Integer zipCode;

    private String email;

    @Column(length = 10)
    private String phoneNumber;

    private Boolean admin = false;

    private String password;

    @Column(columnDefinition="TEXT")
    private String signature;

    private String accessToken;

    public Member(String kfname, String klname, String gender, LocalDate memberDate, String streetName, String streetAddress, String aptNumber, String city, String state, int zipCode, String email, String phoneNumber, String password) {
        this.kfname = kfname;
        this.klname = klname;
        this.gender = gender;
        this.memberDate = memberDate;
        this.streetName = streetName;
        this.streetAddress = streetAddress;
        this.aptNumber = aptNumber;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.email = email;
        this.phoneNumber = phoneNumber;
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
        return email;
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
        return active;
    }
}
