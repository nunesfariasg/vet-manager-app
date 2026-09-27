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
            throw new RuntimeException("Driver not found. Check the libraries.", exception);
        } catch (SQLException exception) {
            throw new RuntimeException("Error connecting to the database. Check the URL, Username or password", exception);
        }
    }
    
}
