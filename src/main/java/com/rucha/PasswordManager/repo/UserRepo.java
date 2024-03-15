package com.rucha.PasswordManager.repo;

import com.rucha.PasswordManager.entity.Password;
import com.rucha.PasswordManager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {
    User findByEmail(String email);
    User findByEmailAndPassword(String email, String password);
}
