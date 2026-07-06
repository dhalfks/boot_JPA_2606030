package com.example.boot.service;

import com.example.boot.dto.CommentDTO;
import com.example.boot.entity.Board;
import com.example.boot.repository.BoardRepository;
import com.example.boot.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    // 두 개 이상의 명령어가 실행 될 때 (두 명령중 하나라도 잘못되면 error)
    @Transactional
    @Override
    public long post(CommentDTO commentDTO) {
        // 저장 대상 => entity (commentDTO comment로 변환)
        // save()
        // 댓글이 등록되면 해당 board의 cmt_qty update + 1
//        Optional<Board> optional = boardRepository.findById(commentDTO.getBno());
//        if(optional.isPresent()){
//            Board board = optional.get();
//            board.setCmtQty(board.getCmtQty()+1);
//        }

        // 내가 굳이 save()를 안해도 update 일어남.
        Board board = boardRepository.findById(commentDTO.getBno())
                .orElseThrow(()-> new EntityNotFoundException());
        board.setCmtQty(board.getCmtQty()+1);
        long cno = commentRepository.save(convertDtoToEntity(commentDTO)).getCno();
        return cno;
    }
}
