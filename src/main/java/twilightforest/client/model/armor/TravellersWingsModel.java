package twilightforest.client.model.armor;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.components.entity.TravellersWingsAnimAttachment;
import twilightforest.init.TFDataAttachments;
import twilightforest.util.TFMathUtil;

import java.util.Collections;

public class TravellersWingsModel extends HumanoidModel<LivingEntity> {
	private static final double TAU = 4;  // Time (in ticks) in which distance reduces in e times
	private static final float ANGLE_10_DEG = Mth.PI / 18;
	private final ModelPart wingBaseRight;
	private final ModelPart wingBaseLeft;

	public TravellersWingsModel(ModelPart root) {
		super(root);
		root = root.getChild("body");
		this.wingBaseLeft = root.getChild("wingBaseLeft");
		this.wingBaseRight = root.getChild("wingBaseRight");
		this.body.skipDraw = true;
	}

	public static LayerDefinition createLayer(float deformation) {
		MeshDefinition mesh = HumanoidModel.createMesh(new CubeDeformation(deformation), 0);
		PartDefinition root = mesh.getRoot().getChild("body");
		createWings(root);
		createBelt(root, 0.0F);

		return LayerDefinition.create(mesh, 128, 32);
	}

	protected static void createWings(PartDefinition root) {
		PartDefinition wbr = root.addOrReplaceChild("wingBaseRight", CubeListBuilder.create()
				.texOffs(64, 9)
				.addBox(-0.5F, -1.0F, 0.0F, 1, 2, 10),
			PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, ANGLE_10_DEG * 3, -ANGLE_10_DEG * 3, 0.0F));

		wbr.addOrReplaceChild("wingEdgeRight", CubeListBuilder.create()
				.texOffs(64, 21)
				.addBox(0.0F, 0.0F, -2.0F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.502F, -1.0F, 10.0F, ANGLE_10_DEG * 3, 0.0F, 0.0F));

		wbr.addOrReplaceChild("wingInsetRight", CubeListBuilder.create()
				.texOffs(70, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.504F, 0.0F, 7.8F, ANGLE_10_DEG * 2, 0.0F, 0.0F));

		wbr.addOrReplaceChild("wingCenterRight", CubeListBuilder.create()
				.texOffs(76, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.506F, 0.3F, 6.3F, ANGLE_10_DEG, 0.0F, 0.0F));

		wbr.addOrReplaceChild("wingFlangeRight", CubeListBuilder.create()
				.texOffs(82, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 8, 2),
			PartPose.offsetAndRotation(-0.508F, 0.3F, 5.1F, 0.0F, 0.0F, 0.0F));

		wbr.addOrReplaceChild("wingAuxRight", CubeListBuilder.create()
				.texOffs(88, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 7, 2),
			PartPose.offsetAndRotation(-0.51F, 0.1F, 4.0F, -ANGLE_10_DEG, 0.0F, 0.0F));

