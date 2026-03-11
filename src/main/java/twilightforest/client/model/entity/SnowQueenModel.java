package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AnimationUtils;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.client.JappaPackReloadListener;
import twilightforest.client.renderer.entity.SnowQueenRenderer;
import twilightforest.entity.boss.SnowQueen;
import twilightforest.entity.boss.SnowQueen.Phase;

public class SnowQueenModel extends HumanoidModel<SnowQueen> implements TrophyBlockModel {

	public SnowQueenModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition checkForPack() {
		return JappaPackReloadListener.INSTANCE.isJappaPackLoaded() ? createJappaModel() : create();
	}

	private static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
			PartPose.offset(0.0F, -4.0F, 0.0F));

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		var crown = head.addOrReplaceChild("crown", CubeListBuilder.create(), PartPose.ZERO);

		makeFrontCrown(crown, -1.0F, -4.0F, 10.0F, 0);
		makeFrontCrown(crown, 0.0F, 4.0F, -10.0F, 1);
		makeSideCrown(crown, -1.0F, -4.0F, 10.0F, 0);
		makeSideCrown(crown, 0.0F, 4.0F, -10.0F, 1);

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(32, 0)
				.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 23.0F, 4.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(16, 16)
				.addBox(-2.0F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F),
			PartPose.offset(-5.0F, 2.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.texOffs(16, 16)
				.addBox(-1.0F, -2.0F, -1.3F, 3.0F, 12.0F, 3.0F),
			PartPose.offset(5.0F, 2.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F),
			PartPose.offset(-1.9F, 12.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-1.5F, 0.0F, -1.5F, 3.0F, 12.0F, 3.0F),
			PartPose.offset(1.9F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	private static void makeSideCrown(PartDefinition parent, float spikeDepth, float crownX, float angle, int iteration) {

		var crownSide = parent.addOrReplaceChild("crown_side_" + iteration, CubeListBuilder.create()
				.texOffs(28, 28)
				.addBox(-3.5F, -0.5F, -0.5F, 7.0F, 1.0F, 1.0F),
			PartPose.offsetAndRotation(crownX, -6.0F, 0.0F, 0.0F, Mth.HALF_PI, 0.0F));

		crownSide.addOrReplaceChild("spike_4", CubeListBuilder.create()
				.texOffs(48, 27)
				.addBox(-0.5F, -3.5F, spikeDepth, 1.0F, 4.0F, 1.0F),
			PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, angle * 1.5F * Mth.DEG_TO_RAD, 0.0F, 0.0F));

		crownSide.addOrReplaceChild("spike_3l", CubeListBuilder.create()
				.texOffs(52, 28)
				.addBox(-0.5F, -2.5F, spikeDepth, 1.0F, 3.0F, 1.0F),
			PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, angle * Mth.DEG_TO_RAD, 0.0F, -10.0F * Mth.DEG_TO_RAD));

		crownSide.addOrReplaceChild("spike_3r", CubeListBuilder.create()
				.texOffs(52, 28)
				.addBox(-0.5F, -2.5F, spikeDepth, 1.0F, 3.0F, 1.0F),
			PartPose.offsetAndRotation(2.5F, 0.0F, 0.0F, angle * Mth.DEG_TO_RAD, 0.0F, 10.0F * Mth.DEG_TO_RAD));

	}

	private static void makeFrontCrown(PartDefinition parent, float spikeDepth, float crownZ, float angle, int iteration) {

		var crownFront = parent.addOrReplaceChild("crown_front_" + iteration, CubeListBuilder.create()
				.texOffs(28, 30)
				.addBox(-4.5F, -0.5F, -0.5F, 9.0F, 1.0F, 1.0F),
			PartPose.offset(0.0F, -6.0F, crownZ));

		crownFront.addOrReplaceChild("spike_4", CubeListBuilder.create()
				.texOffs(48, 27)
				.addBox(-0.5F, -3.5F, spikeDepth, 1.0F, 4.0F, 1.0F),
			PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, angle * 1.5F * Mth.DEG_TO_RAD, 0.0F, 0.0F));

		crownFront.addOrReplaceChild("spike_3l", CubeListBuilder.create()
				.texOffs(52, 28)
				.addBox(-0.5F, -2.5F, spikeDepth, 1.0F, 3.0F, 1.0F),
			PartPose.offsetAndRotation(-2.5F, 0.0F, 0.0F, angle * Mth.DEG_TO_RAD, 0.0F, -10.0F * Mth.DEG_TO_RAD));

		crownFront.addOrReplaceChild("spike_3r", CubeListBuilder.create()
				.texOffs(52, 28)
				.addBox(-0.5F, -2.5F, spikeDepth, 1.0F, 3.0F, 1.0F),
			PartPose.offsetAndRotation(2.5F, 0.0F, 0.0F, angle * Mth.DEG_TO_RAD, 0.0F, 10.0F * Mth.DEG_TO_RAD));

	}

	private static LayerDefinition createJappaModel() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F)
				.texOffs(32, 45)
				.addBox(-4.5F, 10.0F, -2.5F, 9.0F, 14.0F, 5.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.texOffs(14, 32)
				.addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F),
			PartPose.offset(5.0F, 2.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create()
				.texOffs(16, 48)
				.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(1.9F, 12.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(0, 48)
				.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(-1.9F, 12.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(0, 32)
				.addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F),
			PartPose.offset(-5.0F, 2.0F, 0.0F));

		var hat = partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		hat.addOrReplaceChild("crown_front", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F),
			PartPose.offsetAndRotation(0.0F, -6.0F, -4.0F, 0.39269908169872414F, 0.0F, 0.0F));

		hat.addOrReplaceChild("crown_right", CubeListBuilder.create()
				.texOffs(24, 4)
				.addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F),
			PartPose.offsetAndRotation(-4.0F, -6.0F, 0.0F, 0.39269908169872414F, 1.5707963267948966F, 0.0F));

		hat.addOrReplaceChild("crown_left", CubeListBuilder.create()
				.texOffs(44, 4)
				.addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F),
			PartPose.offsetAndRotation(4.0F, -6.0F, 0.0F, -0.39269908169872414F, 1.5707963267948966F, 0.0F));

		hat.addOrReplaceChild("crown_back", CubeListBuilder.create()
				.texOffs(44, 0)
				.addBox(-5.0F, -4.0F, 0.0F, 10.0F, 4.0F, 0.0F),
			PartPose.offsetAndRotation(0.0F, -6.0F, 4.0F, -0.39269908169872414F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(SnowQueen entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		// in beam phase, arms forwards
		if (entity.getCurrentPhase() == Phase.BEAM) {
			if (entity.isBreathing()) {
				float f6 = Mth.sin(this.attackTime * Mth.PI);
				float f7 = Mth.sin((1.0F - (1.0F - this.attackTime) * (1.0F - this.attackTime)) * Mth.PI);
				this.rightArm.zRot = 0.0F;
				this.leftArm.zRot = 0.0F;
				this.rightArm.yRot = -(0.1F - f6 * 0.6F);
				this.leftArm.yRot = 0.1F - f6 * 0.6F;
				this.rightArm.xRot = -Mth.HALF_PI;
				this.leftArm.xRot = -Mth.HALF_PI;
				this.rightArm.xRot -= f6 * 1.2F - f7 * 0.4F;
				this.leftArm.xRot -= f6 * 1.2F - f7 * 0.4F;
				AnimationUtils.bobArms(this.rightArm, this.leftArm, ageInTicks);
			} else {
				// arms up
				this.rightArm.xRot += Mth.PI;
				this.leftArm.xRot += Mth.PI;
			}
		}
	}

	@Override
	public void renderTrophy(PoseStack stack, MultiBufferSource buffer, int light, int overlay, int color, ItemDisplayContext context) {
		if (!JappaPackReloadListener.INSTANCE.isJappaPackLoaded()) {
			stack.translate(0.0F, 0.25F, 0.0F);
		}
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(SnowQueenRenderer.TEXTURE));
		this.head.render(stack, consumer, light, overlay, color);
		this.hat.render(stack, consumer, light, overlay, color);
	}

	@Override
	public void setupRotationsForTrophy(float x, float y, float z, float mouthAngle) {
		this.head.yRot = y * Mth.DEG_TO_RAD;
		this.head.xRot = z * Mth.DEG_TO_RAD;
	}
}
