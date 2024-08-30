package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.entity.KnightPhantomRenderer;
import twilightforest.entity.boss.KnightPhantom;

import javax.annotation.Nullable;

public class KnightPhantomModel extends HumanoidModel<KnightPhantom> implements TrophyBlockModel {

	private static final ResourceLocation PHANTOM_ARMOR_TEXTURE = TwilightForestMod.prefix("textures/models/armor/phantom_layer_1.png");

	@Nullable
	private KnightPhantom knight;
	private ModelPart helmet;

	public KnightPhantomModel(ModelPart root) {
		super(root);
		if (root.hasChild("helmet")) {
			this.helmet = root.getChild("helmet");
		}
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create()
				.texOffs(40, 16)
				.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F),
			PartPose.offset(-5.0F, 2.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
				.texOffs(40, 16)
				.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F),
			PartPose.offset(5.0F, 2.0F, 0.0F));

		partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create().mirror()
				.texOffs(0, 16)
				.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
			PartPose.offset(-2.0F, 12.0F, 0.0F));

		partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
				.texOffs(0, 16)
				.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
			PartPose.offset(2.0F, 12.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	public static LayerDefinition createTrophy() {
		MeshDefinition meshdefinition = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition partdefinition = meshdefinition.getRoot();
		CubeDeformation deformation = new CubeDeformation(0.25F);

		partdefinition.addOrReplaceChild("head",
			CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
			PartPose.offset(0.0F, -4.0F, 0.0F));

		partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(),
			PartPose.ZERO);

		var helm = partdefinition.addOrReplaceChild("helmet",
			CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, deformation),
			PartPose.offset(0.0F, -4.0F, 0.0F));

		var rightHorn = helm.addOrReplaceChild("right_horn_1",
			CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-5.5F, -1.5F, -1.5F, 5.0F, 3.0F, 3.0F, deformation),
			PartPose.offsetAndRotation(-4.0F, -6.5F, 0.0F, 0.0F, -25.0F * Mth.DEG_TO_RAD, 45.0F * Mth.DEG_TO_RAD));

		rightHorn.addOrReplaceChild("right_horn_2",
			CubeListBuilder.create()
				.texOffs(54, 16)
				.addBox(-3.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, deformation),
			PartPose.offsetAndRotation(-4.5F, 0.0F, 0.0F, 0.0F, -15.0F * Mth.DEG_TO_RAD, 45.0F * Mth.DEG_TO_RAD));

		var leftHorn = helm.addOrReplaceChild("left_horn_1",
			CubeListBuilder.create().mirror()
				.texOffs(24, 0)
				.addBox(0.5F, -1.5F, -1.5F, 5.0F, 3.0F, 3.0F, deformation),
			PartPose.offsetAndRotation(4.0F, -6.5F, 0.0F,
				0.0F, 25.0F * Mth.DEG_TO_RAD, -45.0F * Mth.DEG_TO_RAD));

		leftHorn.addOrReplaceChild("left_horn_2",
			CubeListBuilder.create()
				.texOffs(54, 16)
				.addBox(0.5F, -1.0F, -1.0F, 3.0F, 2.0F, 2.0F, deformation),
			PartPose.offsetAndRotation(4.5F, 0.0F, 0.0F,
				0.0F, 15.0F * Mth.DEG_TO_RAD, -45.0F * Mth.DEG_TO_RAD));

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, int color) {
		if (this.knight != null && this.knight.isChargingAtPlayer()) {
			// render full skeleton
			super.renderToBuffer(stack, builder, light, overlay, color);
		}
		this.knight = null;
	}

	@Override
	public void setupAnim(KnightPhantom entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.knight = entity;

		super.setupAnim(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
		this.leftLeg.yRot = 0;
		this.leftLeg.zRot = 0;

		this.rightLeg.yRot = 0;
		this.rightLeg.zRot = 0;

		this.rightLeg.xRot = 0.2F * Mth.sin(ageInTicks * 0.3F) + 0.4F;
		this.leftLeg.xRot = 0.2F * Mth.sin(ageInTicks * 0.3F) + 0.4F;
	}

	@Override
	public void setupRotationsForTrophy(float x, float y, float z, float mouthAngle) {
		this.head.yRot = y * Mth.DEG_TO_RAD;
		this.head.xRot = z * Mth.DEG_TO_RAD;
		this.helmet.xRot = this.head.xRot;
		this.helmet.yRot = this.head.yRot;
	}

	@Override
	public void renderTrophy(PoseStack stack, MultiBufferSource buffer, int light, int overlay, int color, boolean itemForm) {
		stack.translate(0.0F, 0.25F, 0.0F);
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(KnightPhantomRenderer.TEXTURE));
		this.head.render(stack, consumer, light, overlay, color);
		stack.scale(1.1F, 1.1F, 1.1F);
		stack.translate(0.0F, 0.05F, 0.0F);
		VertexConsumer armorConsumer = buffer.getBuffer(RenderType.entityCutoutNoCull(PHANTOM_ARMOR_TEXTURE));
		this.helmet.render(stack, armorConsumer, light, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.colorFromFloat(0.0625F, 1.0F, 1.0F, 1.0F));
	}
}
