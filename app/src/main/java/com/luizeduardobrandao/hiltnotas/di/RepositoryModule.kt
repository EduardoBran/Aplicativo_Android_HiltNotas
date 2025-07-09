package com.luizeduardobrandao.hiltnotas.di

import com.luizeduardobrandao.hiltnotas.data.repository.NoteRepository
import com.luizeduardobrandao.hiltnotas.data.repository.NoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

// * Módulo Hilt para vincular interfaces de repositório às implementações.

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    // Sempre que NoteRepository for requisitado, fornece NoteRepositoryImpl como singleton.
    @Binds
    @Singleton
    abstract fun bindNoteRepository(impl: NoteRepositoryImpl): NoteRepository
}

// Explicação:
// - @Binds cria a regra de vinculação entre NoteRepository (interface) e
//   NoteRepositoryImpl (classe concreta), permitindo ao Hilt injetar a implementação correta.
//
// - O escopo @Singleton garante única instância para toda a aplicação.