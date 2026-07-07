package com.example.boot.controller;

import com.example.boot.dto.BoardDTO;
import com.example.boot.dto.BoardFileDTO;
import com.example.boot.dto.FileDTO;
import com.example.boot.handler.FileHandler;
import com.example.boot.handler.PagingHandler;
import com.example.boot.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RequestMapping("/board/*")
@Controller
public class BoardController {
    private final BoardService boardService;
    private final FileHandler fileHandler;

/*    @GetMapping("/register")
    public String register(){
        *//* /board/register  => (board 컨트롤러에서 register라는 getmapping으로 연결) *//*
        // 들어오는 경로와 나가는 경로가 같을 경우 생략가능
        return "/board/register";
    }*/

    @GetMapping("/register")
    public void register(){}

    @PostMapping("/register")
    public String register(BoardDTO boardDTO,
                           @RequestParam(name = "files", required = false)MultipartFile[] files){
        // 추가된 파일 처리
        // DB로 갈 fileDto 객체 만들기
        // 실제 저장
        List<FileDTO> fileList = null;
        if(files != null && files[0].getSize() > 0){
            // 파일이 존재한다면...  핸들러를 호출
            fileList = fileHandler.uploadFile(files);
        }
        log.info(">> boardDTO >> {}", boardDTO);
        log.info(">> fileList >> {}", fileList);
        Long bno = boardService.insert(new BoardFileDTO(boardDTO, fileList));

        //Long bno = boardService.insert(boardDTO);

        return "redirect:/board/list";
    }

//    @GetMapping("/list")
//    public void list(Model model){
//        // 페이징 없는 리스트
//        List<BoardDTO> list = boardService.getList();
//        model.addAttribute("list", list);
//    }

    @GetMapping("/list")
    public void list(Model model,
                     @RequestParam(name = "pageNo", required = false,
                             defaultValue = "1") int pageNo){
        Page<BoardDTO> list = boardService.getList(pageNo);
//        model.addAttribute("list",list);
//        log.info("getTotalElements >> {}", list.getTotalElements()); // 전체 게시글 수
//        log.info("getTotalPages >> {}", list.getTotalPages()); // realEndPage
//        log.info("list >> {}", list.hasPrevious()); // 이전 버튼의 필요 여부
//        log.info("list >> {}", list.hasNext()); // 다음 버튼의 필요 여부
        PagingHandler ph = new PagingHandler(list,pageNo);
        log.info("ph>>{}",ph);
        model.addAttribute("ph",ph);
    }

    @GetMapping("/detail")
    public void detail(@RequestParam("bno") Long bno, Model model){
        BoardFileDTO boardFileDTO = boardService.getDetail(bno);
        model.addAttribute("boardFile", boardFileDTO);
    }

    @PostMapping("/update")
    public String update(BoardDTO boardDTO,
                         RedirectAttributes redirectAttributes){
        boardService.update(boardDTO);

        // redirect시 해당 위치로 객체를 보내주는 역할
        redirectAttributes.addAttribute("bno",boardDTO.getBno());
        return "redirect:/board/detail";
    }

    @GetMapping("/remove")
    public String remove(@RequestParam("bno")Long bno){
        boardService.remove(bno);
        return "redirect:/board/list";
    }

}
