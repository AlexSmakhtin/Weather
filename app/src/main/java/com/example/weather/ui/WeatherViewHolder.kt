package com.example.weather.ui

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R

class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.cityNameTextView)
        val weather: TextView = itemView.findViewById(R.id.temperatureTextView)
}