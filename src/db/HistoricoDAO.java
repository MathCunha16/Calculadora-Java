package db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import calculadora.entities.HistoricoOperacao;
import calculadora.entities.Operacoes;

public class HistoricoDAO {
    
    // Insere nova operação
    public void inserir(HistoricoOperacao operacao) {
        String sql = "INSERT INTO historico_operacoes (operacao, resultado, data_hora, status_operacao) VALUES (?, ?, ?, ?)";

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, operacao.getOperacao());
            stmt.setString(2, operacao.getResultado());
            stmt.setTimestamp(3, Timestamp.valueOf(operacao.getDataHora()));
            stmt.setString(4, operacao.getStatusOperacao());

            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new DbException("Erro ao salvar histórico: " + e.getMessage());
        }
    }

    // Consulta últimas 10 operações
    public List<HistoricoOperacao> consultarUltimas10Operacoes() {
        String sql = "SELECT * FROM historico_operacoes ORDER BY data_hora DESC LIMIT 10";
        List<HistoricoOperacao> operacoes = new ArrayList<>();

        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                operacoes.add(new HistoricoOperacao(
                    rs.getInt("id"),
                    rs.getString("operacao"),
                    rs.getString("resultado"),
                    rs.getTimestamp("data_hora").toLocalDateTime(),
                    rs.getString("status_operacao")
                ));
            }
        } catch (SQLException e) {
            throw new DbException("Erro ao consultar histórico: " + e.getMessage());
        }
        return operacoes;
    }

    // Busca operação por ID
    public HistoricoOperacao buscarPorId(int id) {
        String sql = "SELECT * FROM historico_operacoes WHERE id = ?";
        
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return new HistoricoOperacao(
                    rs.getInt("id"),
                    rs.getString("operacao"),
                    rs.getString("resultado"),
                    rs.getTimestamp("data_hora").toLocalDateTime(),
                    rs.getString("status_operacao")
                );
            }
            return null;
            
        } catch (SQLException e) {
            throw new DbException("Operação não encontrada: " + e.getMessage());
        }
    }

    // Atualiza operação existente
    public void atualizarOperacao(int id, String novaExpressao, Operacoes calculadora) {
        HistoricoOperacao operacaoExistente = buscarPorId(id);
        if (operacaoExistente == null) {
            throw new DbException("ID " + id + " não encontrado!");
        }

        HistoricoOperacao operacaoAtualizada = processarAtualizacao(novaExpressao, calculadora);
        operacaoAtualizada.setId(id);

        String sql = "UPDATE historico_operacoes SET operacao = ?, resultado = ?, data_hora = ?, status_operacao = ? WHERE id = ?";
        
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, operacaoAtualizada.getOperacao());
            stmt.setString(2, operacaoAtualizada.getResultado());
            stmt.setTimestamp(3, Timestamp.valueOf(operacaoAtualizada.getDataHora()));
            stmt.setString(4, operacaoAtualizada.getStatusOperacao());
            stmt.setInt(5, id);
            
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new DbException("Erro na atualização: " + e.getMessage());
        }
    }
    
    // Deleta operação existente
    public void deletarOperacao(int id) {
        String sql = "DELETE FROM historico_operacoes WHERE id = ?";
        
        try (Connection conn = DB.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();
            
            if (affectedRows == 0) {
                throw new DbException("Nenhuma operação encontrada com o ID " + id);
            }
            
        } catch (SQLException e) {
            throw new DbException("Erro ao deletar operação: " + e.getMessage());
        }
    }
    
    // Processa nova expressão para atualização
    private HistoricoOperacao processarAtualizacao(String expressao, Operacoes calculadora) {
        try {
            double resultado = calculadora.calcularExpressao(expressao);
            return new HistoricoOperacao(
                null,
                expressao,
                String.valueOf(resultado),
                java.time.LocalDateTime.now(),
                "Bem sucedida"
            );
        } catch (Exception e) {
            return new HistoricoOperacao(
                null,
                expressao,
                null,
                java.time.LocalDateTime.now(),
                "Mal sucedida"
            );
        }
    }
}