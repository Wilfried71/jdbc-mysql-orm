package fr.thomas.orm;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import fr.thomas.orm.annotations.Column;
import fr.thomas.orm.annotations.ForeignKey;
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

	public T create(T object) throws Exception {
		// Cr�ation de la connexion � la base de donn�es
		Connection connection = DriverManager.getConnection(getUrl(), ORMConfig.username, ORMConfig.password);

		// Get columns of the class
		List<Field> columns = getColumns();

		// On cr�e la requ�te au format String
		String request = "INSERT INTO " + getTable() + " VALUES (";

		// Pour chaque champ, on ajoute un ? dans la requ�te pr�par�e
		for (Field field : columns) {
			field.setAccessible(true);
			request += "?,";
		}
		// On supprime la derni�re virgule pour �viter une erreur de syntaxe SQL, puis
		// on rajoute )
		request = request.substring(0, request.length() - 1) + ")";

		// On cr�e la requ�te pr�par�e
		PreparedStatement stmt = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);

		int i = 1;
		// On ajoute chaque champ � la requ�te pr�par�e
		for (Field field : columns) {

			setValueIfNotNull(field, object, i, stmt);
			// On incr�mente l'id
			i++;
		}
		
		// Execution de la requete
		stmt.execute();
		// R�cup�ration du dernier id
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		// Renvoie l'objet cr��
		return findById(Long.parseLong(rs.getString(1)));
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
			items.add(bindDataToObject(rs));
		}
		rs.close();
		statement.close();
		connection.close();
		return items;
	}

	public T findById(Long id)
			throws SQLException, IllegalArgumentException, IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException, SecurityException, ParseException, NullPointerException {
		// Cr�ation de la connexion � la base de donn�es
		Connection connection = DriverManager.getConnection(getUrl(), ORMConfig.username, ORMConfig.password);
		// Cr�ation du statement
		Statement statement = connection.createStatement();

		ResultSet rs;
		T item = null;
		try {
			// R�cup�ration du r�sultat de la requ�te
			rs = statement.executeQuery("SELECT * FROM " + getTable() + " WHERE " + getTable() + "."
					+ getPrimaryKeys().get(0).getAnnotation(Column.class).name() + "=" + id);

			// Si il y a un objet correspondant
			if (rs.next()) {
				item = bindDataToObject(rs);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		statement.close();
		connection.close();
		return item;
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
	private T bindDataToObject(ResultSet rs)
			throws IllegalArgumentException, IllegalAccessException, InstantiationException, InvocationTargetException,
			NoSuchMethodException, SecurityException, SQLException, ParseException {
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

			// Si le champ est une cl� �trang�re, on charge l'objet
			if (isForeignKey(field)) {
				// Cr�ation du mod�le pour l'entit� �trang�re
				Model<?> model = getModelOfType(field.getType());

				// On r�cup�re l'objet cl� �trang�re
				Object fkObject = model.findById(rs.getLong(field.getAnnotation(Column.class).name()));
				field.set(tObject, fkObject);
			}
		}
		return tObject;
	}

	/**
	 * Check si le champ est une cl� primaire
	 * 
	 * @param field
	 * @return
	 */
	private Boolean isPrimaryKey(Field field) {
		return field.getAnnotation(PrimaryKey.class) != null;
	}

	/**
	 * Check si le champ est une cl� �trang�re
	 * 
	 * @param field
	 * @return
	 */
	private Boolean isForeignKey(Field field) {
		return field.getAnnotation(ForeignKey.class) != null;
	}

	/**
	 * Retourne le nom de la table � laquelle la classe fait r�f�rence
	 * 
	 * @return Le nom si la table est renseign�e, sinon renvoie null.
	 */
	private String getTable() {
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
	private List<Field> getColumns() {

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

	/**
	 * Instancie un objet {@link Model} avec le type pass� en param�tre
	 * 
	 * @param <X>
	 * @param c
	 * @return
	 */
	private <X> Model<X> getModelOfType(Class<X> c) {
		return new Model<X>(c);
	}

	/**
	 * Renvoie la liste de cl�s primaires de la classe
	 * 
	 * @return
	 */
	private List<Field> getPrimaryKeys() {
		// Cr�ation d'une liste qui va stocker la liste des colonnes.
		List<Field> columns = new ArrayList<Field>();

		// R�cup�ration de la liste des champs de la classe
		Field[] fields = myClass.getDeclaredFields();

		// Pour chaque champ de la classe
		for (Field field : fields) {

			// Si le champ a l'annotation column
			if (field.getAnnotation(PrimaryKey.class) != null && field.getAnnotation(Column.class) != null) {
				columns.add(field);
			}
		}

		return columns;
	}

	/**
	 * Si la valeur du champ est null, renvoie null, sinon renvoie la valeur
	 * 
	 * @param f
	 * @param targetClass Classe dans laquelle on veut caster la valeur du champ
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private PreparedStatement setValueIfNotNull(Field f, T obj, int index, PreparedStatement stmt) throws Exception {
		if (f.get(obj) == null) {
			stmt.setNull(index, Types.NULL);
			return stmt;
		} else {
			// Si Long ou cl� primaire
			if (f.getType().equals(Long.class)) {

				stmt.setLong(index, (Long) f.get(obj));

			} else // Si Float
			if (f.getType().equals(Float.class)) {

				stmt.setFloat(index, (Float) f.get(obj));

			} else // Si String
			if (f.getType().equals(String.class)) {

				stmt.setString(index, (String) f.get(obj));
			} else // Si date
			if (f.getType().equals(Date.class)) {

				stmt.setObject(index, f.get(obj));
			} else // Si c'est une cl� �trang�re
			if (isForeignKey(f)) {
				/*
				 * Model<?> model = getModelOfType(f.getClass()); stmt.setLong(index, (Long)
				 * model.getPrimaryKeys().get(0).get(obj));
				 */
				// FOREIGN KEY pas g�r�es
				stmt.setNull(index, Types.NULL);
				throw new Exception("Les cl�s �trang�res en tant qu'objet ne sont pas encore g�r�es.");
			}
			return stmt;
		}
	}

}
