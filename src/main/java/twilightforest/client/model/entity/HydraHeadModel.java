package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import org.jspecify.annotations.Nullable;
import twilightforest.client.renderer.entity.HydraRenderer;
import twilightforest.client.state.entity.HydraHeadRenderState;

public class HydraHeadModel extends EntityModel<HydraHeadRenderState> implements TrophyBlockModel {

	private final ModelPart head;
	private final ModelPart jaw;

	public HydraHeadModel(ModelPart root) {
		super(root);
		this.head = root.getChild("head");
		this.jaw = this.head.getChild("jaw");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(272, 0)
				.addBox(-16.0F, -14.0F, -16.0F, 32.0F, 24.0F, 32.0F, new CubeDeformation(0.01F))
				.texOffs(272, 56)
				.addBox(-15.0F, -2.0F, -40.0F, 30.0F, 12.0F, 24.0F)
				.texOffs(272, 132)
				.addBox(-15F, 9F, -4F, 30, 8, 16)
				.texOffs(128, 200)
				.addBox(-2.0F, -30.0F, 4.0F, 4.0F, 24.0F, 24.0F)
				.texOffs(272, 156)
				.addBox(-12.0F, 9.0F, -33.0F, 2.0F, 5.0F, 2.0F)
				.texOffs(272, 156)
				.addBox(10.0F, 9.0F, -33.0F, 2.0F, 5.0F, 2.0F)
				.texOffs(280, 156)
				.addBox(-8.0F, 8.0F, -33.0F, 16.0F, 2.0F, 2.0F)
				.texOffs(280, 160)
				.addBox(-10.0F, 8.0F, -29.0F, 2.0F, 2.0F, 16.0F)
				.texOffs(280, 160)
				.addBox(8.0F, 8.0F, -29.0F, 2.0F, 2.0F, 16.0F),
			PartPose.ZERO);

		head.addOrReplaceChild("jaw", CubeListBuilder.create()
				.texOffs(272, 92)
				.addBox(-15.0F, 0.0F, -26.0F, 30.0F, 8.0F, 32.0F)
				.texOffs(272, 156)
				.addBox(-10.0F, -5.0F, -23.0F, 2.0F, 5.0F, 2.0F)
				.texOffs(272, 156)
				.addBox(8.0F, -5.0F, -23.0F, 2.0F, 5.0F, 2.0F)
				.texOffs(280, 156)
				.addBox(-8.0F, -1.0F, -23.0F, 16.0F, 2.0F, 2.0F)
				.texOffs(280, 160)
				.addBox(-10.0F, -1.0F, -19.0F, 2.0F, 2.0F, 16.0F)
				.texOffs(280, 160)
				.addBox(8.0F, -1.0F, -19.0F, 2.0F, 2.0F, 16.0F),
			PartPose.offset(0.0F, 10.0F, -10.0F));

		head.addOrReplaceChild("frill", CubeListBuilder.create()
				.texOffs(272, 200)
				.addBox(-24.0F, -50.0F, 16.0F, 48.0F, 48.0F, 4.0F),
			PartPose.offsetAndRotation(0.0F, 0.0F, -14.0F, -0.5235988F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 512, 256);
	}

	@Override
	public void setupAnim(HydraHeadRenderState state) {
		super.setupAnim(state);
		this.head.yRot = state.yRot * Mth.DEG_TO_RAD;
		this.head.xRot = state.xRot * Mth.DEG_TO_RAD;

		this.head.xRot -= state.mouthAngle * (Mth.PI / 12.0F);
		this.jaw.xRot = state.mouthAngle * (Mth.PI / 3.0F);
	}


	@Override
	public void setupRotationsForTrophy(float animationProgress, float mouthAngle) {
		this.jaw.xRot = mouthAngle * (Mth.PI / 3.0F);
	}

	@Override
	public void renderTrophy(PoseStack stack, SubmitNodeCollector collector, int light, int overlay, ModelFeatureRenderer.@Nullable CrumblingOverlay breakProgress, ItemDisplayContext context) {
		boolean itemForm = context != ItemDisplayContext.NONE;
		stack.scale(0.25F, 0.25F, 0.25F);
		if (itemForm) {
			stack.scale(0.9F, 0.9F, 0.9F);
		}
		if (context == ItemDisplayContext.GUI) {
			stack.translate(0.0F, 0.0F, 0.75f);
		}
		stack.translate(0.0F, -1.0F, itemForm ? -1.0F : 0.0F);
		collector.submitModelPart(this.head, stack, RenderTypes.entityCutout(HydraRenderer.TEXTURE), light, overlay, null, -1, breakProgress);
	}
}