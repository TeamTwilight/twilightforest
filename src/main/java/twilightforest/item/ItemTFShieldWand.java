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
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;

import javax.annotation.Nonnull;
import java.util.List;


public class ItemTFShieldWand extends ItemTF {

	protected ItemTFShieldWand() {
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

	@Nonnull
	@Override
	public EnumRarity getRarity(ItemStack stack) {
		return EnumRarity.RARE;
	}

	@Override
	public void addInformation(ItemStack stack, World world, List<String> list, ITooltipFlag flags) {
		super.addInformation(stack, world, list, flags);
		list.add(I18n.format("twilightforest.scepter_charges", stack.getMaxDamage() - stack.getItemDamage()));
	}
}
