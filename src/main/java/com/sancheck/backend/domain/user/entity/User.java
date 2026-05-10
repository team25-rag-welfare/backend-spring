package com.sancheck.backend.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    private String userId;

    @Column(name = "userAge")
    private int userAge;

    @Column(name = "isPregnant")
    private boolean isPregnant;

    @Column(name = "ChildAge")
    private int ChildAge;
}
