package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.DateWeather;
import zerobase.weather.domain.Diary;
import zerobase.weather.error.WeatherException;
import zerobase.weather.repository.DateWeatherRepository;
import zerobase.weather.repository.DiaryRepository;

import java.time.LocalDate;
import java.util.List;

import static zerobase.weather.type.ErrorCode.*;

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
        
        validateCreateDiary(date);

        Diary diary = new Diary();
        diary.setDateWeather(dateWeather);
        diary.setDate(date);
        diary.setText(text);
        diaryRepository.save(diary);
    }

    private void validateCreateDiary(LocalDate date) {
        if (date.isAfter(LocalDate.ofYearDay(2100, 1)) ||
        date.isBefore(LocalDate.ofYearDay(1900, 12))) {
            throw new WeatherException(INVALID_DATE);
        }
    }

    private DateWeather getDateWeather(LocalDate date) {
        List<DateWeather> weather = dateWeatherRepository.findAllByDate(date);

        validateCreateDiary(date);

        if (weather.size() == 0) {
            return weatherService.getWeatherFromApi();
        } else {
            return weather.get(0);
        }
    }

    public List<Diary> readDiary(LocalDate date) {
        validateCreateDiary(date);

        return diaryRepository.findAllByDate(date);
    }

    public List<Diary> readDiaries(LocalDate startDate, LocalDate endDate) {

        if(startDate.isAfter(endDate)) {
            throw new WeatherException(START_DATE_EXCEED_END_DATE);
        }

        return diaryRepository.findAllByDateBetween(startDate, endDate);
    }

    @Transactional
    public void updateDiary(LocalDate date, String text) {
        Diary diary = diaryRepository.findFirstByDate(date);

        if(diary == null) {
            throw new WeatherException(DIARY_IS_EMPTY);
        }
        diary.setText(text);

        diaryRepository.save(diary);
    }

    @Transactional
    public void deleteDate(LocalDate date) {
        diaryRepository.deleteAllByDate(date);
    }
}
