package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DB {
    private static Properties props = loadProperties(); // Propriedades carregadas uma vez

    
    public static Connection getConnection() {
        try {
            String url = props.getProperty("dburl") + "?allowPublicKeyRetrieval=true";
            return DriverManager.getConnection(url, props); // Retorna uma NOVA conexão
        } catch (SQLException e) {
            throw new DbException("Falha ao conectar ao banco: " + e.getMessage());
        }
    }

    private static Properties loadProperties() {
        try (FileInputStream fs = new FileInputStream("db.properties")) {
            Properties props = new Properties();
            props.load(fs);
            return props;
        } catch (IOException e) {
            throw new DbException("Arquivo db.properties não encontrado!");
        }
    }
}