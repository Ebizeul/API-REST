package com.dao;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;


/**
 * Classe d'instanciation d'une Factory de DAO.
 * 
 * <p>Cette classe ne sera instanciÃ©e que si les informations de configuration sont correctes.</p>
 * 
 * @author Thomas MENARD, Maxime LENORMAND et Lilian BRAUD
 * @see fr.eseo.ld.config.InitialisationDAOFactory
 */
public class DAOFactory {

	public static final String FICHIER_PROPERTIES = "fr/eseo/ld/dao/dao.properties";
	public static final String FICHIER_PROPERTIES_NOM = "dao.properties";
	public static final String FICHIER_DEFAULT_PROPERTIES = "fr/eseo/ld/dao/daoDefault.properties";
	private static final String FICHIER_PROPERTIES_INTROUVABLE = "Le fichier properties %s est introuvable.";
	public static final String PROPERTY_URL = "URL";
	public static final String PROPERTY_DRIVER = "DRIVER";
	public static final String PROPERTY_NOM_UTILISATEUR = "UTILISATEUR";
	public static final String PROPERTY_MOT_DE_PASSE = "MOT_DE_PASSE";
	
	private String url;
	private String nomUtilisateur;
	private String motDePasse;
	private String driver;
	
	/**
	 * Constructeur de la Factory permettant une simple connexion Ã  la base de donnÃ©es.
	 * 
	 * @param daoFactory la Factory permettant la crÃ©ation d'une connexion Ã  la BDD.
	 */
	private DAOFactory(String url, String driver, String nomUtilisateur, String motDePasse) {
		this.url = url;
		this.nomUtilisateur = nomUtilisateur;
		this.motDePasse = motDePasse;
		this.driver = driver;
	}
	
	public DAOFactory()
	{
		String[] proprietes = chargerProprietes();
		this.url = proprietes[0];
		this.driver = proprietes[1];
		this.nomUtilisateur = proprietes[2];
		this.motDePasse = proprietes[3];
	}
	
	/**
	 * RÃ©cupÃ¨re une instance de la Factory permettant une simple connexion Ã  la base de donnÃ©es.
	 * 
	 * @return l'instance de la Factory.
	 */
	public static DAOFactory getInstance() {
		String[] proprietes = chargerProprietes();
		return new DAOFactory(proprietes[0], proprietes[1], proprietes[2], proprietes[3]);
	}
	
	/**
	 * RÃ©cupÃ¨re une instance de la Factory permettant une simple connexion Ã  la base de donnÃ©es.
	 * 
	 * @return l'instance de la Factory.
	 */
	public static DAOFactory getInstance(String cheminProperties) {
		String[] proprietes = chargerProprietes(cheminProperties);
		return new DAOFactory(proprietes[0], proprietes[1], proprietes[2], proprietes[3]);
	}
	
	/**
	 * RÃ©cupÃ¨re une instance de la Factory permettant une connexion au pool de connexions.
	 * 
	 * @return l'instance de la Factory.
	 */
	public static DAOFactory getInstancePool() {
		/* RÃ©cupÃ©ration des proprietes de connexion */
		String[] proprietes = chargerProprietes();
		String url = proprietes[0];
		String nomUtilisateur = proprietes[2];
		String motDePasse = proprietes[3];
		
		/*
		 * CrÃ©ation d'une configuration de pool de connexions via l'objet
		 * HikariConfig et les diffÃ©rents setters associÃ©s
		 */
		HikariConfig config = new HikariConfig();
		/* Mise en place de l'URL, du nom et du mot de passe */
		config.setJdbcUrl(url);
		config.setUsername(nomUtilisateur);
		config.setPassword(motDePasse);
		/* ParamÃ©trage du pool */
		// nombre maximum de connexions dans la pool
		config.setMaximumPoolSize(3);
		// fixe la durÃ©e de vie des connexions
		config.setIdleTimeout(1380);
		config.setMaxLifetime(1380);
		// permet de mettre en cache les requetes preparees
		config.addDataSourceProperty("cachePrepStmts", "true");
		// fixe le nombre de requetes preparees que le Driver met en cache par connexion
		config.addDataSourceProperty("prepStmtCacheSize", "250"); 
		// fixe la taille maximale d'une requete preparee que le Driver met en cache
		config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048"); 
		// permet au serveur d'executer des requetes preparees pour gagner en performances
		config.addDataSourceProperty("useServerPrepStmts", "true"); 
		
		/* CrÃ©ation du pool Ã  partir de la configuration, via l'objet HikariDataSource */
		HikariDataSource dataSource = new HikariDataSource(config);
		
		/* Enregistrement du pool crÃ©Ã© dans une variable d'instance via un appel au constructeur de DAOFactory */
		return new DAOFactory(dataSource);
	}
	
