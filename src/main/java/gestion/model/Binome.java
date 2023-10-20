package gestion.model;

public class Binome {
	private int IdBinome;
	private Etudiant membre1;
	private Etudiant membre2;
	private Projet projet;

	public Binome(int Id, Etudiant e1, Etudiant e2, Projet p) {
		this.IdBinome = Id;
		this.membre1 = e1;
		this.membre2 = e2;
		this.projet = p;

	}
	public Binome( Etudiant e1, Etudiant e2, Projet p) {
		
		this.membre1 = e1;
		this.membre2 = e2;
		this.projet = p;

	}

	public int getIdBinome() {
		return IdBinome;
	}

	public void setIdBinome(int idBinome) {
		IdBinome = idBinome;
	}

	public Etudiant getMembre1() {
		return membre1;
	}

	public void setMembre1(Etudiant membre1) {
		this.membre1 = membre1;
	}

	public Etudiant getMembre2() {
		return membre2;
	}

	public void setMembre2(Etudiant membre2) {
		this.membre2 = membre2;
	}

	public Projet getProjet() {
		return projet;
	}

	public void setProjet(Projet projet) {
		this.projet = projet;
	}

	@Override
	public String toString() {
		return "Binome [IdBinome=" + IdBinome + ", membre1=" + membre1 + ", membre2=" + membre2 + ", projet=" + projet
				+ "]";
	}

}
