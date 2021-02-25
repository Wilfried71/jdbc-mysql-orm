package fr.thomas.orm;

/**
 * Configuration de la connexion à la base de données.<br>
 * Les variables doivent être définies au lancement de l'application.
 * @author tpeyr
 *
 */
public class ORMConfig {
	
	/**
	 * Adresse IP ou nom de domaine du serveur SGBD
	 */
	public static String server;
	
	/**
	 * Port d'accès au serveur SGBD
	 */
	public static String port;
	
	/**
	 * Nom d'utilisateur utilisé pour se connecter au serveur SGBD.
	 */
	public static String username;
	
	/**
	 * Mot de passe utilisé pour se connecter au serveur SGBD
	 */
	public static String password;
	
	/**
	 * Nom de la base de données.
	 */
	public static String database;
	
	/**
	 * Timezone du serveur SGBD (a une influence sur les dates)
	 */
	public static String serverTimeZone;
}
