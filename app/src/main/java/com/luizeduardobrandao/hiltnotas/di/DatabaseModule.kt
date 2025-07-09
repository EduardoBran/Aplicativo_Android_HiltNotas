package com.luizeduardobrandao.hiltnotas.di

import android.content.Context
import androidx.room.Room
import com.luizeduardobrandao.hiltnotas.AppDatabase
import com.luizeduardobrandao.hiltnotas.data.dao.NoteDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// * Módulo Hilt para prover instâncias de AppDatabase e NoteDao.

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    // Fornece o banco de dados "notes_db" como singleton.
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "notes_db"
        )
            .fallbackToDestructiveMigration(false)
            .build()

    // Fornece o DAO para operações de NoteEntity.
    @Provides
    fun provideNoteDao(database: AppDatabase): NoteDao = database.noteDao()
}

// Explicação:
// - @Module + @InstallIn(SingletonComponent::class) registram esse módulo no grafo de injeção
//   de aplicação.
//
// - Função provideDatabase cria o AppDatabase via builder do Room, configurado para falhar
//   limpando o schema se houver mudança de versão.
//
// - Função provideNoteDao extrai o NoteDao da instância de AppDatabase.