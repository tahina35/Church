package com.churchofphilippi.webserver.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "fcm_token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FCMToken {

    @Id
    @SequenceGenerator(
            name = "fcm_token_sequence",
            sequenceName = "fcm_token_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "fcm_token_sequence"
    )
    @Column(
            name = "fcm_token_id",
            updatable = false
    )
    private Long fcmTokenId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private AllMembers member;

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private String deviceId;
}
