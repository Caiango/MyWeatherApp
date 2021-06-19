package com.example.myweatherapp.ui.favourite

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.FragmentFavouriteBinding
import com.example.myweatherapp.model.DatabaseInstance
import com.example.myweatherapp.model.FavouriteCity
import com.example.myweatherapp.model.FavouriteCityDao
import com.example.myweatherapp.ui.adapter.FavouriteAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouriteFragment : Fragment() {

    private lateinit var favouriteViewModel: FavouriteViewModel
    private var _binding: FragmentFavouriteBinding? = null
    private lateinit var rv: RecyclerView
    private lateinit var adapter: FavouriteAdapter
    private lateinit var favouriteList: List<FavouriteCity>
    private lateinit var progressbar: ProgressBar

    companion object {
        val list: MutableLiveData<List<FavouriteCity>> = MutableLiveData()
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
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
        progressbar = root.findViewById(R.id.progressBar)
        rv = root.findViewById(R.id.rv)
        rv.layoutManager = LinearLayoutManager(root.context)
        rv.setHasFixedSize(true)

        favouriteViewModel.list.observe(viewLifecycleOwner, Observer {
            adapter = FavouriteAdapter(
                it,
                this@FavouriteFragment::deleteItem,
                root.context.applicationContext
            )
            rv.adapter = adapter
            adapter.notifyDataSetChanged()
        })
        progressbar.visibility = View.VISIBLE
        val db: FavouriteCityDao? = DatabaseInstance.getInstance(this.requireContext())?.weatherDao
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                favouriteList = db?.getAllFavouriteCities()!!
                withContext(Dispatchers.Main) {
                    list.postValue(favouriteList)
                    Log.d("SUCESSO CONSULTA", db?.getAllFavouriteCities().toString())
                    progressbar.visibility = View.INVISIBLE
                }
            }
        }
        return root
    }

    fun deleteItem(position: Int) {
        val db: FavouriteCityDao? = DatabaseInstance.getInstance(this.requireContext())?.weatherDao
        val element = favouriteList[position]
        progressbar.visibility = View.VISIBLE
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db?.delete(element)
                favouriteList = db?.getAllFavouriteCities()!!
                list.postValue(favouriteList)
                Log.d("SUCESSO REMOÇÃO", db?.getAllFavouriteCities().toString())
            }
            withContext(Dispatchers.Main) {
                progressbar.visibility = View.INVISIBLE
                rv.adapter?.notifyItemChanged(position)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}