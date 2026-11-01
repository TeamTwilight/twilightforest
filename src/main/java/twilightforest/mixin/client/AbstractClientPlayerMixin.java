package twilightforest.mixin.client;

import net.minecraft.client.player.AbstractClientPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.PlayerHooks;

/**
 * Le charme « droit devant » fige le champ de vision.
 *
 * <p>Le jeu calcule le zoom a partir de la vitesse du joueur. Le crochet met cette vitesse de cote
 * a l'entree de la methode et la remet a la sortie, pour que le calcul se fasse comme si le joueur
 * ne bougeait pas.</p>
 */
@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin {

	@Inject(method = "getFieldOfViewModifier", at = @At("HEAD"))
	private void twilightforest$straightAheadNullify(CallbackInfoReturnable<Float> cir) {
		PlayerHooks.straightAheadNullify((AbstractClientPlayer) (Object) this);
	}

	@Inject(method = "getFieldOfViewModifier", at = @At("RETURN"))
	private void twilightforest$straightAheadRestore(CallbackInfoReturnable<Float> cir) {
		PlayerHooks.straightAheadRestore((AbstractClientPlayer) (Object) this);
	}
}
