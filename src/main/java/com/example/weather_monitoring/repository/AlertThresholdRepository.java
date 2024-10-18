package com.example.weather_monitoring.repository;

import com.example.weather_monitoring.model.AlertThreshold;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertThresholdRepository extends JpaRepository<AlertThreshold, Long> {
    static AlertThreshold findByCity(String city) {
		// TODO Auto-generated method stub
		return null;
	}
}
