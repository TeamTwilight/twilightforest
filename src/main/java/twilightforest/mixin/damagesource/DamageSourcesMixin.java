package twilightforest.mixin.damagesource;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.asmhooks.DamageSourceHooks;

/**
 * Les degats d'une creature du mod portent son type de degats a elle.
 *
 * <p>Deux methodes, deux signatures — {@code LivingEntity} d'un cote, {@code Player} de l'autre :
 * un mixin ne peut pas viser les deux d'un coup, d'ou les deux crochets.</p>
 */
@Mixin(DamageSources.class)
public abstract class DamageSourcesMixin {

	@Inject(method = "mobAttack", at = @At("RETURN"), cancellable = true)
	private void twilightforest$customMobDamageSource(LivingEntity attacker, CallbackInfoReturnable<DamageSource> cir) {
		cir.setReturnValue(DamageSourceHooks.getCustomDamageSource(cir.getReturnValue(), attacker));
	}

	@Inject(method = "playerAttack", at = @At("RETURN"), cancellable = true)
	private void twilightforest$customPlayerDamageSource(Player attacker, CallbackInfoReturnable<DamageSource> cir) {
		cir.setReturnValue(DamageSourceHooks.getCustomDamageSource(cir.getReturnValue(), attacker));
	}
}
