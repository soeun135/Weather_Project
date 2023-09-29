package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.InvalidDate;
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
        DateWeather dateWeather = getDateWeather(date);

        Diary diary = new Diary();
        diary.setDateWeather(dateWeather);
        diary.setDate(date);
        diary.setText(text);
        diaryRepository.save(diary);
    }

    private DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> weather = dateWeatherRepository.findAllByDate(date);

        if (weather.size() == 0) {
            return weatherService.getWeatherFromApi();
        } else {
            return weather.get(0);
        }
    }

    public List<Diary> readDiary(LocalDate date) {
//        if(date.isAfter(LocalDate.ofYearDay(3050, 1))) {
//            throw new InvalidDate();
//        }
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
