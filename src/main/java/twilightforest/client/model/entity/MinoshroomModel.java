package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.client.JappaPackReloadListener;
import twilightforest.client.renderer.entity.MinoshroomRenderer;
import twilightforest.client.state.MinoshroomRenderState;

public class MinoshroomModel<T extends MinoshroomRenderState> extends HumanoidModel<T> implements TrophyBlockModel {

	public final ModelPart cowTorso;
	private final ModelPart rightFrontLeg;
	private final ModelPart leftFrontLeg;
	private final ModelPart rightBackLeg;
	private final ModelPart leftBackLeg;

	public MinoshroomModel(ModelPart root) {
		super(root);
		this.cowTorso = root.getChild("cow_torso");
		this.rightFrontLeg = root.getChild("right_front_leg");
		this.leftFrontLeg = root.getChild("left_front_leg");
		this.rightBackLeg = root.getChild("right_back_leg");
		this.leftBackLeg = root.getChild("left_back_leg");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0);
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(96, 16)
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
			PartPose.offset(0.0F, -6.0F, -9.0F));

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

		head.addOrReplaceChild("snout", CubeListBuilder.create()
				.texOffs(105, 28)
				.addBox(-2.0F, -1.0F, -1.0F, 4.0F, 3.0F, 1.0F),
			PartPose.offset(0.0F, -2.0F, -4.0F));

		var rightHorn = head.addOrReplaceChild("right_horn_1", CubeListBuilder.create().mirror()
				.texOffs(0, 0)
				.addBox(-5.5F, -1.5F, -1.5F, 5.0F, 3.0F, 3.0F),
			PartPose.offsetAndRotation(-2.5F, -6.5F, 0.0F, 0.0F, -25.0F * Mth.DEG_TO_RAD, 10.0F * Mth.DEG_TO_RAD));

		rightHorn.addOrReplaceChild("right_horn_2", CubeListBuilder.create().mirror()
				.texOffs(16, 0)
				.addBox(-3.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-4.5F, 0.0F, 0.0F, 0.0F, -15.0F * Mth.DEG_TO_RAD, 45.0F * Mth.DEG_TO_RAD));

		var leftHorn = head.addOrReplaceChild("left_horn_1", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(0.5F, -1.5F, -1.5F, 5.0F, 3.0F, 3.0F),
			PartPose.offsetAndRotation(2.5F, -6.5F, 0.0F, 0.0F, 25.0F * Mth.DEG_TO_RAD, -10.0F * Mth.DEG_TO_RAD));

		leftHorn.addOrReplaceChild("left_horn_2", CubeListBuilder.create()
				.texOffs(16, 0)
				.addBox(0.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(4.5F, 0.0F, 0.0F, 0.0F, 15.0F * Mth.DEG_TO_RAD, -45.0F * Mth.DEG_TO_RAD));

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(64, 0)
				.addBox(-4.0F, 0.0F, -2.5F, 8.0F, 12.0F, 5.0F),
			PartPose.offset(0.0F, -6.0F, -9.0F));

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(90, 0)
				.addBox(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(-5.0F, -4.0F, -9.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
				.texOffs(90, 0)
				.addBox(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(5.0F, -4.0F, -9.0F));

		var body = partdefinition.addOrReplaceChild("cow_torso", CubeListBuilder.create()
				.texOffs(18, 4)
				.addBox(-6.0F, -10.0F, -7.0F, 12.0F, 18.0F, 10.0F),
			PartPose.offsetAndRotation(0.0F, 5.0F, 2.0F, 1.570796F, 0.0F, 0.0F));

		body.addOrReplaceChild("udders", CubeListBuilder.create()
				.texOffs(53, 1)
				.addBox(-2.0F, -3.0F, 0.0F, 4.0F, 6.0F, 1.0F),
			PartPose.offset(0.0F, 5.0F, -8.0F));

		partdefinition.addOrReplaceChild("left_back_leg", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-3.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(-3.0F, 12.0F, 7.0F));

		partdefinition.addOrReplaceChild("right_back_leg", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-1.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(3.0F, 12.0F, 7.0F));

		partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-3.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(-3.0F, 12.0F, -5.0F));

		partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
				.texOffs(0, 16)
				.addBox(-1.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(3.0F, 12.0F, -5.0F));

		return LayerDefinition.create(meshdefinition, 128, 32);
	}

	private static LayerDefinition createJappaModel() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -11.0F, -4.0F, 8.0F, 8.0F, 8.0F)
				.texOffs(0, 16)
				.addBox(-3.0F, -6.0F, -5.0F, 6.0F, 3.0F, 1.0F)
				.texOffs(32, 0)
				.addBox(-8.0F, -10.0F, -1.0F, 4.0F, 2.0F, 3.0F)
				.texOffs(32, 5)
				.addBox(-8.0F, -13.0F, -1.0F, 2.0F, 3.0F, 3.0F)
				.texOffs(46, 0)
				.addBox(4.0F, -10.0F, -1.0F, 4.0F, 2.0F, 3.0F)
				.texOffs(46, 5)
				.addBox(6.0F, -13.0F, -1.0F, 2.0F, 3.0F, 3.0F),
			PartPose.offset(0.0F, -6.0F, -7.0F));

		partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 29)
				.addBox(-5.0F, -3.0F, 0.0F, 10.0F, 12.0F, 5.0F),
			PartPose.offset(0.0F, -6.0F, -9.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create()
				.texOffs(46, 15)
				.addBox(0.0F, -1.0F, -2.0F, 4.0F, 14.0F, 5.0F),
			PartPose.offset(5.0F, -8.0F, -7.0F));

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(28, 15)
				.addBox(-4.0F, -1.0F, -2.0F, 4.0F, 14.0F, 5.0F),
			PartPose.offset(-5.0F, -8.0F, -7.0F));

		partdefinition.addOrReplaceChild("cow_torso", CubeListBuilder.create()
				.texOffs(20, 36)
				.addBox(-6.0F, -14.0F, -2.0F, 12.0F, 18.0F, 10.0F)
				.texOffs(0, 20)
				.addBox(-2.0F, -2.0F, -3.0F, 4.0F, 6.0F, 1.0F),
			PartPose.offsetAndRotation(0.0F, 10.0F, 6.0F, 1.5707963267948966F, 0.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_front_leg", CubeListBuilder.create()
				.texOffs(0, 48)
				.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(-4.0F, 12.0F, -6.0F));

		partdefinition.addOrReplaceChild("left_front_leg", CubeListBuilder.create()
				.texOffs(0, 48)
				.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(4.0F, 12.0F, -6.0F));

		partdefinition.addOrReplaceChild("right_back_leg", CubeListBuilder.create()
				.texOffs(0, 48)
				.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(-4.0F, 12.0F, 7.0F));

		partdefinition.addOrReplaceChild("left_back_leg", CubeListBuilder.create()
				.texOffs(0, 48)
				.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F),
			PartPose.offset(4.0F, 12.0F, 7.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(T entity) {
		super.setupAnim(entity);

		// copied from QuadrupedModel
		this.cowTorso.xRot = Mth.HALF_PI;
		this.leftFrontLeg.xRot = Mth.cos(entity.walkAnimationPos * 0.6662F) * 1.4F * entity.walkAnimationSpeed;
		this.rightFrontLeg.xRot = Mth.cos(entity.walkAnimationPos * 0.6662F + Mth.PI) * 1.4F * entity.walkAnimationSpeed;
		this.leftBackLeg.xRot = Mth.cos(entity.walkAnimationPos * 0.6662F + Mth.PI) * 1.4F * entity.walkAnimationSpeed;
		this.rightBackLeg.xRot = Mth.cos(entity.walkAnimationPos * 0.6662F) * 1.4F * entity.walkAnimationSpeed;

		// Ground slam animation
		float f1 = entity.chargeAnim;
		f1 = f1 * f1;

		boolean jappa = JappaPackReloadListener.INSTANCE.isJappaPackLoaded();

		this.leftFrontLeg.y = 12.0F + ((jappa ? -7.0F : -5.0F) * f1);
		this.leftFrontLeg.z = -4.0F + f1;
		this.rightFrontLeg.y = this.leftFrontLeg.y;
		this.rightFrontLeg.z = this.leftFrontLeg.z;
		this.head.y = (jappa ? -4.0F : -6.0F) + -3.0F * f1;
		this.head.z = -9.0F + 6.0F * f1;
		this.body.y = (jappa ? -4.0F : -6.0F) + -3.0F * f1;
		this.body.z = (jappa ? -11.0F : -9.0F) + 6.0F * f1;
		this.cowTorso.y = (jappa ? 10.0F : 5.0F) + f1;
		this.cowTorso.z = (jappa ? 6.0F : 2.0F) + ((jappa ? 1.0F : 4.0F) * f1);
		this.rightArm.y = (jappa ? -6.0F : -4.0F) - (3.0F * f1);
		this.rightArm.z = -9.0F + (6.0F * f1);
		this.leftArm.y = this.rightArm.y;
		this.leftArm.z = this.rightArm.z;

		if (f1 > 0) {
			if (entity.mainArm == HumanoidArm.RIGHT) {
				this.rightArm.xRot = f1 * -1.8F;
				this.leftArm.xRot = 0.0F;
				this.rightArm.zRot = -0.2F;
			} else {
				this.rightArm.xRot = 0.0F;
				this.leftArm.xRot = f1 * -1.8F;
				this.leftArm.zRot = 0.2F;
			}
			this.cowTorso.xRot = Mth.HALF_PI - f1 * Mth.PI * 0.2F;
			this.leftFrontLeg.xRot -= f1 * Mth.PI * 0.3F;
			this.rightFrontLeg.xRot -= f1 * Mth.PI * 0.3F;
		}
	}

	@Override
	public void setupRotationsForTrophy(float x, float y, float z, float mouthAngle) {
		this.head.yRot = y * Mth.DEG_TO_RAD;
		this.head.xRot = z * Mth.DEG_TO_RAD;
	}

	@Override
	public void renderTrophy(PoseStack stack, MultiBufferSource buffer, int light, int overlay, int color, ItemDisplayContext context) {
		if (!JappaPackReloadListener.INSTANCE.isJappaPackLoaded()) {
			stack.translate(0.0F, 0.375F, 0.56F);
		} else {
			stack.translate(0.0F, 0.5625F, 0.4375F);
		}
		VertexConsumer consumer = buffer.getBuffer(RenderTypes.entityCutout(MinoshroomRenderer.TEXTURE));
		this.head.render(stack, consumer, light, overlay, color);
	}
}
