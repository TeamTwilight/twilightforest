package twilightforest.item.effects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;
import twilightforest.init.TFConsumeEffects;

import java.util.ArrayList;
import java.util.List;

public class StackableEffectConsumeEffect implements ConsumeEffect {

	public static final MapCodec<StackableEffectConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
			StackableEffectInstance.CODEC.listOf().fieldOf("effects").forGetter(o -> o.effects))
		.apply(i, StackableEffectConsumeEffect::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, StackableEffectConsumeEffect> STREAM_CODEC = StreamCodec.composite(
		StackableEffectInstance.STREAM_CODEC.apply(ByteBufCodecs.list()), o -> o.effects,
		StackableEffectConsumeEffect::new
	);

	private final List<StackableEffectInstance> effects;

	private StackableEffectConsumeEffect(List<StackableEffectInstance> effects) {
		this.effects = effects;
	}

	public static Builder builder() {
		return new StackableEffectConsumeEffect.Builder();
	}

	@Override
	public Type<? extends ConsumeEffect> getType() {
		return TFConsumeEffects.STACKABLE_EFFECTS.get();
	}

	@Override
	public boolean apply(Level level, ItemStack stack, LivingEntity user) {
		boolean anyApplied = false;
		for (StackableEffectInstance effect : this.effects) {
			if (effect.chanceToApply() >= user.getRandom().nextFloat()) {
				if (this.applyOrStackEffect(effect, user)) {
					anyApplied = true;
				}
			}
		}
		return anyApplied;
	}

	private boolean applyOrStackEffect(StackableEffectInstance effect, LivingEntity user) {
		int currentDuration = 0;
		MobEffectInstance activeEffect = user.getEffect(effect.effect());
		if (activeEffect != null) {
			currentDuration = activeEffect.getDuration();
		}
		return user.addEffect(new MobEffectInstance(effect.effect(), currentDuration + effect.extraDurationTicks(), effect.amplifier()));
	}

	public static class Builder {
		private final List<StackableEffectInstance> effects = new ArrayList<>();

		public Builder addEffect(Holder<MobEffect> effect, int extraDurationSeconds) {
			this.effects.add(new StackableEffectInstance(effect, extraDurationSeconds * 20, 0, 1.0F));
			return this;
		}

		public Builder addEffect(Holder<MobEffect> effect, int extraDurationSeconds, int amplifier) {
			this.effects.add(new StackableEffectInstance(effect, extraDurationSeconds * 20, amplifier, 1.0F));
			return this;
		}

		public Builder addEffect(Holder<MobEffect> effect, int extraDurationSeconds, float chance) {
			this.effects.add(new StackableEffectInstance(effect, extraDurationSeconds * 20, 0, chance));
			return this;
		}

		public Builder addEffect(Holder<MobEffect> effect, int extraDurationSeconds, int amplifier, float chance) {
			this.effects.add(new StackableEffectInstance(effect, extraDurationSeconds * 20, 0, chance));
			return this;
		}

		public StackableEffectConsumeEffect build() {
			return new StackableEffectConsumeEffect(this.effects);
		}
	}

	private record StackableEffectInstance(Holder<MobEffect> effect, int extraDurationTicks, int amplifier, float chanceToApply) {

		private static final Codec<StackableEffectInstance> CODEC = RecordCodecBuilder.create(i -> i.group(
				MobEffect.CODEC.fieldOf("id").forGetter(StackableEffectInstance::effect),
				Codec.INT.fieldOf("extra_duration").forGetter(StackableEffectInstance::extraDurationTicks),
				Codec.INT.fieldOf("amplifier").forGetter(StackableEffectInstance::amplifier),
				Codec.FLOAT.fieldOf("apply_chance").forGetter(StackableEffectInstance::chanceToApply))
			.apply(i, StackableEffectInstance::new));

		private static final StreamCodec<RegistryFriendlyByteBuf, StackableEffectInstance> STREAM_CODEC = StreamCodec.composite(
			MobEffect.STREAM_CODEC, StackableEffectInstance::effect,
			ByteBufCodecs.INT, StackableEffectInstance::extraDurationTicks,
			ByteBufCodecs.INT, StackableEffectInstance::amplifier,
			ByteBufCodecs.FLOAT, StackableEffectInstance::chanceToApply,
			StackableEffectInstance::new
		);
	}
}
