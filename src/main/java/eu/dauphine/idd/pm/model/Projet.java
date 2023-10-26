package eu.dauphine.idd.pm.model;

import java.util.*;

public class Projet {
	private int IdProjet;
	private String nomMatiere;
	private String Sujet;
	private Date dateRemiseRapport;

	public Projet(int Id, String Matiere, String sujet, Date dateRemise) {
		this.IdProjet = Id;
		this.nomMatiere = Matiere;
		this.Sujet = sujet;
		this.dateRemiseRapport = dateRemise;
	}

	public Projet(String Matiere, String sujet, Date dateRemise) {

		this.nomMatiere = Matiere;
		this.Sujet = sujet;
		this.dateRemiseRapport = dateRemise;
	}

	public Projet() {
		// TODO Auto-generated constructor stub
	}

	public int getIdProjet() {
		return IdProjet;
	}

	public void setIdProjet(int idProjet) {
		IdProjet = idProjet;
	}

	public String getNomMatiere() {
		return nomMatiere;
	}

	public void setNomMatiere(String nomMatiere) {
		this.nomMatiere = nomMatiere;
	}

	public String getSujet() {
		return Sujet;
	}

	public void setSujet(String sujet) {
		Sujet = sujet;
	}

	public Date getDateRemiseRapport() {
		return dateRemiseRapport;
	}

	public void setDateRemiseRapport(Date dateRemiseRapport) {
		this.dateRemiseRapport = dateRemiseRapport;
	}

	@Override
	public String toString() {
		return "Projet [IdProjet=" + IdProjet + ", nomMatiere=" + nomMatiere + ", Sujet=" + Sujet
				+ ", dateRemiseRapport=" + dateRemiseRapport + "]";
	}

}
