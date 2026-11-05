# Rastreador de Tarefas (Java)

Uma aplicação de linha de comando (CLI) para acompanhar e gerenciar sua lista de tarefas. Você adiciona tarefas, altera a descrição, muda o status e remove itens, tudo por comandos digitados no terminal.

🔗 **Repositório:** https://github.com/diogoabreu1992/task-tracker

Projeto baseado no desafio [Task Tracker](https://roadmap.sh/projects/task-tracker), da série de projetos de back-end.

## Funcionalidades

- Adicionar tarefas
- Atualizar a descrição de uma tarefa
- Remover tarefas
- Marcar tarefas como `todo`, `in-progress` ou `done`
- Listar todas as tarefas ou filtrar por status
- Tratamento de entradas inválidas, como IDs inexistentes ou comandos desconhecidos

> **Observação:** nesta versão, as tarefas são mantidas apenas na memória e são perdidas ao encerrar o programa. A persistência em arquivo JSON está planejada como próximo passo.

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

task-tracker/
├── Main.java # Interface de linha de comando e laço de leitura dos comandos
├── Lista.java # Coleção de tarefas e regras para adicionar, buscar e remover
├── Tarefa.java # Modelo de uma tarefa (id, descrição, status e datas)
├── .gitignore # Ignora os arquivos .class gerados na compilação
└── README.md


## Como compilar e executar

Na pasta do projeto:

```bash
javac *.java
java Main
```

Ao iniciar, o programa mostra o prompt `>` e aguarda comandos. Para encerrar, digite `sair`.

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
| `sair` | Encerra o programa |

## Exemplo de uso

```bash
add Estudar Java
Tarefa adicionada com sucesso (ID: 1)
add Fazer o README
Tarefa adicionada com sucesso (ID: 2)
mark-in-progress 1
Tarefa 1 marcada como in-progress.
mark-done 1
Tarefa 1 marcada como done.
list
1 [done] Estudar Java
2 [todo] Fazer o README
list todo
2 [todo] Fazer o README
update 2 Fazer o README e publicar no GitHub
Tarefa 2 atualizada.
delete 2
Tarefa 2 removida.
sair
Até logo!
```

## Descrição das classes

- **`Tarefa`** representa um item da lista. Guarda o `id`, a descrição, o status e as datas de criação e de última atualização (`LocalDateTime`).
- **`Lista`** guarda as tarefas em um `ArrayList` e gera os IDs automaticamente. Os métodos que alteram tarefas retornam `true` ou `false`, indicando se a operação foi concluída.
- **`Main`** lê os comandos do usuário, interpreta cada um e chama a `Lista`. Também é responsável por exibir as mensagens.

## Próximos passos

- Salvar e carregar as tarefas em um arquivo `tasks.json` no diretório atual
- Aceitar comandos como argumentos da linha de comando, por exemplo `java Main add "Estudar Java"`
- Adicionar testes automatizados
- Mostrar as datas de criação e atualização na listagem

## Licença

Este projeto é de uso livre para fins de estudo.