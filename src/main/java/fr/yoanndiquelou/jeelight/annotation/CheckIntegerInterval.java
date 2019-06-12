package fr.yoanndiquelou.jeelight.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation of yeelight property name.
 * @author Y0annD
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface CheckIntegerInterval {
	/** Minimum value. */
	public int min() default Integer.MIN_VALUE;
	/** Maximum value. */
	public int max() default Integer.MAX_VALUE;

}
