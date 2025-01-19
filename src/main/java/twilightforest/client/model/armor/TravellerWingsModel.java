package twilightforest.client.model.armor;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.components.entity.TravellerWingsAnimAttachment;
import twilightforest.init.TFDataAttachments;

import java.util.Collections;

public class TravellerWingsModel extends HumanoidModel<LivingEntity> {
	private static final double tau = 4;  // Time (in ticks) in which distance reduces in e times
	private static final float ANGLE_10_DEG = Mth.PI / 18;
	private final ModelPart wingBaseRight;
	private final ModelPart wingBaseLeft;

	public TravellerWingsModel(ModelPart root) {
		super(root);
		root = root.getChild("body");
		this.wingBaseRight = root.getChild("wingBaseRight");

		this.wingBaseLeft = root.getChild("wingBaseLeft");
		body.skipDraw = true;
	}

	public static LayerDefinition createLayer(float deformation) {
		MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(deformation), 0);
		PartDefinition root = mesh.getRoot().getChild("body");

		PartDefinition wbr = root.addOrReplaceChild("wingBaseRight",
			CubeListBuilder.create()
				.texOffs(0, 9)
				.addBox(-0.5F, -1F, 0F, 1, 2, 10),
			PartPose.offsetAndRotation(-1F, 1F, 0F, ANGLE_10_DEG * 3, -ANGLE_10_DEG * 3, 0F));

		wbr.addOrReplaceChild("wingEdgeRight",
			CubeListBuilder.create()
				.texOffs(0, 21)
				.addBox(0F, 0F, -2F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.502F, -1F, 10F, ANGLE_10_DEG * 3, 0F, 0F));

		wbr.addOrReplaceChild("wingInsetRight",
			CubeListBuilder.create()
				.texOffs(6, 21)
				.addBox(0F, 0F, -1F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.504F, 0F, 7.8F, ANGLE_10_DEG * 2, 0F, 0F));

		wbr.addOrReplaceChild("wingCenterRight",
			CubeListBuilder.create()
				.texOffs(12, 21)
				.addBox(0F, 0F, -1F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.506F, 0.3F, 6.3F, ANGLE_10_DEG, 0F, 0F));

		wbr.addOrReplaceChild("wingFlangeRight",
			CubeListBuilder.create()
				.texOffs(18, 21)
				.addBox(0F, 0F, -1F, 1, 8, 2),
			PartPose.offsetAndRotation(-0.508F, 0.3F, 5.1F, 0F, 0F, 0F));

		wbr.addOrReplaceChild("wingAuxRight",
			CubeListBuilder.create()
				.texOffs(24, 21)
				.addBox(0F, 0F, -1F, 1, 7, 2),
			PartPose.offsetAndRotation(-0.51F, 0.1F, 4F, -ANGLE_10_DEG, 0F, 0F));

		PartDefinition wbl = root.addOrReplaceChild("wingBaseLeft",
			CubeListBuilder.create()
				.texOffs(42, 9)
				.addBox(-0.5F, -1F, 0F, 1, 2, 10),
			PartPose.offsetAndRotation(1F, 1F, 0F, ANGLE_10_DEG * 3, ANGLE_10_DEG * 3, 0F));

		wbl.addOrReplaceChild("wingEdgeLeft",
			CubeListBuilder.create()
				.texOffs(58, 21)
				.addBox(0F, 0F, -2F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.502F, -1F, 10F, ANGLE_10_DEG * 3, 0F, 0F));

		wbl.addOrReplaceChild("wingInsetLeft",
			CubeListBuilder.create()
				.texOffs(52, 21)
				.addBox(0F, 0F, -1F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.504F, 0F, 7.8F, ANGLE_10_DEG * 2, 0F, 0F));

		wbl.addOrReplaceChild("wingCenterLeft",
			CubeListBuilder.create()
				.texOffs(46, 21)
				.addBox(0F, 0F, -1F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.506F, 0.3F, 6.3F, ANGLE_10_DEG, 0F, 0F));

