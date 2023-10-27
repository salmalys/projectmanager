package eu.dauphine.idd.pm.model;

import java.util.*;

public class Projet {
	private int idProjet;
	private String nomMatiere;
	private String sujet;
	private Date dateRemiseRapport;

	public Projet(int id, String matiere, String sujet, Date dateRemise) {
		this.idProjet = id;
		this.nomMatiere = matiere;
		this.sujet = sujet;
		this.dateRemiseRapport = dateRemise;
	}

	public Projet(String Matiere, String sujet, Date dateRemise) {
		this.nomMatiere = Matiere;
		this.sujet = sujet;
		this.dateRemiseRapport = dateRemise;
	}

	public Projet() {}

	public int getIdProjet() {return this.idProjet;}
	public void setIdProjet(int idProjet) {this.idProjet = idProjet;}

	public String getNomMatiere() {return this.nomMatiere;}
	public void setNomMatiere(String nomMatiere) {this.nomMatiere = nomMatiere;}

	public String getSujet() {return this.sujet;}
	public void setSujet(String sujet) {this.sujet = sujet;}

	public Date getDateRemiseRapport() {return dateRemiseRapport;}
	public void setDateRemiseRapport(Date dateRemiseRapport) {this.dateRemiseRapport = dateRemiseRapport;}

	@Override
	public String toString() {
		return "Projet [Id =" + this.idProjet + ", nomMatiere =" + this.nomMatiere + ", Sujet =" + this.sujet
				+ ", dateRemiseRapport =" + this.dateRemiseRapport + "]";
	}
}
