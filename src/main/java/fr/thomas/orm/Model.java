package fr.thomas.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import fr.thomas.orm.annotations.Column;
import fr.thomas.orm.annotations.PrimaryKey;
import fr.thomas.orm.annotations.Table;
import fr.thomas.orm.interfaces.DAO;

/**
 * Classe model qui sera d�riv�e par les entit�s ratach�es � la base de donn�es.
 * 
 * @author tpeyr
 *
 * @param <T>
 */
public class Model<T> implements DAO<T> {

	private Class<T> myClass;

	public Model(Class<T> providedClass) {
		myClass = providedClass;
	}

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

	public List<T> findAll()
			throws SQLException, IllegalArgumentException, IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException, SecurityException, ParseException {
		// Cr�ation de la connexion � la base de donn�es
		Connection connection = DriverManager.getConnection(getUrl(), ORMConfig.username, ORMConfig.password);
		// Cr�ation du statement
		Statement statement = connection.createStatement();
		// R�cup�ration du r�sultat de la requ�te
		ResultSet rs = statement.executeQuery("SELECT * FROM " + getTable());

		// Create items list
		List<T> items = new ArrayList<T>();

		while (rs.next()) {
			items.add(bindData(rs));
		}
		rs.close();
		statement.close();
		connection.close();
		return items;
	}

	public Optional<T> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Convertit un resultset en objet de type T.
	 * 
	 * @param rs
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws InstantiationException
	 * @throws SQLException
	 * @throws ParseException
	 */
	private T bindData(ResultSet rs) throws IllegalArgumentException, IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException, SecurityException, SQLException, ParseException {
		// Get columns of the class
		List<Field> columns = getColumns();

		// Create new Instance of the class
		T tObject = myClass.getDeclaredConstructor().newInstance();

		// Pour chaque champ
		for (Field field : columns) {

			// Rend le champ accessible
			field.setAccessible(true);

			// Si le champ est un Long ou une cl� primaire
			if (field.getType().equals(Long.class) || isPrimaryKey(field)) {
				field.set(tObject, rs.getLong(field.getAnnotation(Column.class).name()));
			}

			// Si le champ est un Long ou une cl� primaire
			if (field.getType().equals(Float.class)) {
				field.set(tObject, rs.getFloat(field.getAnnotation(Column.class).name()));
			}

			// Si le champ est un string
			if (field.getType().equals(String.class)) {
				field.set(tObject, rs.getString(field.getAnnotation(Column.class).name()));
			}

			// Si le champ est une Date
			if (field.getType().equals(Date.class)) {
				field.set(tObject, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
						.parseObject(rs.getString(field.getAnnotation(Column.class).name())));
			}

			// TODO : Appeler le findById si le champ est une cl� �trang�re.
		}
		return tObject;
	}

	/**
	 * Check si le champ est une cl� primaire
	 * 
	 * @param field
	 * @return
	 */
	public Boolean isPrimaryKey(Field field) {
		return field.getAnnotation(PrimaryKey.class) != null;
	}

	/**
	 * Retourne le nom de la table � laquelle la classe fait r�f�rence
	 * 
	 * @return Le nom si la table est renseign�e, sinon renvoie null.
	 */
	public String getTable() {
		// R�cup�ration de l'annotation @Table
		Table tableAnnotation = myClass.getAnnotation(Table.class);
		// Si l'annotation existe
		if (tableAnnotation != null) {
			return tableAnnotation.name();
		} else {
			return null;
		}
	}

	/**
	 * Retourne la liste des champs reli�s � la base.
	 * 
	 * @return La liste des champs qui r�f�rencent une colonne
	 */
	public List<Field> getColumns() {

		// Cr�ation d'une liste qui va stocker la liste des colonnes.
		List<Field> columns = new ArrayList<Field>();

		// R�cup�ration de la liste des champs de la classe
		Field[] fields = myClass.getDeclaredFields();

		// Pour chaque champ de la classe
		for (Field field : fields) {

			// R�cup�ration de l'objet annotation
			Column columnAnnotation = field.getAnnotation(Column.class);

			// Si le champ a l'annotation column
			if (columnAnnotation != null) {
				columns.add(field);
			}
		}

		return columns;
	}

	/**
	 * Get URL to the database
	 * 
	 * @return
	 */
	private String getUrl() {
		return "jdbc:mysql://" + ORMConfig.server + ":" + ORMConfig.port + "/" + ORMConfig.database + "?serverTimezone="
				+ ORMConfig.serverTimeZone;
	}

}
