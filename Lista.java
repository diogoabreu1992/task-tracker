import java.util.ArrayList;

public class Lista {
    private final ArrayList<Tarefa> tarefas = new ArrayList<>();
    private int proximoId = 1;

    public int adicionar(String descricao) {
        Tarefa nova = new Tarefa(proximoId, descricao);
        tarefas.add(nova);
        return proximoId++;
    }

    public Tarefa buscarPorId(int id) {
        for (Tarefa t : tarefas) {
            if (t.getId() == id) {
                return t;
            }
        }
        return null;
    }

    public boolean atualizarDescricao(int id, String novaDescricao) {
        Tarefa t = buscarPorId(id);
        if (t == null) {
            return false;
        }
        t.atualizarDescricao(novaDescricao);
        return true;
    }

    public boolean alterarStatus(int id, String novoStatus) {
        Tarefa t = buscarPorId(id);
        if (t == null) {
            return false;
        }
        t.alterarStatus(novoStatus);
        return true;
    }

    public boolean remover(int id) {
        Tarefa t = buscarPorId(id);
        if (t == null) {
            return false;
        }
        tarefas.remove(t);
        return true;
    }

    // filtro null lista todas; caso contrário, lista só as tarefas com esse status
    public void listar(String filtro) {
        boolean encontrou = false;
        for (Tarefa t : tarefas) {
            if (filtro == null || t.getStatus().equals(filtro)) {
                t.exibir();
                encontrou = true;
            }
        }
        if (!encontrou) {
            System.out.println("Nenhuma tarefa encontrada.");
        }
    }
}