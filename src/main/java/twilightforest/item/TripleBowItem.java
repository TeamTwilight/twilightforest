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
	// TODO: make it less dirty
	@SuppressWarnings("unused")
	@Override
	public void releaseUsing(ItemStack stack, Level worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof Player) {
			Player entityplayer = (Player)entityLiving;
			boolean flag = entityplayer.getAbilities().instabuild || EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, stack) > 0;
			ItemStack itemstack = entityplayer.getProjectile(stack);

			int i = this.getUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !itemstack.isEmpty() || flag);
			if (i < 0) return;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				float f = getPowerForTime(i);
				if (!(f < 0.1D)) {
					boolean flag1 = entityplayer.getAbilities().instabuild || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, entityplayer));
					if (!worldIn.isClientSide) {
						ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
						AbstractArrow entityarrow = arrowitem.createArrow(worldIn, itemstack, entityplayer);
						entityarrow.shootFromRotation(entityplayer, entityplayer.getXRot(), entityplayer.getYRot(), 0.0F, f * 3.0F, 1.0F);

						// other arrows with slight deviation
						AbstractArrow entityarrow1 = arrowitem.createArrow(worldIn, itemstack, entityplayer);
						entityarrow1.shootFromRotation(entityLiving, entityLiving.getXRot(), entityLiving.getYRot(), 0, f * 2, 1);
						entityarrow1.setDeltaMovement(entityarrow1.getDeltaMovement().add(0.0D, 0.0075 * 20F, 0.0D));
						entityarrow1.setPos(entityarrow1.getX(), entityarrow1.getY() + 0.025F, entityarrow1.getZ());
						entityarrow1.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

						AbstractArrow entityarrow2 = arrowitem.createArrow(worldIn, itemstack, entityplayer);
						entityarrow2.shootFromRotation(entityLiving, entityLiving.getXRot(), entityLiving.getYRot(), 0, f * 2, 1);
						entityarrow2.setDeltaMovement(entityarrow2.getDeltaMovement().subtract(0.0D, 0.0075 * 20F, 0.0D));
						entityarrow2.setPos(entityarrow2.getX(), entityarrow2.getY() + 0.025F, entityarrow2.getZ());
						entityarrow2.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;

						if (f == 1.0F) {
							entityarrow.setCritArrow(true);
							entityarrow1.setCritArrow(true);
							entityarrow2.setCritArrow(true);
						}

						int j = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, stack);

						if (j > 0) {
							entityarrow.setBaseDamage(entityarrow.getBaseDamage() + j * 0.5D + 0.5D);
							entityarrow1.setBaseDamage(entityarrow.getBaseDamage() + j * 0.5D + 0.5D);
							entityarrow2.setBaseDamage(entityarrow.getBaseDamage() + j * 0.5D + 0.5D);
						}

						int k = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PUNCH_ARROWS, stack);

						if (k > 0) {
							entityarrow.setKnockback(k);
							entityarrow1.setKnockback(k);
							entityarrow2.setKnockback(k);
						}

						if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, stack) > 0) {
							entityarrow.setSecondsOnFire(100);
							entityarrow1.setSecondsOnFire(100);
							entityarrow2.setSecondsOnFire(100);
						}

						stack.hurtAndBreak(1, entityplayer, (user) -> user.broadcastBreakEvent(entityplayer.getUsedItemHand()));

						if (flag1 || entityplayer.getAbilities().instabuild && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
							entityarrow.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
						}

						worldIn.addFreshEntity(entityarrow);
						worldIn.addFreshEntity(entityarrow1);
						worldIn.addFreshEntity(entityarrow2);
					}

					worldIn.playSound((Player)null, entityplayer.getX(), entityplayer.getY(), entityplayer.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (worldIn.random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!flag1 && !entityplayer.getAbilities().instabuild) {
						itemstack.shrink(1);
						if (itemstack.isEmpty()) {
							entityplayer.getInventory().removeItem(itemstack);
						}
					}

					entityplayer.awardStat(Stats.ITEM_USED.get(this));
				}
			}
		}
	}
}
