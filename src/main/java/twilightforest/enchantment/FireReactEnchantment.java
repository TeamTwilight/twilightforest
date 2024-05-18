package twilightforest.enchantment;

import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import twilightforest.init.TFArmorMaterials;
import twilightforest.init.TFEnchantments;

import javax.annotation.Nullable;

public class FireReactEnchantment extends LootOnlyEnchantment {
	public FireReactEnchantment(int weight) {
		super(ItemTags.ARMOR_ENCHANTABLE, weight, 3, Enchantment.dynamicCost(5, 9), Enchantment.dynamicCost(20, 9), 2, EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET);
	}

	@Override
	public boolean canEnchant(ItemStack stack) {
		if (stack.getItem() instanceof ArmorItem armorItem) {
			ArmorMaterial material = armorItem.getMaterial().value();
			return material != TFArmorMaterials.YETI.get();
		}
		return super.canEnchant(stack);
	}

	@Override
	public void doPostHurt(LivingEntity user, @Nullable Entity attacker, int level) {
		RandomSource random = user.getRandom();
		if (attacker != null && shouldHit(level, random, attacker)) {
			attacker.igniteForSeconds(2 + (random.nextInt(level) * 3));
		}
	}

	public static boolean shouldHit(int level, RandomSource random, Entity attacker) {
		if (level <= 0 || attacker.fireImmune()) {
			return false;
		} else {
			return random.nextFloat() < 0.15F * (float) level;
		}
	}

	@Override
	protected boolean checkCompatibility(Enchantment other) {
		return super.checkCompatibility(other) && other != TFEnchantments.CHILL_AURA.get() && other != Enchantments.THORNS;
	}
}
