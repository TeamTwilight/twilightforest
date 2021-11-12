package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
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
	public boolean showDurabilityBar(ItemStack stack) {
		return false;
	}

	@Override
	public Rarity getRarity(ItemStack stack) {
		return PotionUtils.getMobEffects(stack).isEmpty() ? Rarity.UNCOMMON : Rarity.RARE;
	}

	//no break
	@Override
	public boolean canBreak() {
		return false;
	}

	@Override
	public boolean canBeRefilled(ItemStack stack) {
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
		tooltip.add(new TranslatableComponent("item.twilightforest.flask_doses", stack.getOrCreateTag().getInt("Uses"), 4).withStyle(ChatFormatting.GRAY));
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if(allowdedIn(tab)) {
			ItemStack stack = new ItemStack(this);
			stack.getOrCreateTag().putInt("Uses", 0);
			items.add(stack);
		}
	}
}
