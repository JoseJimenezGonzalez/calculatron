package com.example.calculatron

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import com.example.calculatron.databinding.ActivityMainBinding
import java.util.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val random = Random()


    private var tiempoRestante = 0 // Inicializar con 0 para evitar nullPointerException
    private var acertadas = 0
    private var falladas = 0

    // Nuevas variables para almacenar cuentas anteriores y siguientes
    private var operacionActual = ""
    var operandoActual1 = 0
    var operandoActual2 = 0

    private val nombrePref = "mis_preferencias"
    private lateinit var sharedPreferences: SharedPreferences


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences(nombrePref, Context.MODE_PRIVATE)

        //Teclado y entrada de datos de usuario
        val edtInput: EditText = findViewById(R.id.edtEntradaUsuario)
        val gridLayout: GridLayout = findViewById(R.id.gridLayout)

        // Iterar a través de los botones en el GridLayout
        for (i in 0 until gridLayout.childCount) {
            val button = gridLayout.getChildAt(i) as? Button

            // Asignar la función onButtonClick a los botones numéricos y operadores
            button?.setOnClickListener {
                onButtonClick(it, edtInput)
            }
        }

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

        binding.ivOpciones.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(intent)
        }

        //Meter codigo


        operandoActual1 = generarOperandos()
        operandoActual2 = generarOperandos()
        generarOperacion()

        Log.e("operacion actual", operacionActual)
        Log.e("operandos actuales", "op1$operandoActual1, op2$operandoActual2")

        binding.tvPrimerOperando.text = operandoActual1.toString()
        binding.tvSegundoOperando.text = operandoActual2.toString()
        binding.tvOperacion.text = operacionActual
    }
    fun generarOperacion(){
        val operacionesPermitidas = getResources().getStringArray(R.array.operaciones_permitidas)
        val randomOperationIndex = random.nextInt(operacionesPermitidas.size)
        Log.e("generarOperacion()", "Return: ${operacionesPermitidas[randomOperationIndex]}")
        operacionActual = operacionesPermitidas[randomOperationIndex]
    }

    fun generarOperandos(): Int {
        //Obtener el maximo predeterminado guardado en el sharedpreferences
        var maximo = sharedPreferences.getInt("valor_maximo_predeterminado", 10)

        //Obtener el minimo predeterminado guardado en el sharedpreferences
        var minimo = sharedPreferences.getInt("valor_minimo_predeterminado", 0)
        //Me genera los numeros que están entre el minimo y el maximo
        return random.nextInt(maximo - minimo + 1) + minimo
    }




    fun comprobarResultado(respuestaUsuario: Int) {
        when (operacionActual) {
            "+" -> {
                val calculoResultado = operandoActual1 + operandoActual2
                if(respuestaUsuario == calculoResultado){
                    acertadas++
                    binding.tvAcertadas.text = "Acertadas: $acertadas"
                }else{
                    falladas++
                    binding.tvFalladas.text = "Falladas: $falladas"
                }
            }
            "-" -> {
                val calculoResultado = operandoActual1 - operandoActual2
                if(respuestaUsuario == calculoResultado){
                    acertadas++
                    binding.tvAcertadas.text = "Acertadas: $acertadas"
                }else{
                    falladas++
                    binding.tvFalladas.text = "Falladas: $falladas"
                }
            }
            "*" -> {
                val calculoResultado = operandoActual1 * operandoActual2
                if(respuestaUsuario == calculoResultado){
                    acertadas++
                    binding.tvAcertadas.text = "Acertadas: $acertadas"
                }else{
                    falladas++
                    binding.tvFalladas.text = "Falladas: $falladas"
                }
            }
        }
    }











    //teclado

    fun onButtonClick(view: View, editText: EditText) {
        if (view is Button) {
            val buttonText = view.text.toString()
            val currentText = editText.text.toString()

            when (buttonText) {
                "=" -> {
                    // Realizar cálculos cuando se presiona el botón "="
                    if (currentText.isNotEmpty()) {
                        val respuestaUsuario = currentText.toInt()
                        comprobarResultado(respuestaUsuario)
                    }
                    // Limpiar el texto del EditText
                    editText.setText("")
                }
                "C" -> {
                    // Limpiar el texto del EditText y resetear la operación actual
                    editText.setText("")
                    operacionActual = ""
                }
                "BS" -> {
                    // Eliminar el último carácter cuando se presiona el botón "BS"
                    if (currentText.isNotEmpty()) {
                        editText.setText(currentText.substring(0, currentText.length - 1))
                    }
                }
                else -> {
                    // Agregar el texto del botón al EditText y actualizar la operación actual
                    editText.setText(currentText + buttonText)
                    operacionActual = buttonText
                }
            }
        }
    }
}