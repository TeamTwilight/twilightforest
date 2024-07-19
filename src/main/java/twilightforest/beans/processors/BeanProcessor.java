package twilightforest.beans.processors;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to register {@link AnnotationDataProcessor} and {@link AnnotationDataPostProcessor} classes
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface BeanProcessor {

	/**
	 * Execution order: -1 -> 0 -> 1
	 */
	int priority() default 0;

}
