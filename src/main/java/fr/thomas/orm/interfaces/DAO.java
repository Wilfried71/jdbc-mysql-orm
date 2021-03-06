package fr.thomas.orm.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface de base, qui contient les définitions des méthodes CRUD.
 * @author tpeyr
 *
 * @param <T>
 */
public interface DAO<T> {

	/**
	 * Méthode qui sert à créer un objet dans la base de données.<br>
	 * Par défaut, les champs identifiants doivent être à null.
	 * Si les champs identifiants sont renseignés, la requête sera quand même tentée.
	 * @param object
	 * @return L'objet qui est créé.
	 * @throws Exception 
	 */
	T create(T object) throws Exception;
	
	/**
	 * Méthode qui sert à modifier un objet dont le ou les champs identifiants sont renseignés.<br>
	 * @param object
	 * @return L'objet qui est modifié.
	 * @throws Exception 
	 */
	T update(T object) throws Exception;
	
	/**
	 * Méthode qui sert à supprimer un objet dans la base de données.<br>
	 * Seul le ou les champs identifiants sont pris en compte dans la méthode.
	 * @param object
	 * @throws Exception 
	 */
	void delete(T object) throws Exception;
	
	/**
	 * Méthode qui sert à supprimer un objet dans la base de données, dont l'identifiant est passé en paramètres.<br>
	 * @param object
	 * @throws Exception 
	 */
	void deleteById(Long id) throws Exception;
	
	/**
	 * Fonction qui retourne tous les éléments d'une table donnée
	 * @return
	 * @throws SQLException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 * @throws ParseException 
	 */
	List<T> findAll() throws SQLException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, ParseException;
	
	/**
	 * Réalise une requête préparée de type SELECT.
	 * @param query
	 * @param fields
	 * @return
	 * @throws Exception 
	 */
	List<T> query(String query, List<?> fields) throws Exception;
	
	/**
	 * Fonction qui retourne un objet dont l'identifiant est passé en paramètres
	 * @param id
	 * @return
	 * @throws SQLException 
	 * @throws ParseException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws InstantiationException 
	 * @throws IllegalAccessException 
	 * @throws IllegalArgumentException 
	 */
	T findById(Long id) throws SQLException, IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException, SecurityException, ParseException;
}
