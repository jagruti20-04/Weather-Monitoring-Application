package com.example.weather_monitoring.scheduler;

import com.example.weather_monitoring.model.AlertThreshold;
import com.example.weather_monitoring.model.DailyWeatherSummary;
import com.example.weather_monitoring.model.WeatherData;
import com.example.weather_monitoring.repository.AlertThresholdRepository;
import com.example.weather_monitoring.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class WeatherScheduler {

    @Autowired
    private WeatherService weatherService;

    // Fetch weather data every 5 minutes for predefined cities
    @Scheduled(fixedRate = 300000) // 5 minutes
    public void fetchWeatherDataForCities() {
        String[] cities = {"Delhi", "Mumbai", "Chennai", "Bangalore", "Kolkata", "Hyderabad"};

        for (String city : cities) {
            WeatherData data = weatherService.fetchWeatherData(city);
            processWeatherData(data);
        }
    }

    private void processWeatherData(WeatherData data) {
        // Fetch or create a DailyWeatherSummary object for today
        DailyWeatherSummary summary = getOrCreateSummaryForToday(data.getCity());

        // Update aggregates
        summary.setAverageTemperature(calculateAverage(summary.getAverageTemperature(), data.getTemperature()));
        summary.setMaxTemperature(Math.max(summary.getMaxTemperature(), data.getTemperature()));
        summary.setMinTemperature(Math.min(summary.getMinTemperature(), data.getTemperature()));

        // Determine the dominant weather condition
        updateDominantCondition(summary, data.getWeatherCondition());

        // Save summary to database (make sure to inject the repository)
        // repository.save(summary); // Uncomment this line after injecting the repository

        // Check for alerting thresholds
        checkAlerts(data);
    }

    private DailyWeatherSummary getOrCreateSummaryForToday(String city) {
        // Logic to fetch or create a DailyWeatherSummary object for today
        // Example placeholder return statement; implement the actual logic
        return new DailyWeatherSummary();
    }

    private double calculateAverage(double currentAverage, double newValue) {
        // Example logic to calculate the new average
        // Assuming a simple average calculation for illustration
        return (currentAverage + newValue) / 2; // You may want to refine this logic
    }

    private void updateDominantCondition(DailyWeatherSummary summary, String currentCondition) {
        // Logic to determine the dominant weather condition
        // This could be based on frequency or some other criteria
        summary.setDominantCondition(currentCondition); // Example placeholder logic
    }
    

   
    private void checkAlerts(WeatherData data) {
        AlertThreshold threshold = AlertThresholdRepository.findByCity(data.getCity());
        if (threshold != null) {
            if (data.getTemperature() > threshold.getTemperatureThreshold()) {
                // Trigger alert (you can log it, send an email, etc.)
                System.out.println("Alert: Temperature exceeded threshold for " + data.getCity());
            }
            // You can add checks for weather conditions as well
        }
    }

}
