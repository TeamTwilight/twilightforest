package twilightforest.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MockBean {

	String DEFAULT_VALUE = Component.DEFAULT_VALUE;

	String value() default DEFAULT_VALUE;

}
