package twilightforest.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.BowItem;
import net.minecraft.item.Items;
import net.minecraft.stats.Stats;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemTFTripleBow extends BowItem {

	public ItemTFTripleBow(Properties props) {
		super(props);
	}

	// Half [VanillaCopy]: copy of modified super to fire three arrows
	// TODO: make it less dirty
	@SuppressWarnings("unused")
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, LivingEntity entityLiving, int timeLeft) {
		if (entityLiving instanceof PlayerEntity) {
			PlayerEntity entityplayer = (PlayerEntity)entityLiving;
			boolean flag = entityplayer.abilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
			ItemStack itemstack = entityplayer.findAmmo(stack);

			int i = this.getUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !itemstack.isEmpty() || flag);
			if (i < 0) return;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				float f = getArrowVelocity(i);
				if (!(f < 0.1D)) {
					boolean flag1 = entityplayer.abilities.isCreativeMode || (itemstack.getItem() instanceof ArrowItem && ((ArrowItem)itemstack.getItem()).isInfinite(itemstack, stack, entityplayer));
					if (!worldIn.isRemote) {
						ArrowItem arrowitem = (ArrowItem)(itemstack.getItem() instanceof ArrowItem ? itemstack.getItem() : Items.ARROW);
						AbstractArrowEntity entityarrow = arrowitem.createArrow(worldIn, itemstack, entityplayer);
						entityarrow.func_234612_a_(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

						// other arrows with slight deviation
						AbstractArrowEntity entityarrow1 = arrowitem.createArrow(worldIn, itemstack, entityplayer);
						entityarrow1.func_234612_a_(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0, f * 2, 1);
						entityarrow1.setMotion(entityarrow1.getMotion().add(0.0D, 0.0075 * 20F, 0.0D));
						entityarrow1.setPosition(entityarrow1.getPosX(), entityarrow1.getPosY() + 0.025F, entityarrow1.getPosZ());
						entityarrow1.pickupStatus = ArrowEntity.PickupStatus.CREATIVE_ONLY;

						AbstractArrowEntity entityarrow2 = arrowitem.createArrow(worldIn, itemstack, entityplayer);
						entityarrow2.func_234612_a_(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0, f * 2, 1);
						entityarrow2.setMotion(entityarrow2.getMotion().subtract(0.0D, 0.0075 * 20F, 0.0D));
						entityarrow2.setPosition(entityarrow2.getPosX(), entityarrow2.getPosY() + 0.025F, entityarrow2.getPosZ());
						entityarrow2.pickupStatus = ArrowEntity.PickupStatus.CREATIVE_ONLY;

						if (f == 1.0F) {
							entityarrow.setIsCritical(true);
							entityarrow1.setIsCritical(true);
							entityarrow2.setIsCritical(true);
						}

						int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

						if (j > 0) {
							entityarrow.setDamage(entityarrow.getDamage() + j * 0.5D + 0.5D);
							entityarrow1.setDamage(entityarrow.getDamage() + j * 0.5D + 0.5D);
							entityarrow2.setDamage(entityarrow.getDamage() + j * 0.5D + 0.5D);
						}

						int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

						if (k > 0) {
							entityarrow.setKnockbackStrength(k);
							entityarrow1.setKnockbackStrength(k);
							entityarrow2.setKnockbackStrength(k);
						}

						if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
							entityarrow.setFire(100);
							entityarrow1.setFire(100);
							entityarrow2.setFire(100);
						}

						stack.damageItem(1, entityplayer, (user) -> user.sendBreakAnimation(entityplayer.getActiveHand()));

						if (flag1 || entityplayer.abilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
							entityarrow.pickupStatus = AbstractArrowEntity.PickupStatus.CREATIVE_ONLY;
						}

						worldIn.addEntity(entityarrow);
						worldIn.addEntity(entityarrow1);
						worldIn.addEntity(entityarrow2);
					}

					worldIn.playSound((PlayerEntity)null, entityplayer.getPosX(), entityplayer.getPosY(), entityplayer.getPosZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (random.nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					if (!flag1 && !entityplayer.abilities.isCreativeMode) {
						itemstack.shrink(1);
						if (itemstack.isEmpty()) {
							entityplayer.inventory.deleteStack(itemstack);
						}
					}

					entityplayer.addStat(Stats.ITEM_USED.get(this));
				}
			}
		}
	}
}
