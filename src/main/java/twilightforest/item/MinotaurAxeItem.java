package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MinotaurAxeItem extends AxeItem {

	public MinotaurAxeItem(Tier material, Properties properties) {
		super(material, 6F, material.getSpeed() * 0.05f - 3.4f, properties);
	}

	@Override
	public int getEnchantmentValue() {
		return Tiers.GOLD.getEnchantmentValue();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flags) {
		super.appendHoverText(stack, level, tooltip, flags);
		tooltip.add(Component.translatable("item.twilightforest.minotaur_axe.desc").withStyle(ChatFormatting.GRAY));
	}
}