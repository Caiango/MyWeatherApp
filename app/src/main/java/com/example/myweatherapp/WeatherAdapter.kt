package com.example.myweatherapp

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myweatherapp.data.Lista
import com.example.myweatherapp.databinding.WeatherItemsBinding

class WeatherAdapter(
    private val files: List<Lista>,
//    private val callback: (Int) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<WeatherAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.weather_items, parent, false)
        val binding = WeatherItemsBinding.bind(view)
        val viewHolder = ViewHolder(binding)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val fileName = files[position]

        holder.fileName.text = fileName.name
        holder.temp.text = fileName.main.temp
        val icon: String = fileName.weather[0].icon
        Glide.with(context).load("http://openweathermap.org/img/wn/$icon@4x.png").into(holder.img)

    }

    override fun getItemCount(): Int = files.size


    class ViewHolder(view: WeatherItemsBinding) : RecyclerView.ViewHolder(view.root) {
        val fileName = view.txtCity
        val temp = view.txtTemp
        val img = view.imageView

    }
}