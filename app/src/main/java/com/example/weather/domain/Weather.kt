package com.example.weather.domain

data class Weather(
    val name: String,
    val weather: List<WeatherDescription>,
    val main: MainInfo
)

data class WeatherDescription(
    val main: String,
    val description: String
)

data class MainInfo(
    var temp: Double
)