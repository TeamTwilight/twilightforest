package twilightforest.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated methods will be automatically invoked after bean construction has finished.<br/>
 * Runs after {@link twilightforest.beans.processors.AnnotationDataPostProcessor}<p/>
 *
 * Works for {@link Configurable}
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface PostConstruct {

}
