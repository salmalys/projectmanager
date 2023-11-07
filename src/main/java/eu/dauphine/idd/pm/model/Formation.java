package eu.dauphine.idd.pm.model;

public class Formation {

	private int idFormation;
	private String nom;
	private String promotion;

	public Formation(int idFormation, String nom, String promotion) {
		this.idFormation = idFormation;
		this.nom = nom;
		this.promotion = promotion;
	}

	public Formation(String nom, String promotion) {
		this.nom = nom;
		this.promotion = promotion;
	}

	public Formation() {
	}

	public int getIdFormation() {
		return this.idFormation;
	}

	public void setIdFormation(int idFormation) {
		this.idFormation = idFormation;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPromotion() {
		return this.promotion;
	}

	public void setPromotion(String promotion) {
		this.promotion = promotion;
	}

	@Override
	public String toString() {
		return "Formation [Id = " + this.idFormation + ", nom = " + this.nom + ", promotion = " + this.promotion + "]";
	}
}
