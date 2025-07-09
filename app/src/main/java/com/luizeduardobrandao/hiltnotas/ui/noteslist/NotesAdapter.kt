package com.luizeduardobrandao.hiltnotas.ui.noteslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.luizeduardobrandao.hiltnotas.R
import com.luizeduardobrandao.hiltnotas.data.model.NoteEntity

// * Adapter para exibir a lista de notas.
// * @param notes Lista atual de notas.
// * @param onItemClick Callback quando o item inteiro é clicado (para editar).
// * @param onToggleComplete Callback quando o checkbox é alternado.

class NotesAdapter(
    private var notes: List<NoteEntity>,
    private val onItemClick: (NoteEntity) -> Unit,
    private val onToggleComplete: (NoteEntity) -> Unit
): RecyclerView.Adapter<NotesAdapter.NoteViewHolder>() {

    // ViewHolder que mantém referências para os componentes de cada item.
    inner class NoteViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val titleView: TextView = itemView.findViewById(R.id.textViewTitle)
        val descriptionView: TextView = itemView.findViewById(R.id.textViewTitle)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkBoxComplete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        // Infla o layout do item: item_note.xml (deve conter textViewTitle, textViewDescription, checkBoxComplete)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_note, parent, false)
        return NoteViewHolder(view)
    }

    override fun getItemCount(): Int = notes.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {

        val note = notes[position]

        // Preenche o campo de título
        holder.titleView.text = note.title

        // Exibe um trecho da descrição (por exemplo, primeiros 50 caracteres)
        holder.descriptionView.text =
            if (note.description.length > 50) {
                note.description.substring(0, 50) + "..."
            } else {
                note.description
            }

        // Checkbox
        holder.checkBox.isChecked = note.isComplete

        // Clique no item (nota) inteira para editar
        holder.itemView.setOnClickListener {
            onItemClick(note)
        }

        // Toggle do checkbox de completude
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            // Cria uma nova entidade com o estado invertido e chama o callback
            onToggleComplete(note.copy(isComplete = isChecked))
        }

        // Atualiza a lista de notas e notifica o adapter.
        fun submitList(newNotes: List<NoteEntity>) {
            notes = newNotes
            notifyDataSetChanged()
        }
    }
}

// Explicação: O adapter infla um layout de item (item_note.xml), popula os campos título,
//             descrição e checkbox, e utiliza callbacks para delegar a ação ao ViewModel
//             via Fragment.