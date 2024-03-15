package com.rucha.PasswordManager.helper;

import com.rucha.PasswordManager.entity.User;
import com.rucha.PasswordManager.util.PasswordUtil;
import com.rucha.PasswordManager.vo.LoginUserVO;
import org.springframework.stereotype.Component;

@Component
public class UserHelper {

    public boolean isValidUser(LoginUserVO loginUserVO, User user){
        String saltValue = user.getSaltValue();
        String encryptedPassword = PasswordUtil.generateSecurePassword(loginUserVO.getPassword(), saltValue);
        String databasePassword = user.getPassword();
        return encryptedPassword.equals(databasePassword);
    }
}
