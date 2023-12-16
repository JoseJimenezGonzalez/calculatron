package com.example.calculatron

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.preference.PreferenceManager
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

    //Maximo y minimo
    var maximo = 10 // Inicializo a 10 para evitar excepcion
    var minimo = 0 // Inicializo a 0 para evitar excepcion

    // Nuevas variables para almacenar cuentas anteriores y siguientes
    private var operacionActual = ""
    private var operandosActuales = arrayOf(0, 0)

    private val nombrePref = "mis_preferencias"


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPreferences = getSharedPreferences(nombrePref, Context.MODE_PRIVATE)

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


        //Obtener el maximo predeterminado guardado en el sharedpreferences
        maximo = sharedPreferences.getInt("valor_maximo_predeterminado", 10)

        //Obtener el minimo predeterminado guardado en el sharedpreferences
        minimo = sharedPreferences.getInt("valor_minimo_predeterminado", 0)

        binding.ivOpciones.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(intent)
        }

        //Meter codigo


        binding.tvCuentaActual.text = generarCuentaString()
        Log.e("operacion actual", "$operacionActual")
        Log.e("operandos actuales", "op1${operandosActuales[0]}, op2${operandosActuales[1]}")
    }
    fun generarOperacion(): String {
        val operacionesPermitidas = getResources().getStringArray(R.array.operaciones_permitidas)
        val randomOperationIndex = random.nextInt(operacionesPermitidas.size)
        Log.e("generarOperacion()", "Return: ${operacionesPermitidas[randomOperationIndex]}")
        operacionActual = operacionesPermitidas[randomOperationIndex]
        return operacionesPermitidas[randomOperationIndex]
    }

    fun generarOperandos(): Array<Int>{
        //Me genera los numeros que estan entre el minimo y el maximo
        var operandoActual1 = random.nextInt(maximo - minimo + 1) + minimo
        var operandoActual2 = random.nextInt(maximo - minimo + 1) + minimo
        Log.e("generarOperandos()", "Return: op1$operandoActual1, op2$operandoActual2")
        operandosActuales = arrayOf(operandoActual1, operandoActual2)
        return arrayOf(operandoActual1, operandoActual2)
    }



    fun comprobarResultado(respuestaUsuario: Int) {
        when (operacionActual) {
            "+" -> {
                val calculoResultado = operandosActuales[0] + operandosActuales[1]
                if(respuestaUsuario == calculoResultado){
                    acertadas++
                    binding.tvAcertadas.text = "Acertadas: $acertadas"
                }else{
                    falladas++
                    binding.tvFalladas.text = "Falladas: $falladas"
                }
            }
            "-" -> {
                val calculoResultado = operandosActuales[0] - operandosActuales[1]
                if(respuestaUsuario == calculoResultado){
                    acertadas++
                    binding.tvAcertadas.text = "Acertadas: $acertadas"
                }else{
                    falladas++
                    binding.tvFalladas.text = "Falladas: $falladas"
                }
            }
            "*" -> {
                val calculoResultado = operandosActuales[0] * operandosActuales[1]
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

    private fun generarCuentaString(): String {
        val operacion = generarOperacion()
        val operandos = generarOperandos()
        Log.e("generarCuentaString()", "Return: op$operacion, op1${operandos[0]}, op2${operandos[1]}")
        return "${operandos[0]} $operacion ${operandos[1]} = "
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