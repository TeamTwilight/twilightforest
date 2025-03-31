package twilightforest.client.model.entity;

import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.client.renderer.entity.LichRenderer;
import twilightforest.entity.boss.Lich;

import java.util.Arrays;

public class LichModel<T extends Lich> extends HumanoidModel<T> implements TrophyBlockModel {

	private boolean shadowClone;
	private final ModelPart collar;
	private final ModelPart cloak;

	public LichModel(ModelPart root) {
		super(root);
		this.collar = root.getChild("collar");
		this.cloak = root.getChild("cloak");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create()
				.texOffs(32, 0)
				.addBox(-4.0F, -12.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("collar", CubeListBuilder.create()
				.texOffs(32, 16)
				.addBox(-6.0F, -2.0F, -4.0F, 12.0F, 12.0F, 1.0F, new CubeDeformation(-0.1F)),
			PartPose.offsetAndRotation(0.0F, -3.0F, -1.0F, 2.164208F, 0.0F, 0.0F));

		partdefinition.addOrReplaceChild("cloak", CubeListBuilder.create()
				.texOffs(0, 44)
				.addBox(-6.0F, 2.0F, 0.0F, 12.0F, 19.0F, 1.0F),
			PartPose.offset(0.0F, -4.0F, 2.5F));

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(8, 16)
				.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 24.0F, 4.0F),
			PartPose.offset(0.0F, -4.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F),
			PartPose.offset(-5.0F, -2.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
				.texOffs(0, 16)
				.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F),
			PartPose.offset(5.0F, 2.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
			PartPose.offset(-2.0F, 12.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
				.texOffs(0, 16)
				.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
			PartPose.offset(2.0F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, int color) {
		if (!this.shadowClone) {
			super.renderToBuffer(stack, builder, light, overlay, color);
		} else {
			super.renderToBuffer(stack, builder, light, overlay, FastColor.ARGB32.color((int) (FastColor.ARGB32.alpha(color) * 0.5F), (int) (FastColor.ARGB32.red(color) * 0.333F), (int) (FastColor.ARGB32.green(color) * 0.333F), (int) (FastColor.ARGB32.blue(color) * 0.333F)));
		}
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		if (this.shadowClone) {
			return super.bodyParts();
		} else {
			return Iterables.concat(Arrays.asList(this.cloak, this.collar), super.bodyParts());
		}
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		if (entity.isDeadOrDying() && entity.deathTime < Lich.DEATH_ANIMATION_POINT_A) {
			float partialTicks = Minecraft.getInstance().getTimer().getGameTimeDeltaTicks();
			limbSwingAmount = entity.walkAnimation.speed(partialTicks);
			limbSwing = entity.walkAnimation.position(partialTicks);
			limbSwingAmount *= 1.5F;
			if (limbSwingAmount > 1.0F) limbSwingAmount = 1.0F;
		} else limbSwingAmount *= 0.75F;
		this.shadowClone = entity.isShadowClone();
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		if (entity.getPhase() != 3) {
			float ogSin = Mth.sin(this.attackTime * Mth.PI);
			float otherSin = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * Mth.PI);
			if (entity.tickCount > 0 && !entity.isDeadOrDying()) {
				this.leftArm.zRot = 0.5F;
				this.leftArm.yRot = 0.1F - ogSin * 0.6F;
				this.leftArm.xRot = -3.141593F;
				this.leftArm.xRot -= ogSin * 1.2F - otherSin * 0.4F;
				this.leftArm.zRot -= Mth.cos(ageInTicks * 0.26F) * 0.15F + 0.05F;
				this.leftArm.xRot -= Mth.sin(ageInTicks * 0.167F) * 0.15F;
			} else {
				this.leftArm.xRot = 0.0F;
				this.leftArm.yRot = 0.0F;
			}

			if (!entity.getMainHandItem().isEmpty()) {
				this.rightArm.zRot = 0.0F;
				this.rightArm.yRot = -(0.1F - ogSin * 0.6F);
				this.rightArm.xRot = -Mth.HALF_PI;
				this.rightArm.xRot -= ogSin * 1.2F - otherSin * 0.4F;
				this.rightArm.zRot += Mth.cos(ageInTicks * 0.26F) * 0.15F + 0.05F;
				this.rightArm.xRot += Mth.sin(ageInTicks * 0.167F) * 0.15F;
			} else {
				this.rightArm.xRot = 0.0F;
				this.rightArm.yRot = 0.0F;
			}
		} else {
			float f = 1.0F;
			if (entity.getFallFlyingTicks() > 4) {
				f = (float)entity.getDeltaMovement().lengthSqr();
				f /= 0.2F;
				f *= f * f;
			}

			if (f < 1.0F) f = 1.0F;

			this.leftArm.xRot += -Mth.HALF_PI * 0.25F;

			this.rightArm.xRot -= (Mth.cos(limbSwing * 0.6662F + 3.1415927F) * 2.0F * limbSwingAmount * 0.5F / f) * 0.75F;
			this.rightArm.xRot += -Mth.HALF_PI * 0.75F;
		}

		boolean flag = entity.deathTime > Lich.DEATH_ANIMATION_POINT_A;
		this.body.skipDraw = flag;
		this.leftArm.skipDraw = flag;
		this.rightArm.skipDraw = flag;
		this.leftLeg.skipDraw = flag;
		this.rightLeg.skipDraw = flag;
		this.cloak.skipDraw = flag;
		this.collar.skipDraw = flag;
		this.head.skipDraw = flag;
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack stack) {
		float f = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
		ModelPart modelpart = this.getArm(arm);
		modelpart.x += f;
		modelpart.translateAndRotate(stack);
		modelpart.x -= f;
	}

	@Override
	public void setupRotationsForTrophy(float x, float y, float z, float mouthAngle) {
		this.head.yRot = y * Mth.DEG_TO_RAD;
		this.head.xRot = z * Mth.DEG_TO_RAD;
		this.hat.yRot = this.head.yRot;
		this.hat.xRot = this.head.xRot;
	}

	@Override
	public void renderTrophy(PoseStack stack, MultiBufferSource buffer, int light, int overlay, int color, ItemDisplayContext context) {
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(LichRenderer.TEXTURE));
		this.head.render(stack, consumer, light, overlay, color);
		this.hat.render(stack, consumer, light, overlay, color);
	}
}
