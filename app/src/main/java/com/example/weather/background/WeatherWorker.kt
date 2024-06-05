package com.example.weather.background

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.weather.R
import com.example.weather.data.ApiService
import com.example.weather.data.WeatherRepository
import com.example.weather.domain.Weather
import com.example.weather.utils.LocationHelper
private const val YOUR_API_KEY = "8f942195c000742c92a82c8837d4684c"

class WeatherWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {
        val locationHelper = LocationHelper(applicationContext)
        val apiService = ApiService.create()
        val repository = WeatherRepository(apiService)

        val currentLocation = locationHelper.getCurrentLocation()
        return if (currentLocation != null) {
            val currentWeather = repository.getWeatherByLocation(
                currentLocation.latitude,
                currentLocation.longitude,
                YOUR_API_KEY
            )
            showWeatherNotification(currentWeather)
            Result.success()
        } else {
            Result.retry()
        }
    }

    @SuppressLint("MissingPermission")
    private fun showWeatherNotification(weather: Weather) {
        val builder = NotificationCompat.Builder(applicationContext, "WEATHER_CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Current Weather")
            .setContentText("${weather.weather[0].description}, ${String.format("%.1f", weather.main.temp)}°C")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            notify(1, builder.build())
        }
    }
}