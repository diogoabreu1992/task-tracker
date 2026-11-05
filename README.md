
# Rastreador de Tarefas (Java)

Uma aplicação de linha de comando (CLI) para acompanhar e gerenciar sua lista de tarefas. Você adiciona tarefas, altera a descrição, muda o status e remove itens, tudo por comandos digitados no terminal.

🔗 **Código-fonte:** https://github.com/diogoabreu1992/Desenvolvedor-Backend/tree/main/task_tracker

Projeto da série de desenvolvimento back-end do [repositório principal](https://github.com/diogoabreu1992/Desenvolvedor-Backend), baseado no desafio [Task Tracker](https://roadmap.sh/projects/task-tracker).

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

Clone o repositório e entre na pasta do projeto:

```bash
git clone https://github.com/diogoabreu1992/Desenvolvedor-Backend.git
cd Desenvolvedor-Backend/task_tracker
```

## Estrutura do projeto