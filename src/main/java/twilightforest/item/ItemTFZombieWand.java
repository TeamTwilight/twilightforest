package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.entity.EntityTFLoyalZombie;
import twilightforest.util.EntityUtil;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTFZombieWand extends ItemTF {

	protected ItemTFZombieWand(EnumRarity rarity) {
		super(rarity);
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
			RayTraceResult ray = EntityUtil.rayTrace(player, 20.0);

			if (ray != null && ray.hitVec != null) {
				EntityTFLoyalZombie zombie = new EntityTFLoyalZombie(world);
				zombie.setPositionAndRotation(ray.hitVec.x, ray.hitVec.y, ray.hitVec.z, 1.0F, 1.0F);
				zombie.setTamed(true);
				zombie.setOwnerId(player.getUniqueID());
				zombie.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, 1200, 1));
				world.spawnEntity(zombie);

				stack.damageItem(1, player);
			}
		}

		return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
	}

	@Override
	public float getXpRepairRatio(ItemStack stack) {
		return 0.1f;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(I18n.format("twilightforest.scepter_charges", stack.getMaxDamage() - stack.getItemDamage()));
	}
}
