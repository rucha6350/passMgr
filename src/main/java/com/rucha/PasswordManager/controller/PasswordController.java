package com.rucha.PasswordManager.controller;

import com.rucha.PasswordManager.entity.Password;
import com.rucha.PasswordManager.impl.PasswordServiceImpl;
import com.rucha.PasswordManager.vo.PasswordVO;
import com.rucha.PasswordManager.vo.ResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/password")
public class PasswordController {
    @Autowired
    PasswordServiceImpl passwordService;

    @GetMapping("/getPasswords")
    public ResponseEntity<?> getPasswords(@RequestParam(defaultValue = "0") Integer pageNo,
                                          @RequestParam(defaultValue = "10") Integer pageSize,
                                          HttpServletRequest request) {
        List<Password> passwordList = passwordService.getAllPasswords(pageNo, pageSize, request);
        return new ResponseEntity<>(passwordList, HttpStatus.OK);
    }

    @PostMapping("/addPassword")
    public ResponseStatus addPassword(@RequestBody PasswordVO passwordVO, HttpServletRequest request) throws Exception{
        return passwordService.addPassword(passwordVO,request);
    }

    @PutMapping("/updatePassword")
    public ResponseStatus updatePassword(@RequestParam Long passwordId, @RequestBody PasswordVO passwordVO,
                                         HttpServletRequest request) throws Exception{
        return passwordService.updatePassword(passwordId, passwordVO, request);
    }

    @DeleteMapping("/deletePassword")
    public ResponseStatus deletePassword(@RequestParam Long passwordId,  HttpServletRequest request) {
        return passwordService.deletePassword(passwordId,request);
    }

    @GetMapping("/unlockPassword")
    public ResponseStatus unlockPassword(@RequestParam Long passwordId,  HttpServletRequest request) throws Exception {
        return passwordService.unlockPassword(passwordId,request);
    }
}
