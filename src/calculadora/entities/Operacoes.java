package calculadora.entities;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import calculadora.entities.interfaces.*;

public class Operacoes {

    private OperacaoInterface soma;
    private OperacaoInterface subtracao;
    private OperacaoInterface multiplicacao;
    private OperacaoInterface divisao;

    public Operacoes() {
        soma = new Soma();
        subtracao = new Subtracao();
        multiplicacao = new Multiplicacao();
        divisao = new Divisao();
    }

    // Método principal para calcular expressões
    public double calcularExpressao(String expressao) {
        validarExpressao(expressao);
        String expr = expressao.replace(" ", "").replace(",", ".");
        
        expr = adicionarMultiplicacaoEntreNumeroEParenteses(expr); // adiciona * implicitos
        expr = processarParenteses(expr); // e depois resolve parênteses

        // processa operações na ordem correta 
        while (expr.matches(".*[*/].*")) {
            expr = processarOperacao(expr, "[*/]"); // multiplicações/divisões primeiro
        }

        while (expr.matches(".*(?<!^)-.*|.*\\+.*")) {
            expr = processarOperacao(expr, "[+-]"); // adições/subtrações depois
        }

        return Double.parseDouble(expr);
    }

    // Resolve parênteses recursivamente
    private String processarParenteses(String expr) {
        while (expr.contains("(")) {
            int parenteseAberto = expr.lastIndexOf("(");
            int parenteseFechado = expr.indexOf(")", parenteseAberto);
            
            if (parenteseFechado == -1) {
                throw new IllegalArgumentException("Parênteses desbalanceados!");
            }
            
            String dentroParenteses = expr.substring(parenteseAberto + 1, parenteseFechado);
            double resultado = calcularExpressao(dentroParenteses); // chamada recursiva
            
            expr = expr.substring(0, parenteseAberto) + resultado + expr.substring(parenteseFechado + 1);
        }
        return expr;
    }
    
    // Insere * onde tem multiplicação implícita
    private String adicionarMultiplicacaoEntreNumeroEParenteses(String expr) {
        expr = expr.replaceAll("(\\d)(\\()", "$1*$2");     
        expr = expr.replaceAll("(\\))(-?\\d)", "$1*$2");  
        expr = expr.replaceAll("(\\))(\\()", "$1*$2");      
        return expr;
    }

    // Validações da expressão
    private void validarExpressao(String expressao) {
        if (expressao.isEmpty()) {
            throw new IllegalArgumentException("Expressão vazia!");
        }

        // valida parênteses balanceados
        int balanceamento = 0;
        for (char c : expressao.toCharArray()) {
            if (c == '(') balanceamento++;
            else if (c == ')') balanceamento--;
            if (balanceamento < 0) break;
        }
        if (balanceamento != 0) {
            throw new IllegalArgumentException("Parênteses desbalanceados!");
        }

        // valida caracteres permitidos
        if (!expressao.matches("^[\\d+\\-*/.,()\\s]*$")) {
            throw new IllegalArgumentException("Caractere inválido na expressão!");
        }
        
        // valida números com multiplos pontos/vírgulas
        if (expressao.matches(".*\\b\\d+([.,]\\d+){2,}\\b.*")) {
            throw new IllegalArgumentException("Número com múltiplos pontos/vírgulas!");
        }

        // valida se termina com operador
        if (expressao.matches(".*[-+*/]\\s*$")) {
            throw new IllegalArgumentException("Expressão termina com operador!");
        }
        
        String exprSemEspacos = expressao.replaceAll("\\s+", ""); // adição nessaria 

        // valida operadores unários corretamente (ex: 5+-3 é válido, mas 5+*3 não)
        if (exprSemEspacos.matches(".*([+*/]{2,}|-[+*/]|\\+[+*/]|\\*[+*/]|/[+*/]).*")) {
            throw new IllegalArgumentException("Operadores consecutivos inválidos!");
        }

        // valida operador inicial invalido
        if (expressao.matches("^[*/+].*")) {
            throw new IllegalArgumentException("Operador inválido no início!");
        }

        // valida numeros sem operadores
        if (expressao.matches(".*\\d\\s+\\d.*")) {
            throw new IllegalArgumentException("Números sem operadores!");
        }
    }
    
    // Processa uma operação individual
    private String processarOperacao(String expr, String operadores) {
    	Matcher m = Pattern.compile("(-?\\d+\\.?\\d*)([" + operadores + "])(-?\\d+\\.?\\d*)").matcher(expr);
        
        if (m.find()) {
            try {
                String num1Str = m.group(1).replace("+", "");
                double num1 = Double.parseDouble(num1Str);
                double num2 = Double.parseDouble(m.group(3));
                String op = m.group(2);
                
                double resultado = realizarOperacao(op, num1, num2);
                return expr.replace(m.group(0), String.valueOf(resultado));
                
            } catch (NumberFormatException e) {
                String parteInvalida = e.getMessage().split("\"")[1];
                throw new IllegalArgumentException("Número inválido: '" + parteInvalida + "'");
            }
        }
        return expr;
    }

    // Realiza a operação para o operador correspondente
    private double realizarOperacao(String operador, double num1, double num2) {
        switch (operador) {
            case "+": return soma.calcular(num1, num2);
            case "-": return subtracao.calcular(num1, num2);
            case "*": return multiplicacao.calcular(num1, num2);
            case "/": return divisao.calcular(num1, num2);
            default: throw new IllegalArgumentException("Operação inválida!");
        }
    }


    // Formata o resultado no padrão BR (vírgula decimal)
    public void resultado(double resultado) {
		System.out.printf("O resultado é: %s%n", String.format(Locale.of("pt", "BR"), "%,.2f", resultado));
	}
}