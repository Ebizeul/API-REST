/**
 * 
 */
package com.dao;

import static com.dao.DAOUtilitaire.fermetures;
import static com.dao.DAOUtilitaire.initialisationRequetePreparee;
import static com.dao.DAOUtilitaire.traitementUpdate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.blo.VilleFranceBLO;

/**
 * @author Lilian BRAUD
 *
 */
public class VilleFranceDAO extends DAO<VilleFranceBLO> {
	
	private static Logger logger = Logger.getLogger(VilleFranceDAO.class.getName());
	
	private static final String NOM_ENTITE = "ville_france";
	private static final String ATTRIBUT_CODE_COMMUNE= "Code_commune_INSEE";
	private static final String ATTRIBUT_NOM_COMMUNE = "Nom_commune";
	private static final String ATTRIBUT_CODE_POSTAL = "Code_postal";
	private static final String ATTRIBUT_LIBELLE_ACHEMINEMENT = "Libelle_acheminement";
	private static final String ATTRIBUT_LIGNE_5 = "Ligne_5";
	private static final String ATTRIBUT_LATITUDE = "Latitude";
	private static final String ATTRIBUT_LONGITUDE = "Longitude";
	
	private static final String[] ATTRIBUTS_NOMS = { ATTRIBUT_CODE_COMMUNE, ATTRIBUT_NOM_COMMUNE, ATTRIBUT_CODE_POSTAL,
			ATTRIBUT_LIBELLE_ACHEMINEMENT, ATTRIBUT_LIGNE_5, ATTRIBUT_LATITUDE, ATTRIBUT_LONGITUDE };
	private static final String[] ATTRIBUTS_NOMS_2 = { ATTRIBUT_NOM_COMMUNE, ATTRIBUT_CODE_POSTAL, ATTRIBUT_LIBELLE_ACHEMINEMENT,
			ATTRIBUT_LIGNE_5, ATTRIBUT_LATITUDE, ATTRIBUT_LONGITUDE, ATTRIBUT_CODE_COMMUNE };
	
	private static final String SQL_INSERT = 
			"INSERT INTO ville_france (Code_commune_INSEE, Nom_commune, Code_postal, Libelle_acheminement, Ligne_5, Latitude, Longitude) VALUES (?, ?, ?, ?, ?, ?, ?)";	
	private static final String SQL_SELECT_VILLES = 
			"SELECT Code_commune_INSEE, Nom_commune, Code_postal, Libelle_acheminement, Ligne_5, Latitude, Longitude FROM ville_france ORDER BY Code_commune_INSEE";
	private static final String DELETE = "DELETE";
	
	public static void main(String[] args) {
		try {
			DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			
			Connection connect = DriverManager.getConnection("jdbc:mysql://localhost:3308/technoweb?user=utilisateur&password=network");
			
			Statement stm = connect.createStatement();
			
			ResultSet rset = stm.executeQuery("SELECT * FROM `ville_france` WHERE Code_commune_INSEE > '01001' AND Code_commune_INSEE < '01052'");
			
			while(rset.next()) {
				System.out.println(rset.getString(1) + ", " + rset.getString(2) + ", " + rset.getString(3) 
				+ ", " + rset.getString(4) + ", " + rset.getString(5) + ", " + rset.getString(6) 
				+ ", " + rset.getString(7));
			}
		} catch (SQLException e) {
			logger.log(Level.WARN, "Échec", e);
		}
	}
	
	public VilleFranceDAO(DAOFactory daoFactory) {
		super(daoFactory);
	}
	
