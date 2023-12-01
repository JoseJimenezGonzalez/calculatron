package com.example.calculatron

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.calculatron.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var tiempoRestante = 20000


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        object : CountDownTimer(tiempoRestante.toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                binding.tvTiempoRestante.setText("Tiempo restante: " + millisUntilFinished / 1000)
            }

            override fun onFinish() {
                binding.tvTiempoRestante.setText("Se ha agotado el tiempo!")
            }
        }.start()
    }
}