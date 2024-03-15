package com.rucha.PasswordManager.vo;

import jakarta.persistence.Column;
import lombok.Data;

@Data
public class RegisterUserVO {
    private String fullName;
    private String email;
    private String password;
}
