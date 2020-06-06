package twilightforest.client.renderer.entity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.Entity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import twilightforest.TwilightForestMod;
import twilightforest.client.model.entity.ModelTFNaga;
import twilightforest.entity.boss.EntityTFNaga;

public class RenderTFNaga<T extends EntityTFNaga, M extends ModelTFNaga<T>> extends MobRenderer<T, M> {

	private static final ResourceLocation textureLoc = TwilightForestMod.getModelTexture("nagahead.png");
	private static final ResourceLocation part_TextureLoc = TwilightForestMod.getModelTexture("nagasegment.png");
	private final Model segmentModel = new ModelTFNaga<>();

	public RenderTFNaga(EntityRendererManager manager, M modelbase, float shadowSize) {
		super(manager, modelbase, shadowSize);
		this.addLayer(new NagaEyelidsLayer(this));
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		super.render(entity, entityYaw, partialTicks, stack, buffer, light);
		if (!Minecraft.getInstance().isGamePaused() && entity.isDazed()) {
			Vec3d pos = new Vec3d(entity.getX(), entity.getY() + 3.15D, entity.getZ()).add(new Vec3d(1.5D, 0, 0).rotateYaw((float) Math.toRadians(entity.getRNG().nextInt(360))));
			Minecraft.getInstance().world.addParticle(ParticleTypes.CRIT, pos.x, pos.y, pos.z, 0, 0, 0);
		}

		for (Entity partEntity : entity.getParts()) {
			if (!partEntity.isInvisible()) {
				renderSegment(entity, partEntity, entityYaw, partialTicks, stack, buffer, light);
			}
		}
	}

	@Override
	protected void scale(T entity, MatrixStack stack, float p_225620_3_) {
		super.scale(entity, stack, p_225620_3_);
		//make size adjustment
		stack.translate(0.0F, 1.75F, 0.0F);
		stack.scale(2.0F, 2.0F, 2.0F);
	}

	private void renderSegment(T entity, Entity segment, float yaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer buffer, int light) {
		if (segment != null) {
			double segmentInX = (segment.getX() - entity.getX());
			double segmentInY = (segment.getY() - entity.getY());
			double segmentInZ = (segment.getZ() - entity.getZ());

			stack.push();
			IVertexBuilder ivertexbuilder = buffer.getBuffer(this.segmentModel.getLayer(part_TextureLoc));

			stack.translate(segmentInX, segmentInY, segmentInZ);
			float yawDiff = entity.rotationYaw - entity.prevRotationYaw;
			if (yawDiff > 180) {
				yawDiff -= 360;
			} else if (yawDiff < -180) {
				yawDiff += 360;
			}
			float yaw2 = entity.prevRotationYaw + yawDiff * partialTicks;

			stack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(yaw2));
			stack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(entity.rotationPitch));

			stack.scale(-1.0F, -1.0F, 1.0F);
			stack.scale(2.0F, 2.0F, 2.0F);
			this.segmentModel.render(stack, ivertexbuilder, light, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
			stack.pop();

			//when you allowed debugBoundingBox, you can see Hitbox
			if (this.renderManager.isDebugBoundingBox() && !segment.isInvisible() && !Minecraft.getInstance().isReducedDebug()) {
				stack.push();
				stack.translate(segmentInX, segmentInY, segmentInZ);
				this.renderMultiBoundingBox(stack, buffer.getBuffer(RenderType.getLines()), segment, 0.25F, 1.0F, 0.0F);
				stack.pop();
			}
		}
	}

	private void renderMultiBoundingBox(MatrixStack stack, IVertexBuilder builder, Entity entity, float p_229094_4_, float p_229094_5_, float p_229094_6_) {
		AxisAlignedBB axisalignedbb = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ());
		WorldRenderer.drawBox(stack, builder, axisalignedbb, p_229094_4_, p_229094_5_, p_229094_6_, 1.0F);
	}

	@Override
	public ResourceLocation getEntityTexture(EntityTFNaga entity) {
		return textureLoc;
	}
}
