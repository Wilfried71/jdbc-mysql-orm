package fr.thomas.orm;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.thomas.orm.annotations.Column;
import fr.thomas.orm.annotations.Table;
import fr.thomas.orm.interfaces.DAO;

/**
 * Classe model qui sera d�riv�e par les entit�s ratach�es � la base de donn�es.
 * @author tpeyr
 *
 * @param <T>
 */
public class Model<T> implements DAO<T>{

	public T create(T object) {
		// TODO Auto-generated method stub
		return null;
	}

	public T update(T object) {
		// TODO Auto-generated method stub
		return null;
	}

	public void delete(T object) {
		// TODO Auto-generated method stub
		
	}

	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	public List<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public Optional<T> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
	/**
	 * Retourne le nom de la table � laquelle la classe fait r�f�rence
	 * @param classToAnalyse
	 * @return Le nom si la table est renseign�e, sinon renvoie null.
	 */
	public String getTable(Class<T> classToAnalyse) {
		// R�cup�ration de l'annotation @Table
		Table tableAnnotation = classToAnalyse.getAnnotation(Table.class);
		// Si l'annotation existe
		if(tableAnnotation != null) {
			return tableAnnotation.name();
		} else {
			return null;
		}
	}
	
	
	/**
	 * Retourne la liste des champs reli�s � la base.
	 * @param classToAnalyse
	 * @return La liste des champs qui r�f�rencent une colonne
	 */
	public List<Field> getColumns(Class<T> classToAnalyse) {
		
		// Cr�ation d'une liste qui va stocker la liste des colonnes.
		List<Field> columns = new ArrayList<Field>();
		
		// R�cup�ration de la liste des champs de la classe
		Field[] fields = classToAnalyse.getDeclaredFields();
		
		// Pour chaque champ de la classe
		for (Field field : fields) {
			
			// R�cup�ration de l'objet annotation
			Column columnAnnotation = field.getAnnotation(Column.class);
			
			// Si le champ a l'annotation column
			if(columnAnnotation != null) {
				columns.add(field);
			}
		}
		
		return columns;
	}

}
