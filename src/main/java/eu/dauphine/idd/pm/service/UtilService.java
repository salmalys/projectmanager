package eu.dauphine.idd.pm.service;

import java.util.HashMap;

public interface UtilService {
	public int getTotalProjets();
	public int getTotalEtudiants();
	public int getNbprojetRemisAvantDate();
	public int getNbprojetRemisApresDate();
	public int getNbBinome();
	
	public HashMap<String, Double> getMoyenneParProjet();
}
