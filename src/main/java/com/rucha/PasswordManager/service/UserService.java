package com.rucha.PasswordManager.service;

import com.rucha.PasswordManager.entity.User;
import com.rucha.PasswordManager.vo.LoginUserVO;
import com.rucha.PasswordManager.vo.RegisterUserVO;
import com.rucha.PasswordManager.vo.ResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    ResponseStatus registerUser(RegisterUserVO registerUserVO) throws Exception;
    ResponseStatus loginUser(LoginUserVO loginUserVO, HttpServletResponse response);
}
