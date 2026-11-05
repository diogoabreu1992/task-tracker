import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Lista lista = new Lista();
        Scanner entrada = new Scanner(System.in);

        System.out.println("Rastreador de Tarefas. Digite 'ajuda' para ver os comandos.");

        while (true) {
            System.out.print("> ");
            String linha = entrada.nextLine().trim();
            if (linha.isEmpty()) {
                continue;
            }

            // separa o comando da primeira palavra e o restante são os argumentos
            int espaco = linha.indexOf(' ');
            String comando = (espaco == -1) ? linha : linha.substring(0, espaco);
            String resto = (espaco == -1) ? "" : linha.substring(espaco + 1).trim();

            if (comando.equals("sair")) {
                break;
            }

            switch (comando) {
                case "ajuda" -> mostrarAjuda();

                case "add" -> {
                    if (resto.isEmpty()) {
                        System.out.println("Erro: informe a descrição. Uso: add <descrição>");
                    } else {
                        int id = lista.adicionar(resto);
                        System.out.println("Tarefa adicionada com sucesso (ID: " + id + ")");
                    }
                }

                case "update" -> {
                    String[] p = resto.split(" ", 2);
                    int id = lerId(p[0]);
                    if (p.length < 2 || id == -1) {
                        System.out.println("Uso: update <id> <nova descrição>");
                    } else if (lista.atualizarDescricao(id, p[1])) {
                        System.out.println("Tarefa " + id + " atualizada.");
                    } else {
                        System.out.println("Erro: tarefa " + id + " não encontrada.");
                    }
                }

                case "delete" -> {
                    int id = lerId(resto);
                    if (id == -1) {
                        System.out.println("Uso: delete <id>");
                    } else if (lista.remover(id)) {
                        System.out.println("Tarefa " + id + " removida.");
                    } else {
                        System.out.println("Erro: tarefa " + id + " não encontrada.");
                    }
                }

                case "mark-in-progress" -> alterarStatus(lista, resto, "in-progress");

                case "mark-done" -> alterarStatus(lista, resto, "done");

                case "list" -> {
                    if (resto.isEmpty()) {
                        lista.listar(null);
                    } else if (resto.equals("todo") || resto.equals("in-progress") || resto.equals("done")) {
                        lista.listar(resto);
                    } else {
                        System.out.println("Status inválido. Use: todo, in-progress ou done.");
                    }
                }

                default -> System.out.println("Comando desconhecido: " + comando + ". Digite 'ajuda'.");
            }
        }

        System.out.println("Até logo!");
        entrada.close();
    }

    private static void alterarStatus(Lista lista, String texto, String novoStatus) {
        int id = lerId(texto);
        if (id == -1) {
            System.out.println("Informe um ID válido.");
        } else if (lista.alterarStatus(id, novoStatus)) {
            System.out.println("Tarefa " + id + " marcada como " + novoStatus + ".");
        } else {
            System.out.println("Erro: tarefa " + id + " não encontrada.");
        }
    }

    // converte texto em número; retorna -1 se não for um número válido
    private static int lerId(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void mostrarAjuda() {
        System.out.println("Comandos disponíveis:");
        System.out.println("  add <descrição>                 adiciona uma tarefa");
        System.out.println("  update <id> <nova descrição>    altera a descrição");
        System.out.println("  delete <id>                     remove uma tarefa");
        System.out.println("  mark-in-progress <id>           marca como em andamento");
        System.out.println("  mark-done <id>                  marca como concluída");
        System.out.println("  list [todo|in-progress|done]    lista as tarefas");
        System.out.println("  ajuda                           mostra esta lista");
        System.out.println("  sair                            encerra o programa");
    }
}