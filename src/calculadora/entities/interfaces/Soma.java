package calculadora.entities.interfaces;

public class Soma implements OperacaoInterface {
	@Override
	public double calcular (double num1, double num2) {
		return num1 + num2;
	}
}