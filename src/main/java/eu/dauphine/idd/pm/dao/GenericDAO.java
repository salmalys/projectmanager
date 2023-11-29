package eu.dauphine.idd.pm.dao;

import javafx.collections.ObservableList;

public interface GenericDAO<T>{
    void create(T entity);
    void update(T entity);
    ObservableList<T> findAll();
    T findById(int id);
    void delete(T entity);
    void deleteById(int id);
   
}
