package calculadora;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

import calculadora.entities.HistoricoOperacao;
import calculadora.entities.Operacoes;
import db.HistoricoDAO;

public class CalculadoraMain {
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		Operacoes calculadora = new Operacoes();
		HistoricoDAO historicoDAO = new HistoricoDAO();

		System.out.println("=".repeat(15) + "Calculadora Avançada" + "=".repeat(15));
		System.out.println("Comandos disponíveis:");
		System.out.println("- SAIR: Encerrar calculadora");
		System.out.println("- HISTORICO: Ver últimas operações");
		System.out.println("- EDITAR: Editar operação");
		System.out.println("- DELETAR: Deletar operação)");

		loopPrincipal: // Label pra controle do loop
		while (true) {
			System.out.println("=".repeat(50));
			System.out.print("Digite uma expressão ou comando: ");
			String entrada = scanner.nextLine().trim().toUpperCase();

			switch (entrada) {
			case "SAIR":
				System.out.println("Encerrando calculadora...");
				System.out.println("=".repeat(50));
				break loopPrincipal;

			case "HISTORICO":
				List<HistoricoOperacao> historico = historicoDAO.consultarUltimas10Operacoes();
				exibirHistorico(historico);
				continue loopPrincipal;

			case "EDITAR":
				handleEditar(scanner, historicoDAO, calculadora);
				continue loopPrincipal;

			case "DELETAR":
				handleDeletar(scanner, historicoDAO);
				continue loopPrincipal;

			default:
				processarExpressao(entrada, calculadora, historicoDAO);
			}
		}
		scanner.close();
	}

	// metodo pra processar a conta e enviar pro DB
	private static void processarExpressao(String entrada, Operacoes calculadora, HistoricoDAO historicoDAO) {
		HistoricoOperacao operacao = null;
		try {
			double resultado = calculadora.calcularExpressao(entrada);
			calculadora.resultado(resultado);
			operacao = new HistoricoOperacao(null, // ID é gerado pelo banco
					entrada, String.valueOf(resultado), LocalDateTime.now(), "Bem sucedida" // se der certo vai aplicar
																							// esse status
			);
		} catch (Exception e) {
			operacao = new HistoricoOperacao(null, // ID é gerado pelo banco
					entrada, null, // e setta o resultado como nulo
					LocalDateTime.now(), "Mal sucedida" // se der errado, esse
			);
			System.err.println("Erro -> " + getMensagemErro(e));
		} finally {
			if (operacao != null) {
				historicoDAO.inserir(operacao);
			}
		}
	}

	// metodo pra dar Update
	private static void handleEditar(Scanner scanner, HistoricoDAO historicoDAO, Operacoes calculadora) {
		System.out.print("Digite o ID da operação: ");
		int id = Integer.parseInt(scanner.nextLine());

		System.out.print("Digite a nova expressão: ");
		String novaExpressao = scanner.nextLine().trim();

		try {
			historicoDAO.atualizarOperacao(id, novaExpressao, calculadora);
			System.out.println("Operação atualizada com sucesso!");
		} catch (Exception e) {
			System.err.println("Erro na edição: " + e.getMessage());
		}
	}

	// metodo pra deletar 
	private static void handleDeletar(Scanner scanner, HistoricoDAO historicoDAO) {
		System.out.print("Digite o ID da operação a ser deletada: ");
		int id = Integer.parseInt(scanner.nextLine());

		try {
			historicoDAO.deletarOperacao(id);
			System.out.println("Operação deletada com sucesso!");
		} catch (Exception e) {
			System.err.println("Erro: " + e.getMessage());
		}
	}

	// metodo pra mostar historico
	private static void exibirHistorico(List<HistoricoOperacao> historico) {
	    System.out.println("\nÚltimas 10 operações:");
	    System.out.println("-".repeat(100));  
	    System.out.printf("| %-4s | %-20s | %-30s | %-15s | %-12s |%n", 
	        "ID", "Data/Hora", "Operação", "Resultado", "Status");
	    
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	    for (HistoricoOperacao op : historico) {
	        String resultado = op.getResultado() != null ? op.getResultado() : "ERRO";  // Pega o resultado
	        System.out.printf("| %-4d | %-20s | %-30s | %-15s | %-12s |%n",
	            op.getId(),
	            op.getDataHora().format(formatter),
	            op.getOperacao(),
	            resultado, 
	            op.getStatusOperacao()
	        );
	    }
	    System.out.println("-".repeat(100) + "\n");  
	}

	// metodo de tratamento de erro
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