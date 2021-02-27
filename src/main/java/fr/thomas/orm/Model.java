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
 * Classe model qui sera dérivée par les entités ratachées à la base de données.
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
		// Création de la connexion à la base de données
		Connection connection = DriverManager.getConnection(getUrl(), ORMConfig.username, ORMConfig.password);

		// Get columns of the class
		List<Field> columns = getColumns();

		// On crée la requête au format String
		String request = "INSERT INTO " + getTable() + " VALUES (";

		// Pour chaque champ, on ajoute un ? dans la requête préparée
		for (Field field : columns) {
			field.setAccessible(true);
			request += "?,";
		}
		// On supprime la dernière virgule pour éviter une erreur de syntaxe SQL, puis
		// on rajoute )
		request = request.substring(0, request.length() - 1) + ")";

		// On crée la requête préparée
		PreparedStatement stmt = connection.prepareStatement(request, Statement.RETURN_GENERATED_KEYS);

		int i = 1;
		// On ajoute chaque champ à la requête préparée
		for (Field field : columns) {

			setValueIfNotNull(field, object, i, stmt);
			// On incrémente l'id
			i++;
		}
		
		// Execution de la requete
		stmt.execute();
		// Récupération du dernier id
		ResultSet rs = stmt.getGeneratedKeys();
		rs.next();
		// Renvoie l'objet créé
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
		// Création de la connexion à la base de données
		Connection connection = DriverManager.getConnection(getUrl(), ORMConfig.username, ORMConfig.password);
		// Création du statement
		Statement statement = connection.createStatement();
		// Récupération du résultat de la requête
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
		// Création de la connexion à la base de données
		Connection connection = DriverManager.getConnection(getUrl(), ORMConfig.username, ORMConfig.password);
		// Création du statement
		Statement statement = connection.createStatement();

		ResultSet rs;
		T item = null;
		try {
			// Récupération du résultat de la requête
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

			// Si le champ est un Long ou une clé primaire
			if (field.getType().equals(Long.class) || isPrimaryKey(field)) {
				field.set(tObject, rs.getLong(field.getAnnotation(Column.class).name()));
			}

			// Si le champ est un Long ou une clé primaire
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

			// Si le champ est une clé étrangère, on charge l'objet
			if (isForeignKey(field)) {
				// Création du modèle pour l'entité étrangère
				Model<?> model = getModelOfType(field.getType());

				// On récupère l'objet clé étrangère
				Object fkObject = model.findById(rs.getLong(field.getAnnotation(Column.class).name()));
				field.set(tObject, fkObject);
			}
		}
		return tObject;
	}

	/**
	 * Check si le champ est une clé primaire
	 * 
	 * @param field
	 * @return
	 */
	private Boolean isPrimaryKey(Field field) {
		return field.getAnnotation(PrimaryKey.class) != null;
	}

	/**
	 * Check si le champ est une clé étrangère
	 * 
	 * @param field
	 * @return
	 */
	private Boolean isForeignKey(Field field) {
		return field.getAnnotation(ForeignKey.class) != null;
	}

	/**
	 * Retourne le nom de la table à laquelle la classe fait référence
	 * 
	 * @return Le nom si la table est renseignée, sinon renvoie null.
	 */
	private String getTable() {
		// Récupération de l'annotation @Table
		Table tableAnnotation = myClass.getAnnotation(Table.class);
		// Si l'annotation existe
		if (tableAnnotation != null) {
			return tableAnnotation.name();
		} else {
			return null;
		}
	}

	/**
	 * Retourne la liste des champs reliés à la base.
	 * 
	 * @return La liste des champs qui référencent une colonne
	 */
	private List<Field> getColumns() {

		// Création d'une liste qui va stocker la liste des colonnes.
		List<Field> columns = new ArrayList<Field>();

		// Récupération de la liste des champs de la classe
		Field[] fields = myClass.getDeclaredFields();

		// Pour chaque champ de la classe
		for (Field field : fields) {

			// Récupération de l'objet annotation
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
	 * Instancie un objet {@link Model} avec le type passé en paramètre
	 * 
	 * @param <X>
	 * @param c
	 * @return
	 */
	private <X> Model<X> getModelOfType(Class<X> c) {
		return new Model<X>(c);
	}

	/**
	 * Renvoie la liste de clés primaires de la classe
	 * 
	 * @return
	 */
	private List<Field> getPrimaryKeys() {
		// Création d'une liste qui va stocker la liste des colonnes.
		List<Field> columns = new ArrayList<Field>();

		// Récupération de la liste des champs de la classe
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
			// Si Long ou clé primaire
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
			} else // Si c'est une clé étrangère
			if (isForeignKey(f)) {
				/*
				 * Model<?> model = getModelOfType(f.getClass()); stmt.setLong(index, (Long)
				 * model.getPrimaryKeys().get(0).get(obj));
				 */
				// FOREIGN KEY pas gérées
				stmt.setNull(index, Types.NULL);
				throw new Exception("Les clés étrangères en tant qu'objet ne sont pas encore gérées.");
			}
			return stmt;
		}
	}

}
