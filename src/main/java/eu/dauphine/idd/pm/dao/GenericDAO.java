package eu.dauphine.idd.pm.dao;

import javafx.collections.ObservableList;

public interface GenericDAO<T>{
    public void create(T entity);
    public void update(T entity);
    public ObservableList<T> findAll();
    public T findById(int id);
    public void delete(T entity);
    public void deleteById(int id);
   
}
