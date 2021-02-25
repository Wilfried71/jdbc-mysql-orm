package fr.thomas.orm;

/**
 * Configuration de la connexion � la base de donn�es.<br>
 * Les variables doivent �tre d�finies au lancement de l'application.
 * @author tpeyr
 *
 */
public class ORMConfig {
	
	/**
	 * Adresse IP ou nom de domaine du serveur SGBD
	 */
	public static String server;
	
	/**
	 * Port d'acc�s au serveur SGBD
	 */
	public static String port;
	
	/**
	 * Nom d'utilisateur utilis� pour se connecter au serveur SGBD.
	 */
	public static String username;
	
	/**
	 * Mot de passe utilis� pour se connecter au serveur SGBD
	 */
	public static String password;
	
	/**
	 * Nom de la base de donn�es.
	 */
	public static String database;
	
	/**
	 * Timezone du serveur SGBD (a une influence sur les dates)
	 */
	public static String serverTimeZone;
}
