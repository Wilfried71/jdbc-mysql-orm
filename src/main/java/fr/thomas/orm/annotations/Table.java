package fr.thomas.orm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation qui permet de renseigner le nom de la table 
 * @author tpeyr
 *
 */
@Target(ElementType.TYPE)
public @interface Table {
	/**
	 * Nom de la table dans la base de données.
	 */
	String name();
}
