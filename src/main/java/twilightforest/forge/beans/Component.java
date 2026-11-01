package twilightforest.forge.beans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Classe creee une seule fois au chargement du mod, puis injectee partout ou un champ
 * {@link Autowired} de son type la demande.
 *
 * <p>Remplace l'annotation du meme nom de Beanification, bibliotheque ecrite pour NeoForge et
 * qui ne se charge pas sous Forge. Voir {@link BeanContext}.</p>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
}
