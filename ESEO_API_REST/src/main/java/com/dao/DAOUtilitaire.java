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

public class DAOUtilitaire {
	
	private static Logger logger = Logger.getLogger(DAOUtilitaire.class.getName());
	
	private static final String SELECT = "SELECT";
	private static final String UPDATE = "UPDATE";
	private static final String DELETE = "DELETE";
	private static final String WHERE = " WHERE ";
	private static final String WHERE_ID = " WHERE id";
	private static final String AND = " AND ";
	private static final String AND_ID = " AND id";
	private static final String ID = ".id";
	
	private DAOUtilitaire() {

	}
	
	public static void fermeture(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				logger.log(Level.WARN, "Echec de la fermeture du ResultSet : " + e.getMessage(), e);
			}
		}
	}
	
	public static void fermeture(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				logger.log(Level.WARN, "Echec de la fermeture du Statement : " + e.getMessage(), e);
			}
		}
	}
	
	public static void fermeture(Connection connection) {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.log(Level.WARN, "Echec de la fermeture de la connexion : " + e.getMessage(), e);
			}
		}
	}
	
	public static void fermetures(ResultSet resultSet, Statement statement, Connection connection) {
		fermeture(resultSet);
		fermeture(statement);
		fermeture(connection);
	}
	
	public static void fermetures(Statement statement, Connection connection) {
		fermeture(statement);
		fermeture(connection);
	}
	
	protected static String initialisationRequetePreparee(String choixCRUD,
			String nomEntite, String[][] attributs) {
		// création de la requête préparée
		List<String> valeursAttributs = creationRequete(choixCRUD, nomEntite, attributs);
		String sql = valeursAttributs.remove(0);
		// changement de la structure de la requête si le choix de la méthode CRUD est "UPDATE"
		sql = caseUpdate1Id(sql, choixCRUD, nomEntite)+" ";
		// remplacement des "?" par les valeurs des attributs
		String[] listeSQL = sql.split("\\?");
		StringBuilder newSQL = new StringBuilder(listeSQL[0]);
		for(int i = 0; i<valeursAttributs.size(); i++) {
			newSQL.append("\"" + valeursAttributs.get(i) + "\"" + listeSQL[i+1]);
		}
		return newSQL.toString();
	}
	
	protected static String initialisationRequetePreparee(String sql, Object... objets) {
		String[] listeSQL = (sql+" ").split("\\?");
		StringBuilder newSQL = new StringBuilder(listeSQL[0]);
		for(int i = 0; i<objets.length; i++) {
			newSQL.append("\"" + objets[i] + "\"" + listeSQL[i+1]);
		}
		return newSQL.toString().replaceAll("\"null\"", "null");
	}
	
	protected static ArrayList<String> creationRequete(String choixCRUD, String nomEntite, String[][] attributs) {
		// début de la requête en fonction du choix de la méthode CRUD
		StringBuilder sqlBuilder = new StringBuilder(creationDebutRequete(choixCRUD, nomEntite));

		// création de la requête et stockage des valeurs des attributs
		ArrayList<String> valeursAttributs = new ArrayList<>();
		boolean dejaFait = false;
		for (int i = 0; i < attributs[0].length; i++) {
			// si la valeur de l'attribut courant n'est pas nulle
			if (attributs[1][i] != null && attributs[1][i] != "null") {
				// on la stocke dans une liste
				valeursAttributs.add(attributs[1][i]);
				// on continue la création de la requête
				if (!dejaFait) {
					sqlBuilder.append(attributs[0][i] + " = ?");
					dejaFait = true;
				} else {
					sqlBuilder.append(AND + attributs[0][i] + " = ?");
				}
			}
		}
		valeursAttributs.add(0, sqlBuilder.toString());
		
		return valeursAttributs;
	}
	
	protected static void traitementUpdate(DAOFactory daoFactory, String nomEntite, String[][] attributs,
			Logger logger) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			// création d'une connexion grâce à la DAOFactory placée en attribut de la classe
			connection = daoFactory.getConnection();
			// mise en forme de la requête UPDATE en fonction des attributs de l'objet
			String sql = initialisationRequetePreparee(UPDATE, nomEntite, attributs);
			preparedStatement = connection.prepareStatement(sql, Statement.NO_GENERATED_KEYS);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			logger.log(Level.WARN, "Échec de la mise à jour de l'objet, aucune ligne modifiée dans la table.", e);
		} finally {
			// fermeture des ressources utilisées
			fermetures(preparedStatement, connection);
		}
	}
	
	private static String creationDebutRequete(String choixCRUD, String nomEntite) {
		String sql;
		
		if (SELECT.equals(choixCRUD)) {
			sql = "SELECT * FROM " + nomEntite + WHERE;
		} else if (DELETE.equals(choixCRUD)) {
			sql = "DELETE FROM " + nomEntite + WHERE;
		} else if (UPDATE.equals(choixCRUD)) {
			sql = "UPDATE " + nomEntite + " SET ";
		} else {
			throw new DAOException("Erreur : choixCRUD ne peut être que 'SELECT', 'DELETE' ou 'UPDATE'");
		}
		
		return sql;
	}
	
	private static String caseUpdate1Id (String sql, String choixCRUD, String nomEntite) {
		if (UPDATE.equals(choixCRUD)) {
			sql = sql.replace(AND_ID + nomEntite + " = ?", "");
			sql = sql.replace(AND + nomEntite.toLowerCase() + ID +nomEntite + " = ?", "");
			sql = sql.replace("AND", ", ");
			sql += WHERE_ID + nomEntite + " = ?";
		}
		return sql;
	}

}
