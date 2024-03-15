package com.rucha.PasswordManager.impl;

import com.rucha.PasswordManager.entity.Password;
import com.rucha.PasswordManager.entity.User;
import com.rucha.PasswordManager.helper.CookieHelper;
import com.rucha.PasswordManager.repo.PasswordRepo;
import com.rucha.PasswordManager.repo.UserRepo;
import com.rucha.PasswordManager.service.PasswordService;
import com.rucha.PasswordManager.util.PasswordUtil;
import com.rucha.PasswordManager.vo.PasswordVO;
import com.rucha.PasswordManager.vo.ResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class PasswordServiceImpl implements PasswordService {

    PasswordRepo passwordRepo;
    JWTServiceImpl jwtService;
    UserRepo userRepo;
    CookieHelper cookieHelper;

    @Autowired
    public PasswordServiceImpl(PasswordRepo passwordRepo,
                               JWTServiceImpl jwtService,
                               UserRepo userRepo,
                               CookieHelper cookieHelper){
        this.passwordRepo = passwordRepo;
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.cookieHelper = cookieHelper;
    }

    @Override
    public List<Password> getAllPasswords(Integer pageNo, Integer pageSize, HttpServletRequest request) {
        String token = cookieHelper.getToken(request);
        String username = jwtService.extractUsername(token);
        User user = userRepo.findByEmail(username);
        if (jwtService.validateToken(token,user)){
            Pageable paging = PageRequest.of(pageNo, pageSize);
            return passwordRepo.findAllByUserId(user.getId(), paging);
        }
        return null;
    }

    @Override
    public ResponseStatus addPassword(PasswordVO passwordVO, HttpServletRequest httpServletRequest) throws Exception {
        String token = cookieHelper.getToken(httpServletRequest);

        //Long userId = jwtService.extractUserId(token);
        //User user = userRepo.findByEmail(userName);

        String username = jwtService.extractUsername(token);
        User user = userRepo.findByEmail(username);

        if (jwtService.validateToken(token, user)) {
            if (passwordVO!=null){
                Password password = new Password();
                password.setUserId(user.getId());
                password.setAccountName(passwordVO.getAccountName());
                password.setAccountUsername(passwordVO.getAccountUsername());
                String saltValue = PasswordUtil.getSaltvalue(16);
                password.setSaltPassword(saltValue);
                password.setAccountPassword(PasswordUtil.encrypt(passwordVO.getAccountPassword(),saltValue.getBytes()));
                passwordRepo.save(password);

                //Creating passwordVO to return
                passwordVO.setAccountPassword(password.getAccountPassword());
                return new ResponseStatus(HttpStatus.OK,"Password Added Successfully",passwordVO);
            }
        }
        return new ResponseStatus(HttpStatus.BAD_REQUEST,"Password Addition Failed");
    }
//
    @Override
    public ResponseStatus updatePassword(Long passwordId, PasswordVO passwordVO, HttpServletRequest request) throws Exception {
        String token = cookieHelper.getToken(request);
        String username = jwtService.extractUsername(token);
        User user = userRepo.findByEmail(username);
        if(jwtService.validateToken(token,user)){
            Optional<Password> passwordFound = passwordRepo.findById(passwordId);
            if (passwordFound.isPresent()){
                Password password = passwordFound.get();
                password.setAccountName(passwordVO.getAccountName());
                password.setAccountUsername(passwordVO.getAccountUsername());
                String saltValue = PasswordUtil.getSaltvalue(16);
                password.setSaltPassword(saltValue);
                //password.setAccountPassword(PasswordUtil.generateSecurePassword(passwordVO.getAccountPassword(),saltValue));
                password.setAccountPassword(PasswordUtil.encrypt(passwordVO.getAccountPassword(),saltValue.getBytes()));
                passwordRepo.save(password);

                //Creating passwordVO to return as salt won't be leaked
                passwordVO.setAccountPassword(password.getAccountPassword());
                return new ResponseStatus(HttpStatus.OK,"Password Updated Successfully",passwordVO);
            }
        }
        return new ResponseStatus(HttpStatus.BAD_REQUEST,"Password Updation Failed");
    }

    @Override
    public ResponseStatus deletePassword(Long passwordId, HttpServletRequest request) {
        String token = cookieHelper.getToken(request);
        String username = jwtService.extractUsername(token);
        User user = userRepo.findByEmail(username);
        if(jwtService.validateToken(token,user)){
            passwordRepo.deleteById(passwordId);
            return new ResponseStatus(HttpStatus.OK,"Password Deleted successfully");
        }
        return new ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR,"Password NOT Deleted");
    }

    @Override
    public ResponseStatus unlockPassword(Long passwordId, HttpServletRequest request) throws Exception {
        String token = cookieHelper.getToken(request);
        String username = jwtService.extractUsername(token);
        User user = userRepo.findByEmail(username);
        if(jwtService.validateToken(token,user)) {
            Optional<Password> passwordOptional = passwordRepo.findById(passwordId);
            if (passwordOptional.isPresent()) {
                Password password = passwordOptional.get();
                String encrypted =
                        PasswordUtil.decrypt(password.getAccountPassword(), password.getSaltPassword().getBytes());
                return new ResponseStatus(HttpStatus.OK, "Password Decrypted successfully", encrypted);
            }
        }
        return new ResponseStatus(HttpStatus.OK, "Password not decrypted.");
    }
}
