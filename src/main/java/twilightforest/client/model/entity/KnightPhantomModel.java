package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import twilightforest.entity.boss.KnightPhantom;

import javax.annotation.Nullable;

public class KnightPhantomModel extends HumanoidModel<KnightPhantom> {

	@Nullable
	private KnightPhantom knight;

	public KnightPhantomModel(ModelPart root) {
		super(root);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = HumanoidModel.createMesh(CubeDeformation.NONE, 0.0F);
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("hat", CubeListBuilder.create(),
				PartPose.ZERO);

		definition.addOrReplaceChild("right_arm", CubeListBuilder.create()
						.texOffs(40, 16)
						.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F),
				PartPose.offset(-5.0F, 2.0F, 0.0F));

		definition.addOrReplaceChild("left_arm", CubeListBuilder.create().mirror()
						.texOffs(40, 16)
						.addBox(-1.0F, -2.0F, -1.0F, 2.0F, 12.0F, 2.0F),
				PartPose.offset(5.0F, 2.0F, 0.0F));

		definition.addOrReplaceChild("right_leg", CubeListBuilder.create().mirror()
						.texOffs(0, 16)
						.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
				PartPose.offset(-2.0F, 12.0F, 0.0F));

		definition.addOrReplaceChild("left_leg", CubeListBuilder.create().mirror()
						.texOffs(0, 16)
						.addBox(-1.0F, 0.0F, -1.0F, 2.0F, 12.0F, 2.0F),
				PartPose.offset(2.0F, 12.0F, 0.0F));

		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {
		if (this.knight != null && this.knight.isChargingAtPlayer()) {
			// render full skeleton
			super.renderToBuffer(stack, builder, light, overlay, red, green, blue, scale);
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
}
