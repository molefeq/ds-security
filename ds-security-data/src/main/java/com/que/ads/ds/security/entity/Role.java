package com.que.ads.ds.security.entity;

import lombok.Getter;
import lombok.Setter;
import jakarta.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "role", schema = "que_ads")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 50, nullable = false, unique = false)
    private String name;

    @Column(name = "code", length = 50, nullable = false, unique = true)
    private String code;
}
