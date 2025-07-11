package twilightforest.client.model.armor;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import twilightforest.components.entity.TravellersWingsAnimAttachment;
import twilightforest.init.TFDataAttachments;
import twilightforest.util.TFMathUtil;

import java.util.Collections;

public class TravellersLeggingsModel extends HumanoidModel<LivingEntity> {
	private static final double TAU = 4;  // Time (in ticks) in which distance reduces in e times
	private static final float ANGLE_10_DEG = Mth.PI / 18;
	private final ModelPart wingBaseRight;
	private final ModelPart wingBaseLeft;

	public TravellersLeggingsModel(ModelPart root) {
		super(root);
		root = root.getChild("body");
		this.wingBaseLeft = root.getChild("wingBaseLeft");
		this.wingBaseRight = root.getChild("wingBaseRight");
		body.skipDraw = true;
	}

	public static LayerDefinition createLayer(float deformation) {
		MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(deformation), 0);
		PartDefinition root = mesh.getRoot().getChild("body");
		createWings(root);
		createBelt(root, 0F);

		return LayerDefinition.create(mesh, 128, 32);
	}

	protected static void createWings(PartDefinition root) {
		PartDefinition wbr = root.addOrReplaceChild("wingBaseRight",
			CubeListBuilder.create()
				.texOffs(64, 9)
				.addBox(-0.5F, -1F, 0F, 1, 2, 10),
			PartPose.offsetAndRotation(-1F, 1F, 0F, ANGLE_10_DEG * 3, -ANGLE_10_DEG * 3, 0F));

		wbr.addOrReplaceChild("wingEdgeRight",
			CubeListBuilder.create()
				.texOffs(64, 21)
				.addBox(0F, 0F, -2F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.502F, -1F, 10F, ANGLE_10_DEG * 3, 0F, 0F));

		wbr.addOrReplaceChild("wingInsetRight",
			CubeListBuilder.create()
				.texOffs(70, 21)
				.addBox(0F, 0F, -1F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.504F, 0F, 7.8F, ANGLE_10_DEG * 2, 0F, 0F));

		wbr.addOrReplaceChild("wingCenterRight",
			CubeListBuilder.create()
				.texOffs(76, 21)
				.addBox(0F, 0F, -1F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.506F, 0.3F, 6.3F, ANGLE_10_DEG, 0F, 0F));

		wbr.addOrReplaceChild("wingFlangeRight",
			CubeListBuilder.create()
				.texOffs(82, 21)
				.addBox(0F, 0F, -1F, 1, 8, 2),
			PartPose.offsetAndRotation(-0.508F, 0.3F, 5.1F, 0F, 0F, 0F));

		wbr.addOrReplaceChild("wingAuxRight",
			CubeListBuilder.create()
				.texOffs(88, 21)
				.addBox(0F, 0F, -1F, 1, 7, 2),
			PartPose.offsetAndRotation(-0.51F, 0.1F, 4F, -ANGLE_10_DEG, 0F, 0F));

		PartDefinition wbl = root.addOrReplaceChild("wingBaseLeft",
			CubeListBuilder.create()
				.texOffs(106, 9)
				.addBox(-0.5F, -1F, 0F, 1, 2, 10),
			PartPose.offsetAndRotation(1F, 1F, 0F, ANGLE_10_DEG * 3, ANGLE_10_DEG * 3, 0F));

		wbl.addOrReplaceChild("wingEdgeLeft",
			CubeListBuilder.create()
				.texOffs(122, 21)
				.addBox(0F, 0F, -2F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.502F, -1F, 10F, ANGLE_10_DEG * 3, 0F, 0F));

		wbl.addOrReplaceChild("wingInsetLeft",
			CubeListBuilder.create()
				.texOffs(116, 21)
				.addBox(0F, 0F, -1F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.504F, 0F, 7.8F, ANGLE_10_DEG * 2, 0F, 0F));

		wbl.addOrReplaceChild("wingCenterLeft",
			CubeListBuilder.create()
				.texOffs(110, 21)
				.addBox(0F, 0F, -1F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.506F, 0.3F, 6.3F, ANGLE_10_DEG, 0F, 0F));

		wbl.addOrReplaceChild("wingFlangeLeft",
			CubeListBuilder.create()
				.texOffs(104, 21)
				.addBox(0F, 0F, -1F, 1, 8, 2),
			PartPose.offsetAndRotation(-0.508F, 0.3F, 5.1F, 0F, 0F, 0F));

		wbl.addOrReplaceChild("wingAuxLeft",
			CubeListBuilder.create()
				.texOffs(98, 21)
				.addBox(0F, 0F, -1F, 1, 7, 2),
			PartPose.offsetAndRotation(-0.51F, 0.1F, 4F, -ANGLE_10_DEG, 0F, 0F));
	}

	protected static void createBelt(PartDefinition root, float deformation) {
		CubeDeformation cubeDeformation = new CubeDeformation(deformation);
		root.addOrReplaceChild(
			"buckle",
			CubeListBuilder.create()
				.texOffs(8, 9)
				.addBox(-2F, -2F, 0F, 4, 4, 1, cubeDeformation),
			PartPose.offset(0F, 10F, -2.75F)
		);

		root.addOrReplaceChild(
			"frontRight",
			CubeListBuilder.create()
				.texOffs(0, 9)
				.addBox(-3F, -1F, 0F, 3, 2, 1,cubeDeformation),
			PartPose.offset( -1.75F, 10F, -2.5F)
		);

		root.addOrReplaceChild(
			"frontLeft",
			CubeListBuilder.create()
				.texOffs(18, 9)
				.addBox(0F, -1F, 0F, 3, 2, 1, cubeDeformation),
			PartPose.offset(1.75F, 10F, -2.5F)
		);

		root.addOrReplaceChild(
			"sideRight",
			CubeListBuilder.create()
				.texOffs(0, 3)
				.addBox(-1F, -1F, 0F, 1, 2, 4, cubeDeformation),
			PartPose.offset(-3.75F, 10F, -2F)
		);

		root.addOrReplaceChild(
			"sideLeft",
			CubeListBuilder.create()
				.texOffs(16, 3)
				.addBox(0F, -1F, 0F, 1, 2, 4, cubeDeformation),
			PartPose.offset(3.75F, 10F, -2F)
		);

		root.addOrReplaceChild(
			"back",
			CubeListBuilder.create()
				.texOffs(2, 0)
				.addBox(-4.5F, -1F, 0F, 9, 2, 1, cubeDeformation),
			PartPose.offset(0F, 10F, 1.5F)
		);
	}

	public void setupModelAnimations(LivingEntity entity, float f, float f1, double ageInTicks, float netHeadYaw, float headPitch) {
		super.setupAnim(entity, f, f1, (float) ageInTicks, netHeadYaw, headPitch);

		TravellersWingsAnimAttachment attachment = entity.getData(TFDataAttachments.TRAVELLERS_WINGS_ANIM);

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

		wingBaseRight.xRot = (float) TFMathUtil.interpolateToTarget(attachment.xRotOld, targetXRot, dtInTicks, TAU);
		wingBaseRight.yRot = (float) TFMathUtil.interpolateToTarget(attachment.yRotOld, targetYRot, dtInTicks, TAU);
		wingBaseRight.zRot = (float) TFMathUtil.interpolateToTarget(attachment.zRotOld, targetZRot, dtInTicks, TAU);

		wingBaseLeft.xRot = wingBaseRight.xRot;
		wingBaseLeft.yRot = -wingBaseRight.yRot;
		wingBaseLeft.zRot = -wingBaseRight.zRot;

		attachment.accumulatedPhase = attachment.accumulatedPhase % (2 * Math.PI);
		attachment.oldAgeInTicks = ageInTicks;
		attachment.xRotOld = wingBaseRight.xRot;
		attachment.yRotOld = wingBaseRight.yRot;
		attachment.zRotOld = wingBaseRight.zRot;
	}

	private float[] calculateRotations(TravellersWingsAnimAttachment attachment, double dtInTicks, float phaseDivisor, float xOffset, float yOffset, float zOffset, float[] sinDivisors) {
		attachment.accumulatedPhase += dtInTicks / phaseDivisor;
		float sinT = (float) Math.sin(attachment.accumulatedPhase);
		return new float[]{
			sinT / sinDivisors[0] + xOffset,
			sinT / sinDivisors[1] + yOffset,
			sinT / sinDivisors[2] + zOffset
		};
	}

	@Override
	protected Iterable<ModelPart> headParts() {
		return Collections.emptyList();
	}

	@Override
	protected Iterable<ModelPart> bodyParts() {
		return ImmutableList.of(body, leftLeg, rightLeg);
	}

	public static void skipWings(ModelPart leggingsLayer, boolean skip) {
		ModelPart body = leggingsLayer.getChild("body");

		ModelPart wbl = body.getChild("wingBaseLeft");
		wbl.skipDraw                                  = skip;
		wbl.getChild("wingEdgeLeft").skipDraw   = skip;
		wbl.getChild("wingInsetLeft").skipDraw  = skip;
		wbl.getChild("wingCenterLeft").skipDraw = skip;
		wbl.getChild("wingFlangeLeft").skipDraw = skip;
		wbl.getChild("wingAuxLeft").skipDraw    = skip;

		ModelPart wbr = body.getChild("wingBaseRight");
		wbr.skipDraw                                   = skip;
		wbr.getChild("wingEdgeRight").skipDraw   = skip;
		wbr.getChild("wingInsetRight").skipDraw  = skip;
		wbr.getChild("wingCenterRight").skipDraw = skip;
		wbr.getChild("wingFlangeRight").skipDraw = skip;
		wbr.getChild("wingAuxRight").skipDraw    = skip;
	}

	public static void skipBelt(ModelPart leggingsLayer, boolean skip) {
		ModelPart body = leggingsLayer.getChild("body");

		body.getChild("buckle").skipDraw      = skip;
		body.getChild("frontRight").skipDraw  = skip;
		body.getChild("frontLeft").skipDraw   = skip;
		body.getChild("sideRight").skipDraw   = skip;
		body.getChild("sideLeft").skipDraw    = skip;
		body.getChild("back").skipDraw        = skip;
	}

}
