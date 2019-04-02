package com.dao;

import java.util.List;

public abstract class DAO<T> {
	private DAOFactory daoFactory;

	/**
	 * Constructeur de DAO.
	 * 
	 * @param daoFactory la Factory permettant la création d'une connexion à la BDD.
	 */
	DAO(DAOFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	public DAOFactory getDaoFactory() {
		return this.daoFactory;
	}

	/**
	 * Insère un objet dans la BDD à partir des attributs spécifiés dans un bean objet.
	 * 
	 * @param objet l'objet que l'on souhaite insérer dans la BDD à partir du bean objet.
	 */
	public abstract void creer(T objet);
	
	/**
	 * Liste tous les objets ayant pour attributs les mêmes que ceux
	 * spécifiés dans un bean objet.
	 * 
	 * @param objet l'objet que l'on souhaite trouver dans la BDD.
	 * @return objets la liste des objets trouvés dans la BDD.
	 */
	public abstract List<T> trouver(T objet);
	
	/**
	 * Modifie UN objet ayant pour attributs les mêmes que ceux
	 * spécifiés dans un bean objet et la même clé primaire.
	 * Cette clé primaire ne peut être modifiée.
	 * 
	 * @param objet l'objet que l'on souhaite modifier dans la BDD.
	 */
	public abstract void modifier(T objet);

	/**
	 * Supprime tous les objets ayant pour attributs les mêmes que ceux
	 * spécifiés dans un bean objet.
	 * 
	 * @param objet l'objet que l'on souhaite supprimer dans la BDD.
	 */
	public abstract void supprimer(T objet);

	/**
	 * Liste tous les objets présents dans la BDD.
	 * 
	 * @return objets la liste des objets présents dans la BDD.
	 */
	public abstract List<T> lister();
}
