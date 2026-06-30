package com.example.boot.controller;

import com.example.boot.dto.BoardDTO;
import com.example.boot.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board/*")
@Controller
public class BoardController {
    private final BoardService boardService;

/*    @GetMapping("/register")
    public String register(){
        *//* /board/register  => (board 컨트롤러에서 register라는 getmapping으로 연결) *//*
        // 들어오는 경로와 나가는 경로가 같을 경우 생략가능
        return "/board/register";
    }*/

    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String register(BoardDTO boardDTO){
        log.info(">> boardDTO >> {}", boardDTO);
        Long bno = boardService.insert(boardDTO);

        return "redirect:/board/list";
    }

    @GetMapping("/list")
    public void list(Model model){
        List<BoardDTO> list = boardService.getList();
        model.addAttribute("list", list);
    }

}
