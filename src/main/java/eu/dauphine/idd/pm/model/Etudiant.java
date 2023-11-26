package eu.dauphine.idd.pm.model;

public class Etudiant {

	private int idEtudiant;
	private String nom;
	private String prenom;
	private Formation formation;

	public Etudiant(int Id, String nom, String prenom, Formation formation) {
		this.idEtudiant = Id;
		this.nom = nom;
		this.prenom = prenom;
		this.formation = formation;
	}

	public Etudiant(String nom, String prenom, Formation formation) {
		this.nom = nom;
		this.prenom = prenom;
		this.formation = formation;
	}


	public int getIdEtudiant() {
		return this.idEtudiant;
	}

	public void setIdEtudiant(int id) {
		this.idEtudiant = id;
	}

	public String getNom() {
		return this.nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getPrenom() {
		return this.prenom;
	}

	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}

	public Formation getFormation() {
		return this.formation;
	}

	public void setFormation(Formation formation) {
		this.formation = formation;
	}

	@Override
	public String toString() {
		return "Etudiant [Id =" + this.idEtudiant + ", Nom = " + this.nom + ", Prenom = " + this.prenom
				+ ", formation = " + this.formation.toString() + "]";
	}

}
