package com.rucha.PasswordManager.repo;

import com.rucha.PasswordManager.entity.Password;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PasswordRepo extends JpaRepository<Password, Long> {
    List<Password> findAllByUserId(Long userId, Pageable pageable);
}
