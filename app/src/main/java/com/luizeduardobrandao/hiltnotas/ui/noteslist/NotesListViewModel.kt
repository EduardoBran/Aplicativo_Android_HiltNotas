package com.luizeduardobrandao.hiltnotas.ui.noteslist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luizeduardobrandao.hiltnotas.data.model.NoteEntity
import com.luizeduardobrandao.hiltnotas.domain.usecase.GetNotesUseCase
import com.luizeduardobrandao.hiltnotas.domain.usecase.ToggleCompleteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// * ViewModel responsável por gerenciar e expor o estado da lista de notas na UI.

@HiltViewModel
class NotesListViewModel @Inject constructor(
    // Caso de uso para obter o fluxo de todas as notas
    getNotesUseCase: GetNotesUseCase,
    // Caso de uso para alternar o status de completude de uma nota
    private val toggleCompleteUseCase: ToggleCompleteUseCase
) : ViewModel() {

    // StateFlow que emite a lista atual de notas para a UI.
    //     - Usa o Flow<List<NoteEntity>> retornado pelo GetNotesUseCase.
    //     - Converte em StateFlow para manter sempre o último valor em cache.
    //     - viewModelScope garante que a coleta aconteça enquanto o ViewModel estiver ativo.
    //     - SharingStarted.Lazily inicia a coleta somente quando houver pelo menos um coletor.
    //     - emptyList() é o valor inicial antes dos dados chegarem.
    val notes: StateFlow<List<NoteEntity>> = getNotesUseCase()
        .stateIn(
            viewModelScope,         // escopo para coroutine ligada ao ciclo de vida do ViewModel
            SharingStarted.Lazily,  // só começa a coletar quando houver demanda
            emptyList()             // lista vazia até o primeiro resultado
        )

    // Recebe uma nota e dispara a inversão do seu estado de completude.
    //     - Executa em coroutine dentro do viewModelScope para não bloquear a thread da UI.
    //     - toggleCompleteUseCase atualiza o campo isComplete no banco.
    fun onToggleComplete(note: NoteEntity){
        viewModelScope.launch {
            toggleCompleteUseCase(note)
        }
    }
}

// Explicação: injeta os casos de uso, expõe StateFlow para a lista e função para
//             togglar completude.