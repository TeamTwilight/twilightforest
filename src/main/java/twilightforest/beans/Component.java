package twilightforest.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated classes will be automatically registered with {@link TFBeanContext#register(Class, Object)}.<br/>
 * If a value is set then {@link TFBeanContext#register(Class, String, Object)} is used instead.<p/>
 *
 * Requires a no-arg constructor.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Component {

	String DEFAULT_VALUE = "!twilightforest:internal:bean:DEFAULT!";

	String value() default DEFAULT_VALUE;

}
