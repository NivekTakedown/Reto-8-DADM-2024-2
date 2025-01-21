package unal.edu.co.reto8

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmpresaAdapter(
    private val empresas: List<Empresa>,
    private val onDelete: (Empresa) -> Unit,
    private val onItemClick: (Empresa) -> Unit
) : RecyclerView.Adapter<EmpresaAdapter.EmpresaViewHolder>() {

    class EmpresaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nombre: TextView = itemView.findViewById(R.id.textNombre)
        val clasificacion: TextView = itemView.findViewById(R.id.textClasificacion)
        val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmpresaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_empresa, parent, false)
        return EmpresaViewHolder(view)
    }

    override fun onBindViewHolder(holder: EmpresaViewHolder, position: Int) {
        val empresa = empresas[position]
        holder.nombre.text = empresa.nombre
        holder.clasificacion.text = empresa.clasificacion
        holder.itemView.setOnClickListener {
            onItemClick(empresa)
        }
        holder.btnDelete.setOnClickListener {
            onDelete(empresa)
        }
    }

    override fun getItemCount(): Int = empresas.size
}
