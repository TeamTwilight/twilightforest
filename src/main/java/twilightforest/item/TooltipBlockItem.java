package twilightforest.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import twilightforest.block.TooltipBlock;

import java.util.function.Consumer;

//For creating basic BlockItem where the block has a tooltip.
//If the block has its own BlockItem, use that instead.
public class TooltipBlockItem extends BlockItem {
	public TooltipBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	@Deprecated
	public void appendHoverText(ItemStack itemStack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, context, display, builder, tooltipFlag);

		if (this.getBlock() instanceof TooltipBlock tooltipBlock) {
			tooltipBlock.addTooltip(itemStack, context, display, builder, tooltipFlag);
		}
	}
}
