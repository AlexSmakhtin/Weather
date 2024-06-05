package com.example.weather.data
import com.example.weather.domain.City
import com.example.weather.domain.DailyForecast
import com.example.weather.domain.MainInfo
import com.example.weather.domain.Weather
import com.example.weather.domain.WeatherDescription
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("data/2.5/weather")
    suspend fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") appid: String
    ): WeatherResponse

    @GET("data/2.5/forecast/daily")
    suspend fun getForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("cnt") cnt: Int = 7,
        @Query("appid") appid: String,
        @Query("units") units: String = "metric"
    ): ForecastResponse

    companion object {
        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}

data class WeatherResponse(
    val name: String,
    val weather: List<WeatherDescription>,
    val main: MainInfo
)

data class ForecastResponse(
    val city: City,
    val list: List<DailyForecast>
)

