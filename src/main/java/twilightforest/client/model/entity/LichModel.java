package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import org.jspecify.annotations.Nullable;
import twilightforest.client.renderer.entity.LichRenderer;
import twilightforest.client.state.entity.LichRenderState;
import twilightforest.entity.boss.Lich;

public class LichModel extends HumanoidModel<LichRenderState> implements TrophyBlockModel {

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

		partdefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create()
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
	public void setupAnim(LichRenderState state) {
		super.setupAnim(state);
		this.cloak.skipDraw = state.isShadowClone;
		this.collar.skipDraw = state.isShadowClone;

		if (state.phase != 3) {
			float ogSin = Mth.sin(state.attackTime * Mth.PI);
			float otherSin = Mth.sin((1.0F - (1.0F - state.attackTime) * (1.0F - state.attackTime)) * Mth.PI);
			if (state.ageInTicks > 0 && state.deathTime <= 0) {
				this.leftArm.zRot = 0.5F;
				this.leftArm.yRot = 0.1F - ogSin * 0.6F;
				this.leftArm.xRot = -3.141593F;
				this.leftArm.xRot -= ogSin * 1.2F - otherSin * 0.4F;
				this.leftArm.zRot -= Mth.cos(state.ageInTicks * 0.26F) * 0.15F + 0.05F;
				this.leftArm.xRot -= Mth.sin(state.ageInTicks * 0.167F) * 0.15F;
			} else {
				this.leftArm.xRot = 0.0F;
				this.leftArm.yRot = 0.0F;
			}

			if (!state.getMainHandItemStack().isEmpty()) {
				this.rightArm.zRot = 0.0F;
				this.rightArm.yRot = -(0.1F - ogSin * 0.6F);
				this.rightArm.xRot = -Mth.HALF_PI;
				this.rightArm.xRot -= ogSin * 1.2F - otherSin * 0.4F;
				this.rightArm.zRot += Mth.cos(state.ageInTicks * 0.26F) * 0.15F + 0.05F;
				this.rightArm.xRot += Mth.sin(state.ageInTicks * 0.167F) * 0.15F;
			} else {
				this.rightArm.xRot = 0.0F;
				this.rightArm.yRot = 0.0F;
			}
		} else {
			this.leftArm.xRot += -Mth.HALF_PI * 0.25F;

			this.rightArm.xRot -= (Mth.cos(state.walkAnimationPos * 0.6662F + Mth.PI) * 2.0F * state.walkAnimationSpeed * 0.5F) * 0.75F;
			this.rightArm.xRot += -Mth.HALF_PI * 0.75F;
		}

		boolean flag = state.deathTime > Lich.DEATH_ANIMATION_POINT_A;
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
	public void translateToHand(HumanoidRenderState renderState, HumanoidArm arm, PoseStack stack) {
		float f = arm == HumanoidArm.RIGHT ? 1.0F : -1.0F;
		ModelPart modelpart = this.getArm(arm);
		modelpart.x += f;
		modelpart.translateAndRotate(stack);
		modelpart.x -= f;
	}

	@Override
	public void renderTrophy(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, ItemDisplayContext context) {
		if (context == ItemDisplayContext.GUI) {
			stack.translate(0.0F, 0.1F, 0.0F);
		} else {
			stack.translate(0.0F, 0.25F, 0.0F);
		}
		collector.submitModelPart(this.head, stack, RenderTypes.entityCutout(LichRenderer.TEXTURE), light, overlay, null, -1, breakProgress);
	}

	@Override
	public ModelPart getTrophyRoot() {
		return this.root();
	}
}
