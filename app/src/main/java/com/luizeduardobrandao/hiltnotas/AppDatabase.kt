package com.luizeduardobrandao.hiltnotas

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luizeduardobrandao.hiltnotas.data.dao.NoteDao
import com.luizeduardobrandao.hiltnotas.data.model.NoteEntity

// * Banco de dados principal da aplicação, versão 1.
// * Declara as entidades e fornece o DAO.

@Database(
    entities = [NoteEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase: RoomDatabase() {

    // Fornece acesso às operações de nota.
    abstract fun noteDao(): NoteDao
}

// Explicação: @Database reúne todas as entidades e fornece instâncias de DAOs;
//             Room.databaseBuilder será usado no módulo de DI para criar essa classe.


// * A classe AppDatabase é o coração da sua camada de persistência quando você usa o Room,
//   a biblioteca oficial do Android para mapeamento objeto-relacional (ORM).
//   Veja por que ela é tão importante:
//
// 1. Definição do esquema do banco
// - Ao anotar a classe com @Database e listar todas as suas entidades, você está
//   declarando para o Room:
//      - Quais tabelas existirão no seu banco (aqui, apenas NoteEntity).
//      - Qual a versão do esquema (útil para migrações futuras).
//      - Se deve ou não exportar o schema em arquivos JSON (exportSchema = false desativa isso,
//        mas em apps grandes costuma-se ativar para controle de versões).
//
// 2. Ponto único de configuração
// - Centraliza em um único lugar todas as entidades; se amanhã você precisar adicionar uma nova
//   tabela, basta incluí-la no entities = [...] e incrementar a version.
// - Garante que todas as DAOs sejam criadas a partir do mesmo contexto de banco,
//   evitando inconsistências.
//
// 3. Geração de código pelo Room
// - Em tempo de compilação, o Room vai gerar uma implementação concreta de AppDatabase
//   (algo como AppDatabase_Impl) que já sabe:
//      - Como criar as tabelas no SQLite (com base nas anotações de cada @Entity).
//      - Como atualizar o esquema de uma versão para outra (migrações).
//      - Como retornar instâncias de cada DAO que você declarou.
//
// 4. Fornecimento de DAOs
// - A função abstrata fun noteDao(): NoteDao é a forma que o Room disponibiliza o seu NoteDao.
// - Você nunca instanciará AppDatabase diretamente; em vez disso, no seu módulo de Hilt você chama:
//    " Room.databaseBuilder(app, AppDatabase::class.java, "notes.db")
//      .build() "
//   e o Hilt injeta essa instância para onde for preciso. A partir daí, sempre que você
//   precisar executar uma operação de CRUD, basta chamar appDatabase.noteDao().
//
// 5. Gerenciamento de ciclo de vida e performance
// - O RoomDatabase gerencia conexões seguras com o SQLite em background threads, transações
//   atômicas e cache de entidades.
// - Usando um singleton (via Hilt) você evita criar várias instâncias do banco, economizando
//   recursos e evitando coruja de transações concorrentes.
//
// * Em resumo, a AppDatabase existe para declarar e unificar t0do o modelo de dados do seu
//   aplicativo, permitir que o Room gere o código necessário para criar e manter o banco SQLite,
//   e fornecer acesso consistente e seguro a todas as operações de persistência por meio
//   dos seus DAOs.