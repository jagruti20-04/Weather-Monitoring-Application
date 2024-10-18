package com.example.weather_monitoring.controller;

import com.example.weather_monitoring.model.DailyWeatherSummary;
import com.example.weather_monitoring.model.WeatherAverages;
import com.example.weather_monitoring.model.WeatherData;
import com.example.weather_monitoring.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Controller
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @PostMapping("/weather")
    public String getWeather(@RequestParam String city, Model model) {
        // Call the service to fetch weather data
        WeatherData weatherData = weatherService.fetchWeatherData(city);
        
        weatherData.setCity(city);
        // Format date time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedDateTime = weatherData.getDateTime().format(formatter);
        
        // Add attributes to the model
        model.addAttribute("weather", weatherData);
        model.addAttribute("formattedDateTime", formattedDateTime); // Pass formatted date to model
        return "weather"; // Return the view name for displaying weather data
    }

    @GetMapping("/index")
    public String index() {
        // Returns the view name for the home page
        return "index";
    }

//    @GetMapping("/summary")
//    public String generateDailySummaries(Model model) {
//        List<DailyWeatherSummary> summaries = weatherService.generateAndSaveSummaries();
//        for (DailyWeatherSummary summary : summaries) {
//            // Format the date here and set it in the summary object
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//            String formattedDate = summary.getDate().format(formatter);
//            summary.setFormattedDate(formattedDate); // Ensure you have a setter for this
//        }
//        model.addAttribute("summaries", summaries);
//        return "summery"; // Ensure this matches the template name
//    }
    @GetMapping("/summary")
    public String generateDailySummaries(Model model) {
        List<DailyWeatherSummary> summaries = weatherService.generateAndSaveSummaries();
        for (DailyWeatherSummary summary : summaries) {
          // Format the date here and set it in the summary object
          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
          String formattedDate = summary.getDate().format(formatter);
          summary.setFormattedDate(formattedDate); // Ensure you have a setter for this
      }
        WeatherAverages averages = weatherService.calculateAverages();
        model.addAttribute("summaries", summaries);
        model.addAttribute("averages", averages);
        return "summery"; // Ensure this matches the template name
    }



    @GetMapping("/alert/thresholds")
    public String alertThresholds(Model model) {
        // Retrieve alert thresholds and their statuses
        List<Map<String, String>> thresholds = weatherService.getAlertThresholds();
        
        // Add the thresholds to the model
        model.addAttribute("thresholds", thresholds);
        return "alert_thresholds"; // Return the view name for displaying alert thresholds
    }
}
