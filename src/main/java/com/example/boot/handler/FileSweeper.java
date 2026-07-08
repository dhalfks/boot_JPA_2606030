package com.example.boot.handler;

import com.example.boot.dto.FileDTO;
import com.example.boot.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 스케줄링 사용
@EnableScheduling
@Slf4j
@Component
@RequiredArgsConstructor
public class FileSweeper {
    // 매일 정해진 시간에 스케줄러 실행
    // 매일 해당 날짜의 경로에 DB의 데이터와 폴더안의 파일이 일치하는지 비교
    // DB == file 일치하면 남기고, DB != 폴더 삭제

    private final BoardService boardService;
    private final String BASE_PATH = "D:\\web_260316_omr\\_myProject\\_java\\_fileUpload\\";

    // cron 방식 : 초 분 시 일 월 요일 년도(생략가능)
    // 스프링(Spring) 기반 환경: 초(0~59) 분(0~59) 시(0~23) 일(1~31) 월(1~12) 요일(0~7)
    @Scheduled(cron = "00 39 14 * * * ")
    public void fileSweeper(){
        log.info(">>>>> fileSweeper Start >> {}", LocalDateTime.now());
        // DB에 등록된 파일 리스트 가져오기 (오늘날짜 폴더만)
        // 오늘날짜의 경로가 필요
        LocalDate now = LocalDate.now();
        String today = now.toString().replace("-", File.separator);

        // DB에서 today == saveDir 일치하는 값만 가져오기
        // select * from file where save_dir = today
        List<FileDTO> dbFileList = boardService.getTodayFileList(today);
        log.info(">>> dbFileList >> {}", dbFileList);


        log.info(">>>>> fileSweeper End >> {}", LocalDateTime.now());
    }
}
