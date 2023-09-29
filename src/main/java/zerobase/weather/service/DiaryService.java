package zerobase.weather.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Service
public class DiaryService {
    @Value("${openweathermap.key}")
    private String apiKey;
    public void createDiary(LocalDate date, String text) {
        String weatherDate = getWeatherString();

        Map<String, Object> parsedWeather = parseWeather(weatherDate);
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
