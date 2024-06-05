package com.example.weather.ui

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Build.*
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.data.ApiService
import com.example.weather.data.WeatherRepository
import com.example.weather.domain.WeatherLocation
import com.example.weather.utils.LocationHelper
import kotlinx.coroutines.launch
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.weather.background.WeatherWorker
import com.example.weather.domain.Weather
import java.util.concurrent.TimeUnit
import kotlin.text.*

private const val YOUR_API_KEY = "8f942195c000742c92a82c8837d4684c"

class MainActivity : AppCompatActivity() {

    private lateinit var currentWeatherTextView: TextView
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: WeatherAdapter
    private lateinit var forecastRecyclerView: RecyclerView
    private lateinit var forecastAdapter: ForecastAdapter


    private val weatherLocations = listOf(
        WeatherLocation("Moscow", 55.7558, 37.6173),
        WeatherLocation("Saint Petersburg", 59.93065669438622, 30.351882254945682),
        WeatherLocation("Saint Petersburg", 54.993439, 73.368201)
    )

    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentWeatherTextView = findViewById(R.id.currentWeatherTextView)
        recyclerView = findViewById(R.id.recyclerView)
        forecastRecyclerView = findViewById(R.id.forecastRecyclerView)


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            initialize()
        }

        createNotificationChannel()
        setupPeriodicWeatherUpdate()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                initialize()
            } else {
                currentWeatherTextView.text = "Location permission denied"
            }
        }
    }

    private fun initialize() {
        adapter = WeatherAdapter()
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
        forecastAdapter = ForecastAdapter()
        forecastRecyclerView.layoutManager = LinearLayoutManager(this)
        forecastRecyclerView.adapter = forecastAdapter
        loadWeatherData()
    }

    private fun loadWeatherData() {
        val locationHelper = LocationHelper(this)
        lifecycleScope.launch {
            val apiService = ApiService.create()
            val repository = WeatherRepository(apiService)

            val currentLocation = locationHelper.getCurrentLocation()
            if (currentLocation != null) {
                val currentWeather = repository.getWeatherByLocation(
                    currentLocation.latitude,
                    currentLocation.longitude,
                    YOUR_API_KEY
                )
                currentWeatherTextView.text = buildString {
                    append("Current weather: ")
                    append(currentWeather.weather[0].description)
                    append(", ")
                    append(String.format("%.1f", currentWeather.main.temp))
                    append("°C")
                }
                showWeatherNotification(currentWeather)
//                val forecast = repository.getWeatherForecast(
//                    currentLocation.latitude,
//                    currentLocation.longitude,
//                    YOUR_API_KEY
//                )
 //               forecastAdapter.submitList(forecast)
            }

            val weatherList = weatherLocations.map {
                repository.getWeatherByLocation(it.lat, it.lon, YOUR_API_KEY)
            }

            adapter.submitList(weatherList)
        }
    }

    private fun setupPeriodicWeatherUpdate() {
        val workRequest = PeriodicWorkRequestBuilder<WeatherWorker>(15, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "WeatherUpdate",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }

    private fun createNotificationChannel() {
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
            val name = "Weather Channel"
            val descriptionText = "Channel for weather updates"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel("WEATHER_CHANNEL_ID", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showWeatherNotification(weather: Weather) {
        val builder = NotificationCompat.Builder(this, "WEATHER_CHANNEL_ID")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Current Weather")
            .setContentText("${weather.weather[0].description}, ${String.format("%.1f", weather.main.temp)}°C")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)) {
            if (ActivityCompat.checkSelfPermission(
                    this@MainActivity,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            notify(1, builder.build())
        }
    }
}
