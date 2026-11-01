package twilightforest.mixin.client;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.MultipartHooks;

/**
 * Donne son propre rendu a chaque morceau de creature.
 *
 * <p>Le coremod ne visait que la branche « creature ordinaire » ; ici on prend la sortie de la
 * methode, joueurs compris. Sans effet pour eux : le crochet ne repond que pour les morceaux de
 * creatures du mod, et un joueur n'en est jamais un.</p>
 */
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {

	@Inject(method = "getRenderer", at = @At("RETURN"), cancellable = true)
	private void twilightforest$resolveEntityRenderer(Entity entity, CallbackInfoReturnable<EntityRenderer<?>> cir) {
		cir.setReturnValue(MultipartHooks.resolveEntityRenderer(cir.getReturnValue(), entity));
	}
}
