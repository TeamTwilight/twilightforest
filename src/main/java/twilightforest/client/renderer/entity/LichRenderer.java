package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.LichModel;
import twilightforest.entity.boss.Lich;

public class LichRenderer extends HumanoidMobRenderer<Lich, LichModel> {

	private static final ResourceLocation LICH_TEXTURE = TwilightForestMod.getModelTexture("twilightlich64.png");

	public LichRenderer(EntityRendererProvider.Context manager, LichModel modelbiped, float shadowSize) {
		super(manager, modelbiped, shadowSize);
		addLayer(new ShieldLayer<>(this));
	}

	@Nullable
	@Override
	protected RenderType getRenderType(Lich lich, boolean bodyVisible, boolean translucent, boolean glowing) {
		if (lich.isShadowClone()) return RenderType.entityTranslucent(this.getTextureLocation(lich));
		else return super.getRenderType(lich, bodyVisible, translucent, glowing);
	}

	@Override
	public void render(Lich lich, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource buffer, int packedLight) {
		if (lich.deathTime > 0) {
			stack.pushPose();
			if (lich.deathTime > 50) {
				stack.translate(0.0D, -1.8D * Math.pow(Math.min(((float) (lich.deathTime - 50) + partialTicks) * 0.05D, 1.0D), 3.0D), 0.0D);
			} else {
				float time = (float)lich.deathTime + partialTicks;
				stack.translate(Math.sin(time * time) * 0.01D, 0.0D, Math.cos(time * time) * 0.01D);
			}
		}
		super.render(lich, entityYaw, partialTicks, stack, buffer, packedLight);
		if (lich.deathTime > 0) stack.popPose();
	}

	@Override
	protected float getFlipDegrees(Lich lich) { //Prevent the body from keeling over
		return lich.isDeadOrDying() ? 0.0F : super.getFlipDegrees(lich);
	}

	@Override
	public ResourceLocation getTextureLocation(Lich lich) {
		return LICH_TEXTURE;
	}
}
