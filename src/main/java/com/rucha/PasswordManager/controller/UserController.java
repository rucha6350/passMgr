package com.rucha.PasswordManager.controller;

import com.rucha.PasswordManager.impl.UserServiceImpl;
import com.rucha.PasswordManager.vo.LoginUserVO;
import com.rucha.PasswordManager.vo.RegisterUserVO;
import com.rucha.PasswordManager.vo.ResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceImpl userService;

    @PostMapping("/registerUser")
    @SneakyThrows
    public ResponseStatus registerUser(@RequestBody RegisterUserVO registerUserVO){
        return userService.registerUser(registerUserVO);
    }

    @GetMapping("/loginUser")
    @SneakyThrows
    public ResponseStatus loginUser(@RequestBody LoginUserVO loginUserVO, HttpServletResponse response){
        return userService.loginUser(loginUserVO,response);
    }
}
