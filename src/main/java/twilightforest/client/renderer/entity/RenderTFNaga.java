package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFNaga;
import twilightforest.entity.boss.EntityTFNaga;

public class RenderTFNaga<M extends ModelTFNaga<EntityTFNaga>> extends MobRenderer<EntityTFNaga, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("nagahead.png");

	public RenderTFNaga(EntityRendererManager manager, M modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
		this.addLayer(new NagaEyelidsLayer(this));
	}

	@Override
	public void render(EntityTFNaga entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
		if (!Minecraft.getInstance().isGamePaused() && entity.isDazed()) {
			Vector3d pos = new Vector3d(entity.getPosX(), entity.getPosY() + 3.15D, entity.getPosZ()).add(new Vector3d(1.5D, 0, 0).rotateYaw((float) Math.toRadians(entity.getRNG().nextInt(360))));
			Minecraft.getInstance().world.addParticle(ParticleTypes.CRIT, pos.x, pos.y, pos.z, 0, 0, 0);
		}
	}

	@Override
	protected void preRenderCallback(EntityTFNaga entity, MatrixStack stack, float p_225620_3_) {
		super.preRenderCallback(entity, stack, p_225620_3_);
		//make size adjustment
		stack.translate(0.0F, 1.75F, 0.0F);
		stack.scale(2.0F, 2.0F, 2.0F);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFNaga entity) {
		return textureLoc;
	}
}
