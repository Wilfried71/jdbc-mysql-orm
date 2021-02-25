package fr.thomas.orm.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
public @interface ForeignKey {
	String table();
	String field();
}
