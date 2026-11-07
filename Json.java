import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// Leitor e escritor de JSON simples, usando apenas a biblioteca padrão.
public class Json {

    private final String texto;
    private int pos = 0;

    private Json(String texto) {
        this.texto = texto;
    }

    public static Object parse(String texto) {
        Json leitor = new Json(texto);
        Object valor = leitor.lerValor();
        leitor.pularEspacos();
        if (leitor.pos != texto.length()) {
            throw leitor.erro("conteúdo inesperado após o fim do JSON");
        }
        return valor;
    }

    // Converte um texto Java para JSON, com aspas e caracteres escapados.
    public static String paraTexto(String s) {
        StringBuilder sb = new StringBuilder("\"");
        for (char c : s.toCharArray()) {
            switch (c) {
                case '"' -> sb.append("\\\"");
                case '\\' -> sb.append("\\\\");
                case '\n' -> sb.append("\\n");
                case '\r' -> sb.append("\\r");
                case '\t' -> sb.append("\\t");
                default -> {
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int) c));
                    } else {
                        sb.append(c);
                    }
                }
            }
        }
        return sb.append('"').toString();
    }

    private Object lerValor() {
        pularEspacos();
        char c = atual();
        return switch (c) {
            case '{' -> lerObjeto();
            case '[' -> lerLista();
            case '"' -> lerString();
            case 't' -> lerLiteral("true", Boolean.TRUE);
            case 'f' -> lerLiteral("false", Boolean.FALSE);
            case 'n' -> lerLiteral("null", null);
            default -> {
                if (c == '-' || Character.isDigit(c)) {
                    yield lerNumero();
                }
                throw erro("caractere inesperado '" + c + "'");
            }
        };
    }

    private Map<String, Object> lerObjeto() {
        Map<String, Object> mapa = new LinkedHashMap<>();
        pos++; // pula '{'
        pularEspacos();
        if (atual() == '}') {
            pos++;
            return mapa;
        }
        while (true) {
            pularEspacos();
            if (atual() != '"') {
                throw erro("esperado nome de campo entre aspas");
            }
            String chave = lerString();
            pularEspacos();
            esperar(':');
            mapa.put(chave, lerValor());
            pularEspacos();
            char c = atual();
            pos++;
            if (c == '}') {
                return mapa;
            }
            if (c != ',') {
                throw erro("esperado ',' ou '}'");
            }
        }
    }

    private List<Object> lerLista() {
        List<Object> itens = new ArrayList<>();
        pos++; // pula '['
        pularEspacos();
        if (atual() == ']') {
            pos++;
            return itens;
        }
        while (true) {
            itens.add(lerValor());
            pularEspacos();
            char c = atual();
            pos++;
            if (c == ']') {
                return itens;
            }
            if (c != ',') {
                throw erro("esperado ',' ou ']'");
            }
        }
    }

    private String lerString() {
        pos++; // pula a aspa inicial
        StringBuilder sb = new StringBuilder();
        while (true) {
            char c = atual();
            pos++;
            if (c == '"') {
                return sb.toString();
            }
            if (c == '\\') {
                char e = atual();
                pos++;
                switch (e) {
                    case '"' -> sb.append('"');
                    case '\\' -> sb.append('\\');
                    case '/' -> sb.append('/');
                    case 'n' -> sb.append('\n');
                    case 'r' -> sb.append('\r');
                    case 't' -> sb.append('\t');
                    case 'b' -> sb.append('\b');
                    case 'f' -> sb.append('\f');
                    case 'u' -> {
                        if (pos + 4 > texto.length()) {
                            throw erro("escape unicode incompleto");
                        }
                        sb.append((char) Integer.parseInt(texto.substring(pos, pos + 4), 16));
                        pos += 4;
                    }
                    default -> throw erro("sequência de escape inválida");
                }
            } else {
                sb.append(c);
            }
        }
    }

    private Long lerNumero() {
        int inicio = pos;
        if (atual() == '-') {
            pos++;
        }
        while (pos < texto.length() && Character.isDigit(texto.charAt(pos))) {
            pos++;
        }
        return Long.parseLong(texto.substring(inicio, pos));
    }

    private Object lerLiteral(String palavra, Object valor) {
        if (!texto.startsWith(palavra, pos)) {
            throw erro("valor inválido");
        }
        pos += palavra.length();
        return valor;
    }

    private char atual() {
        if (pos >= texto.length()) {
            throw erro("fim inesperado do conteúdo");
        }
        return texto.charAt(pos);
    }

    private void esperar(char esperado) {
        if (atual() != esperado) {
            throw erro("esperado '" + esperado + "'");
        }
        pos++;
    }

    private void pularEspacos() {
        while (pos < texto.length() && Character.isWhitespace(texto.charAt(pos))) {
            pos++;
        }
    }

    private IllegalArgumentException erro(String mensagem) {
        return new IllegalArgumentException("JSON inválido na posição " + pos + ": " + mensagem);
    }
}