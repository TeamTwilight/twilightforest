package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import twilightforest.client.ISTER;

import java.util.List;
import java.util.function.Consumer;

public class MasonJarItem extends BlockItem {
	public MasonJarItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> components, TooltipFlag flag) {
		super.appendHoverText(stack, context, components, flag);
		ItemContainerContents contents = stack.getComponents().get(DataComponents.CONTAINER);
		if (contents != null) {
			ItemStack storedStack = contents.copyOne();
			if (!storedStack.isEmpty()) components.add(storedStack.getDisplayName().copy().withStyle(ChatFormatting.GRAY));
		}
	}

	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer) {
		consumer.accept(ISTER.CLIENT_ITEM_EXTENSION);
	}
}
