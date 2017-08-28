package twilightforest.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;

public class ItemTFTripleBow extends ItemTFBowBase {
	public ItemTFTripleBow() {
		this.setCreativeTab(TFItems.creativeTab);
	}

	// Half [VanillaCopy]: copy of modified super to fire three arrows
	// TODO: make it less dirty
	@SuppressWarnings("unused")
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer entityplayer = (EntityPlayer) entityLiving;
			boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
			ItemStack itemstack = this.findAmmo(entityplayer);

			int i = this.getMaxItemUseDuration(stack) - timeLeft;
			i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, (EntityPlayer) entityLiving, i, !itemstack.isEmpty() || flag);
			if (i < 0) return;

			if (!itemstack.isEmpty() || flag) {
				if (itemstack.isEmpty()) {
					itemstack = new ItemStack(Items.ARROW);
				}

				float f = getArrowVelocity(i);

				if ((double) f >= 0.1D) {
					boolean flag1 = entityplayer.capabilities.isCreativeMode || (itemstack.getItem() instanceof ItemArrow ? ((ItemArrow) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer) : false);

					if (!worldIn.isRemote) {
						ItemArrow itemarrow = (ItemArrow) ((ItemArrow) (itemstack.getItem() instanceof ItemArrow ? itemstack.getItem() : Items.ARROW));
						EntityArrow entityarrow = getArrow(worldIn, itemstack, entityplayer); // TF: use own entity creator
						entityarrow.setAim(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

						// other arrows with slight deviation
						EntityArrow entityarrow1 = new EntityTippedArrow(worldIn, entityLiving);
						entityarrow1.setAim(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0, f * 2, 1);
						entityarrow1.motionY += 0.007499999832361937D * 20F;
						entityarrow1.posY += 0.025F;
						EntityArrow entityarrow2 = new EntityTippedArrow(worldIn, entityLiving);
						entityarrow2.setAim(entityLiving, entityLiving.rotationPitch, entityLiving.rotationYaw, 0, f * 2, 1);
						entityarrow2.motionY -= 0.007499999832361937D * 20F;
						entityarrow2.posY -= 0.025F;

						if (f == 1.0F) {
							entityarrow.setIsCritical(true);
							entityarrow1.setIsCritical(true);
							entityarrow2.setIsCritical(true);
						}

						int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

						if (j > 0) {
							entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
							entityarrow1.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
							entityarrow2.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
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

						stack.damageItem(1, entityplayer);

						if (flag1) {
							entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
						}

						worldIn.spawnEntity(entityarrow);
						worldIn.spawnEntity(entityarrow1);
						worldIn.spawnEntity(entityarrow2);
					}

					worldIn.playSound((EntityPlayer) null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

					if (!flag1) {
						itemstack.shrink(1);

						if (itemstack.isEmpty()) {
							entityplayer.inventory.deleteStack(itemstack);
						}
					}

					entityplayer.addStat(StatList.getObjectUseStats(this));
				}
			}
		}
	}
}
