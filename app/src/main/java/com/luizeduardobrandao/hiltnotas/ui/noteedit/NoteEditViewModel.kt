package com.luizeduardobrandao.hiltnotas.ui.noteedit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.hiltnotas.data.model.NoteEntity
import com.luizeduardobrandao.hiltnotas.domain.usecase.DeleteNoteUseCase
import com.luizeduardobrandao.hiltnotas.domain.usecase.GetNoteUseCase
import com.luizeduardobrandao.hiltnotas.domain.usecase.SaveNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// * ViewModel responsável por gerenciar a tela de criação/edição de uma nota.

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    // Caso de uso para obter uma nota por ID
    private val getNoteUseCase: GetNoteUseCase,
    // Caso de uso para salvar (inserir ou atualizar) uma nota
    private val saveNoteUseCase: SaveNoteUseCase,
    // Caso de uso para deletar uma nota
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    // Estado interno que mantém a nota atual (ou null se for novo registro)
    private val _note = MutableStateFlow<NoteEntity?>(null)
    // StateFlow público para a UI observar mudanças na nota carregada.
    val note: StateFlow<NoteEntity?> = _note


    // Carrega do banco a nota com o ID informado.
    // - Se o ID for inválido (por exemplo -1), nenhuma alteração acontece.
    fun loadNote(id: Long) {
        viewModelScope.launch {

            // Coleta o Flow retornado pelo caso de uso e atualiza o StateFlow
            getNoteUseCase(id).collect{ fetchedNote ->
                _note.value = fetchedNote
            }
        }
    }

    // Consolida os campos de entrada (título, descrição, completude) em uma entidade NoteEntity
    // e dispara o caso de uso de salvamento.
    //     - Se _note.value for null, assume-se criação (id = 0L).
    //     - Se _note.value contiver uma nota existente, usa seu id para atualização.
    fun save(title: String, description: String, isComplete: Boolean){

        val existing = _note.value
        val entity = NoteEntity(
            id = existing?.id ?: 0L,
            title = title,
            description = description,
            isComplete = isComplete
        )

        viewModelScope.launch {
            // Chama o caso de uso que retorna o ID gerado ou existente
            saveNoteUseCase(entity)
        }
    }

    // Deleta a nota atualmente carregada, se existir.
    fun delete(){
        _note.value?.let { currentNote ->
            viewModelScope.launch {
                // Chama o caso de uso de exclusão
                deleteNoteUseCase(currentNote)
            }
        }
    }
}

// Explicação: injeta casos de uso para leitura, gravação e exclusão, expõe StateFlow da nota
//             e funções de ação.