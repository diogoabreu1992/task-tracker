# Rastreador de Tarefas (Java)

Uma aplicação de linha de comando (CLI) para acompanhar e gerenciar sua lista de tarefas. Você adiciona, altera, conclui e remove tarefas com comandos passados diretamente no terminal. As tarefas são salvas em um arquivo `tasks.json` no diretório atual.

🔗 **Repositório:** https://github.com/diogoabreu1992/task-tracker

Projeto baseado no desafio [Task Tracker](https://roadmap.sh/projects/task-tracker), da série de projetos de back-end.

## Funcionalidades

- Adicionar tarefas
- Atualizar a descrição de uma tarefa
- Remover tarefas
- Marcar tarefas como `todo`, `in-progress` ou `done`
- Listar todas as tarefas ou filtrar por status
- Persistência em arquivo JSON, criado automaticamente na primeira execução
- Leitura e escrita de arquivos com `java.nio.file`, sem bibliotecas externas
- Tratamento de erros para entradas inválidas, IDs inexistentes e arquivo corrompido

## Requisitos

- Java 21 ou superior (JDK, que inclui o `javac`)

Para conferir a instalação:

```bash
java -version
javac -version
```

## Instalação

```bash
git clone https://github.com/diogoabreu1992/task-tracker.git
cd task-tracker
```

## Estrutura do projeto

```text
task-tracker/
├── Main.java      # Interpreta os argumentos da linha de comando e exibe as mensagens
├── Lista.java     # Gerencia as tarefas e lê/grava o arquivo tasks.json
├── Tarefa.java    # Modelo de uma tarefa (id, descrição, status e datas)
├── Json.java      # Leitor e escritor de JSON, usando apenas a biblioteca padrão
├── .gitignore     # Ignora arquivos .class e o arquivo de dados
└── README.md      # Documentação do projeto
```

## Como compilar

Na pasta do projeto:

```bash
javac *.java
```

## Como executar

Os comandos são passados como argumentos:

```bash
java Main <comando> [argumentos]
```

Use aspas quando quiser manter espaços em um argumento, ou deixe sem aspas, pois os argumentos de descrição são juntados automaticamente:

```bash
java Main add "Estudar Java"
java Main add Estudar Java     # mesmo resultado
```

## Comandos

| Comando | Descrição |
|---------|-----------|
| `add <descrição>` | Adiciona uma nova tarefa com status `todo` |
| `update <id> <nova descrição>` | Altera a descrição de uma tarefa |
| `delete <id>` | Remove uma tarefa |
| `mark-in-progress <id>` | Marca a tarefa como `in-progress` |
| `mark-done <id>` | Marca a tarefa como `done` |
| `list` | Lista todas as tarefas |
| `list <status>` | Lista apenas as tarefas com o status informado (`todo`, `in-progress` ou `done`) |
| `ajuda` | Mostra a lista de comandos |

## Exemplo de uso

```bash
$ java Main add "Estudar Java"
Tarefa adicionada com sucesso (ID: 1)

$ java Main add "Fazer o README"
Tarefa adicionada com sucesso (ID: 2)

$ java Main mark-in-progress 1
Tarefa 1 marcada como in-progress.

$ java Main mark-done 1
Tarefa 1 marcada como done.

$ java Main list
1 [done] Estudar Java
2 [todo] Fazer o README

$ java Main list todo
2 [todo] Fazer o README

$ java Main update 2 "Fazer o README e publicar no GitHub"
Tarefa 2 atualizada.

$ java Main delete 2
Tarefa 2 removida.
```

## Arquivo de dados

As tarefas são salvas em `tasks.json`, na pasta de onde o comando é executado:

```json
[
  {
    "id": 1,
    "description": "Estudar Java",
    "status": "done",
    "createdAt": "2026-11-05T10:30:00",
    "updatedAt": "2026-11-05T11:15:00"
  }
]
```

Observações sobre o arquivo:

- Se `tasks.json` não existir, ele é criado automaticamente, com uma lista vazia.
- Como o arquivo fica no diretório atual, executar o programa em outra pasta mostra outra lista de tarefas.
- Ao gravar, o programa usa um arquivo temporário (`tasks.json.tmp`) e só substitui o original quando a escrita termina, para evitar perda de dados.
- Se o JSON estiver corrompido, o programa exibe um erro e não sobrescreve o arquivo.

## Tratamento de erros

O programa informa o problema e retorna código de saída `1` nos seguintes casos:

- Comando desconhecido ou argumentos ausentes
- ID inválido (não numérico ou menor que 1) ou inexistente
- Status inválido ao filtrar a lista
- Descrição vazia em `add` ou `update`
- Arquivo `tasks.json` corrompido, ilegível ou sem permissão de escrita

## Descrição das classes

- **`Main`** lê os argumentos, chama a `Lista` e exibe as mensagens ao usuário.
- **`Lista`** mantém as tarefas em um `ArrayList`, gera os IDs e carrega e salva o `tasks.json`.
- **`Tarefa`** representa um item da lista, com `id`, descrição, status e datas de criação e atualização (`LocalDateTime`).
- **`Json`** converte o texto do arquivo em objetos Java e os objetos de volta em texto, sem bibliotecas externas.

## Licença

Este projeto é de uso livre para fins de estudo.