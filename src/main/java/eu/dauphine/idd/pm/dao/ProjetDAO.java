package eu.dauphine.idd.pm.dao;

import eu.dauphine.idd.pm.model.Projet;

public interface ProjetDAO extends GenericDAO<Projet>{
	public Projet findByCourseSubject(String nom, String sujet);
}
