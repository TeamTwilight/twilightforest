package twilightforest.enchantment;

import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import twilightforest.init.TFArmorMaterials;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFMobEffects;

public class ChillAuraEnchantment extends LootOnlyEnchantment {
	public ChillAuraEnchantment(int weight) {
		super(ItemTags.ARMOR_ENCHANTABLE, weight, 3, Enchantment.dynamicCost(5, 9), Enchantment.dynamicCost(20, 9), 3, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (stack.getItem() instanceof ArmorItem armorItem) {
			ArmorMaterial material = armorItem.getMaterial().value();
			return material != TFArmorMaterials.FIERY.get();
		}
		return super.canEnchant(stack);
	}

	@Override
	public void doPostHurt(LivingEntity user, Entity attacker, int level) {
		if (attacker instanceof LivingEntity entity) {
			doChillAuraEffect(entity, 200, level - 1, this.shouldHit(level, user.getRandom()));
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

	private boolean shouldHit(int level, RandomSource pRnd) {
		if (level <= 0) {
			return false;
		} else {
			return pRnd.nextFloat() < 0.15F * (float) level;
		}
	}

	@Override
	protected boolean checkCompatibility(Enchantment other) {
		return super.checkCompatibility(other) && other != TFEnchantments.FIRE_REACT.get() && other != Enchantments.THORNS;
	}
}
