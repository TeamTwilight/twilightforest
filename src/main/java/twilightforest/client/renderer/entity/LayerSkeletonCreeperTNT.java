package twilightforest.client.renderer.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.entity.EntityTFSkeletonCreeper;

@SideOnly(Side.CLIENT)
public class LayerSkeletonCreeperTNT implements LayerRenderer<EntityTFSkeletonCreeper> {
	private final RenderTFSkeletonCreeper creeperRenderer;

	public LayerSkeletonCreeperTNT(RenderTFSkeletonCreeper creeperRendererIn) {
		this.creeperRenderer = creeperRendererIn;
	}

	@Override
	public void doRenderLayer(EntityTFSkeletonCreeper entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale) {

		GlStateManager.pushMatrix();
		((ModelCreeper) this.creeperRenderer.getMainModel()).body.postRender(0.0625F);
		float f = 0.625F;
		GlStateManager.translate(0.0F, 0.14375F, 0.0F);
		GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
		GlStateManager.scale(0.145F, -0.145F, -0.145F);
		Minecraft.getMinecraft().getItemRenderer().renderItem(entitylivingbaseIn, new ItemStack(Blocks.TNT, 1), ItemCameraTransforms.TransformType.HEAD);
		GlStateManager.popMatrix();

	}

	@Override
	public boolean shouldCombineTextures() {
		return true;
	}
}