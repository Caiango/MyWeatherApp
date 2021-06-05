package com.example.myweatherapp.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: SettingsFragmentBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var btn: Button
    private lateinit var radioC: RadioButton
    private lateinit var radioF: RadioButton
    private lateinit var langP: RadioButton
    private lateinit var langE: RadioButton
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)

        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setupUI(root)

        //val textView: TextView = binding.textNotifications
        settingsViewModel.text.observe(viewLifecycleOwner, Observer {
            //textView.text = it
        })

        return root
    }

    fun setupUI(root: View){
        btn = root.findViewById(R.id.btnSave)
        radioC = root.findViewById(R.id.celsius)
        radioF = root.findViewById(R.id.far)
        langP = root.findViewById(R.id.en)
        langE = root.findViewById(R.id.pt)
        radioC.setOnClickListener { checkTemp(radioC) }
        radioF.setOnClickListener { checkTemp(radioF) }
        langP.setOnClickListener { checkLang(langP) }
        langE.setOnClickListener { checkLang(langE) }
    }

    fun checkTemp(view: View) {
        when (view.id) {
            R.id.celsius -> {
                Toast.makeText(context, "Celcius", Toast.LENGTH_LONG).show()
            }
            R.id.far ->{
                Toast.makeText(context, "Farenheit", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun checkLang(view: View) {
        when (view.id) {
            R.id.en -> {
                Toast.makeText(context, "English", Toast.LENGTH_LONG).show()
            }
            R.id.pt ->{
                Toast.makeText(context, "Portugues", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}