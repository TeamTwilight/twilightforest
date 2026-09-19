package twilightforest.client.model.armor;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.context.ContextKey;
import org.joml.Vector3f;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.TravellersWingsAnimAttachment;
import twilightforest.components.entity.TravellersWingsAttachment;
import twilightforest.util.TFMathUtil;

import java.util.List;

public class TravellersWingsModel extends HumanoidModel<HumanoidRenderState> {

	public static final ContextKey<WingsPose> WINGS_POSE_KEY = new ContextKey<>(TwilightForestMod.prefix("travellers_wings_pose"));

	private static final double TAU = 4;  // Time (in ticks) in which distance reduces in e times
	private static final float ANGLE_10_DEG = Mth.PI / 18;
	private static final Vector3f SMALL_SWING = new Vector3f(8.0F, 8.0F, 8.0F);
	private static final Vector3f BIG_SWING = new Vector3f(15.0F, 15.0F, 15.0F);

	private static final float BASE_OFFSET = -0.50F;
	private static final float PART_OFFSET = 0.002f;

	private final ModelPart wingBaseRight;
	private final ModelPart wingBaseLeft;
	private final List<ModelPart> wingPartsRight;
	private final List<ModelPart> wingPartsLeft;

	public TravellersWingsModel(ModelPart root) {
		super(root);
		root = root.getChild("body");
		this.wingBaseLeft = root.getChild("wingBaseLeft");
		this.wingPartsLeft = List.of(
			this.wingBaseLeft.getChild("wingEdgeLeft"),
			this.wingBaseLeft.getChild("wingInsetLeft"),
			this.wingBaseLeft.getChild("wingCenterLeft"),
			this.wingBaseLeft.getChild("wingFlangeLeft"),
			this.wingBaseLeft.getChild("wingAuxLeft")
		);
		this.wingBaseRight = root.getChild("wingBaseRight");
		this.wingPartsRight = List.of(
			this.wingBaseRight.getChild("wingEdgeRight"),
			this.wingBaseRight.getChild("wingInsetRight"),
			this.wingBaseRight.getChild("wingCenterRight"),
			this.wingBaseRight.getChild("wingFlangeRight"),
			this.wingBaseRight.getChild("wingAuxRight")
		);
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
		PartDefinition wbl = root.addOrReplaceChild("wingBaseLeft", CubeListBuilder.create()
				.texOffs(64, 9).mirror()
				.addBox(BASE_OFFSET, -1.0F, 0.0F, 1, 2, 10),
			PartPose.offsetAndRotation(1.0F, 1.0F, 0.0F, ANGLE_10_DEG * 3, ANGLE_10_DEG * 3, 0.0F));

		wbl.addOrReplaceChild("wingEdgeLeft", CubeListBuilder.create()
				.texOffs(64, 21).mirror()
				.addBox(0.0F, 0.0F, -2.0F, 1, 9, 2),
			PartPose.offsetAndRotation(BASE_OFFSET + PART_OFFSET * 1, -1.0F, 10.0F, ANGLE_10_DEG * 3, 0.0F, 0.0F));

		wbl.addOrReplaceChild("wingInsetLeft", CubeListBuilder.create()
				.texOffs(70, 21).mirror()
				.addBox(0.0F, 0.0F, -1.0F, 1, 9, 2),
			PartPose.offsetAndRotation(BASE_OFFSET + PART_OFFSET * 2, 0.0F, 7.8F, ANGLE_10_DEG * 2, 0.0F, 0.0F));

		wbl.addOrReplaceChild("wingCenterLeft", CubeListBuilder.create()
				.texOffs(76, 21).mirror()
				.addBox(0.0F, 0.0F, -1.0F, 1, 9, 2),
			PartPose.offsetAndRotation(BASE_OFFSET + PART_OFFSET * 3, 0.3F, 6.3F, ANGLE_10_DEG, 0.0F, 0.0F));

		wbl.addOrReplaceChild("wingFlangeLeft", CubeListBuilder.create()
				.texOffs(82, 21).mirror()
				.addBox(0.0F, 0.0F, -1.0F, 1, 8, 2),
			PartPose.offsetAndRotation(BASE_OFFSET + PART_OFFSET * 4, 0.3F, 5.1F, 0.0F, 0.0F, 0.0F));

		wbl.addOrReplaceChild("wingAuxLeft", CubeListBuilder.create()
				.texOffs(88, 21).mirror()
				.addBox(0.0F, 0.0F, -1.0F, 1, 7, 2),
			PartPose.offsetAndRotation(BASE_OFFSET + PART_OFFSET * 5, 0.1F, 4.0F, -ANGLE_10_DEG, 0.0F, 0.0F));

		PartDefinition wbr = root.addOrReplaceChild("wingBaseRight", CubeListBuilder.create()
				.texOffs(98, 9)
				.addBox(BASE_OFFSET, -1.0F, 0.0F, 1, 2, 10),
			PartPose.offsetAndRotation(-1.0F, 1.0F, 0.0F, ANGLE_10_DEG * 3, -ANGLE_10_DEG * 3, 0.0F));

		wbr.addOrReplaceChild("wingEdgeRight", CubeListBuilder.create()
				.texOffs(98, 21)
				.addBox(0.0F, 0.0F, -2.0F, 1, 9, 2),
			PartPose.offsetAndRotation(BASE_OFFSET - PART_OFFSET * 1, -1.0F, 10.0F, ANGLE_10_DEG * 3, 0.0F, 0.0F));

		wbr.addOrReplaceChild("wingInsetRight", CubeListBuilder.create()
				.texOffs(104, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 9, 2),
			PartPose.offsetAndRotation(BASE_OFFSET - PART_OFFSET * 2, 0.0F, 7.8F, ANGLE_10_DEG * 2, 0.0F, 0.0F));

		wbr.addOrReplaceChild("wingCenterRight", CubeListBuilder.create()
				.texOffs(110, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 9, 2),
			PartPose.offsetAndRotation(BASE_OFFSET - PART_OFFSET * 3, 0.3F, 6.3F, ANGLE_10_DEG, 0.0F, 0.0F));

		wbr.addOrReplaceChild("wingFlangeRight", CubeListBuilder.create()
				.texOffs(116, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 8, 2),
			PartPose.offsetAndRotation(BASE_OFFSET - PART_OFFSET * 4, 0.3F, 5.1F, 0.0F, 0.0F, 0.0F));

		wbr.addOrReplaceChild("wingAuxRight", CubeListBuilder.create()
				.texOffs(122, 21)
				.addBox(0.0F, 0.0F, -1.0F, 1, 7, 2),
			PartPose.offsetAndRotation(BASE_OFFSET - PART_OFFSET * 5, 0.1F, 4.0F, -ANGLE_10_DEG, 0.0F, 0.0F));

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
				.addBox(-1.0F, -1.0F, 0.0F, 1, 2, 4, cubeDeformation.extend(-0.01F)),
			PartPose.offset(-3.75F, 10.0F, -2.0F)
		);

