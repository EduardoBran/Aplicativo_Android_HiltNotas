package com.luizeduardobrandao.hiltnotas.domain.usecase

import com.luizeduardobrandao.hiltnotas.data.model.NoteEntity
import com.luizeduardobrandao.hiltnotas.data.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

// * Caso de uso para obter fluxo de todas as notas.

class GetNotesUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    operator fun invoke(): Flow<List<NoteEntity>> = repository.getAllNotes()
}