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
 * @param <T>
 */
public interface GenericDAO<T> {
    
    public void save(T entity);
    
    public void update(T entity);
    
    public void delete(int id);
    
    public List<T> listAll();
    
    public T findById(int id);
    
}
