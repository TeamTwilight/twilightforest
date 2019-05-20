package twilightforest.item;

import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTFShieldWand extends ItemTF {

	protected ItemTFShieldWand(EnumRarity rarity) {
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

		if (!world.isRemote && player.hasCapability(CapabilityList.SHIELDS, null)) {
			IShieldCapability cap = player.getCapability(CapabilityList.SHIELDS, null);
			if(cap != null)
				cap.replenishShields();
			stack.damageItem(1, player);
		}

		if (!player.isCreative())
			player.getCooldownTracker().setCooldown(this, 1200);

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
