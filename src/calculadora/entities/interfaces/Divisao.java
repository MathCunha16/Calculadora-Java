package calculadora.entities.interfaces;

public class Divisao implements OperacaoInterface {
	@Override
	public double calcular (double num1, double num2) {
		if(num2 == 0) {
			throw new ArithmeticException("Não é possível dividir um número por 0.");
		}
		return num1 / num2;
	}
}