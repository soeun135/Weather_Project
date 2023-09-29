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
import zerobase.weather.domain.DateWeather;
import zerobase.weather.repository.DateWeatherRepository;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class WeatherService {
    private final DateWeatherRepository dateWeatherRepository;

    @Value("${openweathermap.key}")
    private String apiKey;

    @Scheduled(cron="0 0 1 * * *")
    public void saveWeather() {
        dateWeatherRepository.save(getWeatherFromApi());
    }
    public DateWeather getWeatherFromApi() {
        String weatherData = getWeatherString();
        Map<String, Object> parsedWeather = parseWeather(weatherData);

        DateWeather dateWeather = new DateWeather();
        dateWeather.setDate(LocalDate.now());
        dateWeather.setWeather((String)parsedWeather.get("weather"));
        dateWeather.setIcon((String)parsedWeather.get("icon"));
        dateWeather.setTemperature((Double)parsedWeather.get("temp"));

        return dateWeather;
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
}
