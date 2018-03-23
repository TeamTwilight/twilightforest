package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
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

		ItemStack stack = player.getHeldItem(hand);

		if (stack.getItemDamage() == stack.getMaxDamage()) {
			return ActionResult.newResult(EnumActionResult.FAIL, stack);
		}

		if (!world.isRemote) {
			// what block is the player pointing at?
			RayTraceResult mop = getPlayerPointVec(world, player, 20.0F);

			if (mop != null && mop.hitVec != null) {
				EntityTFLoyalZombie zombie = new EntityTFLoyalZombie(world);
				zombie.setPositionAndRotation(mop.hitVec.x, mop.hitVec.y, mop.hitVec.z, 1.0F, 1.0F);
				zombie.setTamed(true);
				zombie.setOwnerId(player.getUniqueID());
				zombie.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 1200, 1));
				world.spawnEntity(zombie);

				stack.damageItem(1, player);
			}
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
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
		Vec3d dest = position.addVector(look.x * range, look.y * range, look.z * range);
		return world.rayTraceBlocks(position, dest);
	}

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.RARE;
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flags) {
		super.addInformation(stack, world, list, flags);
		list.add(I18n.format("twilightforest.scepter_charges", stack.getMaxDamage() - stack.getItemDamage()));
	}
}
