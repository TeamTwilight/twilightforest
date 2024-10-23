// Date: 11/5/2012 7:35:56 PM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX
package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;
import twilightforest.client.JappaPackReloadListener;

public class SlimeBeetleModel extends EntityModel<LivingEntityRenderState> {

	private final ModelPart head;
	private final ModelPart rightLeg1;
	private final ModelPart rightLeg2;
	private final ModelPart rightLeg3;
	private final ModelPart leftLeg1;
	private final ModelPart leftLeg2;
	private final ModelPart leftLeg3;
	private final ModelPart tailBottom;
	private final ModelPart tailTop;
	private final ModelPart slime;
	private final ModelPart slimeCenter;

	public SlimeBeetleModel(ModelPart root) {
		super(root);

		this.head = root.getChild("head");

		this.rightLeg1 = root.getChild("right_leg_1");
		this.rightLeg2 = root.getChild("right_leg_2");
		this.rightLeg3 = root.getChild("right_leg_3");

		this.leftLeg1 = root.getChild("left_leg_1");
		this.leftLeg2 = root.getChild("left_leg_2");
		this.leftLeg3 = root.getChild("left_leg_3");

		this.tailBottom = root.getChild("tail_bottom");
		this.tailTop = this.tailBottom.getChild("tail_top");

		this.slimeCenter = this.tailTop.getChild("slime_center");
		this.slime = this.slimeCenter.getChild("slime");
	}

	public static LayerDefinition checkForPack() {
		return JappaPackReloadListener.INSTANCE.isJappaPackLoaded() ? createJappaModel() : create();
	}

	private static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition base = meshdefinition.getRoot();

