package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class MinotaurAxeItem extends AxeItem {

	public MinotaurAxeItem(ToolMaterial material, float damage, float speed, Properties properties) {
		super(material, damage, speed, properties);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, context, display, builder, tooltipFlag);
		builder.accept(Component.translatable("item.twilightforest.minotaur_axe.desc").withStyle(ChatFormatting.GRAY));
	}
}