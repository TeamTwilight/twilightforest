package twilightforest.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import twilightforest.entity.EntityTFLoyalZombie;

import javax.annotation.Nonnull;
import java.util.List;


public class ItemTFZombieWand extends ItemTF {

	protected ItemTFZombieWand() {
		this.maxStackSize = 1;
		this.setMaxDamage(9);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
		player.setActiveHand(hand);

		if (!world.isRemote) {
			// what block is the player pointing at?
			RayTraceResult mop = getPlayerPointVec(world, player, 20.0F);

			if (mop != null && mop.hitVec != null) {
				EntityTFLoyalZombie zombie = new EntityTFLoyalZombie(world);
				zombie.setPositionAndRotation(mop.hitVec.xCoord, mop.hitVec.yCoord, mop.hitVec.zCoord, 1.0F, 1.0F);
				zombie.setTamed(true);
				zombie.setOwnerId(player.getUniqueID());
				zombie.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 1200, 1));
				world.spawnEntity(zombie);

				player.getHeldItem(hand).damageItem(1, player);
			}
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
	}

	/**
	 * What block is the player pointing the wand at?
	 * <p>
	 * This very similar to player.rayTrace, but that method is not available on the server.
	 *
	 * @return
	 */
	private RayTraceResult getPlayerPointVec(World world, EntityPlayer player, float range) {
		Vec3d position = new Vec3d(player.posX, player.posY + player.getEyeHeight(), player.posZ);
		Vec3d look = player.getLook(1.0F);
		Vec3d dest = position.addVector(look.xCoord * range, look.yCoord * range, look.zCoord * range);
		return world.rayTraceBlocks(position, dest);
	}


	@Override
	public int getMaxItemUseDuration(ItemStack par1ItemStack) {
		return 30;
	}

	@Nonnull
	@Override
	public EnumAction getItemUseAction(ItemStack par1ItemStack) {
		return EnumAction.BOW;
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean advanced) {
		list.add((stack.getMaxDamage() - stack.getItemDamage()) + " charges left");
	}
}
