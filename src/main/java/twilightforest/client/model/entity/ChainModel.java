package twilightforest.client.model.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

public class ChainModel extends EntityModel<EntityRenderState> {

	private final ModelPart chain;

	public ChainModel(ModelPart root) {
		super(root);
		this.chain = root.getChild("chain");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("chain", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 32, 16);
	}

	@Override
	public void setupAnim(EntityRenderState entityRenderState) {

	}
}
