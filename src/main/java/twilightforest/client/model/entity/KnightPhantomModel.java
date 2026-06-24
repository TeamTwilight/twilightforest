package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.jspecify.annotations.Nullable;
import twilightforest.TwilightForestMod;
import twilightforest.client.renderer.entity.KnightPhantomRenderer;
import twilightforest.client.state.entity.KnightPhantomRenderState;

public class KnightPhantomModel extends HumanoidModel<KnightPhantomRenderState> implements TrophyBlockModel {

	private static final Identifier PHANTOM_ARMOR_TEXTURE = TwilightForestMod.prefix("textures/models/armor/phantom_layer_1.png");

	@Nullable
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

		partdefinition.getChild("head").addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

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

		var head = partdefinition.addOrReplaceChild("head",
			CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
			PartPose.offset(0.0F, -4.0F, 0.0F));

		head.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.ZERO);

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
	public void setupAnim(KnightPhantomRenderState entity) {
		if (entity.isCharging) {
			this.root().visible = false;
		}

		super.setupAnim(entity);
		this.leftLeg.yRot = 0;
		this.leftLeg.zRot = 0;

		this.rightLeg.yRot = 0;
		this.rightLeg.zRot = 0;

		this.rightLeg.xRot = 0.2F * Mth.sin(entity.ageInTicks * 0.3F) + 0.4F;
		this.leftLeg.xRot = 0.2F * Mth.sin(entity.ageInTicks * 0.3F) + 0.4F;
	}

	@Override
	public void renderTrophy(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, ItemDisplayContext context) {
		if (context == ItemDisplayContext.GUI) {
			stack.pushPose();

			float scale = 16f / 17f; // Helmets are mixelled geometry that is 17 pixels wide when compared to the normal head, scale it down
			stack.scale(scale, scale, scale);
			stack.translate(0.0F, -0.065f, 0.0F);

			stack.pushPose();
			stack.translate(0.0F, 0.3f, 0.0F);
			collector.submitModelPart(this.helmet, stack, RenderTypes.entityCutout(PHANTOM_ARMOR_TEXTURE), light, overlay, null);
			stack.popPose();

			stack.scale(1 / 1.1F, 1 / 1.1F, 1 / 1.1F);
			stack.translate(0.0F, 0.25F, 0.0F);
			collector.submitModelPart(this.head, stack, RenderTypes.entityCutout(KnightPhantomRenderer.TEXTURE), light, overlay, null, -1, breakProgress);
			stack.popPose();
		} else {
			stack.translate(0.0F, 0.25F, 0.0F);
			collector.submitModelPart(this.head, stack, RenderTypes.entityCutout(KnightPhantomRenderer.TEXTURE), light, overlay, null, -1, breakProgress);
			stack.scale(1.1F, 1.1F, 1.1F);
			stack.translate(0.0F, 0.05F, 0.0F);
			collector.submitModelPart(this.helmet, stack, RenderTypes.entityCutout(PHANTOM_ARMOR_TEXTURE), light, overlay, null);
		}
	}
}
