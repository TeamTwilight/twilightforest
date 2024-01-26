package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.projectile.TwilightWandBolt;
import twilightforest.init.TFSounds;

import java.util.List;

public class TwilightWandItem extends Item {

	public TwilightWandItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.getDamageValue() == stack.getMaxDamage()) {
			return InteractionResultHolder.fail(player.getItemInHand(hand));
		} else {
			player.playSound(TFSounds.SCEPTER_PEARL.get(), 1.0F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F);

			if (!level.isClientSide()) {
				level.addFreshEntity(new TwilightWandBolt(level, player));
				stack.hurt(1, level.getRandom(), null);
			}

			return InteractionResultHolder.success(player.getItemInHand(hand));
		}
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Component.translatable("item.twilightforest.scepter.desc", stack.getMaxDamage() - stack.getDamageValue()).withStyle(ChatFormatting.GRAY));
	}
}