package twilightforest.item;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import twilightforest.entity.projectile.IceArrow;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class IceBowItem extends BowItem {

	public IceBowItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		AtomicBoolean badEnchant = new AtomicBoolean();
		EnchantmentHelper.getEnchantments(book).forEach((enchantment, integer) -> {
			if (Objects.equals(Enchantments.FLAMING_ARROWS, enchantment)) {
				badEnchant.set(true);
			}
		});

		return !badEnchant.get();
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return !Enchantments.FLAMING_ARROWS.equals(enchantment) && super.canApplyAtEnchantingTable(stack, enchantment);
	}

	@Override
	public AbstractArrow customArrow(AbstractArrow arrow, ItemStack stack) {
		return new IceArrow(arrow.level(), arrow.getOwner(), stack);
	}

	@Override
	public boolean isValidRepairItem(ItemStack toRepair, ItemStack repairWith) {
		return repairWith.getItem() instanceof BlockItem blockItem && blockItem.getBlock().defaultBlockState().is(BlockTags.ICE) || super.isValidRepairItem(toRepair, repairWith);
	}
}