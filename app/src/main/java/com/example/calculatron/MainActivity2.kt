package com.example.calculatron

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.edit
import com.example.calculatron.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private val nombrePref = "mis_preferencias"
    var esTiempoCorrecto = false

    private lateinit var binding: ActivityMain2Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMain2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener una referencia a SharedPreferences
        val sharedPreferences = getSharedPreferences(nombrePref, Context.MODE_PRIVATE)

        // Inicializar CheckBoxes con valores de SharedPreferences
        binding.sumarCheckbox.isChecked = sharedPreferences.getBoolean("suma_permitida", true)
        binding.restarCheckbox.isChecked = sharedPreferences.getBoolean("resta_permitida", true)
        binding.multiplicarCheckbox.isChecked = sharedPreferences.getBoolean("multiplicacion_permitida", false)


        binding.btnGuardarConfiguracion.setOnClickListener {
            // Modificar el tiempo predeterminado
            val nuevoTiempoPredeterminadoStr = binding.tietCuentaAtras.text.toString()
            // Modificar el valor maximo predeterminado
            val nuevoValorMaximoStr = binding.tietMaximo.text.toString()
            // Modificar el valor minimo predeterminado
            val nuevoValorMinimoStr = binding.tietMinimo.text.toString()

            // Guardar estado de las CheckBoxes
            sharedPreferences.edit {
                if (nuevoTiempoPredeterminadoStr != ""){
                    val nuevoTiempoPredeterminado = binding.tietCuentaAtras.text.toString().toInt() * 1000
                    putInt("tiempo_predeterminado", nuevoTiempoPredeterminado)
                }
                if(nuevoValorMaximoStr != ""){
                    val nuevoValorMaximo = binding.tietMaximo.text.toString().toInt()
                    putInt("valor_maximo_predeterminado", nuevoValorMaximo)
                }
                if(nuevoValorMinimoStr != ""){
                    val nuevoValorMinimo = binding.tietMinimo.text.toString().toInt()
                    putInt("valor_minimo_predeterminado", nuevoValorMinimo)
                }
                putBoolean("suma_permitida", binding.sumarCheckbox.isChecked)
                putBoolean("resta_permitida", binding.restarCheckbox.isChecked)
                putBoolean("multiplicacion_permitida", binding.multiplicarCheckbox.isChecked)
                apply()
            }

            // Volver a la MainActivity
            val intent = Intent(this@MainActivity2, MainActivity::class.java)
            startActivity(intent)
        }
    }
}