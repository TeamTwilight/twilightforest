package twilightforest.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated fields will be automatically injected with {@link TFBeanContext#inject(Class)}.<br/>
 * If a value is set then {@link TFBeanContext#inject(Class, String)} is used instead.<p/>
 *
 * NOT CURRENTLY IMPLEMENTED!
 */
@Deprecated
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {

	String value() default Component.DEFAULT_VALUE;

}
