package twilightforest.client.model.armor;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

import java.util.Collections;

public class TravellerWingsModel extends HumanoidModel<LivingEntity> {
	protected static double cumulativePhase = 0;
	protected static double oldAgeInTicks = 0;
	private static final double tau = 4;
	private static float xRotOld = 0;
	private static float yRotOld = 0;
	private static float zRotOld = 0;
	private static final float angle10deg = Mth.PI / 18;
	private final ModelPart wingBaseRight, wingEdgeRight, wingInsetRight, wingCenterRight, wingFlangeRight, wingAuxRight;
	private final ModelPart wingBaseLeft, wingEdgeLeft, wingInsetLeft, wingCenterLeft, wingFlangeLeft, wingAuxLeft;

	public TravellerWingsModel(ModelPart root) {
		super(root);
		root = root.getChild("body");
		this.wingBaseRight = root.getChild("wingBaseRight");
		this.wingEdgeRight = wingBaseRight.getChild("wingEdgeRight");
		this.wingInsetRight = wingBaseRight.getChild("wingInsetRight");
		this.wingCenterRight = wingBaseRight.getChild("wingCenterRight");
		this.wingFlangeRight = wingBaseRight.getChild("wingFlangeRight");
		this.wingAuxRight = wingBaseRight.getChild("wingAuxRight");

		this.wingBaseLeft = root.getChild("wingBaseLeft");
		this.wingEdgeLeft = wingBaseLeft.getChild("wingEdgeLeft");
		this.wingInsetLeft = wingBaseLeft.getChild("wingInsetLeft");
		this.wingCenterLeft = wingBaseLeft.getChild("wingCenterLeft");
		this.wingFlangeLeft = wingBaseLeft.getChild("wingFlangeLeft");
		this.wingAuxLeft = wingBaseLeft.getChild("wingAuxLeft");
		body.skipDraw = true;
	}

	public static LayerDefinition createLayer(float deformation) {
		MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(deformation), 0);
		PartDefinition root = mesh.getRoot().getChild("body");

		PartDefinition wbr = root.addOrReplaceChild("wingBaseRight",
			CubeListBuilder.create()
				.texOffs(0, 9)
				.addBox(-0.5F, -1F, 0F, 1, 2, 10),
			PartPose.offsetAndRotation(-1F, 1F, 0F, angle10deg * 3, -angle10deg * 3, 0F));

		wbr.addOrReplaceChild("wingEdgeRight",
			CubeListBuilder.create()
				.texOffs(0, 21)
				.addBox(0F, 0F, -2F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.502F, -1F, 10F, angle10deg * 3, 0F, 0F));

		wbr.addOrReplaceChild("wingInsetRight",
			CubeListBuilder.create()
				.texOffs(6, 21)
				.addBox(0F, 0F, -1F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.504F, 0F, 7.8F, angle10deg * 2, 0F, 0F));

		wbr.addOrReplaceChild("wingCenterRight",
			CubeListBuilder.create()
				.texOffs(12, 21)
				.addBox(0F, 0F, -1F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.506F, 0.3F, 6.3F, angle10deg, 0F, 0F));

		wbr.addOrReplaceChild("wingFlangeRight",
			CubeListBuilder.create()
				.texOffs(18, 21)
				.addBox(0F, 0F, -1F, 1, 8, 2),
			PartPose.offsetAndRotation(-0.508F, 0.3F, 5.1F, 0F, 0F, 0F));

		wbr.addOrReplaceChild("wingAuxRight",
			CubeListBuilder.create()
				.texOffs(24, 21)
				.addBox(0F, 0F, -1F, 1, 7, 2),
			PartPose.offsetAndRotation(-0.51F, 0.1F, 4F, -angle10deg, 0F, 0F));

		PartDefinition wbl = root.addOrReplaceChild("wingBaseLeft",
			CubeListBuilder.create()
				.texOffs(42, 9)
				.addBox(-0.5F, -1F, 0F, 1, 2, 10),
			PartPose.offsetAndRotation(1F, 1F, 0F, angle10deg * 3, angle10deg * 3, 0F));

		wbl.addOrReplaceChild("wingEdgeLeft",
			CubeListBuilder.create()
				.texOffs(58, 21)
				.addBox(0F, 0F, -2F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.502F, -1F, 10F, angle10deg * 3, 0F, 0F));

