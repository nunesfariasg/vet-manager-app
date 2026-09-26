/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.sistema.dao;

import java.util.List;

/**
 *
 * @author nunes
 */
public interface GenericDAO<T> {
    
    public void save(T entiti);
    
    public void update(T entiti);
    
    public void delete(int id);
    
    public List<T> findAll();
    
    public T findById(int id);
    
}
