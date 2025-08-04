package twilightforest.asmhooks;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFDataComponents;

@SuppressWarnings("unused")
public class ItemHooks {

	/**
	 * {@link twilightforest.asm.transformers.book.ModifyWrittenBookNameTransformer}<p/>
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.world.item.WrittenBookItem#getName(net.minecraft.world.item.ItemStack)}
	 */
	public static Component modifyWrittenBookName(Component component, ItemStack stack) {
		if (stack.has(TFDataComponents.TRANSLATABLE_BOOK)) {
			return Component.translatable(component.getString());
		} else return component;
	}
}
