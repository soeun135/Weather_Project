package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final DateWeatherRepository dateWeatherRepository;
    private final WeatherService weatherService;

    @Transactional
    public void createDiary(LocalDate date, String text) {
        //날씨 데이터 가져오기 (DB에서)
        DateWeather dateWeather = getDateWeather(date);

        Diary diary = new Diary();
        diary.setDateWeather(dateWeather);
        diary.setDate(date);
        diary.setText(text);
        diaryRepository.save(diary);
    }

    private DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> weather = dateWeatherRepository.findAllByDate(date);

        if(weather.size() == 0) {
            //새로 api에서 날씨 정보 가져와야하는데
            //정책상 정하기 나름 현재 날씨 가져오도록 하거나 날씨 없이 일기 쓰도록
            return weatherService.getWeatherFromApi();
        } else {
            return weather.get(0);
        }
    }

    public List<Diary> readDiary(LocalDate date) {
        return diaryRepository.findAllByDate(date);
    }

    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {
        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    @Transactional
    public void updateDiary(LocalDate date, String text) {
        Diary diary = diaryRepository.findFirstByDate(date);
        diary.setText(text);

        diaryRepository.save(diary);
    }

    @Transactional
    public void deleteDate(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }
}
