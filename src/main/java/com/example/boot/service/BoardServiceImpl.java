package com.example.boot.service;

import com.example.boot.dto.BoardDTO;
import com.example.boot.entity.Board;
import com.example.boot.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class BoardServiceImpl implements BoardService{
    private final BoardRepository boardRepository;

    @Override
    public Long insert(BoardDTO boardDTO) {
        // CRUD에 해당하는 메서드 제공
        // save() : 저장
        // 저장하는 객체는 Entity (Board)
        Board board = convertDtoToEntity(boardDTO);
        Long bno = boardRepository.save(board).getBno();  // 저장 후 bno 가져가기
        return bno;
    }

    @Override
    public List<BoardDTO> getList() {
        // findAll() => 전체 값 리턴
        // select * from board order by bno desc
        // 정렬 : Sort.by(Sort.direction.DESC, "정렬기준 칼럼명")
        // DB에서 가져오는 return List<Board>  => List<BoardDTO> 변환
        List<Board> boardList = boardRepository.findAll(
                Sort.by(Sort.Direction.DESC, "bno"));

        List<BoardDTO> boardDTOList = boardList
                .stream()
                .map(board -> convertEntityToDto(board))
//                .map(this :: convertEntityToDto)
                .toList();
        return boardDTOList;
    }
}
