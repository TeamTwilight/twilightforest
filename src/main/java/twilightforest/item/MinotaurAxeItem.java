package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.util.List;

public class MinotaurAxeItem extends AxeItem {

	public MinotaurAxeItem(Tier material, Properties properties) {
		super(material, properties);
	}

	@Override
	public int getEnchantmentValue() {
		return Tiers.GOLD.getEnchantmentValue();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, context, tooltip, flags);
		tooltip.add(Component.translatable("item.twilightforest.minotaur_axe.desc").withStyle(ChatFormatting.GRAY));
	}
}