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
import android.widget.Toast
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

    // Nuevas variables para almacenar cuentas anteriores y siguientes
    private var cuentaAnterior: String = ""
    private var cuentaActual: String = ""
    private var cuentaSiguiente: String = ""
    private var operacionActual = ""

    private lateinit var sharedPreferences: SharedPreferences


    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mueve este bloque después de inflar el diseño
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
        //No me muestra los operandos
        binding.tvOperandos.text = "operando actual: ${operandoActual1} y ${operandoActual2}"
        return arrayOf(operandoActual1, operandoActual2)
    }

    fun generarCuenta(): Triple<String, String, String> {
        val operacion = generarOperacion()
        val operandos = generarOperandos()
        val resultado = obtenerResultado(operacion, operandos[0], operandos[1])

        // Actualizar cuentas anteriores y siguientes
        cuentaAnterior = cuentaActual
        operacionActual = operacion  // Agrega esta línea para actualizar la operación actual
        cuentaActual = "${operandos[0]} $operacion ${operandos[1]}"
        cuentaSiguiente = generarCuentaString()

        return Triple(cuentaAnterior, cuentaActual, cuentaSiguiente)
    }
    private fun obtenerResultado(operacion: String, operando1: Int, operando2: Int): Int {
        return when (operacion) {
            "+" -> operando1 + operando2
            "-" -> operando1 - operando2
            "*" -> operando1 * operando2
            else -> throw IllegalArgumentException("Operación no válida: $operacion")
        }
    }


    fun comprobarResultado(respuestaUsuario: Int) {
        // Obtener el resultado de la cuenta actual y comparar con la respuesta del usuario
        val resultadoReal = obtenerResultado(operacionActual, operandoActual1, operandoActual2)

        if (respuestaUsuario == resultadoReal) {
            // La respuesta es correcta
            acertadas++
        } else {
            // La respuesta es incorrecta
            falladas++
        }

        // Actualizar la interfaz de usuario con las nuevas estadísticas
        binding.tvAcertadas.text = "Acertadas: $acertadas"
        binding.tvFalladas.text = "Falladas: $falladas"

        // Generar una nueva cuenta
        val cuentas = generarCuenta()
        // Mostrar las cuentas en la interfaz de usuario, por ejemplo:
        binding.tvCuentaAnterior.text = cuentas.first
        binding.tvCuentaActual.text = cuentas.second
        binding.tvCuentaSiguiente.text = cuentas.third
    }

    private fun generarCuentaString(): String {
        val operacion = generarOperacion()
        val operandos = generarOperandos()
        return "${operandos[0]} $operacion ${operandos[1]}"
    }

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