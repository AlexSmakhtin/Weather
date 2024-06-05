package com.example.weather.data

import com.example.weather.domain.DailyForecast
import com.example.weather.domain.Weather
import kotlin.math.roundToInt

class WeatherRepository(private val apiService: ApiService) {
    suspend fun getWeatherByLocation(lat: Double, lon: Double, apiKey: String): Weather {
        val response = apiService.getWeather(lat, lon, apiKey)
        val temperatureCelsius = response.main.temp - 273.15
        response.main.temp = temperatureCelsius.roundToInt().toDouble()
        return Weather(response.name, response.weather, response.main)
    }

    suspend fun getWeatherForecast(lat: Double, lon: Double, apiKey: String): List<DailyForecast> {
        val response = apiService.getForecast(lat, lon, 7, apiKey)
        return response.list
    }
}