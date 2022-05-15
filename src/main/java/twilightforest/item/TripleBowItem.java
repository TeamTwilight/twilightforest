package twilightforest.item;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

public class TripleBowItem extends BowItem {

	public TripleBowItem(Properties props) {
		super(props);
	}

	// Half [VanillaCopy]: copy of modified super to fire three arrows
	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player player) {
			boolean flag = player.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
			ItemStack itemstack = player.getProjectile(stack);

			int i = this.getUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, player, i, !itemstack.isEmpty() || flag);
			if (i < 0) return;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) itemstack = new ItemStack(Items.ARROW);

				float f = getPowerForTime(i);
				if (f >= 0.1D) {
					boolean flag1 = player.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem arrowItem && arrowItem.isInfinite(itemstack, stack, player));
					if (!worldIn.isClientSide) {
						ArrowItem arrowItem = itemstack.getItem() instanceof ArrowItem arrow ? arrow : (ArrowItem)Items.ARROW;
						for (int j = -1; j < 2; j++) {
							AbstractArrow abstractArrow = arrowItem.createArrow(worldIn, itemstack, player);
							abstractArrow.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, f * (3.0F - Math.abs(j)), 1.0F);
							abstractArrow.setDeltaMovement(abstractArrow.getDeltaMovement().add(0.0D, 0.0075 * 20F * j, 0.0D));

							if (j != 0) {
								abstractArrow.setPos(abstractArrow.getX(), abstractArrow.getY() + 0.025F, abstractArrow.getZ());
								abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
							} else if (flag1 || player.getAbilities().instabuild && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
								abstractArrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
							}

							if (f == 1.0F) abstractArrow.setCritArrow(true);

							int p = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);
							if (p > 0) abstractArrow.setBaseDamage(abstractArrow.getBaseDamage() + j * 0.5D + 0.5D);

							int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);
							if (k > 0) abstractArrow.setKnockback(k);

							if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0)
								abstractArrow.setSecondsOnFire(100);

							worldIn.addFreshEntity(abstractArrow);
						}

						stack.hurtAndBreak(1, player, (user) -> user.broadcastBreakEvent(player.getUsedItemHand()));
					}

					worldIn.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (worldIn.random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!flag1 && !player.getAbilities().instabuild) {
						itemstack.shrink(1);
						if (itemstack.isEmpty()) player.getInventory().removeItem(itemstack);
					}

					player.awardStat(Stats.ITEM_USED.get(this));
				}
			}
		}
	}
}