		wbl.addOrReplaceChild("wingFlangeLeft",
			CubeListBuilder.create()
				.texOffs(40, 21)
				.addBox(0F, 0F, -1F, 1, 8, 2),
			PartPose.offsetAndRotation(-0.508F, 0.3F, 5.1F, 0F, 0F, 0F));

		wbl.addOrReplaceChild("wingAuxLeft",
			CubeListBuilder.create()
				.texOffs(34, 21)
				.addBox(0F, 0F, -1F, 1, 7, 2),
			PartPose.offsetAndRotation(-0.51F, 0.1F, 4F, -ANGLE_10_DEG, 0F, 0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	public void setupModelAnimations(LivingEntity entity, float f, float f1, double ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, f, f1, (float) ageInTicks, netHeadYaw, headPitch);

		TravellerWingsAnimAttachment attachment = entity.getData(TFDataAttachments.TRAVELLER_WINGS_ANIM);

		float targetXRot, targetYRot, targetZRot;
		double dtInTicks = ageInTicks - attachment.oldAgeInTicks;

		float[] rotations;  // must be initialized later
		if (this.riding)
			rotations = calculateRotations(attachment, dtInTicks, 17f, 0.9F, -0.7f, -0.3f, new float[]{10f, 5f, 2.5f});
		else if (entity.isInWater())
			rotations = calculateRotations(attachment, dtInTicks, 17f, ANGLE_10_DEG * 3, -0.8f, -0.8f, new float[]{15f, 15f, 15f});
		else if (entity.getDeltaMovement().y < 0 && entity.fallDistance > 2.3f)
			rotations = calculateRotations(attachment, dtInTicks, 20f, 0.7F, -0.8f, -0.3f, new float[]{15f, 15f, 15f});
		else if (entity.isSprinting() || this.attackTime > 0)
			rotations = calculateRotations(attachment, dtInTicks, 1.73f, ANGLE_10_DEG * 3, -0.6f, -0.3f, new float[]{15f, 15f, 15f});
		else {
			float speedFactor = entity.getDeltaMovement().horizontalDistanceSqr() > 0 ? 6f : 17f;
			rotations = calculateRotations(attachment, dtInTicks, speedFactor, ANGLE_10_DEG * 3, -0.6f, -0.3f, new float[]{5f, 3f, 1.5f});
		}

		targetXRot = rotations[0];
		targetYRot = rotations[1];
		targetZRot = rotations[2];

		wingBaseRight.xRot = interpolateToTarget(attachment.xRotOld, targetXRot, dtInTicks);
		wingBaseRight.yRot = interpolateToTarget(attachment.yRotOld, targetYRot, dtInTicks);
		wingBaseRight.zRot = interpolateToTarget(attachment.zRotOld, targetZRot, dtInTicks);

		wingBaseLeft.xRot = wingBaseRight.xRot;
		wingBaseLeft.yRot = -wingBaseRight.yRot;
		wingBaseLeft.zRot = -wingBaseRight.zRot;

		attachment.accumulatedPhase = attachment.accumulatedPhase % (2 * Math.PI);
		attachment.oldAgeInTicks = ageInTicks;
		attachment.xRotOld = wingBaseRight.xRot;
		attachment.yRotOld = wingBaseRight.yRot;
		attachment.zRotOld = wingBaseRight.zRot;
	}

	private float[] calculateRotations(TravellerWingsAnimAttachment attachment, double dtInTicks, float phaseDivisor, float xOffset, float yOffset, float zOffset, float[] sinDivisors) {
		attachment.accumulatedPhase += dtInTicks / phaseDivisor;
		float sinT = (float) Math.sin(attachment.accumulatedPhase);
		return new float[]{
			sinT / sinDivisors[0] + xOffset,
			sinT / sinDivisors[1] + yOffset,
			sinT / sinDivisors[2] + zOffset
		};
	}

	private static float interpolateToTarget(double oPos, double targetPos, double dtInTicks) {
		return (float) (targetPos - (targetPos - oPos) * Math.exp(-dtInTicks / tau));
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
