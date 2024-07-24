package twilightforest.beans;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated fields will be automatically injected with {@link TFBeanContext#inject(Class)}.<br/>
 * If a value is set then {@link TFBeanContext#inject(Class, String)} is used instead.<p/>
 *
 * Fields may be private.<br/>
 * When used inside a Bean or {@link Configurable} the field must be non-static.<br/>
 * When used outside a Bean, the field <b>must</b> be <b>static</b>!
 */
@Nullable
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Autowired {

	String value() default Component.DEFAULT_VALUE;

}
