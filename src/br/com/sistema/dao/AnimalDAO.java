package br.com.sistema.dao;

import br.com.sistema.jdbc.ConnectionFactory;
import br.com.sistema.model.Animal;
import br.com.sistema.model.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author nunes
 */
public class AnimalDAO implements GenericDAO<Animal> {

    @Override
    public void save(Animal entity) {
        String sql = "INSERT INTO animals(name, species, breed, age, client_id) VALUES(?, ?, ?, ?, ?)";
        
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSpecies());
            statement.setString(3, entity.getBreed());
            statement.setInt(4, entity.getAge());
            statement.setInt(5, entity.getClient().getId());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        entity.setId(resultSet.getInt(1));
                    }
                }
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error saving animal: " + e.getMessage(), e);
        }    
    }

    @Override
    public void update(Animal entity) {
        String sql = "UPDATE animals SET name = ?, species = ?, breed = ?, age = ?, client_id = ? WHERE id = ?";
    
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
        
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getSpecies());
            statement.setString(3, entity.getBreed());
            statement.setInt(4, entity.getAge());
            statement.setInt(5, entity.getClient().getId());
            statement.setInt(6, entity.getId());
            
            statement.executeUpdate();
        
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error updating animal: " + e.getMessage(), e);
        }    
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM animals WHERE id = ?";
        
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            statement.executeUpdate();
        
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error deleting animal: " + e.getMessage(), e);
        }    
    }

    @Override
    public List<Animal> listAll() {
        String sql = "SELECT id, name, species, breed, age, client_id FROM animals";
        List<Animal> animals = new ArrayList<>();
        
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            ClientDAO clientDAO = new ClientDAO(); // Instantiated once outside the loop
            
            while (resultSet.next()) {
                animals.add(mapResultSetToAnimal(resultSet, clientDAO));
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error listing animals: " + e.getMessage(), e);
        }
        
        return animals;
    }

    @Override
    public Animal findById(int id) {
        String sql = "SELECT id, name, species, breed, age, client_id FROM animals WHERE id = ?";
        
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    ClientDAO clientDAO = new ClientDAO();
                    return mapResultSetToAnimal(resultSet, clientDAO);
                }
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error finding animal: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    private Animal mapResultSetToAnimal(ResultSet rs, ClientDAO clientDAO) throws SQLException {
        Animal animal = new Animal();
        animal.setId(rs.getInt("id"));
        animal.setName(rs.getString("name"));
        animal.setSpecies(rs.getString("species"));
        animal.setBreed(rs.getString("breed"));
        animal.setAge(rs.getInt("age"));
        
        Client client = clientDAO.findById(rs.getInt("client_id"));
        animal.setClient(client);
        
        return animal;
    }
}