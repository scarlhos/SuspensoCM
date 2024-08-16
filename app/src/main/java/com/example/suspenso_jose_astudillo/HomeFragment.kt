package com.example.suspenso_jose_astudillo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment

class HomeFragment : Fragment() {

    private lateinit var txtLado_1: EditText
    private lateinit var txtLado_2: EditText
    private lateinit var txtLado_3: EditText
    private lateinit var btnIngresar: Button
    private lateinit var txtImpr: TextView

    private lateinit var conexionSQL: ConexionSQL

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        txtLado_1 = view.findViewById(R.id.editText_lado_1)
        txtLado_2 = view.findViewById(R.id.editText_lado_2)
        txtLado_3 = view.findViewById(R.id.editText_lado_3)
        btnIngresar = view.findViewById(R.id.boton_ingresar)
        txtImpr = view.findViewById(R.id.txt_impr)

        conexionSQL = ConexionSQL(requireContext())

        btnIngresar.setOnClickListener {
            val lado_1 = txtLado_1.text.toString().trim()
            val lado_2 = txtLado_2.text.toString().trim()
            val lado_3 = txtLado_3.text.toString().trim()

            if (lado_1.isNotEmpty() && lado_2.isNotEmpty() && lado_3.isNotEmpty()) {
                try {
                    val total_cuenta = lado_1.toFloat()
                    val porcentaje = lado_2.toFloat()
                    val personas = lado_3.toFloat()

                    // Insertar los lados en la base de datos
                    val idInsertado = conexionSQL.insertLados(total_cuenta, porcentaje, personas,)

                    if (idInsertado != -1L) {
                        // Determinar el estado del triángulo directamente aquí

                        val calculo= total_cuenta*(porcentaje/100)

                        val media= ((total_cuenta+calculo)/personas)

                        val formattedIMC = String.format("%.2f", media)
                        val formattedIM = String.format("%.2f", calculo)
                        val resultado = "\ntotal a Pagar: $total_cuenta$\n"+"\nPropina: $formattedIM$"+"\nPersonas: $personas \n"+
                                "Pagar por persona: $formattedIMC$"
                        txtImpr.text = resultado
                        Toast.makeText(requireContext(), "Datos insertados correctamente", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Error al insertar los datos", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Por favor ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Por favor complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        conexionSQL.close()
    }
}
