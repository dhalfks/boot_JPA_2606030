package com.example.boot.service;

import com.example.boot.dto.CommentDTO;
import com.example.boot.entity.Board;
import com.example.boot.entity.Comment;
import com.example.boot.repository.BoardRepository;
import com.example.boot.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Override
    public List<CommentDTO> getList(long bno) {
        // select * from comment where bno = #{bno}
        List<Comment> list = commentRepository.findByBno(bno);
        log.info("commentList >> {}", list);
        List<CommentDTO> commentDTOList = list.stream()
                .map(comment -> convertEntityToDto(comment))
                .toList();
        log.info("commentDTOList >> {}", commentDTOList);
        return commentDTOList;
    }

    @Override
    public Page<CommentDTO> getList(long bno, int page) {
        // select * from comment where bno = #{bno}
        // order by cno desc limit page, qty;
        // Page 된 값을 리턴받으려면 pageable 값을 파라미터로 전송
        Pageable pageable = PageRequest.of(page-1, 5,
                Sort.by("cno").descending());
        Page<Comment> list = commentRepository.findByBno(bno, pageable);

        return list.map(this::convertEntityToDto);
    }

    /* save() => id가 없으면 insert / id가 있으면 update
    EntityNotFoundException : where 에서 검색한 조건의 값이 없을경우 발생
    정보 유실 가능성이 커짐
    dirty checking (변동 감지)
    findById(cno) 먼저 조회 => 영속상태를 만든 후 수정 => save()
     @Transactional => dirty checking만으로 업데이트를 가능 => save() 없이도 업데이트가능

     dirty cheking
     엔티티가 영속성 컨텍스트에 올라가 있는 상태 일때 (=영속상태)
     해당 객체의 필드가 변경되면, 트랜젝션이 종료되기 전에 JPA가 변경한 부분만
     자동으로 감지하여 update 쿼리를 실행
     save()가 없어도 (명시적으로 호출하지 않아도) 수정된 필드를 DB에 자동 반영 가능.
    * */

    @Transactional
    @Override
    public long modify(CommentDTO commentDTO) {
        Comment comment = commentRepository.findById(commentDTO.getBno())
                .orElseThrow(()-> new EntityNotFoundException("해당 댓글을 찾을 수 없습니다."));
        comment.setContent(commentDTO.getContent());
        return comment.getCno();
    }

    @Transactional
    @Override
    public void remove(long cno) {
        Comment comment = commentRepository.findById(cno)
                .orElseThrow(()-> new EntityNotFoundException());

        // 댓글 개수 1개 줄이기
        Board board = boardRepository.findById(comment.getBno())
                .orElseThrow(()-> new EntityNotFoundException());
        board.setCmtQty(board.getCmtQty()-1);
        commentRepository.deleteById(cno);
    }


}
