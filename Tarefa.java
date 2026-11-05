import java.time.LocalDateTime;

public class Tarefa {
    private final int id;
    private String descricao;
    private String status;
    private final LocalDateTime criadaEm;
    private LocalDateTime atualizadaEm;

    public Tarefa(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        this.status = "todo";
        this.criadaEm = LocalDateTime.now();
        this.atualizadaEm = this.criadaEm;
    }

    public int getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public void atualizarDescricao(String novaDescricao) {
        this.descricao = novaDescricao;
        this.atualizadaEm = LocalDateTime.now();
    }

    public void alterarStatus(String novoStatus) {
        this.status = novoStatus;
        this.atualizadaEm = LocalDateTime.now();
    }

    public void exibir() {
        System.out.println(id + " [" + status + "] " + descricao);
    }
}