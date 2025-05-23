package twilightforest.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;

public class KeepsakeCasketItem extends BlockItem {
	public KeepsakeCasketItem(Properties properties) {
		super(TFBlocks.KEEPSAKE_CASKET.get(), properties);
	}

	@Override
	public Component getName(ItemStack stack) {
		if (stack.getOrDefault(TFDataComponents.CASKET_DAMAGE, 0) > 0) {
			String damage = stack.get(TFDataComponents.CASKET_DAMAGE) == 1 ? "chipped_" : "damaged_";
			return Component.translatable("block.twilightforest." + damage + "keepsake_casket");
		}
		return super.getName(stack);
	}
}
