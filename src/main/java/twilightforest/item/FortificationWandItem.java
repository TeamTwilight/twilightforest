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
import twilightforest.init.TFDataAttachments;

import javax.annotation.Nonnull;
import java.util.List;

public class FortificationWandItem extends Item {

	public FortificationWandItem(Properties properties) {
		super(properties);
	}

	@Nonnull
	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, @Nonnull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.getDamageValue() == stack.getMaxDamage()) {
			return InteractionResultHolder.fail(stack);
		}

		if (!level.isClientSide()) {
			player.getData(TFDataAttachments.FORTIFICATION_SHIELDS).replenishShields(player);
			stack.hurt(1, level.getRandom(), null);
		}

		if (!player.isCreative())
			player.getCooldowns().addCooldown(this, 1200);

		return InteractionResultHolder.success(stack);
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
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, level, tooltip, flags);
		tooltip.add(Component.translatable("item.twilightforest.scepter.desc", stack.getMaxDamage() - stack.getDamageValue()).withStyle(ChatFormatting.GRAY));
	}
}