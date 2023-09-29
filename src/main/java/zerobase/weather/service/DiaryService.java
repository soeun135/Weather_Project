package zerobase.weather.service;

import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zerobase.weather.domain.Diary;
import zerobase.weather.repository.DiaryRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class DiaryService {
    private final DiaryRepository diaryRepository;
    @Value("${openweathermap.key}")
    private String apiKey;

    @Transactional
    public void createDiary(LocalDate date, String text) {
        String weatherData = getWeatherString();

        Map<String, Object> parsedWeather = parseWeather(weatherData);

        Diary diary = new Diary();
        diary.setWeather((String)parsedWeather.get("weather"));
        diary.setIcon((String)parsedWeather.get("icon"));
        diary.setTemperature((Double)parsedWeather.get("temp"));
        diary.setText(text);
        diary.setDate(date);
        diaryRepository.save(diary);
    }


    private String getWeatherString() {
        String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=seoul&appid=" + apiKey;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();

            BufferedReader br;
            if(responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            String inputLine;
            StringBuilder sb = new StringBuilder();
            while((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            br.close();
            conn.disconnect();
            return sb.toString();
        } catch (Exception e) {
            return "failed get Data";
        }
    }
    private HashMap<String, Object> parseWeather(String jsonString) {
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try {
            jsonObject = (JSONObject) jsonParser.parse(jsonString);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        HashMap<String, Object> map = new HashMap<>();
        JSONArray weatherData = (JSONArray) jsonObject.get("weather");
        JSONObject data = (JSONObject) weatherData.get(0);
        map.put("weather", data.get("main"));
        map.put("icon", data.get("icon"));

        JSONObject mainData = (JSONObject) jsonObject.get("main");
        map.put("temp", mainData.get("temp"));

        return map;
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
