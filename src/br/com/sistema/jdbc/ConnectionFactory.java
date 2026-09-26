package br.com.sistema.jdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author nunes
 */
public class ConnectionFactory {
    private static final String URL = "jdbc:postgresql://localhost:5432/vet_manager_db";
    private static final String USER = "postgres";
    private static final String PASS = "123";
        
    public static Connection getConnection() throws ClassNotFoundException, SQLException {
        try {
            
            Class.forName("org.postgresql.Driver");
            
            return DriverManager.getConnection(URL, USER, PASS);
            
        } catch (ClassNotFoundException exception) {
            throw new RuntimeException("Driver não encontrado. Verifique as bibliotecas.", exception);
        } catch (SQLException exception) {
            throw new RuntimeException("Erro ao conectar ao banco de dados. Verifique a URL, Usuário ou senha.", exception);
        }
    }
    
}
