package com.example.calculatron

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.calculatron.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val random = Random()


    private var tiempoRestante = 0 // Inicializar con 0 para evitar nullPointerException
    private var acertadas = 0 //Inicializar con 0 para evitar excepcion
    private var falladas = 0 //Inicializar con 0 para evitar excepcion

    //Maximo y minimo
    private var maximo = 10 // Inicializo a 10 para evitar excepcion
    private var minimo = 0 // Inicializo a 0 para evitar excepcion

    //Cuenta actual
    private var operandoActual1 = 0
    private var operandoActual2 = 0

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
        //Obtener el maximo predeterminado guardado en el sharedpreferences
        maximo = sharedPreferences.getInt(getString(R.integer.valor_maximo_predeterminado), 0)

        //Obtener el minimo predeterminado guardado en el sharedpreferences
        minimo = sharedPreferences.getInt(getString(R.integer.valor_minimo_predeterminado), 0)

        binding.ivOpciones.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(intent)
        }
    }
    fun generarOperacion(): String {
        val operacionesPermitidas = getResources().getStringArray(R.array.operaciones_permitidas)
        val randomOperationIndex = random.nextInt(operacionesPermitidas.size)
        return operacionesPermitidas[randomOperationIndex]
    }

    //0 -> primer operando
    //1 -> segundo operando
    fun generarOperandos(): Array<Int>{
        //Me genera los numeros que estan entre el minimo y el maximo
        operandoActual1 = random.nextInt(maximo - minimo + 1) + minimo
        operandoActual2 = random.nextInt(maximo - minimo + 1) + minimo
        return arrayOf(operandoActual1, operandoActual2)
    }

    fun generarCuenta(){
        //Genero el string de la cuenta completa
    }
    fun obtenerRusultado(){
        //Me genera el resultado de la cuenta para comprobar el resultado
    }
}