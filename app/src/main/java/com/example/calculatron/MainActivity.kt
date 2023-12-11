package com.example.calculatron

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.calculatron.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var tiempoRestante = 0 // Inicializar con 0 para evitar nullPointerException
    private var acertadas = 0 //Inicializar con 0 para evitar excepcion
    private var falladas = 0 //Inicializar con 0 para evitar excepcion
    private lateinit var sharedPreferences: SharedPreferences


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        sharedPreferences = getSharedPreferences("nombrePref", Context.MODE_PRIVATE)

        // Obtener el tiempo predeterminado guardado en SharedPreferences
        tiempoRestante = sharedPreferences.getInt(getString(R.integer.tiempo_predeterminado), 20000)

        object : CountDownTimer(tiempoRestante.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.tvTiempoRestante.text = "Tiempo restante: " + millisUntilFinished / 1000
            }

            override fun onFinish() {
                binding.tvTiempoRestante.text = "Se ha agotado el tiempo!"
            }
        }.start()

        //Obtener las acertadas predeterminadas guardadas en el sharedpreferences
        acertadas = sharedPreferences.getInt(getString(R.integer.acertadas_predeterminado), 0)
        binding.tvAcertadas.text = "Acertadas: " + acertadas.toString()
        //Obtener las falladas predeterminadas guardadas en el sharedpreferences
        falladas = sharedPreferences.getInt(getString(R.integer.falladas_predeterminado), 0)
        binding.tvFalladas.text = "Falladas: " + falladas.toString()

        binding.ivOpciones.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(intent)
        }
    }
}