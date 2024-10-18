package com.example.weather_monitoring.repository;



import com.example.weather_monitoring.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
}

