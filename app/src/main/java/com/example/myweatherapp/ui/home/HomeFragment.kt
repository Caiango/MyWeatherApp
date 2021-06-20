package com.example.myweatherapp.ui.home

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.data.Lista
import com.example.myweatherapp.data.Resp
import com.example.myweatherapp.databinding.FragmentHomeBinding
import com.example.myweatherapp.model.DatabaseInstance
import com.example.myweatherapp.model.FavouriteCity
import com.example.myweatherapp.model.FavouriteCityDao
import com.example.myweatherapp.service.Call
import com.example.myweatherapp.ui.adapter.WeatherAdapter
import com.example.myweatherapp.utils.SharedPrefsConfig
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!
    private lateinit var btnSearch: Button
    private lateinit var progressbar: ProgressBar
    private lateinit var inputCity: TextInputLayout
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: WeatherAdapter
    private lateinit var cityList: List<Lista>
    private lateinit var temp: String
    private lateinit var shared: SharedPrefsConfig

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupUI()
        setListeners(root.context)
        shared = SharedPrefsConfig(requireContext())
        temp = shared.getFromSharedPrefs()
        return root
    }

    private fun setupUI() {
        btnSearch = binding.btnSearch
        progressbar = binding.progressBar
        inputCity = binding.txtxInputCity
        recycler = binding.rv
        recycler.layoutManager = LinearLayoutManager(requireContext())
        recycler.setHasFixedSize(true)
    }

    private fun setListeners(context: Context) {
        btnSearch.setOnClickListener {
            if (isInternetAvailable(context)) {
                val cidade = inputCity.editText?.text.toString()
                if (cidade.equals("")) {
                    inputCity.editText?.error = "Insira uma Cidade"
                } else {
                    val finalTemp = when (temp) {
                        "C" -> {
                            "metric"
                        }
                        "F" -> {
                            "imperial"
                        }
                        else -> {
                            ""
                        }
                    }
                    progressbar.visibility = View.VISIBLE
                    Call.callByCityName(finalTemp, cidade, context, this::callBackFromSearch)
                }
            } else {
                Toast.makeText(context, "Sem conexão com internet", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun callBackFromSearch(response: Resp?, context: Context, success: Boolean) {
        if (success) {
            cityList = response!!.list
            adapter = WeatherAdapter(response.list, this::setFavourite, context)
            recycler.adapter = adapter
            progressbar.visibility = View.INVISIBLE
        } else {
            progressbar.visibility = View.INVISIBLE
        }
    }

    private fun setFavourite(position: Int) {
        val db: FavouriteCityDao? = DatabaseInstance.getInstance(this.requireContext())?.weatherDao
        val element = cityList[position]
        progressbar.visibility = View.VISIBLE
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db?.insert(
                    FavouriteCity(
                        element.id,
                        element.name,
                        element.main.temp,
                        element.weather[0].icon
                    )
                )
                Log.d(getString(R.string.success), db?.getAllFavouriteCities().toString())
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.favourited),
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
            withContext(Dispatchers.Main) {
                progressbar.visibility = View.INVISIBLE
            }
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false

        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager

        cm.getNetworkCapabilities(cm.activeNetwork)?.run {
            result = when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }

        return result
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}