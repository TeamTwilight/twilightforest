package twilightforest.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDataComponents;

import java.util.List;

public class KeepsakeCasketItem extends BlockItem {

	private static final List<Component> CASKET_NAMES = List.of(
		Component.translatable("block.twilightforest.keepsake_casket"),
		Component.translatable("block.twilightforest.chipped_keepsake_casket"),
		Component.translatable("block.twilightforest.damaged_keepsake_casket")
	);

	public KeepsakeCasketItem(Properties properties) {
		super(TFBlocks.KEEPSAKE_CASKET.get(), properties);
	}

	@Override
	public Component getName(ItemStack stack) {
		Component ret = super.getName(stack);
		if (ret.getString().isEmpty()) {
			return CASKET_NAMES.get(stack.getOrDefault(TFDataComponents.CASKET_DAMAGE, 0));
		}
		return ret;
	}
}
