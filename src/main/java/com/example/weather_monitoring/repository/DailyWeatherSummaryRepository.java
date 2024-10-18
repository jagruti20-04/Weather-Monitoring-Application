package com.example.weather_monitoring.repository;

import com.example.weather_monitoring.model.DailyWeatherSummary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface DailyWeatherSummaryRepository extends JpaRepository<DailyWeatherSummary, Long> {
    List<DailyWeatherSummary> findByCityIgnoreCase(String city);
}

