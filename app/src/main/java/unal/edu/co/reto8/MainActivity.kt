
package unal.edu.co.reto8

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import unal.edu.co.reto8.ui.theme.Reto8Theme
class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerViewEmpresas: RecyclerView
    private lateinit var searchBar: EditText
    private lateinit var classificationSpinner: Spinner
    private lateinit var fabAddEmpresa: FloatingActionButton
    private lateinit var empresaAdapter: EmpresaAdapter
    private var empresas: List<Empresa> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        recyclerViewEmpresas = findViewById(R.id.recyclerViewEmpresas)
        searchBar = findViewById(R.id.searchBar)
        classificationSpinner = findViewById(R.id.classificationSpinner)
        fabAddEmpresa = findViewById(R.id.fabAddEmpresa)

        // Cargar empresas desde la base de datos
        empresas = dbHelper.getAllEmpresas()
        empresaAdapter = EmpresaAdapter(empresas) { empresa ->
            showDeleteConfirmation(empresa)
        }

        recyclerViewEmpresas.layoutManager = LinearLayoutManager(this)
        recyclerViewEmpresas.adapter = empresaAdapter

        // Configurar búsqueda por nombre
        searchBar.addTextChangedListener { text ->
            filterEmpresas()
        }

        // Configurar filtro por clasificación
        classificationSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                filterEmpresas()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        fabAddEmpresa.setOnClickListener {
            showAddEmpresaDialog()
        }
    }

    private fun showAddEmpresaDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_empresa, null)

        val editNombre = dialogView.findViewById<EditText>(R.id.editNombre)
        val editUrl = dialogView.findViewById<EditText>(R.id.editUrl)
        val editTelefono = dialogView.findViewById<EditText>(R.id.editTelefono)
        val editEmail = dialogView.findViewById<EditText>(R.id.editEmail)
        val editProductos = dialogView.findViewById<EditText>(R.id.editProductos)
        val spinnerClasificacion = dialogView.findViewById<Spinner>(R.id.spinnerClasificacion)

        AlertDialog.Builder(this)
            .setTitle("Agregar Empresa")
            .setView(dialogView)
            .setPositiveButton("Guardar") { _, _ ->
                val nombre = editNombre.text.toString()
                val url = editUrl.text.toString()
                val telefono = editTelefono.text.toString()
                val email = editEmail.text.toString()
                val productos = editProductos.text.toString()
                val clasificacion = spinnerClasificacion.selectedItem.toString()

                if (nombre.isNotBlank() && email.isNotBlank()) {
                    val nuevaEmpresa = Empresa(
                        id = 0,
                        nombre = nombre,
                        url = url,
                        telefono = telefono,
                        email = email,
                        productosServicios = productos,
                        clasificacion = clasificacion
                    )

                    dbHelper.insertEmpresa(nuevaEmpresa) // Agrega la empresa a la base de datos
                    refreshEmpresas() // Actualiza la lista del RecyclerView
                } else {
                    Toast.makeText(this, "El nombre y el email son obligatorios", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
    private fun filterEmpresas() {
        val searchQuery = searchBar.text.toString().lowercase()
        val selectedClassification = classificationSpinner.selectedItem.toString()

        val filteredEmpresas = empresas.filter { empresa ->
            val matchesName = empresa.nombre.lowercase().contains(searchQuery)
            val matchesClassification = if (selectedClassification == "Todas") {
                true
            } else {
                empresa.clasificacion == selectedClassification
            }
            matchesName && matchesClassification
        }

        empresaAdapter = EmpresaAdapter(filteredEmpresas) { empresa ->
            showDeleteConfirmation(empresa)
        }
        recyclerViewEmpresas.adapter = empresaAdapter
    }

    private fun showDeleteConfirmation(empresa: Empresa) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Empresa")
            .setMessage("¿Estás seguro de eliminar ${empresa.nombre}?")
            .setPositiveButton("Sí") { _, _ ->
                dbHelper.deleteEmpresa(empresa.id)
                refreshEmpresas()
            }
            .setNegativeButton("No", null)
            .show()
    }

    private fun refreshEmpresas() {
        empresas = dbHelper.getAllEmpresas()
        filterEmpresas() // Aplica el filtro automáticamente tras actualizar
    }
}
