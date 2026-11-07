import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Lista {

    private final ArrayList<Tarefa> tarefas = new ArrayList<>();
    private int proximoId = 1;
    private final Path arquivo;

    // Carrega as tarefas do arquivo. Se ele não existir, cria um vazio.
    public Lista(Path arquivo) throws IOException {
        this.arquivo = arquivo;
        carregar();
    }

    @SuppressWarnings("unchecked")
    private void carregar() throws IOException {
        if (!Files.exists(arquivo)) {
            salvar();
            return;
        }

        String conteudo = Files.readString(arquivo, StandardCharsets.UTF_8);
        if (conteudo.isBlank()) {
            return;
        }

        Object raiz;
        try {
            raiz = Json.parse(conteudo);
        } catch (IllegalArgumentException e) {
            throw new IOException("o arquivo está corrompido (" + e.getMessage() + "). "
                    + "Corrija ou apague o arquivo para continuar.", e);
        }

        if (!(raiz instanceof List<?> itens)) {
            throw new IOException("o arquivo deve conter uma lista JSON de tarefas.");
        }

        for (Object item : itens) {
            if (!(item instanceof Map<?, ?> mapa)) {
                throw new IOException("há um item que não é um objeto de tarefa.");
            }
            try {
                Tarefa t = Tarefa.fromMap((Map<String, Object>) mapa);
                tarefas.add(t);
                proximoId = Math.max(proximoId, t.getId() + 1);
            } catch (IllegalArgumentException e) {
                throw new IOException("tarefa inválida no arquivo: " + e.getMessage(), e);
            }
        }
    }

    // Grava em um arquivo temporário e só então substitui o original,
    // para não corromper o JSON caso ocorra um erro no meio da escrita.
    public void salvar() throws IOException {
        StringBuilder sb = new StringBuilder("[\n");
        for (int i = 0; i < tarefas.size(); i++) {
            sb.append(tarefas.get(i).paraJson());
            if (i < tarefas.size() - 1) {
                sb.append(',');
            }
            sb.append('\n');
        }
        sb.append("]\n");

        Path temporario = Path.of(arquivo + ".tmp");
        Files.writeString(temporario, sb.toString(), StandardCharsets.UTF_8);
        Files.move(temporario, arquivo, StandardCopyOption.REPLACE_EXISTING);
    }

    public int adicionar(String descricao) throws IOException {
        int id = proximoId++;
        tarefas.add(new Tarefa(id, descricao));
        salvar();
        return id;
    }

    public Tarefa buscarPorId(int id) {
        for (Tarefa t : tarefas) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    public boolean atualizarDescricao(int id, String novaDescricao) throws IOException {
        Tarefa t = buscarPorId(id);
        if (t == null) {
            return false;
        }
        t.atualizarDescricao(novaDescricao);
        salvar();
        return true;
    }

    public boolean alterarStatus(int id, String novoStatus) throws IOException {
        Tarefa t = buscarPorId(id);
        if (t == null) {
            return false;
        }
        t.alterarStatus(novoStatus);
        salvar();
        return true;
    }

    public boolean remover(int id) throws IOException {
        Tarefa t = buscarPorId(id);
        if (t == null) {
            return false;
        }
        tarefas.remove(t);
        salvar();
        return true;
    }

    // filtro null retorna todas as tarefas
    public List<Tarefa> filtrar(String filtro) {
        return tarefas.stream()
                .filter(t -> filtro == null || t.getStatus().equals(filtro))
                .toList();
    }
}