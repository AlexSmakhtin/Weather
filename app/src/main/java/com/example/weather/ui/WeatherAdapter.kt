package com.example.weather.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.domain.Weather

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    private var weatherList = listOf<Weather>()

    fun submitList(list: List<Weather>) {
        weatherList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_weather, parent, false)
        return WeatherViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val weather = weatherList[position]
        holder.bind(weather)
    }

    override fun getItemCount() = weatherList.size

    class WeatherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView = itemView.findViewById(R.id.cityNameTextView)
        private val temperatureTextView: TextView = itemView.findViewById(R.id.temperatureTextView)

        fun bind(weather: Weather) {
            dateTextView.text =buildString {
                append("City: ")
                append(weather.name)
            }
            temperatureTextView.text = buildString {
                append("Temperature: ")
                append(weather.main.temp.toString())
                append("°C")
            }
        }
    }
}