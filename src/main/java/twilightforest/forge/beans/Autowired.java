package twilightforest.forge.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Champ rempli avec le {@link Component} de son type.
 *
 * <p>Un champ statique est rempli au chargement du mod, quelle que soit la classe qui le porte.
 * Un champ d'instance l'est dans les composants eux-memes, et dans les classes
 * {@link Configurable} qui appellent {@link BeanContext#inject(Object)}.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Autowired {
}
