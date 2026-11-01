package twilightforest.mixin.enums;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageEffects;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import twilightforest.init.TFSounds;

import java.util.Arrays;

/**
 * Ajoute l'effet de degats {@code twilightforest:pinch} (la pince du scarabee pinceur).
 *
 * <p>Sous NeoForge, c'est le fichier {@code enumextensions.json} qui l'ajoutait ; Forge 1.21.1 ne
 * le lit pas, et {@code DamageEffects} n'est pas extensible chez lui. Or le type de degats
 * {@code clamped.json} designe cet effet par son nom : sans la constante, le chargement du monde
 * echoue sur le codec.</p>
 *
 * <p>La constante est ajoutee dans l'initialiseur statique, juste apres l'affectation de
 * {@code $VALUES} et avant la construction de {@code CODEC} : le codec la connait donc des le
 * depart.</p>
 */
@Mixin(DamageEffects.class)
public abstract class DamageEffectsMixin {

	private static final String PINCH_ID = "twilightforest:pinch";

	@Shadow(remap = false)
	@Final
	@Mutable
	private static DamageEffects[] $VALUES;

	@Inject(method = "<clinit>", remap = false, at = @At(value = "FIELD", opcode = Opcodes.PUTSTATIC, shift = At.Shift.AFTER, remap = false,
		target = "Lnet/minecraft/world/damagesource/DamageEffects;$VALUES:[Lnet/minecraft/world/damagesource/DamageEffects;"))
	private static void twilightforest$addPinch(CallbackInfo ci) {
		DamageEffects[] values = $VALUES;
		// Le son du scarabee n'est pas encore enregistre a ce stade : un son vanilla tient la place,
		// et sound() ci-dessous rend le vrai.
		DamageEffects pinch = DamageEffectsInvoker.twilightforest$create("TWILIGHTFOREST_PINCH", values.length, PINCH_ID, SoundEvents.PLAYER_HURT);
		DamageEffects[] extended = Arrays.copyOf(values, values.length + 1);
		extended[values.length] = pinch;
		$VALUES = extended;
	}

	@Inject(method = "sound", at = @At("HEAD"), cancellable = true)
	private void twilightforest$pinchSound(CallbackInfoReturnable<SoundEvent> cir) {
		if (PINCH_ID.equals(((DamageEffects) (Object) this).getSerializedName())) {
			cir.setReturnValue(TFSounds.PINCH_BEETLE_ATTACK.get());
		}
	}
}
