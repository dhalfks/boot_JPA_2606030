package com.example.boot.security;

import com.example.boot.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Slf4j
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        // 로그인 성공시
        // lastlogin 시간 업데이트
        log.info(">> authentication>>{}", authentication);
        userService.lastloginUpdate(authentication.getName());

        // 직전 로그인 실패 기록 지우기
        // WebAttributes.AUTHENTICATION_EXCEPTION 로그인 실패시 세션에 기록
        HttpSession ses = request.getSession();
        if(ses == null){
            return;
        }else{
            ses.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }

        // 직전 url의 정보로 리다이렉트 해줘야 함

    }
}
