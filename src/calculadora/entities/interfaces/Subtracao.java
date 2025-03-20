package calculadora.entities.interfaces;

public class Subtracao implements OperacaoInterface {
	@Override
	public double calcular (double num1, double num2) {
		return num1 - num2;
	}
}
