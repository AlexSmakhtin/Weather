package com.example.weather.domain

data class City(
    val name: String,
    val coord: Coord
)

data class Coord(
    val lat: Double,
    val lon: Double
)

data class DailyForecast(
    val dt: Long,
    val temp: Temperature,
    val weather: List<WeatherDescription>,
    val pressure: Double,
    val humidity: Int,
    val wind: Wind,
    val clouds: Int,
    val rain: Double? = null,
    val snow: Double? = null,
    val pop: Double
)

data class Temperature(
    val day: Double,
    val min: Double,
    val max: Double,
    val night: Double,
    val eve: Double,
    val morn: Double
)

data class Wind(
    val speed: Double,
    val deg: Int,
    val gust: Double? = null
)