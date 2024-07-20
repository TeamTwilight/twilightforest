package twilightforest.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated classes that exist as registry objects will be handled as psuedo-beans for {@link twilightforest.beans.processors.AnnotationDataPostProcessor}.<br/>
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Configurable {

}
