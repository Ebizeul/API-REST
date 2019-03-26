/**
 * 
 */
package com.dao;

import java.sql.Connection;
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

	private static final String NOM_ENTITE = "Semestre";
	private static final String ATTRIBUT_ID_SEMESTRE = "idSemestre";
	private static final String ATTRIBUT_DATE_DEBUT = "dateDebut";
	private static final String ATTRIBUT_DATE_FIN = "dateFin";
	private static final String ATTRIBUT_NOM = "nom";
	private static final String[] ATTRIBUTS_NOMS = {  ATTRIBUT_ID_SEMESTRE, ATTRIBUT_DATE_DEBUT, ATTRIBUT_DATE_FIN, ATTRIBUT_NOM };
	private static final String[] ATTRIBUTS_NOMS_ID_FIN = { ATTRIBUT_DATE_DEBUT, ATTRIBUT_DATE_FIN, ATTRIBUT_NOM , ATTRIBUT_ID_SEMESTRE };
	
	// tous les attributs du bean doivent Ãªtre Ã©crits dans la requÃªte INSERT
	private static final String SQL_INSERT = "INSERT INTO Semestre (dateDebut, dateFin, nom) VALUES (?, ?, ?)";
	private static final String SQL_SELECT = "SELECT * FROM Semestre";
	private static final String SQL_SELECT_NOM = "SELECT * FROM Semestre WHERE Semestre.nom = ?";
	private static Logger logger = Logger.getLogger(SemestreDAO.class.getName());
	
	/**
	 * Constructeur de DAO.
	 * 
	 * @param daoFactory la Factory permettant la crÃ©ation d'une connexion Ã  la BDD.
	 */
	public SemestreDAO(DAOFactory daoFactory) {
		super(daoFactory);
	}

	/**
	 * InsÃ¨re un Etudiant dans la BDD Ã  partir des attributs spÃ©cifiÃ©s dans un bean Etudiant.
	 * 
	 * @param etudiant l'Etudiant que l'on souhaite insÃ©rer dans la BDD Ã  partir du bean Etudiant.
	 */
	@Override
	public void creer(Semestre semestre) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			// crÃ©ation d'une connexion grÃ¢ce Ã  la DAOFactory placÃ©e en attribut de la classe
			connection = this.creerConnexion();			
			// les "?" de la requÃªte sont comblÃ©s par les attributs de l'objet en paramÃ¨tre
			// ceux-ci peuvent Ãªtre nuls Ã  condition que la BDD accepte les donnÃ©es de type "null"
			preparedStatement = connection.prepareStatement(initialisationRequetePreparee(SQL_INSERT,
					semestre.getDateDebutString(), 
					semestre.getDateFinString(),
					semestre.getNom()),
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.WARN, "Ã‰chec de la crÃ©ation de l'objet, aucune ligne ajoutÃ©e dans la table.", e);
		} finally {
			// fermeture des ressources utilisÃ©es
			fermetures(preparedStatement, connection);
		}
	}

	/**
	 * Liste tous les Etudiants ayant pour attributs les mÃªmes que ceux spÃ©cifiÃ©s dans un bean Etudiant.
	 * 
	 * @param etudiant l'Etudiant que l'on souhaite trouver dans la BDD.
	 * @return etudiants la liste des Etudiants trouvÃ©s dans la BDD.
	 */
	@Override
	public List<Semestre> trouver(Semestre semestre) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Semestre> semestres = new ArrayList<>();
		// tableau de Strings regroupant tous les noms des attributs d'un objet ainsi que les valeurs correspondantes
		String[][] attributs = { ATTRIBUTS_NOMS,
				{ String.valueOf(semestre.getIdSemestre()), semestre.getDateDebutString(),
			semestre.getDateFinString(), semestre.getNom() } };
		try {
			// crÃ©ation d'une connexion grÃ¢ce Ã  la DAOFactory placÃ©e en attribut de la classe
			connection = this.creerConnexion();
			// mise en forme de la requÃªte SELECT en fonction des attributs de l'objet etudiant
			preparedStatement = connection.prepareStatement(initialisationRequetePreparee("SELECT",
					NOM_ENTITE, attributs),Statement.NO_GENERATED_KEYS);
			resultSet = preparedStatement.executeQuery();
			// rÃ©cupÃ©ration des valeurs des attributs de la BDD pour les mettre dans une liste
			while (resultSet.next()) {
				semestres.add(recupererSemestre(resultSet));
			}
		} catch (SQLException e) {
			logger.log(Level.WARN, "Ã‰chec de la recherche de l'objet.", e);
		} finally {
			// fermeture des ressources utilisÃ©es
			fermetures(resultSet, preparedStatement, connection);
		}
		return semestres;
	}	

	/**
	 * Modifie UN Etudiant ayant pour attributs les mÃªmes que ceux
	 * spÃ©cifiÃ©s dans un bean Etudiant et la mÃªme clÃ© primaire.
	 * Cette clÃ© primaire ne peut Ãªtre modifiÃ©e.
	 * 
	 * @param etudiant l'Etudiant que l'on souhaite modifier dans la BDD.
	 */
	@Override
	public void modifier(Semestre semestre) {
		// tableau de Strings regroupant tous les noms des attributs d'un objet ainsi que les valeurs correspondantes
		// /!\ bien placer la clÃ© primaire et sa valeur Ã  la FIN du tableau sinon erreur
		String[][] attributs = { ATTRIBUTS_NOMS_ID_FIN,
				{ semestre.getDateDebutString(), semestre.getDateFinString(), 
			 String.valueOf(semestre.getNom()), String.valueOf(semestre.getIdSemestre()) } };
		
		/* Traite la mise Ã  jour de la BDD */
		traitementUpdate(this.getDaoFactory(), NOM_ENTITE, attributs, logger);
	}

	/**
	 * Supprime tous les Etudiants ayant pour attributs les mÃªmes que ceux
	 * spÃ©cifiÃ©s dans un bean Etudiant.
	 * 
	 * @param etudiant l'Etudiant que l'on souhaite supprimer dans la BDD.
	 */
	@Override
	public void supprimer(Semestre semestre) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		// tableau de Strings regroupant tous les noms des attributs d'un objet ainsi que les valeurs correspondantes
		String[][] attributs = { ATTRIBUTS_NOMS,
				{ String.valueOf(semestre.getIdSemestre()), semestre.getDateDebutString(),
			semestre.getDateFinString(),semestre.getNom() } };
		try {
			// crÃ©ation d'une connexion grÃ¢ce Ã  la DAOFactory placÃ©e en attribut de la classe
			connection = this.creerConnexion();
			preparedStatement = connection.prepareStatement(initialisationRequetePreparee("DELETE",
					NOM_ENTITE, attributs),Statement.NO_GENERATED_KEYS);
			int statut = preparedStatement.executeUpdate();
			if (statut == 0) {
				throw new SQLException();
			} else {
				// suppression du bean
				semestre.setIdSemestre(null);
			}
		} catch (SQLException e) {
			logger.log(Level.WARN, "Ã‰chec de la suppression de l'objet, aucune ligne supprimÃ©e de la table.", e);
		} finally {
			// fermeture des ressources utilisÃ©es
			fermetures(preparedStatement, connection);
		}
	}

	/**
	 * Liste tous les Etudiants prÃ©sents dans la BDD.
	 * 
	 * @return etudiants la liste des Etudiants prÃ©sents dans la BDD.
	 */
	@Override
	public List<Semestre> lister() {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Semestre> semestres = new ArrayList<>();
		try {
			// crÃ©ation d'une connexion grÃ¢ce Ã  la DAOFactory placÃ©e en attribut de la classe
			connection = this.creerConnexion();
			preparedStatement = connection.prepareStatement(SQL_SELECT);
			resultSet = preparedStatement.executeQuery();
			// rÃ©cupÃ©ration des valeurs des attributs de la BDD pour les mettre dans une liste
			if(!resultSet.isBeforeFirst())
			{
				throw new SQLException();
			} else {
				while (resultSet.next()) {
					semestres.add(recupererSemestre(resultSet));
				}
			}
		} catch (SQLException e) {
			logger.log(Level.WARN, "Ã‰chec du listage des objets.", e);
		} finally {
			fermetures(resultSet, preparedStatement, connection);
		}
		return semestres;
	}
	
	/**
	 * CrÃ©e une connexion Ã  la BDD.
	 * 
	 * @return connection la connexion Ã  la BDD.
	 * @throws SQLException
	 */
	protected Connection creerConnexion() throws SQLException {
		return this.getDaoFactory().getConnection();
	}
	
	/**
	 * Liste tous les TypeEpreuves ayant pour attributs les mÃªmes que ceux
	 * spÃ©cifiÃ©s dans un bean TypeEpreuve.
	 * 
	 * @param typeEpreuve
	 *            l'TypeEpreuve que l'on souhaite trouver dans la BDD.
	 * @return typeEpreuves la liste des TypeEpreuves trouvÃ©s dans la BDD.
	 */
	public List<Semestre> trouverParNom(Semestre semestre) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		List<Semestre> semestres = new ArrayList<>();
		try {
			// crÃ©ation d'une connexion grÃ¢ce Ã  la DAOFactory placÃ©e en attribut
			// de la classe
			connection = this.creerConnexion();
			// les "?" de la requÃªte sont comblÃ©s par les attributs de l'objet
			// en paramÃ¨tre
			// ceux-ci peuvent Ãªtre nuls Ã  condition que la BDD accepte les
			// donnÃ©es de type "null"
			preparedStatement = connection.prepareStatement(initialisationRequetePreparee(SQL_SELECT_NOM, 
					String.valueOf(semestre.getNom())),
					Statement.NO_GENERATED_KEYS);
			resultSet = preparedStatement.executeQuery();
			// rÃ©cupÃ©ration des valeurs des attributs de la BDD pour les mettre
			// dans une liste
			while (resultSet.next()) {
				semestres.add(recupererSemestre(resultSet));
			}
		} catch (SQLException e) {
			logger.log(Level.WARN, "Ã‰chec de la recherche de l'objet.", e);
		} finally {
			// fermeture des ressources utilisÃ©es
			fermetures(resultSet, preparedStatement, connection);
		}
		return semestres;
	}
	
	/**
	 * Fait la correspondance (le mapping) 
	 * entre une ligne issue de la table Etudiant (un ResultSet) et un bean Etudiant.
	 * 
	 * @param resultSet la ligne issue de la table Etudiant.
	 * @return etudiant le bean dont on souhaite faire la correspondance.
	 * @throws SQLException
	 */
	public static Semestre recupererSemestre(ResultSet resultSet) throws SQLException {
		Semestre semestre = new Semestre();
		semestre.setIdSemestre(resultSet.getLong(ATTRIBUT_ID_SEMESTRE));
		semestre.setDateDebut(resultSet.getDate(ATTRIBUT_DATE_DEBUT));
		semestre.setDateFin(resultSet.getDate(ATTRIBUT_DATE_FIN));
		semestre.setNom(resultSet.getString(ATTRIBUT_NOM));
		return semestre;
	}

}
