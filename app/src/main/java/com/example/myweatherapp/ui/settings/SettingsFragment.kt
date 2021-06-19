package com.example.myweatherapp.ui.settings

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.myweatherapp.R
import com.example.myweatherapp.databinding.SettingsFragmentBinding

class SettingsFragment : Fragment() {

    private lateinit var settingsViewModel: SettingsViewModel
    private var _binding: SettingsFragmentBinding? = null
    lateinit var sharedPref: SharedPreferences
    lateinit var editor: SharedPreferences.Editor

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var btn: Button
    private lateinit var radioC: RadioButton
    private lateinit var radioF: RadioButton
    private lateinit var langP: RadioButton
    private lateinit var langE: RadioButton
    var temp = ""
    var lang = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel =
            ViewModelProvider(this).get(SettingsViewModel::class.java)
        _binding = SettingsFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPref = root.context.getSharedPreferences(
            getString(R.string.shared_settings), Context.MODE_PRIVATE
        )
        editor = sharedPref.edit()
        setupUI(root)
        checkShared()
        return root
    }

    private fun checkShared() {
        if (sharedPref != null) {
            if (sharedPref.getString("temp", null) == "C") {
                radioC.isChecked = true
            } else if (sharedPref.getString("temp", null) == "F"){
                radioF.isChecked = true
            }
            if (sharedPref.getString("lang", null) == "P") {
                langP.isChecked = true
            } else if (sharedPref.getString("lang", null) == "E"){
                langE.isChecked = true
            }
        }
    }

    fun setupUI(root: View) {
        btn = root.findViewById(R.id.btnSave)
        btn.setOnClickListener { saveData() }
        radioC = root.findViewById(R.id.celsius)
        radioF = root.findViewById(R.id.far)
        langP = root.findViewById(R.id.en)
        langE = root.findViewById(R.id.pt)

    }

    private fun saveData() {
        if (radioC.isChecked) temp = "C"
        if (radioF.isChecked) temp = "F"
        if (langP.isChecked) lang = "P"
        if (langE.isChecked) lang = "E"
        editor.apply {
            putString("lang", lang)
            putString("temp", temp)
            apply()
        }
        Toast.makeText(context, "SALVO", Toast.LENGTH_LONG).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}