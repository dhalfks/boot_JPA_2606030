package com.example.boot.controller;

import com.example.boot.dto.CommentDTO;
import com.example.boot.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequestMapping("/comment/*")
@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping(value = "/post",
        consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.TEXT_PLAIN_VALUE
    )
    public ResponseEntity<String> post(@RequestBody CommentDTO commentDTO){
        long cno = commentService.post(commentDTO);

        return cno > 0 ? new ResponseEntity<String>("1", HttpStatus.OK) :
                new ResponseEntity<String>("0", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
