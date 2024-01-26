package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GreaterFlaskItem extends BrittleFlaskItem {

	public GreaterFlaskItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateTag().putInt("Uses", 0);
		PotionUtils.setPotion(stack, Potions.EMPTY);
		return stack;
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		return PotionUtils.getMobEffects(stack).isEmpty() ? Rarity.UNCOMMON : Rarity.RARE;
	}

	//no breaking
	@Override
	public boolean canBreak() {
		return false;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		PotionUtils.addPotionTooltip(stack, tooltip, 1.0F, level == null ? 20.0F : level.tickRateManager().tickrate());
		if (stack.getTag() != null)
			tooltip.add(Component.translatable("item.twilightforest.flask.doses", stack.getTag().getInt("Uses"), DOSES).withStyle(ChatFormatting.GRAY));
	}
}