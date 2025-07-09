package com.luizeduardobrandao.hiltnotas.data.repository

import com.luizeduardobrandao.hiltnotas.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

// * Interface que define operações de repositório para notas.

interface NoteRepository {

    // Retorna fluxo de lista de notas.
    fun getAllNotes(): Flow<List<NoteEntity>>

    // Retorna fluxo de única nota por ID.
    fun getNoteById(id: Long): Flow<NoteEntity?>

    // Salva (insere ou atualiza) a nota. Retorna o ID gerado ou existente.
    suspend fun saveNote(note: NoteEntity): Long

    // Marca ou desmarca completude de uma nota.
    suspend fun toggleComplete(note: NoteEntity)

    // Deleta a nota informada.
    suspend fun deleteNote(note: NoteEntity)

}

// Explicação: esta interface define um contrato desacoplado entre a camada de negócio e
//             a implementação concreta.