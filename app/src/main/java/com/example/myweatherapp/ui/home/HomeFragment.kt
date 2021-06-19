package com.example.myweatherapp.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.WeatherAdapter
import com.example.myweatherapp.data.Resp
import com.example.myweatherapp.databinding.FragmentHomeBinding
import com.example.myweatherapp.db.DatabaseInstance
import com.example.myweatherapp.db.FavouriteCity
import com.example.myweatherapp.db.FavouriteCityDao
import com.example.myweatherapp.repository.RetrofitInitializer
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
    private lateinit var inputCity: TextInputLayout
    private lateinit var recycler: RecyclerView
    private lateinit var adapter: WeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        btnSearch = root.findViewById(R.id.btnSearch)
        progressbar = root.findViewById(R.id.progressBar)
        inputCity = root.findViewById(R.id.txtxInputCity)
        recycler = root.findViewById(R.id.rv)
        recycler.layoutManager = LinearLayoutManager(root.context)
        recycler.setHasFixedSize(true)
        setListeners(root.context)

        val db: FavouriteCityDao? = DatabaseInstance.getInstance(this.requireContext())?.weatherDao
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db?.insert(FavouriteCity(1, "CIDADE", "23", "IMG"))
                Log.d("LISTA DO DAO", db?.getAllFavouriteCities().toString())
            }
            withContext(Dispatchers.Main) {
                progressbar.visibility = View.VISIBLE
            }
        }

        return root
    }

    private fun setListeners(context: Context) {
        btnSearch.setOnClickListener {
            if (isInternetAvailable(context)) {
                val cidade = inputCity.editText?.text.toString()
                if (cidade.equals("")) {
                    inputCity.editText?.error = "Insira uma Cidade"
                } else {
                    progressbar.visibility = View.VISIBLE
                    call(cidade, context)
                }
            } else {
                Toast.makeText(context, "Sem conexão com internet", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun call(cidade: String, context: Context) {
        val call = RetrofitInitializer().repoService().getWeather(cidade)

        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: retrofit2.Call<Resp>, resp: Response<Resp>) {
                resp?.body()?.let {
                    val reponse: Resp = it
                    callBackFromSearch(reponse, context, true)

//                    val db = DatabaseInstance.getInstance(context)
//                    lifecycleScope.launch {
//                        db?.weatherDao()?.getAllFavouriteCities()
//                        Log.d("LISTA DO DAO", db?.weatherDao()?.getAllFavouriteCities().toString())
//                    }

                }
            }

            override fun onFailure(call: retrofit2.Call<Resp>, t: Throwable) {
                Log.d("erro", t.toString())
                callBackFromSearch(null, context, false)
            }

        })
    }

    fun callBackFromSearch(response: Resp?, context: Context, success: Boolean) {
        if (success) {
            adapter = WeatherAdapter(response!!.list, context)
            recycler.adapter = adapter
            progressbar.visibility = View.INVISIBLE
        } else {
            progressbar.visibility = View.INVISIBLE
        }
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