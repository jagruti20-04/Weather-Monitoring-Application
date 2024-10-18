package com.example.weather_monitoring.model;

public class WeatherAverages {


    private double averageTemperature;
    private double maxTemperature;
    private double minTemperature;

    public WeatherAverages(double averageTemperature, double maxTemperature, double minTemperature) {
        this.averageTemperature = averageTemperature;
        this.maxTemperature = maxTemperature;
        this.minTemperature = minTemperature;
    }

    // Getters and setters
    public double getAverageTemperature() {
        return averageTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public double getMinTemperature() {
        return minTemperature;
    }
}