		PartDefinition headpart = base.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -4.0F, -6.0F, 8.0F, 6.0F, 6.0F),
			PartPose.offset(0.0F, 19.0F, -5.0F));

		headpart.addOrReplaceChild("left_antenna", CubeListBuilder.create()
				.texOffs(38, 4)
				.addBox(0.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F),
			PartPose.offsetAndRotation(1.0F, -3.0F, -5.0F, 0.0F, 1.047198F, -0.296706F));

		headpart.addOrReplaceChild("right_antenna", CubeListBuilder.create()
				.texOffs(38, 4)
				.addBox(0.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F),
			PartPose.offsetAndRotation(-1.0F, -3.0F, -5.0F, 0.0F, 2.094395F, 0.296706F));

		headpart.addOrReplaceChild("left_eye", CubeListBuilder.create()
				.texOffs(15, 12)
				.addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
			PartPose.offset(3.0F, -2.0F, -5.0F));

		headpart.addOrReplaceChild("right_eye", CubeListBuilder.create()
				.texOffs(15, 12)
				.addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
			PartPose.offset(-3.0F, -2.0F, -5.0F));

		headpart.addOrReplaceChild("mouth", CubeListBuilder.create()
				.texOffs(17, 12)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 1.0F),
			PartPose.offset(0.0F, 1.0F, -6.0F));

		base.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(31, 6)
				.addBox(-4.0F, -11.0F, -4.0F, 8.0F, 10.0F, 8.0F),
			PartPose.offsetAndRotation(0.0F, 18.0F, 7.0F, 1.570796F, 0.0F, 0.0F));

		base.addOrReplaceChild("left_leg_1", CubeListBuilder.create()
				.texOffs(40, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(2.0F, 21.0F, -4.0F, 0.0F, 0.2792527F, 0.3490659F));

		base.addOrReplaceChild("right_leg_1", CubeListBuilder.create().mirror()
				.texOffs(40, 0)
				.addBox(-9.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-2.0F, 21.0F, -4.0F, 0.0F, -0.2792527F, -0.3490659F));

		base.addOrReplaceChild("left_leg_2", CubeListBuilder.create()
				.texOffs(40, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(2.0F, 21.0F, -1.0F, 0.0F, -0.2792527F, 0.3490659F));

		base.addOrReplaceChild("right_leg_2", CubeListBuilder.create().mirror()
				.texOffs(40, 0)
				.addBox(-9.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-2.0F, 21.0F, -1.0F, 0.0F, 0.2792527F, -0.3490659F));

		base.addOrReplaceChild("left_leg_3", CubeListBuilder.create()
				.texOffs(40, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(2.0F, 21.0F, 4.0F, 0.0F, -0.6981317F, 0.3490659F));

		base.addOrReplaceChild("right_leg_3", CubeListBuilder.create().mirror()
				.texOffs(40, 0)
				.addBox(-9.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-2.0F, 21.0F, 4.0F, 0.0F, 0.6981317F, -0.3490659F));

		base.addOrReplaceChild("connector", CubeListBuilder.create()
				.texOffs(0, 12)
				.addBox(-3.0F, -3.0F, -1.0F, 6.0F, 6.0F, 1.0F),
			PartPose.offset(0.0F, 19.0F, -4.0F));

		PartDefinition tail1 = base.addOrReplaceChild("tail_bottom", CubeListBuilder.create()
				.texOffs(0, 20)
				.addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F),
			PartPose.offset(0.0F, 19.0F, 9.0F));

		PartDefinition tail2 = tail1.addOrReplaceChild("tail_top", CubeListBuilder.create()
				.texOffs(0, 20)
				.addBox(-3.0F, -6.0F, -3.0F, 6.0F, 6.0F, 6.0F),
			PartPose.offset(0.0F, -3.0F, 2.0F));

		var center = tail2.addOrReplaceChild("slime_center", CubeListBuilder.create()
				.texOffs(32, 24)
				.addBox(-4.0F, -10.0F, -7.0F, 8.0F, 8.0F, 8.0F),
			PartPose.offset(0.0F, -6.0F, 0.0F));

		center.addOrReplaceChild("slime", CubeListBuilder.create()
				.texOffs(0, 40)
				.addBox(-6.0F, -12.0F, -9.0F, 12.0F, 12.0F, 12.0F),
			PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	private static LayerDefinition createJappaModel() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -3.0F, -6.0F, 8.0F, 6.0F, 6.0F),
			PartPose.offset(0.0F, 17.0F, -8.0F));

		head.addOrReplaceChild("right_antenna", CubeListBuilder.create()
				.texOffs(38, 4)
				.addBox(-12.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F),
			PartPose.offsetAndRotation(-0.5F, -1.5F, -5.0F, 0.0F, -0.7853981633974483F, 0.7853981633974483F));

		head.addOrReplaceChild("left_antenna", CubeListBuilder.create()
				.texOffs(38, 6)
				.addBox(0.0F, -0.5F, -0.5F, 12.0F, 1.0F, 1.0F),
			PartPose.offsetAndRotation(0.5F, -1.5F, -5.0F, 0.0F, 0.7853981633974483F, -0.7853981633974483F));

		head.addOrReplaceChild("right_eye", CubeListBuilder.create()
				.texOffs(0, 12)
				.addBox(-2.0F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F),
			PartPose.offset(-2.5F, -1.0F, -4.5F));

		head.addOrReplaceChild("left_eye", CubeListBuilder.create()
				.texOffs(16, 12)
				.addBox(-1.0F, -1.0F, -2.0F, 3.0F, 3.0F, 3.0F),
			PartPose.offset(2.5F, -1.0F, -4.5F));

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(32, 8)
				.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 10.0F, 8.0F),
			PartPose.offsetAndRotation(0.0F, 17.0F, -8.0F, 1.5707963267948966F, 0.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_leg_1", CubeListBuilder.create()
				.texOffs(40, 0)
				.addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-2.0F, 20.0F, -6.0F, 0.0F, -0.4363323129985824F, -0.4363323129985824F));

		partdefinition.addOrReplaceChild("right_leg_2", CubeListBuilder.create()
				.texOffs(40, 0)
				.addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-2.0F, 20.0F, -4.0F, 0.0F, 0.2181661564992912F, -0.4363323129985824F));

		partdefinition.addOrReplaceChild("right_leg_3", CubeListBuilder.create()
				.texOffs(40, 0)
				.addBox(-10.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-2.0F, 20.0F, -2.0F, 0.0F, 0.7853981633974483F, -0.4363323129985824F));

		partdefinition.addOrReplaceChild("left_leg_1", CubeListBuilder.create().mirror()
				.texOffs(40, 0)
				.addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(2.0F, 20.0F, -6.0F, 0.0F, 0.4363323129985824F, 0.4363323129985824F));

		partdefinition.addOrReplaceChild("left_leg_2", CubeListBuilder.create().mirror()
				.texOffs(40, 0)
				.addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(2.0F, 20.0F, -4.0F, 0.0F, -0.2181661564992912F, 0.4363323129985824F));

		partdefinition.addOrReplaceChild("left_leg_3", CubeListBuilder.create().mirror()
				.texOffs(40, 0)
				.addBox(0.0F, -1.0F, -1.0F, 10.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(2.0F, 20.0F, -2.0F, 0.0F, -0.7853981633974483F, 0.4363323129985824F));

		var tailBottom = partdefinition.addOrReplaceChild("tail_bottom", CubeListBuilder.create()
				.texOffs(0, 34)
				.addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 6.0F),
			PartPose.offset(0.0F, 18.0F, 2.0F));

		var tailTop = tailBottom.addOrReplaceChild("tail_top", CubeListBuilder.create()
				.texOffs(32, 28)
				.addBox(-3.0F, -9.0F, -1.0F, 6.0F, 6.0F, 6.0F),
			PartPose.offset(0.0F, 0.0F, 3.0F));

		var center = tailTop.addOrReplaceChild("slime_center", CubeListBuilder.create()
				.texOffs(0, 18)
				.addBox(-4.0F, -10.0F, -5.0F, 8.0F, 8.0F, 8.0F),
			PartPose.offset(0.0F, -9.0F, 2.0F));

		center.addOrReplaceChild("slime", CubeListBuilder.create()
				.texOffs(16, 40)
				.addBox(-6.0F, -12.0F, -7.0F, 12.0F, 12.0F, 12.0F),
			PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	public void copyTailTo(SlimeBeetleModel model) {
		model.tailBottom.copyFrom(this.tailBottom);
		model.tailTop.copyFrom(this.tailTop);
		model.slimeCenter.copyFrom(this.slimeCenter);
		model.slime.copyFrom(this.slime);
	}

	public void renderTail(PoseStack stack, VertexConsumer builder, int light, int overlay) {
		this.slime.visible = true;
		this.tailBottom.render(stack, builder, light, overlay);
	}

	@Override
	public void setupAnim(LivingEntityRenderState state) {
		this.slime.visible = false;
		this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
		this.head.xRot = state.xRot * Mth.DEG_TO_RAD;

		// legs!
		float legZ = Mth.PI / 11.0F;
		this.leftLeg1.zRot = legZ;
		this.rightLeg1.zRot = -legZ;
		this.leftLeg2.zRot = legZ * 0.74F;
		this.rightLeg2.zRot = -legZ * 0.74F;
		this.leftLeg3.zRot = legZ;
		this.rightLeg3.zRot = -legZ;

		float var9 = -0.0F;
		float var10 = 0.3926991F;
		this.leftLeg1.yRot = var10 * 2.0F + var9;
		this.rightLeg1.yRot = -var10 * 2.0F - var9;
		this.leftLeg2.yRot = var10 + var9;
		this.rightLeg2.yRot = -var10 - var9;
		this.leftLeg3.yRot = -var10 * 2.0F + var9;
		this.rightLeg3.yRot = var10 * 2.0F - var9;

		float var11 = -(Mth.cos(state.walkAnimationPos * 0.6662F * 2.0F + 0.0F) * 0.4F) * state.walkAnimationSpeed;
		float var12 = -(Mth.cos(state.walkAnimationPos * 0.6662F * 2.0F + Mth.PI) * 0.4F) * state.walkAnimationSpeed;
		float var14 = -(Mth.cos(state.walkAnimationPos * 0.6662F * 2.0F + (Mth.PI * 3.0F / 2.0F)) * 0.4F) * state.walkAnimationSpeed;

		float var15 = Math.abs(Mth.sin(state.walkAnimationPos * 0.6662F + 0.0F) * 0.4F) * state.walkAnimationSpeed;
		float var16 = Math.abs(Mth.sin(state.walkAnimationPos * 0.6662F + Mth.PI) * 0.4F) * state.walkAnimationSpeed;
		float var18 = Math.abs(Mth.sin(state.walkAnimationPos * 0.6662F + (Mth.PI * 3.0F / 2.0F)) * 0.4F) * state.walkAnimationSpeed;

		this.leftLeg1.yRot += var11;
		this.rightLeg1.yRot -= var11;
		this.leftLeg2.yRot += var12;
		this.rightLeg2.yRot -= var12;
		this.leftLeg3.yRot += var14;
		this.rightLeg3.yRot -= var14;

		this.leftLeg1.zRot += var15;
		this.rightLeg1.zRot -= var15;

		this.leftLeg2.zRot += var16;
		this.rightLeg2.zRot -= var16;

		this.leftLeg3.zRot += var18;
		this.rightLeg3.zRot -= var18;

		// tail wiggle
		this.tailBottom.xRot = Mth.cos(state.ageInTicks * 0.3335F) * 0.15F;
		this.tailTop.xRot = Mth.cos(state.ageInTicks * 0.4445F) * 0.20F;
		this.slimeCenter.xRot = Mth.cos(state.ageInTicks * 0.5555F + 0.25F) * 0.25F;
	}
}
