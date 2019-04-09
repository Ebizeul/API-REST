package getInfoFromAPI;

public class Ville {
	
	private String codeCommune;
	private String nomCommune;
	private String codePostal;
	private String libelleAcheminement;
	private String ligne5;	
	private String latitude;
	private String longitude;
	
	
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
	
	/**
	 * Constructeur par défaut.
	 */
	public Ville() {
		super();
	}
	
	/**
	 * Redéfinition de la méthode toString().
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
