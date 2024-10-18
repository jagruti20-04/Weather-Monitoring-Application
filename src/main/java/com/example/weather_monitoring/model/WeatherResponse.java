package com.example.weather_monitoring.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherResponse {

    private List<Weather> weather;
    private Main main;
    private String name;
    private Wind wind; // Add this field

    @JsonProperty("temp")
    private double temp;

    @JsonProperty("feels_like")
    private Double feelsLike;

    // Getters and setters for the fields
    public double getTemp() {
        return temp;
    }

    public void setTemp(double temp) {
        this.temp = temp;
    }

    public Double getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Double feelsLike) {
        this.feelsLike = feelsLike;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public Main getMain() {
        return main;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Wind getWind() { // Add this method
        return wind;
    }

    public void setWind(Wind wind) { // Add this method
        this.wind = wind;
    }

    // Nested class for 'main' part of the response
    public static class Main {
        private double temp;
        private Double feels_like;  // Changed to Double to allow null values
        private int humidity; // Add humidity field

        // Getters and setters for the fields
        public double getTemp() {
            return temp;
        }

        public void setTemp(double temp) {
            this.temp = temp;
        }

        @JsonProperty("feels_like")
        public Double getFeelsLike() {
            return feels_like;
        }

        public void setFeelsLike(Double feels_like) {
            this.feels_like = feels_like;
        }

        public int getHumidity() { // Add this method for humidity
            return humidity;
        }

        public void setHumidity(int humidity) { // Add this method for humidity
            this.humidity = humidity;
        }
    }

    // Nested class for 'weather' part of the response
    public static class Weather {
        private String main;
        private String description;

        // Getters and setters for the fields
        public String getMain() {
            return main;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    // Nested class for 'wind' part of the response
    public static class Wind { // Add this class
        private double speed;

        // Getter and setter for speed
        public double getSpeed() {
            return speed;
        }

        public void setSpeed(double speed) {
            this.speed = speed;
        }
    }
}