		PartDefinition wbl = root.addOrReplaceChild("wingBaseLeft", CubeListBuilder.create()
				.texOffs(106, 9)
				.addBox(-0.5F, -1.0F, 0.0F, 1, 2, 10),
			PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, ANGLE_10_DEG * 3, ANGLE_10_DEG * 3, 0.0F));

		wbl.addOrReplaceChild("wingEdgeLeft", CubeListBuilder.create()
				.texOffs(122, 21)
				.addBox(0.0F, 0.0F, -2.0F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.502F, -1.0F, 10.0F, ANGLE_10_DEG * 3, 0.0F, 0.0F));

		wbl.addOrReplaceChild("wingInsetLeft", CubeListBuilder.create()
				.texOffs(116, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.504F, 0.0F, 7.8F, ANGLE_10_DEG * 2, 0.0F, 0.0F));

		wbl.addOrReplaceChild("wingCenterLeft", CubeListBuilder.create()
				.texOffs(110, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 9, 2),
			PartPose.offsetAndRotation(-0.506F, 0.3F, 6.3F, ANGLE_10_DEG, 0.0F, 0.0F));

		wbl.addOrReplaceChild("wingFlangeLeft", CubeListBuilder.create()
				.texOffs(104, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 8, 2),
			PartPose.offsetAndRotation(-0.508F, 0.3F, 5.1F, 0.0F, 0.0F, 0.0F));

		wbl.addOrReplaceChild("wingAuxLeft", CubeListBuilder.create()
				.texOffs(98, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 7, 2),
			PartPose.offsetAndRotation(-0.51F, 0.1F, 4.0F, -ANGLE_10_DEG, 0.0F, 0.0F));
	}

	protected static void createBelt(PartDefinition root, float deformation) {
		CubeDeformation cubeDeformation = new CubeDeformation(deformation);
		root.addOrReplaceChild("buckle", CubeListBuilder.create()
				.texOffs(8, 9)
				.addBox(-2.0F, -2.0F, 0.0F, 4, 4, 1, cubeDeformation),
			PartPose.offset(0.0F, 10.0F, -2.75F)
		);

		root.addOrReplaceChild("frontRight", CubeListBuilder.create()
				.texOffs(0, 9)
				.addBox(-3.0F, -1.0F, 0.0F, 3, 2, 1, cubeDeformation),
			PartPose.offset(-1.75F, 10.0F, -2.5F)
		);

		root.addOrReplaceChild("frontLeft", CubeListBuilder.create()
				.texOffs(18, 9)
				.addBox(0.0F, -1.0F, 0.0F, 3, 2, 1, cubeDeformation),
			PartPose.offset(1.75F, 10.0F, -2.5F)
		);

		root.addOrReplaceChild("sideRight", CubeListBuilder.create()
				.texOffs(0, 3)
				.addBox(-1.0F, -1.0F, 0.0F, 1, 2, 4, cubeDeformation),
			PartPose.offset(-3.75F, 10.0F, -2.0F)
		);

		root.addOrReplaceChild("sideLeft", CubeListBuilder.create()
				.texOffs(16, 3)
				.addBox(0.0F, -1.0F, 0.0F, 1, 2, 4, cubeDeformation),
			PartPose.offset(3.75F, 10.0F, -2.0F)
		);

		root.addOrReplaceChild("back", CubeListBuilder.create()
				.texOffs(2, 0)
				.addBox(-4.5F, -1.0F, 0.0F, 9, 2, 1, cubeDeformation),
			PartPose.offset(0.0F, 10.0F, 1.5F)
		);
	}

	public void setupModelAnimations(LivingEntity entity, float f, float f1, double ageInTicks, float netHeadYaw, float headPitch) {
		this.bodyParts().forEach(modelPart -> modelPart.getAllParts().forEach(ModelPart::resetPose));
		super.setupAnim(entity, f, f1, (float) ageInTicks, netHeadYaw, headPitch);
		TravellersWingsAnimAttachment attachment = entity.getData(TFDataAttachments.TRAVELLERS_WINGS_ANIM);

		float targetXRot, targetYRot, targetZRot;
		double dtInTicks = ageInTicks - attachment.oldAgeInTicks;

		//slightly move wings down when crouching so they arent detached
		if (entity.isCrouching()) {
			this.wingBaseRight.y += 2;
			this.wingBaseLeft.y += 2;
		}

		if (attachment.doubleJump && attachment.doubleJumpTime < 40) {
			this.wingBaseRight.xRot = (float) TFMathUtil.interpolateToTarget(attachment.xRotOld, -0.4F, dtInTicks, TAU - 1);
			this.wingBaseRight.yRot = (float) TFMathUtil.interpolateToTarget(attachment.yRotOld, -0.8F, dtInTicks, TAU - 1);
			this.wingBaseRight.zRot = (float) TFMathUtil.interpolateToTarget(attachment.zRotOld, -0.1F, dtInTicks, TAU - 1);
			attachment.doubleJumpTime++;
		} else {
			float[] rotations;  // must be initialized later
			if (this.riding)
				rotations = this.calculateRotations(attachment, dtInTicks, 10.0F, ANGLE_10_DEG * 3, -0.6F, -0.3F, new float[]{15.0F, 15.0F, 15.0F});
			else if (entity.isSwimming())
				rotations = this.calculateRotations(attachment, dtInTicks, 17.0F, ANGLE_10_DEG * 4, -1.0F, -0.5F, new float[]{15.0F, 15.0F, 15.0F});
			else if (!entity.onGround() && entity.fallDistance < 2.3F && (!(entity instanceof Player player) || !player.getAbilities().flying))
				rotations = this.calculateRotations(attachment, dtInTicks, 17.0F, ANGLE_10_DEG * 5, -1.1F, -0.1F, new float[]{15.0F, 15.0F, 15.0F});
			else if (entity.getDeltaMovement().y < 0 && entity.fallDistance > 2.3F)
				rotations = this.calculateRotations(attachment, dtInTicks, 2.0F, ANGLE_10_DEG * 4, -1.1F, -0.3F, new float[]{8.0F, 8.0F, 8.0F});
			else if (entity.isSprinting() || this.attackTime > 0)
				rotations = this.calculateRotations(attachment, dtInTicks, 2.0F, ANGLE_10_DEG * 3, -0.3F, 0.0F, new float[]{15.0F, 15.0F, 15.0F});
			else {
				boolean moving = entity.getDeltaMovement().horizontalDistanceSqr() > 0;
				float speedFactor = moving ? 4.0F : 20.0F;
				rotations = this.calculateRotations(attachment, dtInTicks, speedFactor, ANGLE_10_DEG * 3, -0.6F, -0.3F, new float[]{15.0F, 15.0F, 15.0F});
			}

			targetXRot = rotations[0];
			targetYRot = rotations[1];
			targetZRot = rotations[2];

			this.wingBaseRight.xRot = (float) TFMathUtil.interpolateToTarget(attachment.xRotOld, targetXRot, dtInTicks, TAU);
			this.wingBaseRight.yRot = (float) TFMathUtil.interpolateToTarget(attachment.yRotOld, targetYRot, dtInTicks, TAU);
			this.wingBaseRight.zRot = (float) TFMathUtil.interpolateToTarget(attachment.zRotOld, targetZRot, dtInTicks, TAU);
		}

		this.wingBaseLeft.xRot = this.wingBaseRight.xRot;
		this.wingBaseLeft.yRot = -this.wingBaseRight.yRot;
		this.wingBaseLeft.zRot = -this.wingBaseRight.zRot;

		attachment.accumulatedPhase = attachment.accumulatedPhase % Mth.TWO_PI;
		attachment.oldAgeInTicks = ageInTicks;
		attachment.xRotOld = this.wingBaseRight.xRot;
		attachment.yRotOld = this.wingBaseRight.yRot;
		attachment.zRotOld = this.wingBaseRight.zRot;
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
		wbl.skipDraw = skip;
		wbl.getChild("wingEdgeLeft").skipDraw = skip;
		wbl.getChild("wingInsetLeft").skipDraw = skip;
		wbl.getChild("wingCenterLeft").skipDraw = skip;
		wbl.getChild("wingFlangeLeft").skipDraw = skip;
		wbl.getChild("wingAuxLeft").skipDraw = skip;

		ModelPart wbr = body.getChild("wingBaseRight");
		wbr.skipDraw = skip;
		wbr.getChild("wingEdgeRight").skipDraw = skip;
		wbr.getChild("wingInsetRight").skipDraw = skip;
		wbr.getChild("wingCenterRight").skipDraw = skip;
		wbr.getChild("wingFlangeRight").skipDraw = skip;
		wbr.getChild("wingAuxRight").skipDraw = skip;
	}

	public static void skipBelt(ModelPart leggingsLayer, boolean skip) {
		ModelPart body = leggingsLayer.getChild("body");

		body.getChild("buckle").skipDraw = skip;
		body.getChild("frontRight").skipDraw = skip;
		body.getChild("frontLeft").skipDraw = skip;
		body.getChild("sideRight").skipDraw = skip;
		body.getChild("sideLeft").skipDraw = skip;
		body.getChild("back").skipDraw = skip;
	}
}
