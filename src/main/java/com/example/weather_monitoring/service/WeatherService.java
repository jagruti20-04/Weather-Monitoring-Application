package com.example.weather_monitoring.service;

import com.example.weather_monitoring.model.DailyWeatherSummary;
import com.example.weather_monitoring.model.WeatherAverages;
import com.example.weather_monitoring.model.WeatherData;
import com.example.weather_monitoring.model.WeatherResponse;
import com.example.weather_monitoring.repository.DailyWeatherSummaryRepository;
import com.example.weather_monitoring.repository.WeatherDataRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WeatherService {

    @Autowired
    private WeatherDataRepository repository;
    @Autowired
    private DailyWeatherSummaryRepository dailyWeatherSummaryRepository; // Add this line
    
    @Autowired
    private WeatherDataRepository weatherDataRepository;


    private final String API_KEY = "Your_Api_key";
    private final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather?q=%s&appid=" + API_KEY;
    

    public WeatherData fetchWeatherData(String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(BASE_URL, city);
        WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

        if (response == null) {
            throw new RuntimeException("Failed to fetch weather data for city: " + city);
        }

        WeatherData weatherData = new WeatherData();
        weatherData.setCity(response.getName());

        // Convert temperature from Kelvin to Celsius
        weatherData.setTemperature(kelvinToCelsius(response.getMain().getTemp()));

        // Handle feels_like temperature
        weatherData.setFeelsLike(response.getMain().getFeelsLike() != null
                ? kelvinToCelsius(response.getMain().getFeelsLike())
                : Double.NaN); // Set to NaN if feels_like is missing

        weatherData.setWeatherCondition(response.getWeather().get(0).getMain());
        weatherData.setDateTime(LocalDateTime.now());

        // Handle wind speed and humidity
        weatherData.setWindSpeed(response.getWind() != null ? response.getWind().getSpeed() : 0.0);
        weatherData.setHumidity(response.getMain() != null ? response.getMain().getHumidity() : 0);

        // Handle potential NaN or extreme values for feels_like
        if (Double.isNaN(weatherData.getFeelsLike()) || 
            weatherData.getFeelsLike() < -50 || 
            weatherData.getFeelsLike() > 60) {
            weatherData.setFeelsLike(20.0); // Set to a reasonable default value
        }

        repository.save(weatherData);
        return weatherData;
    }

    private double kelvinToCelsius(double kelvin) {
        return Math.round((kelvin - 273.15) * 100.0) / 100.0; // Round to two decimal places
    }
    public WeatherData fetchAndSaveWeatherData(String city) {
        RestTemplate restTemplate = new RestTemplate();
        String url = String.format(BASE_URL, city);
        WeatherResponse response = restTemplate.getForObject(url, WeatherResponse.class);

        if (response == null) {
            throw new RuntimeException("Failed to fetch weather data for city: " + city);
        }

        WeatherData weatherData = new WeatherData();
        weatherData.setCity(response.getName());

        // Convert temperature from Kelvin to Celsius
        weatherData.setTemperature(kelvinToCelsius(response.getMain().getTemp()));

        // Handle feels_like temperature
        weatherData.setFeelsLike(response.getMain().getFeelsLike() != null
                ? kelvinToCelsius(response.getMain().getFeelsLike())
                : Double.NaN); // Set to NaN if feels_like is missing

        weatherData.setWeatherCondition(response.getWeather().get(0).getMain());
        weatherData.setDateTime(LocalDateTime.now());

        // Handle wind speed and humidity
        weatherData.setWindSpeed(response.getWind() != null ? response.getWind().getSpeed() : 0.0);
        weatherData.setHumidity(response.getMain() != null ? response.getMain().getHumidity() : 0);

        // Handle potential NaN or extreme values for feels_like
        if (Double.isNaN(weatherData.getFeelsLike()) || 
            weatherData.getFeelsLike() < -50 || 
            weatherData.getFeelsLike() > 60) {
            weatherData.setFeelsLike(20.0); // Set to a reasonable default value
        }

        // Save the fetched weather data to the database
        repository.save(weatherData);

        // Now, create and save the daily summary
        saveDailyWeatherSummary(weatherData);

        return weatherData;
    }

    // Create and save the daily weather summary based on the fetched WeatherData
    public void saveDailyWeatherSummary(WeatherData weatherData) {
        DailyWeatherSummary summary = new DailyWeatherSummary();
        
        summary.setCity(weatherData.getCity());
        summary.setDate(weatherData.getDateTime().toLocalDate());
        summary.setAverageTemperature(weatherData.getTemperature());
        summary.setMaxTemperature(weatherData.getTemperature()); // Assuming single data point for now
        summary.setMinTemperature(weatherData.getTemperature()); // Assuming single data point for now
        summary.setDominantCondition(weatherData.getWeatherCondition());

        // Save the summary to the database
        dailyWeatherSummaryRepository.save(summary);

        System.out.println("Daily weather summary saved for city: " + weatherData.getCity());
    }

    
    @Transactional
    public List<DailyWeatherSummary> generateAndSaveDailyWeatherSummaries() {
        // Fetch all weather data from the repository
        List<WeatherData> weatherDataList = weatherDataRepository.findAll();
        Map<String, Map<LocalDate, List<WeatherData>>> cityDateMap = new HashMap<>();

        // Group weather data by city and date
        for (WeatherData data : weatherDataList) {
            String city = data.getCity().toLowerCase();
            LocalDate date = data.getDateTime().toLocalDate();
            
            cityDateMap.putIfAbsent(city, new HashMap<>());
            cityDateMap.get(city).putIfAbsent(date, new ArrayList<>());
            cityDateMap.get(city).get(date).add(data);
        }

        List<DailyWeatherSummary> dailySummaries = new ArrayList<>();

        // Iterate over city-date map to calculate summaries
        for (Map.Entry<String, Map<LocalDate, List<WeatherData>>> cityEntry : cityDateMap.entrySet()) {
            String city = cityEntry.getKey();
            Map<LocalDate, List<WeatherData>> dateMap = cityEntry.getValue();

            for (Map.Entry<LocalDate, List<WeatherData>> dateEntry : dateMap.entrySet()) {
                LocalDate date = dateEntry.getKey();
                List<WeatherData> dailyWeather = dateEntry.getValue();

                // Calculate average, max, and min temperatures
                double avgTemp = dailyWeather.stream().mapToDouble(WeatherData::getTemperature).average().orElse(0);
                double maxTemp = dailyWeather.stream().mapToDouble(WeatherData::getTemperature).max().orElse(0);
                double minTemp = dailyWeather.stream().mapToDouble(WeatherData::getTemperature).min().orElse(0);

                // Determine dominant weather condition
                String dominantCondition = dailyWeather.stream()
                        .map(WeatherData::getWeatherCondition)
                        .reduce((a, b) -> a.equals(b) ? a : "Mixed")
                        .orElse("Unknown");

                // Create and populate DailyWeatherSummary object
                DailyWeatherSummary summary = new DailyWeatherSummary();
                summary.setCity(city);
                summary.setDate(date);
                summary.setAverageTemperature(avgTemp);
                summary.setMaxTemperature(maxTemp);
                summary.setMinTemperature(minTemp);
                summary.setDominantCondition(dominantCondition);

                // Save summary to database
                dailyWeatherSummaryRepository.save(summary);
                dailySummaries.add(summary);
            }
        }

        return dailySummaries;
    }


    public List<DailyWeatherSummary> getDailyWeatherSummaries() {
        // Fetch all weather data using the repository instance
        List<WeatherData> weatherDataList = weatherDataRepository.findAll(); 
        Map<LocalDate, List<WeatherData>> dailyDataMap = new HashMap<>();

        // Group weather data by date
        for (WeatherData data : weatherDataList) {
            LocalDate date = data.getDateTime().toLocalDate();
            dailyDataMap.putIfAbsent(date, new ArrayList<>());
            dailyDataMap.get(date).add(data);
        }

        List<DailyWeatherSummary> dailySummaries = new ArrayList<>();

        // Iterate over the daily data map to calculate summaries
        for (Map.Entry<LocalDate, List<WeatherData>> entry : dailyDataMap.entrySet()) {
            LocalDate date = entry.getKey();
            List<WeatherData> dailyWeather = entry.getValue();

            // Calculate average, max, and min temperatures
            double avgTemp = dailyWeather.stream().mapToDouble(WeatherData::getTemperature).average().orElse(0);
            double maxTemp = dailyWeather.stream().mapToDouble(WeatherData::getTemperature).max().orElse(0);
            double minTemp = dailyWeather.stream().mapToDouble(WeatherData::getTemperature).min().orElse(0);

            // Determine the dominant weather condition
            String dominantCondition = dailyWeather.stream()
                    .map(WeatherData::getWeatherCondition)
                    .reduce((a, b) -> a) // Modify as necessary for a better representation
                    .orElse("Unknown");

            // Create and populate a DailyWeatherSummary object
            DailyWeatherSummary summary = new DailyWeatherSummary();
            summary.setDate(date);
            summary.setAverageTemperature(avgTemp);
            summary.setMaxTemperature(maxTemp);
            summary.setMinTemperature(minTemp);
            summary.setDominantCondition(dominantCondition);

            // Add the summary to the list
            dailySummaries.add(summary);
        }

        return dailySummaries;
    }


    public List<Map<String, String>> getAlertThresholds() {
        List<Map<String, String>> thresholds = new ArrayList<>();
        
        // Simulate temperature threshold
        Map<String, String> temperatureThreshold = new HashMap<>();
        temperatureThreshold.put("type", "Temperature");
        temperatureThreshold.put("value", "30°C");
        temperatureThreshold.put("status", "Exceeded");
        
        // Simulate wind speed threshold
        Map<String, String> windThreshold = new HashMap<>();
        windThreshold.put("type", "Wind Speed");
        windThreshold.put("value", "10 m/s");
        windThreshold.put("status", "Normal");
        
        thresholds.add(temperatureThreshold);
        thresholds.add(windThreshold);
        
        return thresholds;
    }
    
    public List<DailyWeatherSummary> generateAndSaveSummaries() {
        List<DailyWeatherSummary> summaries = generateAndSaveDailyWeatherSummaries();
        
        if (summaries.isEmpty()) {
            System.out.println("No weather summaries were generated.");
        } else {
            // Assuming you have a repository to save the summaries
            dailyWeatherSummaryRepository.saveAll(summaries); // Save summaries to the database
            System.out.println("Daily weather summaries generated and saved successfully.");
        }

        return summaries; // Return the list of summaries
    }

    @Scheduled(cron = "0 0 0 * * *") // This will trigger every day at midnight
    public void scheduledSummaryGeneration() {
        generateAndSaveSummaries();
    }
    
    public WeatherAverages calculateAverages() {
        List<DailyWeatherSummary> summaries = dailyWeatherSummaryRepository.findAll();

        if (summaries.isEmpty()) {
            return new WeatherAverages(0, 0, 0); // Return zeros if no data
        }

        double averageTemperature = summaries.stream()
                .mapToDouble(DailyWeatherSummary::getAverageTemperature)
                .average()
                .orElse(0);

        double maxTemperature = summaries.stream()
                .mapToDouble(DailyWeatherSummary::getMaxTemperature)
                .average()
                .orElse(0);

        double minTemperature = summaries.stream()
                .mapToDouble(DailyWeatherSummary::getMinTemperature)
                .average()
                .orElse(0);

        return new WeatherAverages(averageTemperature, maxTemperature, minTemperature);
    }
}
