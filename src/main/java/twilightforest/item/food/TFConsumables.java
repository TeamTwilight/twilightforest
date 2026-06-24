package twilightforest.item.food;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.component.Consumable;
import net.minecraft.world.item.component.Consumables;
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect;
import twilightforest.item.effects.StackableEffectConsumeEffect;

public class TFConsumables extends Consumables {

	public static final Consumable TORCHBERRIES = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.GLOWING, 100), 0.75F)).hasConsumeParticles(false).build();
	public static final Consumable HYDRA_CHOP = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 100))).build();
	public static final Consumable SLIME_DROP = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.SPEED, 600))).build();
	public static final Consumable MAZE_SLIME_DROP = Consumable.builder().onConsume(new ApplyStatusEffectsConsumeEffect(new MobEffectInstance(MobEffects.REGENERATION, 600))).build();

	public static final Consumable BLIGHTBERRY = Consumable.builder().consumeSeconds(0.8F).onConsume(StackableEffectConsumeEffect.builder()
		.addEffect(MobEffects.REGENERATION, 8)
		.addEffect(MobEffects.POISON, 5, 0.75F)
		.addEffect(MobEffects.WITHER, 5, 0.15F)
		.build()).build();

	public static final Consumable DUSKBERRY = Consumable.builder().consumeSeconds(0.8F).onConsume(StackableEffectConsumeEffect.builder()
		.addEffect(MobEffects.NIGHT_VISION, 15)
		.addEffect(MobEffects.BLINDNESS, 3, 0.75F)
		.build()).build();

	public static final Consumable SKYBERRY = Consumable.builder().consumeSeconds(0.8F).onConsume(StackableEffectConsumeEffect.builder()
		.addEffect(MobEffects.JUMP_BOOST, 8)
		.addEffect(MobEffects.SLOWNESS, 3, 0.75F)
		.build()).build();
	public static final Consumable STINGBERRY = Consumable.builder().consumeSeconds(0.8F).onConsume(StackableEffectConsumeEffect.builder()
		.addEffect(MobEffects.STRENGTH, 10)
		.addEffect(MobEffects.MINING_FATIGUE, 10, 0.75F)
		.build()).build();

}
