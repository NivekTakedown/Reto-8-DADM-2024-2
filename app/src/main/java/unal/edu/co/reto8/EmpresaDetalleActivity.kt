package unal.edu.co.reto8

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EmpresaDetalleActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private var empresaId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_empresa_detalle)

        dbHelper = DatabaseHelper(this)

        // Obtener el ID de la empresa desde el Intent
        empresaId = intent.getIntExtra("EMPRESA_ID", 0)

        // Recuperar la empresa desde la base de datos
        val empresa = dbHelper.getEmpresaById(empresaId)

        // Mostrar los detalles de la empresa
        findViewById<TextView>(R.id.tvNombreEmpresa).text = "Nombre: ${empresa?.nombre}"
        findViewById<TextView>(R.id.tvUrlEmpresa).text = "URL: ${empresa?.url}"
        findViewById<TextView>(R.id.tvTelefonoEmpresa).text = "Teléfono: ${empresa?.telefono}"
        findViewById<TextView>(R.id.tvEmailEmpresa).text = "Email: ${empresa?.email}"
        findViewById<TextView>(R.id.tvProductosEmpresa).text = "Productos: ${empresa?.productosServicios}"
        findViewById<TextView>(R.id.tvClasificacionEmpresa).text = "Clasificación: ${empresa?.clasificacion}"

        // Configurar botón de eliminar
        findViewById<Button>(R.id.btnEliminarEmpresa).setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Eliminar Empresa")
                .setMessage("¿Estás seguro de eliminar esta empresa?")
                .setPositiveButton("Sí") { _, _ ->
                    dbHelper.deleteEmpresa(empresaId)
                    Toast.makeText(this, "Empresa eliminada", Toast.LENGTH_SHORT).show()
                    finish() // Cierra la actividad y vuelve al menú principal
                }
                .setNegativeButton("No", null)
                .show()
        }

        // Configurar botón de volver
        findViewById<Button>(R.id.btnVolver).setOnClickListener {
            finish() // Vuelve al menú principal
        }
    }
}