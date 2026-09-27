/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistema.dao;

import br.com.sistema.model.Client;
import br.com.sistema.jdbc.ConnectionFactory;

import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author nunes
 */
public class ClientDAO implements GenericDAO<Client>{

    @Override
    public void save(Client entity) {
        String sql = "INSERT INTO clients(name, cpf, phone, email) "
                + "VALUES(?, ?, ?, ?)";
        
        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql, 
                        PreparedStatement.RETURN_GENERATED_KEYS)){
            
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getCpf());
            statement.setString(3, entity.getPhone());
            statement.setString(4, entity.getEmail());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        entity.setId(resultSet.getInt(1));
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Error saving client" + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
        throw new RuntimeException("Database driver not found: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Client entity) {    
    String sql = "UPDATE clients "
            + "SET name = ?, cpf = ?, phone = ?, email = ? "
            + "WHERE id = ?";
    
        try (Connection connection = ConnectionFactory.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
        
        statement.setString(1, entity.getName());
        statement.setString(2, entity.getCpf());
        statement.setString(3, entity.getPhone());
        statement.setString(4, entity.getEmail());
        statement.setInt(5, entity.getId());
        
        statement.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException("Error updating client: " + e.getMessage(), e);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Database driver not found: " + ex.getMessage(), ex);
        }       
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM clients "
                + "WHERE id = ?";
        
        try (Connection connection = ConnectionFactory.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            statement.executeUpdate();
        
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting client: " + e.getMessage(), e);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Database driver not found: " + ex.getMessage(), ex);
        }
    }

    @Override
    public List<Client> listAll() {
        String sql = "SELECT id, name, cpf, phone, email "
                + "FROM clients";
        List<Client> clients = new ArrayList<>();
        
        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                
                Client client = new Client();

                client.setId(resultSet.getInt("id"));
                client.setName(resultSet.getString("name"));
                client.setCpf(resultSet.getString("cpf"));
                client.setPhone(resultSet.getString("phone"));
                client.setEmail(resultSet.getString("email"));
                
                clients.add(client);
                
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing clients: " + e.getMessage(), e);
        } catch (ClassNotFoundException ex) {
            throw new RuntimeException("Database driver not found: " + ex.getMessage(), ex);
        }
        
        return clients;
        
    }

    @Override
    public Client findById(int id) {
        String sql = "SELECT id, name, cpf, phone, email "
                + "FROM clients "
                + "WHERE id = ?";
        
        try (Connection connection = ConnectionFactory.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Client client = new Client();
                    client.setId(resultSet.getInt("id"));
                    client.setName(resultSet.getString("name"));
                    client.setCpf(resultSet.getString("cpf"));
                    client.setPhone(resultSet.getString("phone"));
                    client.setEmail(resultSet.getString("email"));
                    return client;
                }
            }
            
            System.out.println("There is no client with this ID");
            
        } catch (SQLException e) {
            throw new RuntimeException("Error finding clients: " + e.getMessage(), e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Database driver not found: " + e.getMessage(), e);
        }
        
        return null;
    }
}
