package zerobase.weather.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.InvalidDate;
import zerobase.weather.service.DiaryService;

import java.time.LocalDate;
import java.util.*;

@RequiredArgsConstructor
@RestController
public class DiaryController {
    private final DiaryService diaryService;

    @ApiOperation("일기와 날씨를 DB에 저장")
    @PostMapping("/create/diary")
    public void createDiary(
            @RequestParam @DateTimeFormat(iso= DateTimeFormat.ISO.DATE)
            @ApiParam(value="일기를 작성할 날짜", example = "2023-09-30")
            LocalDate date,
            @RequestBody String text
    ) {
        diaryService.createDiary(date, text);
    }

    @ApiOperation("입력한 날짜의 모든 일기 데이터 가져옴")
    @GetMapping("/read/diary")
    public List<Diary> readDiary(
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
            @ApiParam(value="조회할 날짜", example = "2023-09-30")
            LocalDate date
    ){
//        if(date.isAfter(LocalDate.ofYearDay(3050, 1))) {
//            throw new InvalidDate();
//        }
        return diaryService.readDiary(date);
    }

    @ApiOperation("입력한 기간의 모든 일기 데이터 가져옴")
    @GetMapping("/read/diaries")
    public List<Diary> readDiaries(
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
            @ApiParam(value="조회할 기간의 시작날", example = "2023-09-10") LocalDate startDate,
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
            @ApiParam(value="조회할 기간의 마지막날", example = "2023-09-30")
            LocalDate endDate
    ) {
        return diaryService.readDiaries(startDate, endDate);
    }

    @ApiOperation("입력한 날짜의 일기 텍스트를 수정")
    @PutMapping("/update/diary")
    public void updateDiary(
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
            @ApiParam(value="일기를 수정할 날짜", example = "2023-09-30")
            LocalDate date,
            @RequestBody String text
    ) {
        diaryService.updateDiary(date, text);
    }

    @ApiOperation("입력한 날짜의 모든 일기 삭제")
    @DeleteMapping("delete/diary")
    public void deleteDiary(
            @RequestParam @DateTimeFormat(iso=DateTimeFormat.ISO.DATE)
            @ApiParam(value="모든 일기를 삭제할 날짜", example = "2023-09-30")
            LocalDate date
    ) {
        diaryService.deleteDate(date);
    }
}
