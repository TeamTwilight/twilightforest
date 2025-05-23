package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.entity.AlphaYetiRenderer;
import twilightforest.client.state.AlphaYetiRenderState;

public class AlphaYetiModel extends HumanoidModel<AlphaYetiRenderState> implements TrophyBlockModel {

	public AlphaYetiModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 0.0F, 0.0F, 0.0F),
			PartPose.ZERO);

		head.addOrReplaceChild("hat", CubeListBuilder.create()
				.texOffs(32, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 0.0F, 0.0F, 0.0F),
			PartPose.ZERO);

		var body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(80, 0)
				.addBox(-24.0F, -60.0F, -18.0F, 48.0F, 72.0F, 36.0F),
			PartPose.offset(0.0F, -6.0F, 0.0F));

		body.addOrReplaceChild("mouth", CubeListBuilder.create()
				.texOffs(121, 50)
				.addBox(-17.0F, -7.0F, -1.5F, 34.0F, 29.0F, 2.0F),
			PartPose.offset(0.0F, -37.0F, -18.0F));

		body.addOrReplaceChild("right_eye", CubeListBuilder.create()
				.texOffs(64, 0)
				.addBox(-6.0F, -6.0F, -1.5F, 12.0F, 12.0F, 2.0F),
			PartPose.offset(-14.0F, -50.0F, -18.0F));

		body.addOrReplaceChild("left_eye", CubeListBuilder.create()
				.texOffs(64, 0)
				.addBox(-6.0F, -6.0F, -1.5F, 12.0F, 12.0F, 2.0F),
			PartPose.offset(14.0F, -50.0F, -18.0F));

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-15.0F, -6.0F, -8.0F, 16.0F, 48.0F, 16.0F),
			PartPose.offset(-25.0F, -26.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
				.texOffs(0, 0)
				.addBox(-1.0F, -6.0F, -8.0F, 16.0F, 48.0F, 16.0F),
			PartPose.offset(25.0F, -26.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create()
				.texOffs(0, 66)
				.addBox(-10.0F, 0.0F, -10.0F, 20.0F, 20.0F, 20.0F),
			PartPose.offset(-13.5F, 4.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
				.texOffs(0, 66)
				.addBox(-10.0F, 0.0F, -10.0F, 20.0F, 20.0F, 20.0F),
			PartPose.offset(13.5F, 4.0F, 0.0F));

		addPairHorns(body, -58.0F, 35F, 1);
		addPairHorns(body, -46.0F, 15F, 2);
		addPairHorns(body, -36.0F, -5F, 3);

		return LayerDefinition.create(meshdefinition, 256, 128);
	}

	public static LayerDefinition createTrophy() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(80, 0)
				.addBox(-24.0F, -24.0F, -18.0F, 48.0F, 54.0F, 36.0F)
				.texOffs(64, 0)
				.addBox(8.0F, -20.0F, -19.5F, 12.0F, 12.0F, 2.0F)
				.texOffs(121, 50)
				.addBox(-17.0F, -8.0F, -19.5F, 34.0F, 29.0F, 2.0F)
				.texOffs(64, 0)
				.addBox(-20.0F, -20.0F, -19.5F, 12.0F, 12.0F, 2.0F),
			PartPose.offset(0.0F, -6.0F, 0.0F));

		addPairHorns(head, -16.0F, 35F, 1);
		addPairHorns(head, -4.0F, 15F, 2);
		addPairHorns(head, 6.0F, -5F, 3);

		return LayerDefinition.create(meshdefinition, 256, 128);
	}

	/**
	 * Add a pair of horns
	 */
	private static void addPairHorns(PartDefinition partdefinition, float height, float zangle, int set) {

		var leftHorn = partdefinition.addOrReplaceChild("left_horn_" + set,
			CubeListBuilder.create()
				.texOffs(0, 108)
				.addBox(-9.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F),
			PartPose.offsetAndRotation(-24.0F, height, -8.0F,
				0.0F, -30F * Mth.DEG_TO_RAD, zangle * Mth.DEG_TO_RAD));

		leftHorn.addOrReplaceChild("left_horn_" + set + "_top",
			CubeListBuilder.create()
				.texOffs(40, 108)
				.addBox(-14.0F, -4.0F, -4.0F, 18.0F, 8.0F, 8.0F),
			PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F,
				0.0F, -20F * Mth.DEG_TO_RAD, zangle * Mth.DEG_TO_RAD));

		var rightHorn = partdefinition.addOrReplaceChild("right_horn_" + set,
			CubeListBuilder.create()
				.texOffs(0, 108)
				.addBox(-1.0F, -5.0F, -5.0F, 10.0F, 10.0F, 10.0F),
			PartPose.offsetAndRotation(24.0F, height, 0.0F,
				0.0F, 30F * Mth.DEG_TO_RAD, -zangle * Mth.DEG_TO_RAD));

		rightHorn.addOrReplaceChild("right_horn_" + set + "_top",
			CubeListBuilder.create()
				.texOffs(40, 108)
				.addBox(-2.0F, -4.0F, -4.0F, 18.0F, 8.0F, 8.0F),
			PartPose.offsetAndRotation(8.0F, 0.0F, 0.0F,
				0.0F, 20F * Mth.DEG_TO_RAD, -zangle * Mth.DEG_TO_RAD));
	}

	@Override
	public void setupAnim(AlphaYetiRenderState state) {
		this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
		this.head.xRot = state.xRot * Mth.DEG_TO_RAD;

		this.body.xRot = state.xRot * Mth.DEG_TO_RAD;

		this.rightLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 1.4F * state.walkAnimationSpeed;
		this.leftLeg.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + Mth.PI) * 1.4F * state.walkAnimationSpeed;
		this.rightLeg.yRot = 0.0F;
		this.leftLeg.yRot = 0.0F;

		float f6 = Mth.sin(state.attackTime * Mth.PI);
		float f7 = Mth.sin((1.0F - (1.0F - state.attackTime) * (1.0F - state.attackTime)) * Mth.PI);
		this.rightArm.zRot = 0.0F;
		this.leftArm.zRot = 0.0F;
		this.rightArm.yRot = -(0.1F - f6 * 0.6F);
		this.leftArm.yRot = 0.1F - f6 * 0.6F;
		this.rightArm.xRot = -Mth.HALF_PI;
		this.leftArm.xRot = -Mth.HALF_PI;
		this.rightArm.xRot -= f6 * 1.2F - f7 * 0.4F;
		this.leftArm.xRot -= f6 * 1.2F - f7 * 0.4F;
		this.rightArm.zRot += Mth.cos(state.ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.leftArm.zRot -= Mth.cos(state.ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.rightArm.xRot += Mth.sin(state.ageInTicks * 0.067F) * 0.05F;
		this.leftArm.xRot -= Mth.sin(state.ageInTicks * 0.067F) * 0.05F;

		this.body.y = -6F;
		this.rightLeg.y = 4F;
		this.leftLeg.y = 4F;

		if (state.isTired) {
			// arms down
			this.rightArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F + Mth.PI) * 2.0F * state.walkAnimationSpeed * 0.5F;
			this.leftArm.xRot = Mth.cos(state.walkAnimationPos * 0.6662F) * 2.0F * state.walkAnimationSpeed * 0.5F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.zRot = 0.0F;

			// legs out
			this.rightArm.xRot -= (Mth.PI / 5F);
			this.leftArm.xRot -= (Mth.PI / 5F);
			this.rightLeg.xRot = -(Mth.PI * 2F / 5F);
			this.leftLeg.xRot = -(Mth.PI * 2F / 5F);
			this.rightLeg.yRot = (Mth.PI / 10F);
			this.leftLeg.yRot = -(Mth.PI / 10F);

			//body down
			this.body.y = 6.0F;
			this.rightLeg.y = 12.0F;
			this.leftLeg.y = 12.0F;
		}

		if (state.isRampaging) {
			// arms up
			this.rightArm.xRot = Mth.cos(state.walkAnimationPos * 0.66F + Mth.PI) * 2.0F * state.walkAnimationSpeed * 0.5F;
			this.leftArm.xRot = Mth.cos(state.walkAnimationPos * 0.66F) * 2.0F * state.walkAnimationSpeed * 0.5F;

			this.rightArm.yRot += Mth.cos(state.walkAnimationPos * 0.25F) * 0.5F + 0.5F;
			this.leftArm.yRot -= Mth.cos(state.walkAnimationPos * 0.25F) * 0.5F + 0.5F;

			this.rightArm.xRot += Mth.PI * 1.25F;
			this.leftArm.xRot += Mth.PI * 1.25F;
			this.rightArm.zRot = 0.0F;
			this.leftArm.zRot = 0.0F;
		}

		if (state.isHoldingEntity) {
			// arms up!
			this.rightArm.xRot += Mth.PI;
			this.leftArm.xRot += Mth.PI;
		}
	}

	@Override
	public void setupRotationsForTrophy(float x, float y, float z, float mouthAngle) {
		this.head.yRot = y * Mth.DEG_TO_RAD;
		this.head.xRot = z * Mth.DEG_TO_RAD;
	}

	@Override
	public void renderTrophy(PoseStack stack, MultiBufferSource buffer, int light, int overlay, int color, ItemDisplayContext context) {
		stack.scale(0.2F, 0.2F, 0.2F);
		stack.translate(0.0F, -1.5F, 0.0F);
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(AlphaYetiRenderer.TEXTURE));
		this.head.render(stack, consumer, light, overlay, color);
	}
}
