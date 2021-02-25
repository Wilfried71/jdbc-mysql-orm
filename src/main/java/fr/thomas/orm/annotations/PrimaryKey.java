package fr.thomas.orm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation qui spécifie que le champ est une clé primaire
 * @author tpeyr
 *
 */
@Target(ElementType.FIELD)
public @interface PrimaryKey {
	
}
