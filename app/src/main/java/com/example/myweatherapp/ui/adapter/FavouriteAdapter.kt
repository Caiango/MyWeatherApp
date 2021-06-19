package com.example.myweatherapp.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myweatherapp.R
import com.example.myweatherapp.data.Lista
import com.example.myweatherapp.databinding.FavouriteItemsBinding
import com.example.myweatherapp.databinding.WeatherItemsBinding
import com.example.myweatherapp.model.FavouriteCity

class FavouriteAdapter(
    private val cities: List<FavouriteCity>,
    private val callback: (Int) -> Unit,
    private val context: Context
) : RecyclerView.Adapter<FavouriteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.favourite_items, parent, false)
        val binding = FavouriteItemsBinding.bind(view)
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
        holder.temp.text = "${city.temp} ºC"
        val icon: String = city.img
        Glide.with(context).load("http://openweathermap.org/img/wn/$icon@4x.png").into(holder.img)

    }

    override fun getItemCount(): Int = cities.size


    class ViewHolder(view: FavouriteItemsBinding) : RecyclerView.ViewHolder(view.root) {
        val fileName = view.txtCity
        val temp = view.txtTemp
        val img = view.imageView
        val fav = view.imgDel

    }
}