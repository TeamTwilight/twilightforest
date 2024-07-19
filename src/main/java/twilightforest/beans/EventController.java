package twilightforest.beans;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotated classes will be automatically registered with {@link TFBeanContext.TFBeanContextRegistrar#register(Class, Object)}.<br/>
 * If a value is set then {@link TFBeanContext.TFBeanContextRegistrar#register(Class, String, Object)} is used instead.<p/>
 *
 * This has the same capabilities as {@link net.neoforged.fml.common.EventBusSubscriber}<br/>
 * The difference is modid is now inferred and methods annotated with {@link net.neoforged.bus.api.SubscribeEvent} are required to be non-static instead of static.<p/>
 *
 * Requires a no-arg constructor.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface EventController {

	String value() default Component.DEFAULT_VALUE;

	/**
	 * @see EventBusSubscriber#value()
	 */
	Dist[] dist() default { Dist.CLIENT, Dist.DEDICATED_SERVER };

	/**
	 * @see EventBusSubscriber#bus()
	 */
	EventBusSubscriber.Bus bus() default EventBusSubscriber.Bus.GAME;

}
