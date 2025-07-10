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

// * ViewModel responsável por:
//    - carregar uma nota existente (ou iniciar uma nova)
//    - manter o estado da nota e do formulário
//    - validar inputs
//    - salvar ou deletar a nota via casos de uso

@HiltViewModel
class NoteEditViewModel @Inject constructor(
    // Caso de uso para obter uma nota por ID
    private val getNoteUseCase: GetNoteUseCase,
    // Caso de uso para salvar (inserir ou atualizar) uma nota
    private val saveNoteUseCase: SaveNoteUseCase,
    // Caso de uso para deletar uma nota
    private val deleteNoteUseCase: DeleteNoteUseCase
) : ViewModel() {

    // Estado interno que armazena a nota carregada (ou null para nova)
    private val _note = MutableStateFlow<NoteEntity?>(null)
    // StateFlow público para a UI observar mudanças na nota carregada.
    val note: StateFlow<NoteEntity?> = _note

    // Estado interno para sinalizar se o formulário está válido
    private val _isFormValid = MutableStateFlow<Boolean>(false)
    // Exposição imutável para a UI observar a validação do formulário
    val isFormValid: StateFlow<Boolean> = _isFormValid


    // Carrega uma nota em modo edição.
    //    - Executa getNoteUseCase(id), que retorna um Flow<NoteEntity?>
    //    - Coleta o resultado e atualiza _note
    //    - Se a nota não for nula, chama onInputChanged para pré-preencher e validar
    fun loadNote(id: Long) = viewModelScope.launch {
        getNoteUseCase(id)
            .collect{ loaded ->
                // Atualiza o StateFlow com a nota retornada
                _note.value = loaded
            }
        // Após carregar, usa os valores para atualizar e validar o formulário
        _note.value?.let { onInputChanged(it.title, it.description) }
    }


    // Recebe cada mudança nos campos de título e descrição.
    //    - Atualiza _isFormValid para true somente se ambos não estiverem em branco.
    fun onInputChanged(title: String, description: String) {
        _isFormValid.value = title.isNotBlank() && description.isNotBlank()
    }


    // Salva a nota (novo registro ou atualização).
    //    - Cria uma cópia de NoteEntity usando valores atuais se já existir
    //    - Caso contrário, instancia um novo NoteEntity
    //    - Chama saveNoteUseCase para persistir
    fun save(title: String, description: String, isComplete: Boolean) = viewModelScope.launch {
        val entity = _note.value?.copy(
            title = title,
            description = description,
            isComplete = isComplete
        ) ?: NoteEntity(
            title = title,
            description = description,
            isComplete = isComplete
        )
        saveNoteUseCase(entity)
    }

    
    // Deleta a nota atualmente carregada.
    //    - Só executa se _note.value não for nulo
    //    - Chama deleteNoteUseCase para remover do repositório
    fun delete() = viewModelScope.launch {
        _note.value?.let { toDelete ->
            deleteNoteUseCase(toDelete)
        }
    }
}

// Explicação: injeta casos de uso para leitura, gravação e exclusão, expõe StateFlow da nota
//             e funções de ação.