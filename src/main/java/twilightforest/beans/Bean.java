package twilightforest.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated methods will be automatically registered with {@link TFBeanContext#register(Class, Object)}.<br/>
 * If a value is set then {@link TFBeanContext#register(Class, String, Object)} is used instead.<p/>
 *
 * The returned object class can extend/implement the method return type. The method return type is what's used for bean identification.<br/>
 * This for example allows for interfaces to be used as beans.<p/>
 *
 * NOT CURRENTLY IMPLEMENTED!
 */
@Deprecated
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Bean {

	String value() default Component.DEFAULT_VALUE;

}