	/**
	 * Fournit une connexion Ã  la base de donnÃ©es ou au pool de connexions.
	 * 
	 * @return connection la connexion Ã  la base de donnÃ©es ou au pool de connexions.
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {
		// si on souhaite une simple connexion Ã  la BDD
		if (this.url != null || this.nomUtilisateur != null || this.motDePasse != null) {
			return DriverManager.getConnection(this.url, this.nomUtilisateur, this.motDePasse);
		} else { // sinon, on souhaite une connexion au pool de connexions
			return this.dataSource.getConnection();
		}
	}

	/**
	 * Charge les propriÃ©tÃ©s de connexion Ã  la base de donnÃ©es.
	 * 
	 * @return le tableau de Strings contenant les propriÃ©tÃ©s de connexion Ã  la base de donnÃ©es.
	 * @throws DAOConfigurationException
	 */
	public static String[] chargerProprietes(String cheminProperties) {
		Properties properties = new Properties();
		String url;
		String driver;
		String nomUtilisateur;
		String motDePasse;

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream fichierProperties = classLoader.getResourceAsStream(cheminProperties);

		if (fichierProperties == null) {
			throw new DAOException(String.format(FICHIER_PROPERTIES_INTROUVABLE, cheminProperties));
		}

		try {
			properties.load(fichierProperties);
			url = properties.getProperty(PROPERTY_URL);
			driver = properties.getProperty(PROPERTY_DRIVER);
			nomUtilisateur = properties.getProperty(PROPERTY_NOM_UTILISATEUR,"");
			motDePasse = properties.getProperty(PROPERTY_MOT_DE_PASSE,"");
		} catch (FileNotFoundException e) {
			throw new DAOException(String.format(FICHIER_PROPERTIES_INTROUVABLE, cheminProperties), e);
		} catch (IOException e) {
			throw new DAOException("Impossible de charger le fichier properties " + cheminProperties, e);
		}

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new DAOException("Le driver est introuvable dans le classpath.", e);
		}
		
		return new String[] { url, driver, nomUtilisateur, motDePasse};
	}
	
	public static String[] chargerProprietes() {
		return chargerProprietes(FICHIER_PROPERTIES);
	}
	
	/**
	 * 
	 * @param nouvellesValeurs
	 */
	public void sauvegarderProprietes(String[] nouvellesValeurs) {
		Properties properties = new Properties();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		InputStream fichierProperties = classLoader.getResourceAsStream(FICHIER_PROPERTIES);

		if (fichierProperties == null) {
			throw new DAOException(String.format(FICHIER_PROPERTIES_INTROUVABLE, FICHIER_PROPERTIES));
		}

		try {
			properties.load(fichierProperties);
			properties.setProperty(PROPERTY_URL, nouvellesValeurs[0]);
			properties.setProperty(PROPERTY_DRIVER, nouvellesValeurs[1]);
			properties.setProperty(PROPERTY_NOM_UTILISATEUR, nouvellesValeurs[2]);
			properties.setProperty(PROPERTY_MOT_DE_PASSE, nouvellesValeurs[3]);

			File fileObject = new File(this.getClass().getResource(FICHIER_PROPERTIES_NOM).toURI());
			FileOutputStream out = new FileOutputStream(fileObject);
			properties.store(out, null);
			out.close();
		} catch (FileNotFoundException | URISyntaxException e) {
			throw new DAOException(String.format(FICHIER_PROPERTIES_INTROUVABLE, FICHIER_PROPERTIES), e);
		} catch (IOException e) {
			throw new DAOException("Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e);
		}
		
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			throw new DAOException("Le driver " + driver + " est introuvable dans le classpath.", e);
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean testConnexion()
	{
		try { 
			Class.forName(this.driver); 
			DriverManager.getConnection(url, nomUtilisateur, motDePasse); 
		} catch (ClassNotFoundException | SQLException e) { 
			return false;
		}
		return true;
	}
	
	/**
	 * 
	 * @param url
	 * @param driver
	 * @param nomUtilisateur
	 * @param motDePasse
	 * @return
	 */
	public boolean testConnexion(String url, String driver, String nomUtilisateur, String motDePasse)
	{
		try { 
			Class.forName(driver); 
			DriverManager.getConnection(url, nomUtilisateur, motDePasse); 
		} catch (ClassNotFoundException | SQLException e) { 
			return false;
		}
		return true;
	}
	
	/* MÃ©thodes de rÃ©cupÃ©ration des diffÃ©rents DAO */
	public UtilisateurDAO getUtilisateurDao() {
		return new UtilisateurDAO(this);
	}

	public SujetDAO getSujetDao() {
		return new SujetDAO(this);
	}

	public PosterDAO getPosterDao() {
		return new PosterDAO(this);
	}

	public NotificationDAO getNotificationDao() {
		return new NotificationDAO(this);
	}

	public CommentaireDAO getCommentaireDao() {
		return new CommentaireDAO(this);
	}

	public EtudiantDAO getEtudiantDao() {
		return new EtudiantDAO(this);
	}

	public RoleDAO getRoleDAO() {
		return new RoleDAO(this);
	}

	public OptionESEODAO getOptionESEODAO() {
		return new OptionESEODAO(this);
	}

	public ProfesseurDAO getProfesseurDAO() {
		return new ProfesseurDAO(this);
	}

	public EquipeDAO getEquipeDAO() {
		return new EquipeDAO(this);
	}

	public PartageRoleDAO getPartageDAO() {
		return new PartageRoleDAO(this);
	}

	public NoteInteretTechnoDAO getNoteInteretTechnoDAO() {
		return new NoteInteretTechnoDAO(this);
	}

	public NoteInteretSujetDAO getNoteInteretSujetDAO() {
		return new NoteInteretSujetDAO(this);
	}

	public SemestreDAO getSemestreDAO() {
		return new SemestreDAO(this);
	}
	
	public EvaluationDAO getEvaluationDAO() {
		return new EvaluationDAO(this);
	}
	
	public JuryDAO getJuryDAO() {
		return new JuryDAO(this);
	}
	
	public EpreuveDAO getEpreuveDAO() {
		return new EpreuveDAO(this);
	}
	
	public TypeEpreuveDAO getTypeEpreuveDAO() {
		return new TypeEpreuveDAO(this);
	}
	
	public ReunionDAO getReunionDAO() {
		return new ReunionDAO(this);
	}
}