package calculadora;

import calculadora.entities.Operacoes;
import java.util.Scanner;

public class CalculadoraMain {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Operacoes calculadora = new Operacoes();

        System.out.println("=".repeat(15) + "Calculadora Avançada" + "=".repeat(15));
        System.out.println("Digite 'sair' a qualquer momento para encerrar.");

        while (true) {
            System.out.println("=".repeat(50));
            System.out.print("Digite a expressão (ex: 3 + 5 * 2): ");
            String entrada = scanner.nextLine().trim().toUpperCase();

            if (entrada.equals("SAIR")) {
                System.out.println("Encerrando calculadora...");
                System.out.println("=".repeat(50));
                break;
            }

            try {
                double resultado = calculadora.calcularExpressao(entrada);
                calculadora.resultado(resultado);
            } catch (Exception e) {
                System.err.println("Erro -> " + getMensagemErro(e));
            }
        }

        scanner.close();
    }

    // Metodo que traduz as mensagens de erro pro user
    private static String getMensagemErro(Exception e) {
        if (e instanceof ArithmeticException) {
            return e.getMessage(); 
        } else if (e instanceof IllegalArgumentException) {
            return e.getMessage(); 
        } else if (e.getCause() != null && e.getCause() instanceof NumberFormatException) {
            String parteInvalida = e.getCause().getMessage().split("\"")[1];
            return "Formato numérico inválido: '" + parteInvalida + "'";
        }
        return "Erro inesperado: " + e.getClass().getSimpleName();
    }
}