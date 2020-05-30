package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.IEntityRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFNaga;
import twilightforest.entity.boss.EntityTFNaga;

@OnlyIn(Dist.CLIENT)
public class NagaEyelidsLayer<T extends EntityTFNaga, M extends ModelTFNaga<T>> extends LayerRenderer<T, M> {
	private static final ResourceLocation textureLocDazed = TwilightForestMod.getModelTexture("nagahead_dazed.png");

	public NagaEyelidsLayer(IEntityRenderer<T, M> renderer) {
		super(renderer);
	}

	@Override
	public void render(MatrixStack stack, IRenderTypeBuffer buffer, int i, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		if(entitylivingbaseIn.isDazed()) {
			IVertexBuilder vertex = buffer.getBuffer(RenderType.getEntityCutoutNoCull(textureLocDazed));
			this.getEntityModel().render(stack, vertex, i, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}
