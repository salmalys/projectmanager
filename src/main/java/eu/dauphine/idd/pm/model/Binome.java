package eu.dauphine.idd.pm.model;

import java.util.Date;

public class Binome {
	private int idBinome;
	private Etudiant membre1;
	private Etudiant membre2;
	private Projet projet;
	private Date dateRemiseEffective;

	public Binome(int id, Etudiant e1, Etudiant e2, Projet p, Date d) {
		this.idBinome = id;
		this.membre1 = e1;
		this.membre2 = e2;
		this.projet = p;
		this.dateRemiseEffective = d;
	}

	public Binome(Etudiant e1, Etudiant e2, Projet p, Date d) {
		this.membre1 = e1;
		this.membre2 = e2;
		this.projet = p;
		this.dateRemiseEffective = d;
	}

	public Binome() {
	}

	public int getIdBinome() {
		return this.idBinome;
	}

	public void setIdBinome(int idBinome) {
		this.idBinome = idBinome;
	}

	public Etudiant getMembre1() {
		return this.membre1;
	}

	public void setMembre1(Etudiant membre1) {
		this.membre1 = membre1;
	}

	public Etudiant getMembre2() {
		return this.membre2;
	}

	public void setMembre2(Etudiant membre2) {
		this.membre2 = membre1;
	}

	public Projet getProjet() {
		return this.projet;
	}

	public void setProjet(Projet projet) {
		this.projet = projet;
	}
	
	public Date getDateRemiseEffective() {
		return this.dateRemiseEffective;
	}

	public void setDateRemiseEffective(Date date) {
		this.dateRemiseEffective = date;
	}

	@Override
	public String toString() {
		return "Binome [Id = " + this.idBinome + ", membre1 = " + this.membre1.toString() + ", membre2 = "
				+ this.membre2.toString() + ", projet = " + this.projet.toString() + ", dateRemiseEffective =" + this.dateRemiseEffective + "]";
	}

}
