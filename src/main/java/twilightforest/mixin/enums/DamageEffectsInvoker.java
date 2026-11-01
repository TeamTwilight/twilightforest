package twilightforest.mixin.enums;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * Le constructeur prive de {@link DamageEffects}, appele par {@link DamageEffectsMixin}.
 *
 * <p>Un enum ne se construit pas par reflexion ({@code Constructor.newInstance} le refuse) : un
 * invoker de Mixin, lui, emet un vrai {@code new} dans le bytecode et passe.</p>
 */
@Mixin(DamageEffects.class)
public interface DamageEffectsInvoker {

	@Invoker("<init>")
	static DamageEffects twilightforest$create(String enumName, int ordinal, String id, SoundEvent sound) {
		throw new AssertionError();
	}
}
