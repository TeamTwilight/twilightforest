package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFHydraMortar;
import twilightforest.entity.boss.EntityTFHydraMortar;

public class RenderTFHydraMortar extends EntityRenderer<EntityTFHydraMortar> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("hydramortar.png");
	private final ModelTFHydraMortar mortarModel = new ModelTFHydraMortar();

	public RenderTFHydraMortar(EntityRendererManager manager) {
		super(manager);
		this.shadowSize = 0.5F;
	}

	@Override
	public void render(EntityTFHydraMortar mortar, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffers, int light) {
		stack.push();
		float blink;
		// [VanillaCopy] TNTRenderer
		if (mortar.fuse - partialTicks + 1.0F < 10.0F) {
			float f = 1.0F - (mortar.fuse - partialTicks + 1.0F) / 10.0F;
			f = MathHelper.clamp(f, 0.0F, 1.0F);
			f = f * f;
			f = f * f;
			float f1 = 1.0F + f * 0.3F;
			stack.scale(f1, f1, f1);
		}

		float alpha = (1.0F - (mortar.fuse - partialTicks + 1.0F) / 100.0F) * 0.8F;

		IVertexBuilder builder = buffers.getBuffer(mortarModel.getRenderType(textureLoc));
		mortarModel.render(stack, builder, light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 0.075F);

		if (mortar.fuse / 5 % 2 == 0) {
			builder = buffers.getBuffer(RenderType.getEntityTranslucent(textureLoc));
			mortarModel.render(stack, builder, light, OverlayTexture.getPackedUV(OverlayTexture.getU(1), 10), 1.0F, 1.0F, 1.0F, alpha);
		}

		stack.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFHydraMortar entity) {
		return textureLoc;
	}
}
