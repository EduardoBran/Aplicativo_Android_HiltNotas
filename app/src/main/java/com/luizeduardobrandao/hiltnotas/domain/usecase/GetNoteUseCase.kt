package com.luizeduardobrandao.hiltnotas.domain.usecase

import com.luizeduardobrandao.hiltnotas.data.model.NoteEntity
import com.luizeduardobrandao.hiltnotas.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// * Caso de uso para obter fluxo de uma nota por ID.

class GetNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(id: Long): Flow<NoteEntity?> = repository.getNoteById(id)
}