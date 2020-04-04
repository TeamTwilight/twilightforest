package twilightforest.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.SoundEvents;
import net.minecraft.item.Rarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.entity.projectile.EntityTFTwilightWandBolt;
import twilightforest.entity.TFEntities;

import javax.annotation.Nullable;
import java.util.List;

public class ItemTFTwilightWand extends ItemTF {

	protected ItemTFTwilightWand(Rarity rarity, Properties props) {
		super(rarity, props.maxDamage(99).group(TFItems.creativeTab));
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand) {
		ItemStack stack = player.getHeldItem(hand);

		if (stack.getDamage() == stack.getMaxDamage()) {
			return new ActionResult<>(ActionResultType.FAIL, player.getHeldItem(hand));
		} else {
			player.playSound(SoundEvents.ENTITY_GHAST_SHOOT, 1.0F, (world.rand.nextFloat() - world.rand.nextFloat()) * 0.2F + 1.0F);

			if (!world.isRemote) {
				world.addEntity(new EntityTFTwilightWandBolt(TFEntities.wand_bolt.get(), world, player));
				stack.damageItem(1, player, (user) -> user.sendBreakAnimation(hand));
			}

			return new ActionResult<>(ActionResultType.SUCCESS, player.getHeldItem(hand));
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
