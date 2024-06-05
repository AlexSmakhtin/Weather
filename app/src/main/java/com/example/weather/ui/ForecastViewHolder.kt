package com.example.weather.ui

import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.domain.DailyForecast
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
    private val temperatureTextView: TextView = itemView.findViewById(R.id.temperatureTextView)
    private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)

    @RequiresApi(Build.VERSION_CODES.O)
    fun bind(forecast: DailyForecast) {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(forecast.dt), ZoneId.systemDefault())
        dateTextView.text = localDateTime.format(formatter)
        temperatureTextView.text = buildString {
            append("Temperature: ")
            append(forecast.temp.day.toString())
            append("°C")
        }
        descriptionTextView.text = forecast.weather.firstOrNull()?.description ?: ""
    }
}