		wbl.addOrReplaceChild("wingInsetLeft",
			CubeListBuilder.create()
				.texOffs(52, 21)
				.addBox(0F, 0F, -1F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.504F, 0F, 7.8F, angle10deg * 2, 0F, 0F));

		wbl.addOrReplaceChild("wingCenterLeft",
			CubeListBuilder.create()
				.texOffs(46, 21)
				.addBox(0F, 0F, -1F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.506F, 0.3F, 6.3F, angle10deg, 0F, 0F));

		wbl.addOrReplaceChild("wingFlangeLeft",
			CubeListBuilder.create()
				.texOffs(40, 21)
				.addBox(0F, 0F, -1F, 1, 8, 2),
			PartPose.offsetAndRotation(-0.508F, 0.3F, 5.1F, 0F, 0F, 0F));

		wbl.addOrReplaceChild("wingAuxLeft",
			CubeListBuilder.create()
				.texOffs(34, 21)
				.addBox(0F, 0F, -1F, 1, 7, 2),
			PartPose.offsetAndRotation(-0.51F, 0.1F, 4F, -angle10deg, 0F, 0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	public void setupModelAnimations(LivingEntity entity, float f, float f1, double ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, f, f1, (float) ageInTicks, netHeadYaw, headPitch);

		float targetXRot, targetYRot, targetZRot;

		if (this.riding) {
			cumulativePhase += (ageInTicks - oldAgeInTicks) / 17f;
			float t = (float) (cumulativePhase % (2 * Math.PI));
			targetXRot = Mth.sin(t) / 10f + 0.9F;
			targetYRot = Mth.sin(t) / 5f - 0.7f;
			targetZRot = Mth.sin(t) / 2.5f - 0.3f;
		} else if (entity.isInWater()) {
			cumulativePhase += (ageInTicks - oldAgeInTicks) / 17f;
			float t = (float) (cumulativePhase % (2 * Math.PI));
			targetXRot = Mth.sin(t) / 15f + angle10deg * 3;
			targetYRot = Mth.sin(t) / 15f - 0.8f;
			targetZRot = Mth.sin(t) / 15f - 0.8f;
		} else if (entity.getDeltaMovement().y < 0 && entity.fallDistance > 2.3f) {
			cumulativePhase += (ageInTicks - oldAgeInTicks) / 20f;
			float t = (float) (cumulativePhase % (2 * Math.PI));
			targetXRot = Mth.sin(t) / 15f + 0.7F;
			targetYRot = Mth.sin(t) / 15f - 0.8f;
			targetZRot = Mth.sin(t) / 15f - 0.3f;
		} else if (entity.isSprinting() || this.attackTime > 0) {
			cumulativePhase += (ageInTicks - oldAgeInTicks) / 1.73f;
			float t = (float) (cumulativePhase % (2 * Math.PI));
			targetXRot = Mth.sin(t) / 15f + angle10deg * 3;
			targetYRot = Mth.sin(t) / 15f - 0.6f;
			targetZRot = Mth.sin(t) / 15f - 0.3f;
		} else {
			cumulativePhase += (ageInTicks - oldAgeInTicks) / ((entity.getDeltaMovement().horizontalDistanceSqr() > 0) ? 6f : 17f);
			float t = (float) (cumulativePhase % (2 * Math.PI));
			targetXRot = Mth.sin(t) / 5f + angle10deg * 3;
			targetYRot = Mth.sin(t) / 3f - 0.6f;
			targetZRot = Mth.sin(t) / 1.5f - 0.3f;
		}

		xRotOld = moveToTarget(xRotOld, targetXRot, ageInTicks - oldAgeInTicks);
		yRotOld = moveToTarget(yRotOld, targetYRot, ageInTicks - oldAgeInTicks);
		zRotOld = moveToTarget(zRotOld, targetZRot, ageInTicks - oldAgeInTicks);

		wingBaseRight.xRot = xRotOld;
		wingBaseRight.yRot = yRotOld;
		wingBaseRight.zRot = zRotOld;

		wingBaseLeft.xRot = xRotOld;
		wingBaseLeft.yRot = -yRotOld;
		wingBaseLeft.zRot = -zRotOld;
		oldAgeInTicks = ageInTicks;
	}

	private static float moveToTarget(double oPos, double targetPos, double dt) {
		return (float) (targetPos - (targetPos - oPos) * Math.exp(-dt / tau));
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return Collections.emptyList();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(body);
	}
}
