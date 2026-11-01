package twilightforest.mixin.client;

import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.asmhooks.MultipartHooks;

import java.util.Iterator;

/**
 * Fait entrer les morceaux des grosses creatures dans la liste des entites a dessiner.
 *
 * <p>Une hydre n'est qu'une entite pour le jeu : ses tetes et ses cous n'existent pas dans la
 * liste du monde. Le crochet les ajoute au moment ou le rendu parcourt cette liste.</p>
 *
 * <p>Verifie dans le jar du jeu : {@code renderLevel} n'appelle {@code Iterable.iterator()} qu'une
 * seule fois, juste apres {@code entitiesForRendering()}. Le detournement ne peut donc pas se
 * tromper de cible.</p>
 */
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {

	@SuppressWarnings("unchecked")
	@Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Ljava/lang/Iterable;iterator()Ljava/util/Iterator;"))
	private Iterator<Entity> twilightforest$resolveEntitiesForRendering(Iterable<Entity> entities) {
		return MultipartHooks.resolveEntitiesForRendering(entities.iterator());
	}
}
