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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.WeatherAdapter
import com.example.myweatherapp.data.Resp
import com.example.myweatherapp.databinding.FragmentHomeBinding
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

        return root
    }

    private fun setListeners(context: Context) {
        btnSearch.setOnClickListener {
            if (isInternetAvailable(context)) {
                val cidade = inputCity.editText?.text.toString()
                if (cidade.equals("")) {
                    inputCity.editText?.error = "Insira uma Cidade"
                } else {
                    call(cidade, context)
                }
            } else {
                Toast.makeText(context, "Sem conexão com internet", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun call(cidade: String, context: Context) {
        progressbar.visibility = View.VISIBLE
        val call = RetrofitInitializer().repoService().getWeather(cidade)

        call.enqueue(object : Callback<Resp> {
            override fun onResponse(call: retrofit2.Call<Resp>, resp: Response<Resp>) {
                resp?.body()?.let {
                    val reponse: Resp = it
                    adapter = WeatherAdapter(reponse.list, context)
                    recycler.adapter = adapter
                    progressbar.visibility = View.INVISIBLE
                }
            }

            override fun onFailure(call: retrofit2.Call<Resp>, t: Throwable) {
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