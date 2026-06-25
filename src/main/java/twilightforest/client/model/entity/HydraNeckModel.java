package twilightforest.client.model.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import twilightforest.client.state.entity.HydraNeckRenderState;

public class HydraNeckModel extends EntityModel<HydraNeckRenderState> {

	private final ModelPart neck;

	public HydraNeckModel(ModelPart root) {
		super(root);
		this.neck = root.getChild("neck");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("neck", CubeListBuilder.create()
				.texOffs(128, 136)
				.addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
				.texOffs(128, 200)
				.addBox(-2.0F, -23.0F, 0.0F, 4.0F, 24.0F, 24.0F),
			PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 512, 256);
	}

	@Override
	public void setupAnim(HydraNeckRenderState entity) {
		// The HydraNeckRenderer already applies the neck's yaw rotation to the pose stack
		// (via stack.mulPose(Axis.YN.rotationDegrees(yaw2 + 180))).
		// The model should NOT apply yaw/xRot here, matching the old 1.21.1 behavior
		// where netHeadYaw came from renderYawOffset (always 0 for PartEntity).
	}
}