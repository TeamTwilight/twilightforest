package twilightforest.client.model.entity;

import com.google.common.collect.Iterables;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

	private final float OG_SIN = Mth.sin(this.attackTime * Mth.PI);
	private final float ATTACK_DELTA_SQUARED = Mth.square(1.0F - this.attackTime);
	private final float OTHER_SIN = Mth.sin((1.0F - ATTACK_DELTA_SQUARED) * Mth.PI);

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

	private void initializeArmsAnim() {
		this.leftArm.xRot = 0.0F;
		this.leftArm.yRot = 0.0F;

		this.rightArm.xRot = 0.0F;
		this.rightArm.yRot = 0.0F;
	}

	private void setupLeftHandNotDyingAnim(float ageInTicks) {
		this.leftArm.xRot = -Mth.PI;
		this.leftArm.xRot -= OG_SIN * 1.2F - OTHER_SIN * 0.4F;
		this.leftArm.xRot -= Mth.sin(ageInTicks * 0.167F) * 0.15F;

		this.leftArm.yRot = 0.1F - OG_SIN * 0.6F;

		this.leftArm.zRot = 0.5F;
		this.leftArm.zRot -= Mth.cos(ageInTicks * 0.26F) * 0.15F + 0.05F;
	}

	private void setupMainHandNonEmptyAnim(float ageInTicks) {
		this.rightArm.xRot = -Mth.HALF_PI;
		this.rightArm.xRot -= OG_SIN * 1.2F - OTHER_SIN * 0.4F;
		this.rightArm.xRot += Mth.sin(ageInTicks * 0.167F) * 0.15F;

		this.rightArm.yRot = -(0.1F - OG_SIN * 0.6F);

		this.rightArm.zRot = 0.0F;
		this.rightArm.zRot += Mth.cos(ageInTicks * 0.26F) * 0.15F + 0.05F;
	}

	private void setSkipDrawAnim(boolean flag) {
		this.body.skipDraw = flag;

		this.leftArm.skipDraw = flag;
		this.rightArm.skipDraw = flag;

		this.leftLeg.skipDraw = flag;
		this.rightLeg.skipDraw = flag;

		this.cloak.skipDraw = flag;
		this.collar.skipDraw = flag;
		this.head.skipDraw = flag;
	}

	private void setupHandsAnim(T entity, float ageInTicks) {
		initializeArmsAnim();

		if (entity.tickCount > 0 && !entity.isDeadOrDying()) {
			setupLeftHandNotDyingAnim(ageInTicks);
		}

		if (!entity.getMainHandItem().isEmpty()) {
			setupMainHandNonEmptyAnim(ageInTicks);
		}
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.shadowClone = entity.isShadowClone();
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		setupHandsAnim(entity, ageInTicks);

		setSkipDrawAnim(entity.deathTime > 50);
	}

	@Override
	public void translateToHand(HumanoidArm arm, PoseStack stack) {
		final float offset = (arm == HumanoidArm.RIGHT ? 1.0F : -1.0F);
		ModelPart modelpart = this.getArm(arm);
		modelpart.x += offset;
		modelpart.translateAndRotate(stack);
		modelpart.x -= offset;
	}

	@Override
	public void setupRotationsForTrophy(float x, float y, float z, float mouthAngle) {
		final float X_ROTATION_ANGLE = z * Mth.DEG_TO_RAD;
		final float Y_ROTATION_ANGLE = y * Mth.DEG_TO_RAD;

		this.head.xRot = X_ROTATION_ANGLE;
		this.head.yRot = Y_ROTATION_ANGLE;

		this.hat.xRot = X_ROTATION_ANGLE;
		this.hat.yRot = Y_ROTATION_ANGLE;
	}

	@Override
	public void renderTrophy(PoseStack stack, MultiBufferSource buffer, int light, int overlay, int color, ItemDisplayContext context) {
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(LichRenderer.TEXTURE));
		this.head.render(stack, consumer, light, overlay, color);
		this.hat.render(stack, consumer, light, overlay, color);
	}
}
