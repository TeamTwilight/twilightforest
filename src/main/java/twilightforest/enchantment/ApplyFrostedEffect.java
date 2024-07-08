package twilightforest.enchantment;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import twilightforest.init.TFMobEffects;

public record ApplyFrostedEffect(LevelBasedValue duration, LevelBasedValue amplifier) implements EnchantmentEntityEffect {

	public static final MapCodec<ApplyFrostedEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			LevelBasedValue.CODEC.fieldOf("duration").forGetter(ApplyFrostedEffect::duration),
			LevelBasedValue.CODEC.fieldOf("amplifier").forGetter(ApplyFrostedEffect::amplifier))
		.apply(instance, ApplyFrostedEffect::new));

	@Override
	public void apply(ServerLevel level, int enchantLevel, EnchantedItemInUse item, Entity victim, Vec3 position) {
		if (victim instanceof LivingEntity entity) {
			int duration = Math.round(this.duration.calculate(enchantLevel) * 20.0F);
			int amplifier = Math.max(0, Math.round(this.amplifier.calculate(enchantLevel)));
			doChillAuraEffect(entity, duration, amplifier, true);
		}
	}

	public static void doChillAuraEffect(LivingEntity victim, int duration, int amplifier, boolean shouldHit) {
		if (shouldHit && !victim.getType().is(EntityTypeTags.FREEZE_IMMUNE_ENTITY_TYPES)) {
			if (!victim.getItemBySlot(EquipmentSlot.HEAD).is(ItemTags.FREEZE_IMMUNE_WEARABLES) &&
				!victim.getItemBySlot(EquipmentSlot.CHEST).is(ItemTags.FREEZE_IMMUNE_WEARABLES) &&
				!victim.getItemBySlot(EquipmentSlot.LEGS).is(ItemTags.FREEZE_IMMUNE_WEARABLES) &&
				!victim.getItemBySlot(EquipmentSlot.FEET).is(ItemTags.FREEZE_IMMUNE_WEARABLES)) {
				if (!(victim instanceof Player player) || !player.isCreative()) {
					victim.addEffect(new MobEffectInstance(TFMobEffects.FROSTY, duration, amplifier));
				}
			}
		}
	}

	@Override
	public MapCodec<? extends EnchantmentEntityEffect> codec() {
		return CODEC;
	}
}
