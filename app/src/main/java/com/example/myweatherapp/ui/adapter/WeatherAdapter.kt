package com.example.myweatherapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myweatherapp.R
import com.example.myweatherapp.data.Lista
import com.example.myweatherapp.databinding.WeatherItemsBinding

class WeatherAdapter(
    private val cities: List<Lista>,
    private val callback: (Int) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.weather_items, parent, false)
        val binding = WeatherItemsBinding.bind(view)
        val viewHolder = ViewHolder(binding)
        viewHolder.fav.setOnClickListener {
            val position = viewHolder.adapterPosition
            callback(position)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val city = cities[position]

        holder.fileName.text = city.name
        holder.temp.text = "${city.main.temp} ºC"
        val icon: String = city.weather[0].icon
        Glide.with(context).load("http://openweathermap.org/img/wn/$icon@4x.png").into(holder.img)

    }

    override fun getItemCount(): Int = cities.size


    class ViewHolder(view: WeatherItemsBinding) : RecyclerView.ViewHolder(view.root) {
        val fileName = view.txtCity
        val temp = view.txtTemp
        val img = view.imageView
        val fav = view.imgFav

    }
}