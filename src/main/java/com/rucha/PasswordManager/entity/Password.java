package com.rucha.PasswordManager.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "Password")
public class Password {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Id")
    private Long id;

    @Column(name = "UserId")
    private Long userId;

    @Column(name = "AccountName")
    private String accountName;

    @Column(name = "AccountUsername")
    private String accountUsername;

    @Column(name = "AccountPassword")
    private String accountPassword;

    @Column(name = "SaltPassword")
    private String saltPassword;
}
