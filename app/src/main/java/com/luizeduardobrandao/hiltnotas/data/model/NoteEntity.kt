package com.luizeduardobrandao.hiltnotas.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

// * Entidade que representa uma nota na tabela "notes".
// * @property id identificador único (auto-gerado).
// * @property title título da nota.
// * @property description conteúdo da nota.
// * @property isComplete indica se a nota está marcada como completa.

@Entity(tableName = "notes")
data class NoteEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val description: String,
    val isComplete: Boolean
)

// Explicação: anotações @Entity e @PrimaryKey informam ao Room como mapear essa classe
// para a tabela SQLite.