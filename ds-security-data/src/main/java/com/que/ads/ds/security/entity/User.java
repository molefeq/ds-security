package com.que.ads.ds.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;

@Entity
@Getter
@Setter
@Table(name = "user", schema = "que_ads")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 100, nullable = false, unique = false)
    private String name;

    @Column(name = "last_name", length = 100, nullable = true, unique = false)
    private String lastName;

    @Column(name = "email_address", length = 200, nullable = false, unique = false)
    private String emailAddress;

    @Column(name = "username", length = 100, nullable = false, unique = true)
    private String userName;

    @Column(name = "password", length = 20, nullable = false, unique = false)
    private String password;

    @Column(name = "forgot_password_key", length = 10, nullable = true, unique = false)
    private String forgotPasswordKey;

    @Column(name = "last_login_date", nullable = true)
    private Timestamp lastLoginDate;

    @Column(name = "create_date", nullable = false)
    private Timestamp createDate;

    @OneToOne
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}
