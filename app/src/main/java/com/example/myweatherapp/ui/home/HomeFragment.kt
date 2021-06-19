package com.example.myweatherapp.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentHomeBinding
import com.example.myweatherapp.repository.City
import com.example.myweatherapp.repository.RetrofitInitializer
import com.google.android.material.textfield.TextInputLayout
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var btnSearch: Button
    private lateinit var progressbar: ProgressBar
    private lateinit var cityName: TextView
    private lateinit var cityID: TextView
    private lateinit var inputCity: TextInputLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        btnSearch = root.findViewById<Button>(R.id.btnSearch)
        progressbar = root.findViewById(R.id.progressBar)
        cityID = root.findViewById(R.id.txtId)
        cityName = root.findViewById(R.id.txtName)
        inputCity = root.findViewById(R.id.txtxInputCity)
        setListeners(root.context)

        return root
    }

    private fun setListeners(context: Context) {
        btnSearch.setOnClickListener {
            if (isInternetAvailable(context)) {
                val cidade = inputCity.editText?.text.toString()
                if (cidade.equals("")) {
                    inputCity.editText?.error = "Insira uma Cidade"
                } else {
                    call(cidade)
                }
            } else {
                Toast.makeText(context, "Sem conexão com internet", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun call(cidade: String) {
        progressbar.visibility = View.VISIBLE
        val call = RetrofitInitializer().repoService().getWeather(cidade)

        call.enqueue(object : Callback<City> {
            override fun onResponse(call: retrofit2.Call<City>, response: Response<City>) {
                response?.body()?.let {
                    val city: City = it
                    cityName.text = "Nome: ${city.list[0].name}"
                    cityID.text = "ID: ${city.list[0].id.toString()}"
                    progressbar.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: retrofit2.Call<City>, t: Throwable) {
                Log.d("erro", t.toString())
                progressbar.visibility = View.INVISIBLE
            }

        })
    }


    fun isInternetAvailable(context: Context): Boolean {
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