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
import twilightforest.TFSounds;
import twilightforest.entity.projectile.EntityTFTwilightWandBolt;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTFTwilightWand extends Item {

	protected ItemTFTwilightWand(Properties props) {
		super(props);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (stack.getDamage() == stack.getMaxDamage()) {
			return ActionResult.resultFail(player.getHeldItem(hand));
		} else {
			player.playSound(TFSounds.SCEPTER_PEARL, 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);

			if (!world.isRemote) {
				world.addEntity(new EntityTFTwilightWandBolt(world, player));
				stack.attemptDamageItem(1, random, (ServerPlayerEntity) null);
			}

			return ActionResult.resultSuccess(player.getHeldItem(hand));
		}
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
