package unal.edu.co.reto8

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
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

        // Configurar botón de editar
        findViewById<Button>(R.id.btnEditarEmpresa).setOnClickListener {
            showEditEmpresaDialog(empresa)
        }

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

    private fun showEditEmpresaDialog(empresa: Empresa?) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_empresa, null)

        val editNombre = dialogView.findViewById<EditText>(R.id.editNombre)
        val editUrl = dialogView.findViewById<EditText>(R.id.editUrl)
        val editTelefono = dialogView.findViewById<EditText>(R.id.editTelefono)
        val editEmail = dialogView.findViewById<EditText>(R.id.editEmail)
        val editProductos = dialogView.findViewById<EditText>(R.id.editProductos)
        val spinnerClasificacion = dialogView.findViewById<Spinner>(R.id.spinnerClasificacion)

        // Rellenar los campos con los datos actuales de la empresa
        editNombre.setText(empresa?.nombre)
        editUrl.setText(empresa?.url)
        editTelefono.setText(empresa?.telefono)
        editEmail.setText(empresa?.email)
        editProductos.setText(empresa?.productosServicios)
        // Configurar el spinner de clasificación (esto puede variar según cómo esté configurado tu spinner)

        AlertDialog.Builder(this)
            .setTitle("Editar Empresa")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = editNombre.text.toString()
                val url = editUrl.text.toString()
                val telefono = editTelefono.text.toString()
                val email = editEmail.text.toString()
                val productos = editProductos.text.toString()
                val clasificacion = spinnerClasificacion.selectedItem.toString()

                if (nombre.isNotBlank() && email.isNotBlank()) {
                    val empresaEditada = Empresa(
                        id = empresa?.id ?: 0,
                        nombre = nombre,
                        url = url,
                        telefono = telefono,
                        email = email,
                        productosServicios = productos,
                        clasificacion = clasificacion
                    )

                    dbHelper.updateEmpresa(empresaEditada) // Actualiza la empresa en la base de datos
                    Toast.makeText(this, "Empresa actualizada", Toast.LENGTH_SHORT).show()
                    finish() // Cierra la actividad y vuelve al menú principal
                } else {
                    Toast.makeText(this, "El nombre y el email son obligatorios", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}