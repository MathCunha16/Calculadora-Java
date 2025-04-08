package calculadora.entities;

import java.time.LocalDateTime;

public class HistoricoOperacao {
	private Integer id;
	private String operacao;
	private String resultado;
	private LocalDateTime dataHora;
	private String statusOperacao;
	
	public HistoricoOperacao() {
		
	}
	
	public HistoricoOperacao(Integer id, String operacao, String resultado, LocalDateTime dataHora, String statusOperacao) {
        this.id = id;
        this.operacao = operacao;
        this.resultado = resultado;
        this.dataHora = dataHora;
        this.statusOperacao = statusOperacao;
    }
	
	public Integer getId() {
        return id;
    }

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOperacao() {
		return operacao;
	}

	public String getResultado() {
		return resultado;
	}

	public LocalDateTime getDataHora() {
		return dataHora;
	}

	public String getStatusOperacao() {
		return statusOperacao;
	}

	public void setStatusOperacao(String statusOperacao) {
		this.statusOperacao = statusOperacao;
	}

}
