package twilightforest.beans;

import java.lang.annotation.*;

/**
 * Annotated classes that exist as registry objects will be handled as pseudo-beans for {@link twilightforest.beans.processors.AnnotationDataPostProcessor}.<p/>
 *
 * This annotation is {@link Inherited} to subclasses.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Configurable {

}
