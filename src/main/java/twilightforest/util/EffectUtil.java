package twilightforest.util;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

public final class EffectUtil {
	private EffectUtil() {
	}

	/**
	 * Keeps a hidden "while equipped/held" effect applied without re-adding it every tick.
	 * <p>
	 * Calling {@code addEffect} each tick with a 2-5 tick duration always counts as an upgrade (the old instance has
	 * already ticked down), so vanilla treats every call as a forced effect update: an effect packet to the player, an
	 * EFFECTS_CHANGED advancement check, and the effect's attribute modifiers removed and re-added, which in turn sends
	 * an attribute update to the player and everyone tracking them - every tick, per effect.
	 * <p>
	 * Instead the effect is given {@code duration} ticks and only topped up once it falls to {@code refreshAt} or below
	 * (the beacon/conduit pattern), so updates happen roughly every {@code duration - refreshAt} ticks. The trade-off is
	 * that the effect may linger for up to {@code duration} ticks after the source is removed.
	 */
	public static void maintainHiddenEffect(LivingEntity entity, Holder<MobEffect> effect, int amplifier, int duration, int refreshAt) {
		MobEffectInstance existing = entity.getEffect(effect);
		if (existing != null && (existing.getAmplifier() > amplifier
			|| (existing.getAmplifier() == amplifier && (existing.isInfiniteDuration() || existing.getDuration() > refreshAt))))
			return;
		entity.addEffect(new MobEffectInstance(effect, duration, amplifier, false, false, false));
	}

	/**
	 * Whether the entity's current instance of {@code effect} looks like one applied by {@link #maintainHiddenEffect}
	 * (hidden, no icon, not ambient, short), as opposed to a potion or beacon that should be left alone.
	 */
	public static boolean isMaintainedHiddenEffect(MobEffectInstance instance, int maxDuration) {
		return !instance.isVisible() && !instance.showIcon() && !instance.isAmbient() && !instance.isInfiniteDuration() && instance.getDuration() <= maxDuration;
	}
}
