package com.example.calculatron

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
    private lateinit var countDownTimer: CountDownTimer



    private var tiempoRestante = 0 // Inicializar con 0 para evitar nullPointerException
    private var acertadas = 0
    private var falladas = 0

    // Nuevas
    var operacionActual = ""
    var operandoActual1 = 0
    var operandoActual2 = 0
    // Futuras
    var operacionFutura = ""
    var operandoFuturo1 = 0
    var operandoFuturo2 = 0
    // Pasado
    var acertoLaUltima = false
    var cuentaPasada = ""
    //Respuesta de usuario
    var respuestaUsuario = 0

    private val nombrePref = "mis_preferencias"
    private lateinit var sharedPreferences: SharedPreferences


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



        binding.ivOpciones.setOnClickListener {
            val intent = Intent(this@MainActivity, MainActivity2::class.java)
            startActivity(intent)
        }

        //Meter codigo aquí
        tiempoRestante = sharedPreferences.getInt("tiempo_predeterminado", 20000)
        iniciarTemporizador()
        pintarCuentas()
        Log.e("Mierda", "Return: $operacionActual")
        Log.e("Mierda", "Return: $operacionFutura")


    }

    private fun iniciarTemporizador() {
        countDownTimer = object : CountDownTimer(tiempoRestante.toLong(), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                binding.tvTiempoRestante.text = "Tiempo restante: " + millisUntilFinished / 1000
            }

            override fun onFinish() {
                binding.tvTiempoRestante.text = "Se ha agotado el tiempo!"

                // Pasar a la otra actividad y enviar datos
                val intent = Intent(this@MainActivity, MainActivity3::class.java)
                intent.putExtra("acertadas", acertadas)
                intent.putExtra("falladas", falladas)
                startActivity(intent)
            }
        }.start()
    }

    override fun onPause() {
        super.onPause()
        countDownTimer.cancel()
    }

    override fun onStop() {
        super.onStop()
        countDownTimer.cancel()
    }

    fun generarOperacion(): String {
        //¿Que operaciones se permiten?
        var sePermiteSuma = sharedPreferences.getBoolean("suma_permitida", true)
        var sePermiteResta = sharedPreferences.getBoolean("resta_permitida", true)
        var sePermiteMultiplicacion = sharedPreferences.getBoolean("multiplicacion_permitida", false)
        //Genero una mutable list para meter los simbolos de las permitidas
        var mutableConSignosPermitidos = mutableListOf<String>()
        if(sePermiteSuma){
            var simboloSuma = sharedPreferences.getString("suma_permitida_simbolo", "+")
            if (simboloSuma != null) {
                mutableConSignosPermitidos.add(simboloSuma)
            }
        }
        if(sePermiteResta){
            var simboloResta = sharedPreferences.getString("resta_permitida_simbolo", "-")
            if (simboloResta != null) {
                mutableConSignosPermitidos.add(simboloResta)
            }
        }
        if(sePermiteMultiplicacion){
            var simboloMultiplicacion = sharedPreferences.getString("multiplicacion_permitida_simbolo", "*")
            if (simboloMultiplicacion != null) {
                mutableConSignosPermitidos.add(simboloMultiplicacion)
            }
        }
        if (mutableConSignosPermitidos.isEmpty()){
            return "No se han escogido operaciones"
        }
        val randomOperationIndex = random.nextInt(mutableConSignosPermitidos.size)
        val operacionSeleccionada = mutableConSignosPermitidos[randomOperationIndex]

        Log.e("generarOperacion()", "Return: $operacionSeleccionada")
        return operacionSeleccionada
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
        Log.e("Dentro de comprobar resultado", "Operacion actual: $operacionActual")
        Log.e("Dentro de comprobar resultado", "Respuesta usuario: $respuestaUsuario")
        val calculoResultado = when (operacionActual) {
            "+" -> operandoActual1 + operandoActual2
            "-" -> operandoActual1 - operandoActual2
            "*" -> operandoActual1 * operandoActual2
            else -> throw IllegalArgumentException("Operación no soportada: $operacionActual")
        }
        Log.e("Dentro de comprobar resultado", "Calculo resultado: $calculoResultado")
        if (respuestaUsuario == calculoResultado) {
            acertadas++
        } else {
            falladas++
        }

        binding.tvAcertadas.text = "Acertadas: $acertadas"
        binding.tvFalladas.text = "Falladas: $falladas"
    }


    fun pintarCuentas(){
        operandoActual1 = generarOperandos()
        operandoActual2 = generarOperandos()
        operacionActual = generarOperacion()

        operandoFuturo1 = generarOperandos()
        operandoFuturo2 = generarOperandos()
        operacionFutura = generarOperacion()

        if(operacionActual == "No se han escogido operaciones"){
            binding.tvCuentaActual.text = operacionActual
            binding.tvCuentaAnterior.visibility = View.GONE
            binding.tvCuentaSiguiente.visibility = View.GONE
        }else{
            binding.tvCuentaAnterior.visibility = View.VISIBLE
            binding.tvCuentaSiguiente.visibility = View.VISIBLE
            binding.tvCuentaActual.text = "${operandoActual1.toString()} ${operacionActual} ${operandoActual2.toString()} = "
            binding.tvCuentaSiguiente.text = "${operandoFuturo1.toString()} ${operacionFutura} ${operandoFuturo2.toString()} = "
        }
    }

    fun pintarCuentasBoton(){

        cuentaPasada = "${operandoActual1.toString()} $operacionActual ${operandoActual2.toString()} = $respuestaUsuario"
        binding.tvCuentaAnterior.text = cuentaPasada


        operacionActual = operacionFutura
        operandoActual1 = operandoFuturo1
        operandoActual2 = operandoFuturo2
        binding.tvCuentaActual.text = "${operandoActual1.toString()} ${operacionActual} ${operandoActual2.toString()} = "


        operandoFuturo1 = generarOperandos()
        operandoFuturo2 = generarOperandos()
        operacionFutura = generarOperacion()
        binding.tvCuentaSiguiente.text = "${operandoFuturo1.toString()} ${operacionFutura} ${operandoFuturo2.toString()} = "
    }











    //teclado

    fun onButtonClick(view: View, editText: EditText) {
        if (view is Button) {
            val buttonText = view.text.toString()
            val currentText = editText.text.toString()

            when (buttonText) {
                "=" -> {
                    // Realizar cálculos cuando se presiona el botón "="
                    if (currentText.isNotEmpty() && operacionActual != "No se han escogido operaciones") {
                        Log.e("entra en el =", "Entra en el igual")
                        respuestaUsuario = currentText.toInt()
                        Log.e("Respuesta del usuario", "$respuestaUsuario")
                        comprobarResultado(respuestaUsuario)
                        pintarCuentasBoton()
                    }
                    // Limpiar el texto del EditText
                    editText.setText("")
                }
                "C" -> {
                    // Limpiar el texto del EditText y resetear la operación actual
                    editText.setText("")
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
                }
            }
        }
    }
}