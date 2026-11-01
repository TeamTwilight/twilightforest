package twilightforest.forge.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classe creee avec {@code new}, hors du contexte, mais qui a besoin de champs {@link Autowired}.
 *
 * <p>Beanification tissait l'injection dans ses constructeurs au moment de la compilation, par un
 * plugin Gradle qui ne s'applique pas ici. L'annotation reste un simple repere : ces classes
 * appellent elles-memes {@link BeanContext#inject(Object)} dans leur constructeur.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Configurable {
}
