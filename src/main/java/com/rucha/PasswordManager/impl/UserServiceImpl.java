package com.rucha.PasswordManager.impl;

import com.rucha.PasswordManager.constants.JWTConstants;
import com.rucha.PasswordManager.entity.User;
import com.rucha.PasswordManager.helper.CookieHelper;
import com.rucha.PasswordManager.helper.UserHelper;
import com.rucha.PasswordManager.repo.UserRepo;
import com.rucha.PasswordManager.service.UserService;
import com.rucha.PasswordManager.util.PasswordUtil;
import com.rucha.PasswordManager.vo.LoginUserVO;
import com.rucha.PasswordManager.vo.RegisterUserVO;
import com.rucha.PasswordManager.vo.ResponseStatus;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Map;

@Component
public class UserServiceImpl implements UserService {

    UserRepo userRepo;
    //JWTGeneratorServiceImpl jwtGeneratorServiceImpl;
    JWTServiceImpl jwtGeneratorServiceImpl;
    UserHelper userHelper;
    CookieHelper cookieHelper;

    @Autowired
    public UserServiceImpl(UserRepo userRepo,
                           JWTServiceImpl jwtGeneratorServiceImpl,
                           UserHelper userHelper,
                           CookieHelper cookieHelper){
        this.userRepo = userRepo;
        this.jwtGeneratorServiceImpl = jwtGeneratorServiceImpl;
        this.userHelper = userHelper;
        this.cookieHelper = cookieHelper;
    }

    public ResponseStatus registerUser(RegisterUserVO registerUserVO) throws Exception {
        if (registerUserVO != null){
            User user = new User();
            user.setFullName(registerUserVO.getFullName());
            user.setEmail(registerUserVO.getEmail());
            String saltValue = PasswordUtil.getSaltvalue(30);
            user.setSaltValue(saltValue);
            user.setPassword(PasswordUtil.generateSecurePassword(registerUserVO.getPassword(), saltValue));
            userRepo.save(user);
            return new ResponseStatus(HttpStatus.CREATED, "Registered User Successfully.");
        }
        return new ResponseStatus(HttpStatus.BAD_REQUEST,"Credentials missing");
    }

    @Override
    public ResponseStatus loginUser(LoginUserVO loginUserVO, HttpServletResponse response) {
        User user = userRepo.findByEmail(loginUserVO.getEmail());

        if(userHelper.isValidUser(loginUserVO, user)){
            Map<String,String> accessTokenMap = jwtGeneratorServiceImpl.generateToken(user);
            String accessToken = accessTokenMap.get(user.getEmail());
            ResponseCookie cookie = cookieHelper.setCookie(accessToken);
            response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
            return new ResponseStatus(HttpStatus.OK, accessToken);
        }
        return new ResponseStatus( HttpStatus.CONFLICT,"Conflict raised");
    }

    public User loadUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }
}
