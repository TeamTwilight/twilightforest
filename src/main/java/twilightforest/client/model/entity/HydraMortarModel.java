package twilightforest.client.model.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import twilightforest.client.state.entity.HydraMortarRenderState;

public class HydraMortarModel extends EntityModel<HydraMortarRenderState> {

	private final ModelPart root;

	public HydraMortarModel(ModelPart root) {
		super(root);
		this.root = root;
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("mortar", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
			PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(HydraMortarRenderState renderState) {
	}
}
