package com.example.boot.controller;

import com.example.boot.dto.UserDTO;
import com.example.boot.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user/*")
@Controller
public class UserController {
    private final UserService userService;

    @GetMapping("/join")
    public void join(){}

    @PostMapping("/join")
    public String join(UserDTO userDTO){
        String email = userService.insert(userDTO);
        log.info("회원가입 완료");
        return "index";
    }

    @GetMapping("/login")
    public void login(HttpServletRequest request, Model model){
        String errMsg = (String)request.getSession().getAttribute("errMsg");
        if(errMsg != null){
            log.info(">> errMsg>>{}", errMsg);
            model.addAttribute("errMsg", errMsg);
        }
        // 전송 후 지우기
        request.getSession().removeAttribute("errMsg");
    }

    @GetMapping("/admin")
    public String admin(){
        String adminEmail = "admin@test.com";
        userService.grantAdminRole(adminEmail);
        log.info(">>> grantADminRole success!!");
        return "index";
    }

    @GetMapping("/modify")
    public void modify(Model model, Principal principal){
        // principal : 로그인한 유저 이름(username)을 저장하는 객체
        UserDTO userDTO = userService.getDetail(principal.getName());
        model.addAttribute("userDTO", userDTO);
    }


}