		root.addOrReplaceChild("sideLeft", CubeListBuilder.create()
				.texOffs(16, 3)
				.addBox(0.0F, -1.0F, 0.0F, 1, 2, 4, cubeDeformation.extend(-0.01F)),
			PartPose.offset(3.75F, 10.0F, -2.0F)
		);

		root.addOrReplaceChild("back", CubeListBuilder.create()
				.texOffs(2, 0)
				.addBox(-4.5F, -1.0F, 0.0F, 9, 2, 1, cubeDeformation),
			PartPose.offset(0.0F, 10.0F, 1.5F)
		);
	}

	public static WingsPose advanceAnimation(TravellersWingsAnimAttachment animAttachment, TravellersWingsAttachment attachment, float ageInTicks, float walkAnimationSpeed) {
		double dtInTicks = ageInTicks - animAttachment.oldAgeInTicks;
		if (dtInTicks <= 0)
			return new WingsPose(animAttachment.xRotOld, animAttachment.yRotOldRight, animAttachment.yRotOldLeft, animAttachment.zRotOld);

		Vector3f rightWingRotations;  // must be initialized later
		double interpolationSpeed = TAU;
		switch (attachment.state) {
			case DOUBLE_JUMP -> {
				rightWingRotations = new Vector3f(-0.4F, -0.8F, -0.1F);
				interpolationSpeed = TAU - 1;
			}
			case RIDE -> rightWingRotations = calculateRotations(animAttachment, dtInTicks, 10.0F, ANGLE_10_DEG * 3, -0.6F, -0.3F, BIG_SWING);
			case SWIM -> rightWingRotations = calculateRotations(animAttachment, dtInTicks, 17.0F, ANGLE_10_DEG * 4, -1.0F, -0.5F, BIG_SWING);
			case FALL_SLOW -> rightWingRotations = calculateRotations(animAttachment, dtInTicks, 17.0F, ANGLE_10_DEG * 5, -1.1F, -0.1F, BIG_SWING);
			case FALL_FAST -> rightWingRotations = calculateRotations(animAttachment, dtInTicks, 2.0F, ANGLE_10_DEG * 4, -1.1F, -0.3F, SMALL_SWING);
			case SPRINT -> rightWingRotations = calculateRotations(animAttachment, dtInTicks, 2.0F, ANGLE_10_DEG * 3, -0.3F, 0.0F, BIG_SWING);
			case SIDESTEP -> rightWingRotations = calculateRotations(animAttachment, dtInTicks, 2.0F, ANGLE_10_DEG * 3, attachment.sidestepLeft ? -0.6F : 0.4F, 0.0F, BIG_SWING);
			default -> {
				float phaseDivisor = walkAnimationSpeed > 0.1 ? 4.0F : 20.0F;  // use 0.1 instead of isMoving to avoid increasing animation speed when legs barely move
				rightWingRotations = calculateRotations(animAttachment, dtInTicks, phaseDivisor, ANGLE_10_DEG * 3, -0.6F, -0.3F, BIG_SWING);
			}
		}

		float targetLeftWingY = attachment.state.equals(TravellersWingsAttachment.WingState.SIDESTEP)
			? calculateRotations(animAttachment, dtInTicks, 2.0F, ANGLE_10_DEG * 3, attachment.sidestepLeft ? -0.4F : 0.6F, 0.0F, BIG_SWING).y()
			: -rightWingRotations.y();

		animAttachment.accumulatedPhase = animAttachment.accumulatedPhase % Mth.TWO_PI;
		animAttachment.oldAgeInTicks = ageInTicks;
		animAttachment.xRotOld = (float) TFMathUtil.interpolateToTarget(animAttachment.xRotOld, rightWingRotations.x, dtInTicks, interpolationSpeed);
		animAttachment.yRotOldRight = (float) TFMathUtil.interpolateToTarget(animAttachment.yRotOldRight, rightWingRotations.y, dtInTicks, interpolationSpeed);
		animAttachment.yRotOldLeft = (float) TFMathUtil.interpolateToTarget(animAttachment.yRotOldLeft, targetLeftWingY, dtInTicks, interpolationSpeed);
		animAttachment.zRotOld = (float) TFMathUtil.interpolateToTarget(animAttachment.zRotOld, rightWingRotations.z, dtInTicks, interpolationSpeed);

		return new WingsPose(animAttachment.xRotOld, animAttachment.yRotOldRight, animAttachment.yRotOldLeft, animAttachment.zRotOld);
	}

	@Override
	public void setupAnim(HumanoidRenderState state) {
		super.setupAnim(state);
		WingsPose pose = state.getRenderData(WINGS_POSE_KEY);
		if (pose == null)
			return;

		//slightly move wings down when crouching so they arent detached
		if (state.isCrouching) {
			this.wingBaseRight.y += 2;
			this.wingBaseLeft.y += 2;
		}

		this.wingBaseRight.xRot = pose.xRot();
		this.wingBaseRight.yRot = pose.yRotRight();
		this.wingBaseRight.zRot = pose.zRot();
		this.wingBaseLeft.xRot = pose.xRot();
		this.wingBaseLeft.yRot = pose.yRotLeft();
		this.wingBaseLeft.zRot = -pose.zRot();

		// If the wing model keeps a non-changing offset then looking at it with a spyglass even 4 chunks away will reveal Z-fighting.
		float distance = (float) (Math.sqrt(state.distanceToCameraSq) * PART_OFFSET);
		// The below solution is to animate its offset based off of camera distance. The animation is not time-based.
		int partCount = Math.min(this.wingPartsLeft.size(), this.wingPartsRight.size());
		for (int partIndex = 0; partIndex < partCount; partIndex++) {
			float offset = (partIndex + 1) * distance;
			this.wingPartsLeft.get(partIndex).x = BASE_OFFSET + offset;
			this.wingPartsRight.get(partIndex).x = BASE_OFFSET - offset;
		}
	}

	private static Vector3f calculateRotations(TravellersWingsAnimAttachment attachment, double dtInTicks, float phaseDivisor, float xOffset, float yOffset, float zOffset, Vector3f sinDivisors) {
		attachment.accumulatedPhase += dtInTicks / phaseDivisor;
		float sinT = (float) Math.sin(attachment.accumulatedPhase);
		return new Vector3f(
			sinT / sinDivisors.x + xOffset,
			sinT / sinDivisors.y + yOffset,
			sinT / sinDivisors.z + zOffset
		);
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

	public record WingsPose(float xRot, float yRotRight, float yRotLeft, float zRot) {
	}
}
