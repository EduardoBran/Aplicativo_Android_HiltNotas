package com.luizeduardobrandao.hiltnotas.domain.usecase

import com.luizeduardobrandao.hiltnotas.data.model.NoteEntity
import com.luizeduardobrandao.hiltnotas.data.repository.NoteRepository
import javax.inject.Inject

// * Caso de uso para salvar (inserir/atualizar) uma nota.

class SaveNoteUseCase @Inject constructor(
    private val repository: NoteRepository
) {
    suspend operator fun invoke(note: NoteEntity): Long = repository.saveNote(note)
}