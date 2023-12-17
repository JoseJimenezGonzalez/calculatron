package com.example.calculatron

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.content.edit
import com.example.calculatron.databinding.ActivityMain2Binding

class MainActivity2 : AppCompatActivity() {

    private val nombrePref = "mis_preferencias"
    var esTiempoCorrecto = true
    var esMaxYMinCorrecto = true
    //Esta es para guardar la opcion seleccionada del spinner
    var opcionSeleccionada: String = ""
    var numeroOpcionSeleccionada = 0

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

        //Configurar el spinner
        //Obtenemos el spinner
        val spinner = findViewById<Spinner>(R.id.spinner)
        //Creamos un array con el que vamos a inflar al spinner
        val opcionesAnimacion = resources.getStringArray(R.array.opciones_animacion)
        // Crear un ArrayAdapter utilizando la lista de opciones y el diseño predeterminado
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, opcionesAnimacion)
        // Aplicar el adaptador al Spinner
        spinner.adapter = adapter
        // Recuperar la posición guardada y establecerla en el Spinner
        val posicionGuardada = sharedPreferences.getInt("posicion_seleccionada", 0)
        spinner.setSelection(posicionGuardada)
        // Configurar un listener para manejar la selección de elementos en el Spinner
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: android.view.View?, position: Int, id: Long) {
                // Obtener la opción seleccionada
                val selectedItem = opcionesAnimacion[position]

                // Asignar la opción seleccionada
                opcionSeleccionada = selectedItem
                numeroOpcionSeleccionada = position

                // Mostrar la opción seleccionada
                Toast.makeText(this@MainActivity2, "Seleccionaste: $selectedItem", Toast.LENGTH_SHORT).show()

                // Agregar la siguiente línea para verificar el valor
                Log.e("Seleccion spinner", "numeroOpcionSeleccionada: $numeroOpcionSeleccionada")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Manejar caso en que no se selecciona nada
            }
        }


        binding.btnGuardarConfiguracion.setOnClickListener {
            esTiempoCorrecto = true
            esMaxYMinCorrecto = true
            // Modificar el tiempo predeterminado
            val nuevoTiempoPredeterminadoStr = binding.tietCuentaAtras.text.toString()
            // Modificar el valor maximo predeterminado
            val nuevoValorMaximoStr = binding.tietMaximo.text.toString()
            // Modificar el valor minimo predeterminado
            val nuevoValorMinimoStr = binding.tietMinimo.text.toString()

            if(nuevoTiempoPredeterminadoStr != ""){
                val nuevoTiempoPredeterminado = binding.tietCuentaAtras.text.toString().toInt() * 1000
                if(nuevoTiempoPredeterminado < 3000){
                    esTiempoCorrecto = false
                }
            }

            //Maximo y min correcto
            //Me traigo los valores antiguos
            val maximoAntiguo = sharedPreferences.getInt("valor_maximo_predeterminado", 10)
            val minimoAntiguo = sharedPreferences.getInt("valor_minimo_predeterminado", 0)
            if(nuevoValorMaximoStr.isNotEmpty() && nuevoValorMinimoStr.isNotEmpty()){
                val nuevoValorMaximo = binding.tietMaximo.text.toString().toInt()
                val nuevoValorMinimo = binding.tietMinimo.text.toString().toInt()
                if(nuevoValorMaximo <= nuevoValorMinimo){
                    esMaxYMinCorrecto = false
                }
            }else if(nuevoValorMaximoStr.isNotEmpty() && nuevoValorMinimoStr.isEmpty()){
                val nuevoValorMaximo = binding.tietMaximo.text.toString().toInt()
                if(nuevoValorMaximo <= minimoAntiguo){
                    esMaxYMinCorrecto = false
                }
            }else if(nuevoValorMaximoStr.isEmpty() && nuevoValorMinimoStr.isNotEmpty()){
                val nuevoValorMinimo = binding.tietMinimo.text.toString().toInt()
                if(maximoAntiguo <= nuevoValorMinimo){
                    esMaxYMinCorrecto = false
                }
            }
            if(esTiempoCorrecto && esMaxYMinCorrecto){
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
                    putInt("posicion_seleccionada", numeroOpcionSeleccionada)
                    apply()
                }

                // Volver a la MainActivity
                val intent = Intent(this@MainActivity2, MainActivity::class.java)
                startActivity(intent)
            }else{
                if(esTiempoCorrecto){
                    Toast.makeText(this, "El maximo debe de ser mayor que el minimo", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(this, "El tiempo tiene que ser mayor a 3 segundos", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}