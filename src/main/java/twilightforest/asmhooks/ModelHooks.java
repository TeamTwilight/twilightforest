package twilightforest.asmhooks;

import net.minecraft.world.item.ItemStack;
import twilightforest.init.TFBlocks;

@SuppressWarnings({"JavadocReference", "unused"})
public class ModelHooks {

	/**
	 * {@link twilightforest.asm.transformers.model.AnimateSpecialModelsTransformer}
	 *
	 * Injection Point:<br/>
	 * {@link net.minecraft.client.renderer.item.SpecialModelWrapper#update(ItemStackRenderState, ItemStack, ItemModelResolver, ItemDisplayContext, ClientLevel, ItemOwner, int)}<br/>
	 * Targets: {@link net.minecraft.client.renderer.item.ItemStackRenderState#newLayer()}
	 */
	public static boolean shouldSpecialModelUpdate(ItemStack item) {
		return !item.hasFoil() && (item.is(TFBlocks.MOONWORM.asItem()) || item.is(TFBlocks.FIREFLY.asItem()));
	}
}