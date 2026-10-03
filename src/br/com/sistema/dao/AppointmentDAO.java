/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistema.dao;

import br.com.sistema.jdbc.ConnectionFactory;
import br.com.sistema.model.Animal;
import br.com.sistema.model.Appointment;
import br.com.sistema.model.Client;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author nunes
 */
public class AppointmentDAO implements GenericDAO<Appointment>{

    @Override
    public void save(Appointment entity) {
        String sql = "INSERT INTO appointments(animal_id, date_appointment, time_appointment, reson, notes) VALUES(?, ?, ?, ?, ?)";
        
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            statement.setInt(1, entity.getAnimal().getId());
            statement.setDate(2, java.sql.Date.valueOf(entity.getDate()));
            statement.setTime(3, java.sql.Time.valueOf(entity.getTime()));
            statement.setString(4, entity.getReason());
            statement.setString(5, entity.getNotes());
            
            int rowsAffected = statement.executeUpdate();
            
            if (rowsAffected > 0) {
                try (ResultSet resultSet = statement.getGeneratedKeys()) {
                    if (resultSet.next()) {
                        entity.setId(resultSet.getInt(1));
                    }
                }
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error saving appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Appointment entity) {
        String sql = "UPDATE appointments SET animal_id = ?, date_appointment = ?, time_appointment = ?, reson = ?, notes = ? WHERE id = ?";
    
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
        
            statement.setInt(1, entity.getAnimal().getId());
            statement.setDate(2, java.sql.Date.valueOf(entity.getDate()));
            statement.setTime(3, java.sql.Time.valueOf(entity.getTime()));
            statement.setString(4, entity.getReason());
            statement.setString(5, entity.getNotes());
            statement.setInt(6, entity.getId());
            
            statement.executeUpdate();
        
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error updating appointment: " + e.getMessage(), e);
        } 
    }

    @Override
    public void delete(int id) {
        String sql = "DELETE FROM appointments WHERE id = ?";
        
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            statement.executeUpdate();
        
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error deleting appointment: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Appointment> listAll() {
        String sql = "SELECT id, animal_id, date_appointment, time_appointment, reson, notes FROM appointments";
        List<Appointment> appointments = new ArrayList<>();
        
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            
            AnimalDAO animalDAO = new AnimalDAO(); // Instantiated once outside the loop
            
            while (resultSet.next()) {
                appointments.add(mapResultSetToAppointment(resultSet, animalDAO));
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error listing appointments: " + e.getMessage(), e);
        }
        
        return appointments;
    }

    @Override
    public Appointment findById(int id) {
        String sql = "SELECT id, animal_id, date_appointment, time_appointment, reson, notes FROM appointments WHERE id = ?";
        
        try (Connection connection = ConnectionFactory.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            
            statement.setInt(1, id);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    AnimalDAO animalDAO = new AnimalDAO();
                    return mapResultSetToAppointment(resultSet, animalDAO);
                }
            }
            
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException("Error finding appointment: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    private Appointment mapResultSetToAppointment(
        ResultSet rs, AnimalDAO animalDAO) throws SQLException {

    Appointment appointment = new Appointment();

    appointment.setId(rs.getInt("id"));
    appointment.setAnimal(
        animalDAO.findById(rs.getInt("animal_id"))
    );
    appointment.setDate(
        rs.getDate("date_appointment").toLocalDate()
    );
    appointment.setTime(
        rs.getTime("time_appointment").toLocalTime()
    );
    appointment.setReason(rs.getString("reson"));
    appointment.setNotes(rs.getString("notes"));

    return appointment;
}
    
}
