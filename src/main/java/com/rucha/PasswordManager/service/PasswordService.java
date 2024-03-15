package com.rucha.PasswordManager.service;

import com.rucha.PasswordManager.entity.Password;
import com.rucha.PasswordManager.vo.PasswordVO;
import com.rucha.PasswordManager.vo.ResponseStatus;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PasswordService {
    List<Password> getAllPasswords(Integer pageNo, Integer pageSize, HttpServletRequest httpServletRequest);
    ResponseStatus addPassword(PasswordVO passwordVO, HttpServletRequest httpServletRequest) throws Exception;
    ResponseStatus updatePassword(Long passwordId, PasswordVO passwordVO, HttpServletRequest httpServletRequest) throws Exception;
    ResponseStatus deletePassword(Long passwordId, HttpServletRequest request);
    ResponseStatus unlockPassword(Long passwordId, HttpServletRequest request) throws Exception;
}
