package fr.thomas.orm.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

/**
 * Interface de base, qui contient les d�finitions des m�thodes CRUD.
 * @author tpeyr
 *
 * @param <T>
 */
public interface DAO<T> {

	/**
	 * M�thode qui sert � cr�er un objet dans la base de donn�es.<br>
	 * Par d�faut, les champs identifiants doivent �tre � null.
	 * Si les champs identifiants sont renseign�s, la requ�te sera quand m�me tent�e.
	 * @param object
	 * @return L'objet qui est cr��.
	 * @throws Exception 
	 */
	T create(T object) throws Exception;
	
	/**
	 * M�thode qui sert � modifier un objet dont le ou les champs identifiants sont renseign�s.<br>
	 * @param object
	 * @return L'objet qui est modifi�.
	 */
	T update(T object);
	
	/**
	 * M�thode qui sert � supprimer un objet dans la base de donn�es.<br>
	 * Seul le ou les champs identifiants sont pris en compte dans la m�thode.
	 * @param object
	 */
	void delete(T object);
	
	/**
	 * M�thode qui sert � supprimer un objet dans la base de donn�es, dont l'identifiant est pass� en param�tres.<br>
	 * @param object
	 */
	void deleteById(Long id);
	
	/**
	 * Fonction qui retourne tous les �l�ments d'une table donn�e
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
	 * Fonction qui retourne un objet dont l'identifiant est pass� en param�tres
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
