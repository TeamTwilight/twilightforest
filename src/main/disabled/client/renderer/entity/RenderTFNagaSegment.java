package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.util.ResourceLocation;
import twilightforest.TwilightForestMod;

public class RenderTFNagaSegment<T extends EntityTFNagaSegment> extends EntityRenderer<T> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("nagasegment.png");
	private final Model model;

	public RenderTFNagaSegment(EntityRendererManager manager, T model) {
		super(manager);
		this.model = model;
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		stack.push();
		//stack.translate((float) x, (float) y, (float) z);
		stack.scale(-1.0F, -1.0F, 1.0F);

		// I forget what this glitch is called, but let's fix it
		float yawDiff = entity.rotationYaw - entity.prevRotationYaw;
		if (yawDiff > 180) {
			yawDiff -= 360;
		} else if (yawDiff < -180) {
			yawDiff += 360;
		}
		float yaw = entity.prevRotationYaw + yawDiff * partialTicks;

		RenderSystem.rotatef(yaw, 0.0F, 1.0F, 0.0F);
		RenderSystem.rotatef(entity.rotationPitch, 1.0F, 0.0F, 0.0F);
		this.bindTexture(textureLoc);
		this.model.render(entity, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		stack.pop();
	}

	@Override
	public ResourceLocation getEntityTexture(T entity) {
		return textureLoc;
	}
}
