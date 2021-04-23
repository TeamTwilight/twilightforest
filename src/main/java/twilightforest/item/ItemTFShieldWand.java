package twilightforest.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.capabilities.CapabilityList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class ItemTFShieldWand extends Item {

	protected ItemTFShieldWand(Properties props) {
		super(props);
	}

	@Nonnull
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, @Nonnull Hand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (stack.getDamage() == stack.getMaxDamage()) {
			return ActionResult.resultFail(stack);
		}

		if (!world.isRemote) {
			player.getCapability(CapabilityList.SHIELDS).ifPresent(cap -> {
				cap.replenishShields();
				stack.attemptDamageItem(1, random, (ServerPlayerEntity) null);
			});
		}

		if (!player.isCreative())
			player.getCooldownTracker().setCooldown(this, 1200);

		return ActionResult.resultSuccess(stack);
	}

	@Override
	public float getXpRepairRatio(ItemStack stack) {
		return 1f;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flags) {
		super.addInformation(stack, world, tooltip, flags);
		tooltip.add(new TranslationTextComponent("twilightforest.scepter_charges", stack.getMaxDamage() - stack.getDamage()));
	}
}
