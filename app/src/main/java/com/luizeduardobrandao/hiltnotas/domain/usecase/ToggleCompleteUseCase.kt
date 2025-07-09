package com.luizeduardobrandao.hiltnotas.domain.usecase

import com.luizeduardobrandao.hiltnotas.data.model.NoteEntity
import com.luizeduardobrandao.hiltnotas.data.repository.NoteRepository
import javax.inject.Inject

// * Caso de uso para marcar/desmarcar completude de uma nota.

class ToggleCompleteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: NoteEntity) = repository.toggleComplete(note)
}