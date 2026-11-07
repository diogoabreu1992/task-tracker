import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

public class Tarefa {

    public static final String TODO = "todo";
    public static final String IN_PROGRESS = "in-progress";
    public static final String DONE = "done";
    public static final List<String> STATUS_VALIDOS = List.of(TODO, IN_PROGRESS, DONE);

    private final int id;
    private String descricao;
    private String status;
    private final LocalDateTime criadaEm;
    private LocalDateTime atualizadaEm;

    // Nova tarefa, criada pelo usuário
    public Tarefa(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
        this.status = TODO;
        this.criadaEm = agora();
        this.atualizadaEm = this.criadaEm;
    }

    // Construtor usado ao carregar do JSON
    private Tarefa(int id, String descricao, String status,
                   LocalDateTime criadaEm, LocalDateTime atualizadaEm) {
        this.id = id;
        this.descricao = descricao;
        this.status = status;
        this.criadaEm = criadaEm;
        this.atualizadaEm = atualizadaEm;
    }

    private static LocalDateTime agora() {
        return LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    }

    // Cria uma tarefa a partir do mapa lido do JSON, validando cada campo
    public static Tarefa fromMap(Map<String, Object> m) {
        Object id = m.get("id");
        Object descricao = m.get("description");
        Object status = m.get("status");
        Object criada = m.get("createdAt");
        Object atualizada = m.get("updatedAt");

        if (!(id instanceof Long n) || !(descricao instanceof String d)
                || !(status instanceof String s) || !(criada instanceof String c)
                || !(atualizada instanceof String u)) {
            throw new IllegalArgumentException("tarefa com campos ausentes ou com tipo inválido");
        }
        if (!STATUS_VALIDOS.contains(s)) {
            throw new IllegalArgumentException("status inválido: " + s);
        }
        try {
            return new Tarefa(n.intValue(), d, s, LocalDateTime.parse(c), LocalDateTime.parse(u));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("data inválida na tarefa " + n, e);
        }
    }

    public int getId() {
        return id;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getStatus() {
        return status;
    }

    public void atualizarDescricao(String novaDescricao) {
        this.descricao = novaDescricao;
        this.atualizadaEm = agora();
    }

    public void alterarStatus(String novoStatus) {
        this.status = novoStatus;
        this.atualizadaEm = agora();
    }

    public void exibir() {
        System.out.println(id + " [" + status + "] " + descricao);
    }

    // Representação JSON desta tarefa, com a indentação usada no arquivo
    public String paraJson() {
        return "  {\n"
                + "    \"id\": " + id + ",\n"
                + "    \"description\": " + Json.paraTexto(descricao) + ",\n"
                + "    \"status\": " + Json.paraTexto(status) + ",\n"
                + "    \"createdAt\": " + Json.paraTexto(criadaEm.toString()) + ",\n"
                + "    \"updatedAt\": " + Json.paraTexto(atualizadaEm.toString()) + "\n"
                + "  }";
    }
}