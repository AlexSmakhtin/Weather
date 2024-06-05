package com.example.weather.ui

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.weather.R
import com.example.weather.domain.DailyForecast


class ForecastAdapter : RecyclerView.Adapter<ForecastViewHolder>() {

    private var forecastList = listOf<DailyForecast>()

    fun submitList(list: List<DailyForecast>) {
        forecastList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.forecast, parent, false)
        return ForecastViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val forecast = forecastList[position]
        holder.bind(forecast)
    }

    override fun getItemCount() = forecastList.size

}