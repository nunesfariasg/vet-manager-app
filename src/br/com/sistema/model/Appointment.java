/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistema.model;

import java.time.LocalTime;
import java.time.LocalDate;
import javafx.scene.chart.PieChart;

/**
 *
 * @author nunes
 */
public class Appointment {
    
    private int id;
    private Animal animalID;
    private LocalDate date;
    private LocalTime time;
    private String reason;
    private String notes;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Animal getAnimalID() {
        return animalID;
    }

    public void setAnimalID(Animal animalID) {
        this.animalID = animalID;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    
}
