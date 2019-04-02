package com.blo;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Classe du bean Semestre.
 * 
 * <p>DÃ©finition des attributs de l'objet Semestre ainsi que des accesseurs et des mutateurs.</p>
 * 
 * @author Lilian BRAUD
 */
public class VilleFranceBLO {

	private String codeCommune;
	private String nomCommune;
	private String codePostal;
	private String libelleAcheminement;
	private String ligne5;	
	private String latitude;
	
	
	public String getCodeCommune() {
		return codeCommune;
	}

	public void setCodeCommune(String codeCommune) {
		this.codeCommune = codeCommune;
	}

	public String getNomCommune() {
		return nomCommune;
	}

	public void setNomCommune(String nomCommune) {
		this.nomCommune = nomCommune;
	}

	public String getCodePostal() {
		return codePostal;
	}

	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	public String getLibelleAcheminement() {
		return libelleAcheminement;
	}

	public void setLibelleAcheminement(String libelleAcheminement) {
		this.libelleAcheminement = libelleAcheminement;
	}

	public String getLigne5() {
		return ligne5;
	}

	public void setLigne5(String ligne5) {
		this.ligne5 = ligne5;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	private String longitude;
	
	/**
	 * Constructeur par dÃ©faut.
	 */
	public VilleFranceBLO() {
		super();
	}
	
	/**
	 * RedÃ©finition de la mÃ©thode toString().
	 */
	@Override
	public String toString() {
		return "Ville [codeCommune=" + this.codeCommune 
				+ ", nomCommune=" + this.nomCommune 
				+ ", codePostal=" + this.codePostal 
				+ ", libelleAcheminement=" + this.libelleAcheminement 
				+ ", ligne5=" + this.ligne5 
				+ ", latitude=" + this.latitude 
				+ ", longitude=" + this.longitude 
				+ "]";
	}
}
