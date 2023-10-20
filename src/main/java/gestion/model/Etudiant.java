package gestion.model;

public class Etudiant {

	private int IdEtudiant;
	private String Nom;
	private String Prenom;
	private Formation formation;

	public Etudiant(int Id, String nom, String prenom,Formation formation) {
		this.IdEtudiant = Id;
		this.Nom = nom;
		this.Prenom = prenom;
		this.formation=formation;
	}
	public Etudiant( String nom, String prenom,Formation formation) {
	
		this.Nom = nom;
		this.Prenom = prenom;
		this.formation=formation;
	}
	

	public Etudiant() {
		// TODO Auto-generated constructor stub
	}
	public int getIdEtudiant() {
		return IdEtudiant;
	}

	public void setIdEtudiant(int idEtudiant) {
		IdEtudiant = idEtudiant;
	}

	public String getNom() {
		return Nom;
	}

	public void setNom(String nom) {
		Nom = nom;
	}

	public String getPrenom() {
		return Prenom;
	}

	public void setPrenom(String prenom) {
		Prenom = prenom;
	}
	

	public Formation getFormation() {
		return formation;
	}


	public void setFormation(Formation formation) {
		this.formation = formation;
	}


	@Override
	public String toString() {
		return "Etudiant [IdEtudiant=" + IdEtudiant + ", Nom=" + Nom + ", Prenom=" + Prenom + ", formation=" + formation.toString()
				+ "]";
	}


	

}
