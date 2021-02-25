package fr.thomas.orm.interfaces;

import java.util.List;
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
	 */
	T create(T object);
	
	/**
	 * Méthode qui sert à modifier un objet dont le ou les champs identifiants sont renseignés.<br>
	 * @param object
	 * @return L'objet qui est modifié.
	 */
	T update(T object);
	
	/**
	 * Méthode qui sert à supprimer un objet dans la base de données.<br>
	 * Seul le ou les champs identifiants sont pris en compte dans la méthode.
	 * @param object
	 */
	void delete(T object);
	
	/**
	 * Méthode qui sert à supprimer un objet dans la base de données, dont l'identifiant est passé en paramètres.<br>
	 * @param object
	 */
	void deleteById(Long id);
	
	/**
	 * Fonction qui retourne tous les éléments d'une table donnée
	 * @return
	 */
	List<T> findAll();
	
	/**
	 * Fonction qui retourne un objet dont l'identifiant est passé en paramètres
	 * @param id
	 * @return
	 */
	Optional<T> findById(Long id);
}
