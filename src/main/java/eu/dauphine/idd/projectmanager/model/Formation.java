package eu.dauphine.idd.projectmanager.model;

public class Formation {
	private int IdFormation;
	private String NomFormation;
	private String promot;

	public Formation(int Id, String nomF, String promot) {
		this.IdFormation = Id;
		this.NomFormation = nomF;
		this.promot = promot;
	}
	public Formation( String nomF, String promot) {
		
		this.NomFormation = nomF;
		this.promot = promot;
	}

	public int getIdFormation() {
		return IdFormation;
	}

	public void setIdFormation(int idFormation) {
		IdFormation = idFormation;
	}

	public String getNomFormation() {
		return NomFormation;
	}

	public void setNomFormation(String nomFormation) {
		NomFormation = nomFormation;
	}

	public String getPromotion() {
		return this.promot;
	}

	public void setPromotion(String promotion) {
		this.promot = promotion;
	}

	@Override
	public String toString() {
		return "Formation [IdFormation=" + IdFormation + ", NomFormation=" + NomFormation + ", promt=" + promot + "]";
	}

}
