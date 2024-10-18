# Weather-Monitoring-Application


## Overview
The Weather Monitoring Application is a Java-based application designed to fetch, store, and summarize weather data for various cities using the OpenWeatherMap API. It provides functionality to retrieve real-time weather information and generate daily weather summaries.

## Features
- Fetch real-time weather data for any city.
  
- Store detailed weather information, including
- temperature,
- humidity,
- wind speed, and
- weather conditions
- feels_Like
- Date and Time.
  
- Generate daily weather summaries, including:
  - Average temperature
  - Maximum temperature
  - Minimum temperature
  - Dominant weather condition
    
- Retrieve weather summaries for specific cities.
  
- Use Hibernate with Spring Data JPA for database interactions.
  
- Threshold Alerts: Implement thresholds for various weather parameters to send alerts based on predefined criteria.


  ## Topics Covered
- Spring Boot Framework: The core framework used to build the application.
- RESTful API Integration: Using the RestTemplate to consume external weather data.
- Hibernate & JPA: Object-Relational Mapping (ORM) to interact with the MySQL database.
- Data Validation: Implementing checks to ensure the integrity of fetched weather data.
- Service Layer: Designing services to handle business logic for weather data management.
- Data Summarization: Aggregating weather data to generate daily summaries.
- Error Handling: Implementing exception handling for API calls and database operations.


## Technologies Used
- Java 21
- Spring Boot
- Spring Data JPA
- Hibernate
- MySQL
- OpenWeatherMap API
- RESTful Web Services
- Lombok
- Jakarta Persistence

## Getting Started

### Prerequisites
- Java 21
- MySQL Database
- Maven
- An OpenWeatherMap API key

### Installation
1. Clone the repository:
  
   git clone https://github.com/yourusername/weather-monitoring.git
   cd weather-monitoring


## Configuration of MySQL database:

Create a new database named weather_monitoring.
Update the application.properties file with your database connection details.

spring.datasource.url=jdbc:mysql://localhost:3306/weather_monitoring

spring.datasource.username=your_username

spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update


## Add your OpenWeatherMap API key in the application.properties:

weather.api.key=your_api_key
If you do not have an API key, you can obtain one by signing up at [OpenWeatherMap](https://openweathermap.org/).

 update your api key at WeatherService also
  private final String API_KEY = "your_api_key";


Build the project using Maven:

mvn clean install

Run the application:
mvn spring-boot:run

Usage
To fetch weather data for a specific city, use the following endpoint:

 http://localhost:8080/weather/{city}

To retrieve daily weather summaries for a specific city, use the endpoint:

GET http://localhost:8080/summary/{city}

Index Page: Access the main page of the application at:

http://localhost:8080/index

Threshold Management: To view or set threshold values for weather parameters, use:

http://localhost:8080/threshold


## API Endpoints
GET /weather/{city}: Fetches current weather data for the specified city.
GET /summary/{city}: Retrieves the daily weather summary for the specified city.
GET /index: Access the main page of the application.
GET /threshold: To view or set threshold values for weather parameters.


Database Schema
The application uses the following tables:

daily_weather_summary

Column Name     	        Data Type

id	                      BIGINT

date	                    DATE

average_temperature	      DOUBLE

max_temperature	          DOUBLE

min_temperature	          DOUBLE

dominant_condition	      VARCHAR(255)

city	                    VARCHAR(255)


    
weather_data


Column Name	        Data Type

id	                BIGINT

date_time	          DATETIME

city	              VARCHAR(255)

temperature	        DOUBLE

feels_like	        DOUBLE

weather_condition	  VARCHAR(255)

wind_speed	        DOUBLE

humidity	          INT



Acknowledgements

OpenWeatherMap API for weather data.

Spring Boot and Hibernate for simplifying database operations.


