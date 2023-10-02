package zerobase.weather.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@Transactional
class DiaryServiceTest {

    @Mock
    private DiaryRepository diaryRepository;

    @Mock
    private DateWeatherRepository dateWeatherRepository;

    @Mock
    private WeatherService weatherService;

    @InjectMocks
    private DiaryService diaryService;

    @Test
    void successCreateDiary() {
        //given
        DateWeather dateWeather = DateWeather.builder()
                .date(LocalDate.ofEpochDay(2023 - 10 - 02))
                .weather("cloud")
                .icon("12D")
                .build();
        Diary diary = Diary.builder()
                .text("안녕")
                .date(LocalDate.ofEpochDay(2023 - 10 - 02))
                .build();
        diary.setDateWeather(dateWeather);

        //when
        diaryRepository.save(diary);
        ArgumentCaptor<Diary> captor = ArgumentCaptor.forClass(Diary.class);

        //then
        verify(diaryRepository, times(1)).save(captor.capture());
    }

    @Test
    void getDateWeatherTest() {
        //when
        dateWeatherRepository.findAllByDate(any());
        ArgumentCaptor<DateWeather> captor = ArgumentCaptor.forClass(DateWeather.class);

        //then
        verify(dateWeatherRepository, times(1)).findAllByDate(any());
    }

    @Test
    void readDiaryTest() {
        //when
        diaryRepository.findAllByDate(any());
        ArgumentCaptor<Diary> captor = ArgumentCaptor.forClass(Diary.class);

        //then
        verify(diaryRepository, times(1)).findAllByDate(any());
    }

    @Test
    void readDiariesTest() {
        //when
        diaryRepository.findAllByDateBetween(any(), any());
        ArgumentCaptor<Diary> captor = ArgumentCaptor.forClass(Diary.class);

        //then
        verify(diaryRepository, times(1)).findAllByDateBetween(any(),any());
    }

    @Test
    void updateDiaryTest() {
        //given
        //when
        Diary diary = diaryRepository.findFirstByDate(any());
        ArgumentCaptor<Diary> captor = ArgumentCaptor.forClass(Diary.class);
        diaryRepository.save(diary);
        ArgumentCaptor<Diary> captor1 = ArgumentCaptor.forClass(Diary.class);

        //then
        verify(diaryRepository, times(1)).findFirstByDate(any());
        verify(diaryRepository, times(1)).save(captor.capture());

    }

    @Test
    void deleteDateTest() {
        //given
        //when
        diaryRepository.deleteAllByDate(any());
        ArgumentCaptor<Diary> captor = ArgumentCaptor.forClass(Diary.class);

        //then
        verify(diaryRepository, times(1)).deleteAllByDate(any());

    }
}