package com.example.calculatron

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.core.content.edit
import com.example.calculatron.databinding.ActivityMain2Binding
import com.example.calculatron.databinding.ActivityMainBinding

class MainActivity2 : AppCompatActivity() {

    private val nombrePref = "mis_preferencias"

    private lateinit var binding: ActivityMain2Binding
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener una referencia a SharedPreferences
        val sharedPreferences = getSharedPreferences(nombrePref, Context.MODE_PRIVATE)

        binding.btnGuardarConfiguracion.setOnClickListener {
            //Hay problema si no cambio algo
            // Modificar el tiempo predeterminado
            val nuevoTiempoPredeterminado = binding.tietCuentaAtras.text.toString().toInt() * 1000
            val claveTiempoPredeterminado = getString(R.integer.tiempo_predeterminado)
            //Modificar el valor maximo predeterminado
            val nuevoValorMaximo = binding.tietMaximo.text.toString().toInt()
            val claveValorMaximo = getString(R.integer.valor_maximo_predeterminado)
            //Modificar el valor minimo predeterminado
            val nuevoValorMinimo = binding.tietMinimo.text.toString().toInt()
            val claveValorMinimo = getString(R.integer.valor_minimo_predeterminado)
            sharedPreferences.edit {
                putInt(claveTiempoPredeterminado, nuevoTiempoPredeterminado)
                putInt(claveValorMaximo, nuevoValorMaximo)
                putInt(claveValorMinimo, nuevoValorMinimo)
            }
            //Volver a la main activity
            val intent = Intent(this@MainActivity2, MainActivity::class.java)
            startActivity(intent)
        }
    }
}