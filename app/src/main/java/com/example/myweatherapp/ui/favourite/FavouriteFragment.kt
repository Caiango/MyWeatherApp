package com.example.myweatherapp.ui.favourite

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.data.RespFromID
import com.example.myweatherapp.databinding.FragmentFavouriteBinding
import com.example.myweatherapp.model.DatabaseInstance
import com.example.myweatherapp.model.FavouriteCity
import com.example.myweatherapp.model.FavouriteCityDao
import com.example.myweatherapp.service.Call
import com.example.myweatherapp.ui.adapter.FavouriteAdapter
import com.example.myweatherapp.utils.SharedPrefsConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteFragment : Fragment() {

    private lateinit var favouriteViewModel: FavouriteViewModel
    private var _binding: FragmentFavouriteBinding? = null
    private lateinit var rv: RecyclerView
    private lateinit var adapter: FavouriteAdapter
    private lateinit var favouriteListFromRoom: List<FavouriteCity>
    private lateinit var favouriteListToAdapter: ArrayList<FavouriteCity>
    private lateinit var progressbar: ProgressBar
    private lateinit var temp: String
    private lateinit var db: FavouriteCityDao
    private lateinit var shared: SharedPrefsConfig

    companion object {
        val list: MutableLiveData<List<FavouriteCity>> = MutableLiveData()
    }

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        favouriteViewModel =
            ViewModelProvider(this).get(FavouriteViewModel::class.java)

        _binding = FragmentFavouriteBinding.inflate(inflater, container, false)
        val root: View = binding.root
        db = DatabaseInstance.getInstance(this.requireContext())?.weatherDao!!
        shared = SharedPrefsConfig(requireContext())

        setupUI()

        temp = shared.getFromSharedPrefs()
        favouriteListToAdapter = ArrayList()

        favouriteViewModel.list.observe(viewLifecycleOwner, { it ->
            adapter = FavouriteAdapter(
                it.sortedBy { it.name },
                this@FavouriteFragment::deleteItemCallback,
                root.context.applicationContext
            )
            rv.adapter = adapter
            adapter.notifyDataSetChanged()
        })

        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                favouriteListFromRoom = db.getAllFavouriteCities()
                updateFavouriteCity()
                withContext(Dispatchers.Main) {
                    Log.d("SUCESSO CONSULTA", db.getAllFavouriteCities().toString())
                    progressbar.visibility = View.INVISIBLE
                }
            }
        }
        return root
    }

    private fun setupUI() {
        progressbar = binding.progressBar
        rv = binding.rv
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.setHasFixedSize(true)
        progressbar.visibility = View.VISIBLE
    }

    private fun updateFavouriteCity() {
        favouriteListFromRoom.forEach {
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
            Call.callByCityId(
                finalTemp,
                it.id.toString(),
                requireContext(),
                this@FavouriteFragment::callBackFromSearch
            )
        }
    }

    private fun callBackFromSearch(response: RespFromID?, context: Context, success: Boolean) {
        if (success) {
            val cidade = FavouriteCity(
                response?.id!!.toInt(),
                response.name,
                response.main.temp,
                response.weather[0].icon
            )
            favouriteListToAdapter.add(cidade)
        } else {
            Toast.makeText(context, getString(R.string.failed), Toast.LENGTH_LONG).show()
        }
        list.postValue(favouriteListToAdapter)
        progressbar.visibility = View.INVISIBLE
    }

    private fun deleteItemCallback(position: Int) {
        var organizedList = favouriteListToAdapter.sortedBy { it.name }
        val element = organizedList[position]
        progressbar.visibility = View.VISIBLE
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.delete(element)
                favouriteListToAdapter = db.getAllFavouriteCities() as ArrayList<FavouriteCity>
                list.postValue(favouriteListToAdapter)
                Log.d(getString(R.string.removed_success), db.getAllFavouriteCities().toString())
                withContext(Dispatchers.Main) {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.removed_from_fav),
                        Toast.LENGTH_LONG
                    ).show()
                }

            }
            withContext(Dispatchers.Main) {
                progressbar.visibility = View.INVISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}