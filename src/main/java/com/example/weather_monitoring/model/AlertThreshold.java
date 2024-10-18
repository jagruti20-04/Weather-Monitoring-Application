package com.example.weather_monitoring.model;



import jakarta.persistence.*;

@Entity
public class AlertThreshold {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private double temperatureThreshold;
    public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public double getTemperatureThreshold() {
		return temperatureThreshold;
	}
	public void setTemperatureThreshold(double temperatureThreshold) {
		this.temperatureThreshold = temperatureThreshold;
	}
	public String getWeatherCondition() {
		return weatherCondition;
	}
	public void setWeatherCondition(String weatherCondition) {
		this.weatherCondition = weatherCondition;
	}
	private String weatherCondition;

    
}
