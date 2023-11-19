package eu.dauphine.idd.pm.service;

import eu.dauphine.idd.pm.model.Formation;
import javafx.collections.ObservableList;

public interface FormationService {
	public int createFormation(String nom, String promotion);
	public void deleteFormationById(int id);
	public ObservableList<Formation> listFormations();
	public void update(int id, String nom, String promotion);
}
