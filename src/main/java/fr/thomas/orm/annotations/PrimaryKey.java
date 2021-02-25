package fr.thomas.orm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Annotation qui sp�cifie que le champ est une cl� primaire
 * @author tpeyr
 *
 */
@Target(ElementType.FIELD)
public @interface PrimaryKey {
	
}
