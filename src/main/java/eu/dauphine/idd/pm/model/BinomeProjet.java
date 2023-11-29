package eu.dauphine.idd.pm.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class BinomeProjet {
	private int idBinome;
	private Etudiant membre1;
	private Etudiant membre2;
	private Projet projet;
	private Date dateRemiseEffective;

	public BinomeProjet(int id, Etudiant e1, Etudiant e2, Projet p, Date dateRemiseEffective2) {
		this.idBinome = id;
		this.membre1 = e1;
		this.membre2 = e2;
		this.projet = p;
		this.dateRemiseEffective = dateRemiseEffective2;
	}

	public BinomeProjet(Etudiant e1, Etudiant e2, Projet p, Date dateRemiseEffective2) {
		this.membre1 = e1;
		this.membre2 = e2;
		this.projet = p;
		this.dateRemiseEffective = dateRemiseEffective2;
	}
	
	public BinomeProjet(int id, Etudiant e1, Projet p, Date dateRemiseEffective2) {
		this.idBinome = id;
		this.membre1 = e1;
		this.projet = p;
		this.dateRemiseEffective = dateRemiseEffective2;
	}
	
	public BinomeProjet(Etudiant e1, Projet p, Date dateRemiseEffective2) {
		this.membre1 = e1;
		this.projet = p;
		this.dateRemiseEffective = dateRemiseEffective2;
	}

	public BinomeProjet() {
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
		this.membre2 = membre2;
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
	    StringBuilder builder = new StringBuilder();
	    builder.append("BinomeProjet [idBinome=").append(idBinome).append(", membre1=").append(membre1)
	            .append(", membre2=").append(membre2).append(", projet=").append(projet);

	    if (dateRemiseEffective != null) {
	        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
	        String formattedDate = formatter.format(dateRemiseEffective);
	        builder.append(", dateRemiseEffective=").append(formattedDate);
	    } else {
	        builder.append(", dateRemiseEffective=null");
	    }

	    builder.append("]");
	    return builder.toString();
	}
}
