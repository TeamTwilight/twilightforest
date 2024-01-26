package twilightforest.client.model.entity;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.CarminiteGolem;

public class CarminiteGolemModel<T extends CarminiteGolem> extends HierarchicalModel<T> {
	private final ModelPart root, head, rightArm, leftArm, leftLeg, rightLeg;

	public CarminiteGolemModel(ModelPart root) {
		this.root = root;
		this.head = this.root.getChild("head");
		this.rightArm = this.root.getChild("right_arm");
		this.leftArm = this.root.getChild("left_arm");
		this.rightLeg = this.root.getChild("right_leg");
		this.leftLeg = this.root.getChild("left_leg");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-3.5F, -10F, -3F, 7, 8, 6)
						.texOffs(0, 14)
						.addBox(-4F, -6F, -3.5F, 8, 4, 6),
				PartPose.offset(0F, -11F, -2F));

		definition.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(0, 26)
						.addBox(-8F, 0F, -5F, 16, 10, 10),
				PartPose.offset(0F, -13F, 0F));

		definition.addOrReplaceChild("ribs", CubeListBuilder.create()
						.texOffs(0, 46)
						.addBox(-5F, 0F, -3F, 10, 6, 6),
				PartPose.offset(0F, -3F, 0F));

		definition.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(52, 0) // arm
						.addBox(-5F, -2F, -1.5F, 3, 14, 3)
						.texOffs(52, 17) // fist
						.addBox(-7F, 12F, -3F, 6, 12, 6)
						.texOffs(52, 36) // shoulder top
						.addBox(-7F, -3F, -3.5F, 7, 2, 7)
						.texOffs(52, 45) // shoulder front
						.addBox(-7F, -1F, -3.5F, 7, 5, 2)
						.texOffs(52, 45) // shoulder back
						.addBox(-7F, -1F, 1.5F, 7, 5, 2)
						.texOffs(52, 54) // shoulder inner
						.addBox(-2F, -1F, -2F, 2, 5, 3),
				PartPose.offset(-8F, -12F, 0F));

		definition.addOrReplaceChild("left_arm", CubeListBuilder.create()
						.mirror()
						.texOffs(52, 0) // arm
						.addBox(2F, -2F, -1.5F, 3, 14, 3)
						.texOffs(52, 17) // fist
						.addBox(1F, 12F, -3F, 6, 12, 6)
						.texOffs(52, 36) // shoulder top
						.addBox(0F, -3F, -3.5F, 7, 2, 7)
						.texOffs(52, 45) // shoulder front
						.addBox(0F, -1F, -3.5F, 7, 5, 2)
						.texOffs(52, 45) // shoulder back
						.addBox(0F, -1F, 1.5F, 7, 5, 2)
						.texOffs(52, 54) // shoulder inner
						.addBox(0F, -1F, -2F, 2, 5, 3),
				PartPose.offset(8F, -12F, 0F));

		definition.addOrReplaceChild("hips", CubeListBuilder.create()
						.texOffs(84, 25)
						.addBox(-5F, 0F, -2F, 10, 3, 4),
				PartPose.offset(0F, 1F, 0F));

		definition.addOrReplaceChild("spine", CubeListBuilder.create()
						.texOffs(84, 18)
						.addBox(-1.5F, 0F, -1.5F, 3, 4, 3),
				PartPose.offset(0F, -3F, 0F));

		definition.addOrReplaceChild("right_leg", CubeListBuilder.create()
						.texOffs(84, 32)
						.addBox(-3F, 0F, -1.5F, 3, 8, 3)
						.texOffs(84, 43)
						.addBox(-5.5F, 8F, -4F, 6, 14, 7),
				PartPose.offset(-1F, 2F, 0F));

		definition.addOrReplaceChild("left_leg", CubeListBuilder.create()
						.mirror()
						.texOffs(84, 32)
						.addBox(0F, 0F, -1.5F, 3, 8, 3)
						.texOffs(84, 43)
						.addBox(-0.5F, 8F, -4F, 6, 14, 7),
				PartPose.offset(1F, 2F, 0F));

		return LayerDefinition.create(mesh, 128, 64);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, limbSwing and limbSwingAmount are used for animating the movement of arms
	 * and legs, where limbSwing represents the time(so that arms and legs swing back and forth) and limbSwingAmount represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.head.xRot = headPitch * Mth.DEG_TO_RAD;
		this.leftLeg.xRot = -1.5F * this.func_78172_a(limbSwing, 13.0F) * limbSwingAmount;
		this.rightLeg.xRot = 1.5F * this.func_78172_a(limbSwing, 13.0F) * limbSwingAmount;
		this.leftLeg.yRot = 0.0F;
		this.rightLeg.yRot = 0.0F;


        this.rightArm.zRot = Mth.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.leftArm.zRot = -Mth.cos(ageInTicks * 0.09F) * 0.05F - 0.05F;
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		int var6 = entity.getAttackTimer();

		if (var6 > 0) {
			this.rightArm.xRot = -2.0F + 1.5F * this.func_78172_a(var6 - partialTicks, 10.0F);
			this.leftArm.xRot = -2.0F + 1.5F * this.func_78172_a(var6 - partialTicks, 10.0F);
		} else {
			this.rightArm.xRot = (-0.2F + 1.5F * this.func_78172_a(limbSwing, 25.0F)) * limbSwingAmount;
			this.leftArm.xRot = (-0.2F - 1.5F * this.func_78172_a(limbSwing, 25.0F)) * limbSwingAmount;
		}
	}

	private float func_78172_a(float par1, float par2) {
		return (Math.abs((par1 % par2) - (par2 * 0.5F)) - par2 * 0.25F) / (par2 * 0.25F);
	}
}
