package com.example.calculatron

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.edit
import com.example.calculatron.databinding.ActivityMain2Binding
import com.example.calculatron.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityMain2Binding
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener una referencia a SharedPreferences
        val sharedPreferences: SharedPreferences = getSharedPreferences("nombrePref", Context.MODE_PRIVATE)

        binding.btnGuardarConfiguracion.setOnClickListener {
            // Modificar el tiempo predeterminado
            val nuevoTiempoPredeterminado = binding.tietCuentaAtras.text.toString().toInt() * 1000
            val claveTiempoPredeterminado = getString(R.integer.tiempo_predeterminado)
            sharedPreferences.edit {
                putInt(claveTiempoPredeterminado, nuevoTiempoPredeterminado)
            }
        }
    }
}