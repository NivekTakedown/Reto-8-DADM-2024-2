
package unal.edu.co.reto8

import android.app.AlertDialog
import android.content.ContentValues
import android.os.Bundle
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import unal.edu.co.reto8.ui.theme.Reto8Theme
class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerViewEmpresas: RecyclerView
    private lateinit var fabAddEmpresa: FloatingActionButton
    private lateinit var empresaAdapter: EmpresaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)
        recyclerViewEmpresas = findViewById(R.id.recyclerViewEmpresas)
        fabAddEmpresa = findViewById(R.id.fabAddEmpresa)

        // Cargar empresas desde la base de datos
        val empresas = dbHelper.getAllEmpresas()
        empresaAdapter = EmpresaAdapter(empresas) { empresa ->
            showDeleteConfirmation(empresa)
        }

        recyclerViewEmpresas.layoutManager = LinearLayoutManager(this)
        recyclerViewEmpresas.adapter = empresaAdapter

        fabAddEmpresa.setOnClickListener {
            // Abre un diálogo o actividad para agregar una nueva empresa
        }
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
        val empresas = dbHelper.getAllEmpresas()
        empresaAdapter = EmpresaAdapter(empresas) { empresa ->
            showDeleteConfirmation(empresa)
        }
        recyclerViewEmpresas.adapter = empresaAdapter
    }
}
