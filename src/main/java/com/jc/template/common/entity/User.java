package com.jc.template.common.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    String name;

    @Column(name = "user_id")
    String userId;

    @Column(name = "password")
    String password;

    @Column(name = "email")
    String email;

    @Column(name = "mobile_number")
    String mobileNumber;

    @Column(name = "address", length = 1000)
    String address;

    //
    @Column(name = "dob")
    String dob;

    @Column(name = "role")
    String role;

    @Column(name = "status")
    String status;

    @Column(name = "jwt_token")
    String jwtToken;

    private boolean enabled;
    private String verificationCode;

    private boolean accountNonLocked;
    private int failedAttempts;
    private Date lockTime;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;
}