	@Override
	public void creer(VilleFranceBLO ville) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			// création d'une connexion grâce à la DAOFactory placée en attribut de la classe
			connection = this.creerConnexion();
			// les "?" de la requête sont comblés par les attributs de l'objet en paramètre
			// ceux-ci peuvent être nuls à condition que la BDD accepte les données de type "null"
			preparedStatement = connection.prepareStatement(
					initialisationRequetePreparee(SQL_INSERT, ville.getCodeCommune(), ville.getNomCommune(), ville.getCodePostal(),
					ville.getLibelleAcheminement(), ville.getLigne5(), ville.getLatitude(),
					ville.getLongitude()), Statement.RETURN_GENERATED_KEYS);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.WARN, "Échec de la création de l'objet, aucune ligne ajoutée dans la table.", e);
		} finally {
			// fermeture des ressources utilisées
			fermetures(preparedStatement, connection);
		}
	}
	
	@Override
	public void supprimer(VilleFranceBLO ville) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		// tableau de Strings regroupant tous les noms des attributs d'un objet ainsi que les valeurs correspondantes
		String[][] attributs = { ATTRIBUTS_NOMS,
				{ String.valueOf(ville.getCodeCommune()), ville.getNomCommune(), ville.getCodePostal(),
					ville.getLibelleAcheminement(), ville.getLigne5(), ville.getLatitude(),
					String.valueOf(ville.getLongitude()) } };
		try {
			// création d'une connexion grâce à la DAOFactory placée en attribut de la classe
			connection = this.creerConnexion();
			// mise en forme de la requête DELETE en fonction des attributs de l'objet utilisateur
			preparedStatement = connection.prepareStatement(initialisationRequetePreparee(DELETE, NOM_ENTITE, attributs), Statement.NO_GENERATED_KEYS);
			int statut = preparedStatement.executeUpdate();
			if (statut == 0) {
				throw new SQLException();
			} else {
				// suppression du bean
				ville.setCodeCommune(null);
			}
		} catch (SQLException e) {
			logger.log(Level.WARN, "Échec de la suppression de l'objet, aucune ligne supprimée de la table.", e);
		} finally {
			// fermeture des ressources utilisées
			fermetures(preparedStatement, connection);
		}
	}
	
	@Override
	public void modifier(VilleFranceBLO ville) {
		// tableau de Strings regroupant tous les noms des attributs d'un objet ainsi que les valeurs correspondantes
		// /!\ bien placer la clé primaire et sa valeur à la FIN du tableau sinon erreur
		String[][] attributs = { ATTRIBUTS_NOMS_2,
				{ ville.getNomCommune(), ville.getCodePostal(),
				  ville.getLibelleAcheminement(), ville.getLigne5(), ville.getLatitude(),
				  ville.getLongitude(), ville.getCodeCommune()} };
		/* Traite la mise à jour de la BDD */
		traitementUpdate(this.getDaoFactory(), NOM_ENTITE, attributs, logger);
	}
	
	@Override
	public List<VilleFranceBLO> trouver(VilleFranceBLO ville) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<VilleFranceBLO> villes = new ArrayList<>();
		// tableau de Strings regroupant tous les noms des attributs d'un objet ainsi que les valeurs correspondantes
		String[][] attributs = { ATTRIBUTS_NOMS,
				{ String.valueOf(ville.getCodeCommune()), ville.getNomCommune(), ville.getCodePostal(),
						ville.getLibelleAcheminement(), ville.getLigne5(), ville.getLatitude(),
						String.valueOf(ville.getLongitude()) } };
		try {
			// création d'une connexion grâce à la DAOFactory placée en attribut de la classe
			connection = this.creerConnexion();
			// mise en forme de la requête SELECT en fonction des attributs de l'objet utilisateur
			preparedStatement = connection.prepareStatement(initialisationRequetePreparee("SELECT", NOM_ENTITE, attributs), Statement.NO_GENERATED_KEYS);
			resultSet = preparedStatement.executeQuery();
			// récupération des valeurs des attributs de la BDD pour les mettre dans une liste
			while (resultSet.next()) {
				villes.add(recupererVille(resultSet));
			}
		} catch (SQLException e) {
			logger.log(Level.WARN, "Échec de la recherche de l'objet.", e);
		} finally {
			// fermeture des ressources utilisées
			fermetures(resultSet, preparedStatement, connection);
		}
		return villes;
	}
	
	/**
	 * Liste tous les Utilisateurs présents dans la BDD.
	 * 
	 * @return utilisateurs la liste des Utilisateurs présents dans la BDD.
	 */
	@Override
	public List<VilleFranceBLO> lister() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<VilleFranceBLO> villes = new ArrayList<>();
		try {
			// création d'une connexion grâce à la DAOFactory placée en attribut de la classe
			connection = this.creerConnexion();
			preparedStatement = connection.prepareStatement(SQL_SELECT_VILLES);
			resultSet = preparedStatement.executeQuery();
			// récupération des valeurs des attributs de la BDD pour les mettre dans une liste
			while (resultSet.next()) {
				villes.add(recupererVille(resultSet));
			}
		} catch (SQLException e) {
			logger.log(Level.FATAL, "Échec du listage des objets.", e);
		} finally {
			// fermeture des ressources utilisées
			fermetures(resultSet, preparedStatement, connection);
		}
		return villes;
	}
	
	protected Connection creerConnexion() throws SQLException {
		return this.getDaoFactory().getConnection();
	}
	
	public static VilleFranceBLO recupererVille(ResultSet resultSet) throws SQLException {
		VilleFranceBLO ville = new VilleFranceBLO();
		ville.setCodeCommune(resultSet.getString(ATTRIBUT_CODE_COMMUNE));
		ville.setNomCommune(resultSet.getString(ATTRIBUT_NOM_COMMUNE));
		ville.setCodePostal(resultSet.getString(ATTRIBUT_CODE_POSTAL));
		ville.setLibelleAcheminement(resultSet.getString(ATTRIBUT_LIBELLE_ACHEMINEMENT));
		ville.setLigne5(resultSet.getString(ATTRIBUT_LIGNE_5));
		ville.setLatitude(resultSet.getString(ATTRIBUT_LATITUDE));
		ville.setLongitude(resultSet.getString(ATTRIBUT_LONGITUDE));
		return ville;
	}

}
