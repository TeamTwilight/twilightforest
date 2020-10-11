package twilightforest.client.model.item;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityItemStackRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFSpikeBlock;
import twilightforest.item.TFItems;

public class TFChainBlockStackRender extends TileEntityItemStackRenderer {

	private final ModelTFSpikeBlock spikeBlockModel = new ModelTFSpikeBlock();
	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("blockgoblin.png");

	public void renderByItem(ItemStack itemStackIn, float partialTicks) {
		Item item = itemStackIn.getItem();

		if (item == TFItems.block_and_chain) {
			Minecraft.getMinecraft().getTextureManager().bindTexture(textureLoc);
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F, -1.0F, -1.0F);
			GlStateManager.scale(1.8F, 1.8F, 1.8F);
			GlStateManager.translate(0.55F, 0.05F, 0.0F);
			this.spikeBlockModel.render(0.0625f);
			GlStateManager.popMatrix();
		}
	}
}
