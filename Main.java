import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Main {

    // Caminho relativo: o arquivo fica na pasta de onde o comando é executado
    private static final Path ARQUIVO = Path.of("tasks.json");

    public static void main(String[] args) {
        if (args.length == 0 || args[0].equals("ajuda") || args[0].equals("--help")) {
            mostrarAjuda();
            return;
        }

        int codigo;
        try {
            Lista lista = new Lista(ARQUIVO);
            codigo = executar(lista, args);
        } catch (IOException e) {
            System.err.println("Erro ao acessar " + ARQUIVO + ": " + e.getMessage());
            codigo = 1;
        }
        System.exit(codigo);
    }

    private static int executar(Lista lista, String[] args) throws IOException {
        String comando = args[0];
        String[] resto = Arrays.copyOfRange(args, 1, args.length);

        switch (comando) {
            case "add" -> {
                String descricao = juntar(resto);
                if (descricao.isEmpty()) {
                    return erro("Uso: java Main add <descrição>");
                }
                int id = lista.adicionar(descricao);
                System.out.println("Tarefa adicionada com sucesso (ID: " + id + ")");
            }

            case "update" -> {
                if (resto.length < 2) {
                    return erro("Uso: java Main update <id> <nova descrição>");
                }
                int id = lerId(resto[0]);
                if (id == -1) {
                    return erro("ID inválido: " + resto[0]);
                }
                String novaDescricao = juntar(Arrays.copyOfRange(resto, 1, resto.length));
                if (novaDescricao.isEmpty()) {
                    return erro("A nova descrição não pode ser vazia.");
                }
                if (!lista.atualizarDescricao(id, novaDescricao)) {
                    return erro("Tarefa " + id + " não encontrada.");
                }
                System.out.println("Tarefa " + id + " atualizada.");
            }

            case "delete" -> {
                int id = (resto.length == 1) ? lerId(resto[0]) : -1;
                if (id == -1) {
                    return erro("Uso: java Main delete <id>");
                }
                if (!lista.remover(id)) {
                    return erro("Tarefa " + id + " não encontrada.");
                }
                System.out.println("Tarefa " + id + " removida.");
            }

            case "mark-in-progress" -> {
                return marcar(lista, comando, resto, Tarefa.IN_PROGRESS);
            }

            case "mark-done" -> {
                return marcar(lista, comando, resto, Tarefa.DONE);
            }

            case "list" -> {
                if (resto.length > 1) {
                    return erro("Uso: java Main list [todo|in-progress|done]");
                }
                String filtro = (resto.length == 1) ? resto[0] : null;
                if (filtro != null && !Tarefa.STATUS_VALIDOS.contains(filtro)) {
                    return erro("Status inválido. Use: todo, in-progress ou done.");
                }
                List<Tarefa> encontradas = lista.filtrar(filtro);
                if (encontradas.isEmpty()) {
                    System.out.println("Nenhuma tarefa encontrada.");
                } else {
                    encontradas.forEach(Tarefa::exibir);
                }
            }

            default -> {
                return erro("Comando desconhecido: " + comando + ". Use 'ajuda' para ver os comandos.");
            }
        }
        return 0;
    }

    private static int marcar(Lista lista, String comando, String[] resto, String novoStatus)
            throws IOException {
        int id = (resto.length == 1) ? lerId(resto[0]) : -1;
        if (id == -1) {
            return erro("Uso: java Main " + comando + " <id>");
        }
        if (!lista.alterarStatus(id, novoStatus)) {
            return erro("Tarefa " + id + " não encontrada.");
        }
        System.out.println("Tarefa " + id + " marcada como " + novoStatus + ".");
        return 0;
    }

    // Junta os argumentos separados por espaço, para aceitar descrições sem aspas
    private static String juntar(String[] partes) {
        return String.join(" ", partes).trim();
    }

    // Converte o texto em ID positivo; retorna -1 se for inválido
    private static int lerId(String texto) {
        try {
            int id = Integer.parseInt(texto);
            return id > 0 ? id : -1;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static int erro(String mensagem) {
        System.err.println("Erro: " + mensagem);
        return 1;
    }

    private static void mostrarAjuda() {
        System.out.println("Rastreador de Tarefas");
        System.out.println("Uso: java Main <comando> [argumentos]");
        System.out.println();
        System.out.println("  add <descrição>                 adiciona uma tarefa");
        System.out.println("  update <id> <nova descrição>    altera a descrição");
        System.out.println("  delete <id>                     remove uma tarefa");
        System.out.println("  mark-in-progress <id>           marca como em andamento");
        System.out.println("  mark-done <id>                  marca como concluída");
        System.out.println("  list [todo|in-progress|done]    lista as tarefas");
        System.out.println("  ajuda                           mostra esta lista");
    }
}