package twilightforest.forge.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Methode d'un {@link Component} appelee une fois tous les composants crees et remplis.
 *
 * <p>Elle ne prend aucun parametre, ou un seul {@code IEventBus} : le bus du mod par defaut, le
 * bus general du jeu avec {@link Bus#GAME}.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface PostConstruct {

	Bus value() default Bus.MOD;

	enum Bus {
		MOD,
		GAME
	}
}
