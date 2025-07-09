package com.luizeduardobrandao.hiltnotas.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.luizeduardobrandao.hiltnotas.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow

// * Data Access Object para operações de CRUD na tabela "notes".

@Dao
interface NoteDao {

    // Retorna todas as notas, mais recentes primeiro.
    @Query("SELECT * FROM notes ORDER BY id DESC")
    fun getAll(): Flow<List<NoteEntity>>

    // Retorna uma nota pelo ID, ou null se não existir.
    @Query("SELECT * FROM notes WHERE id = :id")
    fun getById(id: Long): Flow<NoteEntity?>

    // Insere uma nova nota e retorna o ID gerado.
    @Insert
    suspend fun insert(note: NoteEntity): Long

    // Atualiza uma nota existente.
    @Update
    suspend fun update(note: NoteEntity)

    // Remove a nota informada.
    @Delete
    suspend fun delete(note: NoteEntity)
}

// Explicação: o @Dao define métodos para consultar, inserir, atualizar e deletar registros,
//             usando SQL mínimo em @Query.

// * Por que só inserir, atualizar e deletar são suspend?

// - As consultas (@Query) que retornam Flow não precisam ser suspend porque o próprio Flow já
//   encapsula a execução assíncrona e observa o banco em background automaticamente.
//   Você chama getAll() ou getById(id) e coleta o fluxo, sem travar a thread principal.
//
// - Já os métodos que modificam dados (@Insert, @Update, @Delete) são operações pontuais de
//   escrita no banco e podem levar tempo; marcá-los suspend deixa claro que devem ser chamados
//   dentro de uma coroutine, evitando bloqueio da UI.