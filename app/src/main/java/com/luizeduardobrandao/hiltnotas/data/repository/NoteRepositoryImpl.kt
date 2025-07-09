package com.luizeduardobrandao.hiltnotas.data.repository

import com.luizeduardobrandao.hiltnotas.data.dao.NoteDao
import com.luizeduardobrandao.hiltnotas.data.model.NoteEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

// * Implementação concreta de NoteRepository que delega todas as operações ao NoteDao do Room.
// * Marca-se como @Singleton para garantir única instância no grafo do Hilt.

@Singleton
class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao
): NoteRepository {

    // Retorna um Flow que emite a lista completa de notas sempre que houver mudança no banco.
    // Delegamos diretamente ao mét0do getAll() do DAO.
    override fun getAllNotes(): Flow<List<NoteEntity>> = dao.getAll()

    // Retorna um Flow que emite a nota de ID especificado, ou null se não existir.
    // Permite observar mudanças isoladas naquela única entidade.
    override fun getNoteById(id: Long): Flow<NoteEntity?> = dao.getById(id)

    // Salva a nota no banco:
    //    - Se o id for 0 (entidade nova), insere e retorna o id gerado pelo SQLite.
    //    - Se já existir (id ≠ 0), atualiza o registro e devolve o mesmo id.
    override suspend fun saveNote(note: NoteEntity): Long =
        if (note.id == 0L){
            // Insere nova nota e recebe id autogerado
            dao.insert(note)
        } else {
            // Atualiza campo title/description/isComplete de nota existente
            dao.update(note)
            note.id
        }

    // Marca ou desmarca a completude de uma nota.
    // Basta reenviar a entidade ao DAO para que o campo isComplete seja persistido.
    override suspend fun toggleComplete(note: NoteEntity) {
        dao.update(note)
    }

    // Remove definitivamente a nota informada do banco.
    override suspend fun deleteNote(note: NoteEntity) {
        dao.delete(note)
    }
}

// Explicação: faz a ponte entre os métodos do DAO e as necessidades da aplicação,
//             tratando lógica de inserção vs. atualização.


// * Por que criamos o NoteRepositoryImpl e fazemos override das funções da interface?
// - A interface NoteRepository define o que podemos fazer (contrato): listar, buscar por ID,
//   salvar, alternar completude e deletar.
// - A implementação NoteRepositoryImpl responde como essas operações são realizadas no
//   banco de dados, delegando ao NoteDao gerado pelo Room.
// - O uso de "override" em cada função garante que a classe implemente fielmente todos os
//   comportamentos declarados na interface.

// - Separar interface e implementação traz os benefícios de:
//      - Desacoplamento: o restante da aplicação depende apenas do contrato (NoteRepository),
//        sem conhecer detalhes do Room ou do DAO.
//      - Testabilidade: em testes, você pode prover um stub ou fake de NoteRepository sem
//        precisar do banco real.
//      - Clareza: cada camada (DAO, repositório, use cases, ViewModels) tem responsabilidades
//        bem definidas.