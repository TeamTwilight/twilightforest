package twilightforest.client.model.entity;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import twilightforest.entity.boss.HydraMortarHead;

public class HydraMortarModel extends HierarchicalModel<HydraMortarHead> {

	public ModelPart root;

	public HydraMortarModel(ModelPart root) {
		this.root = root;
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition partRoot = mesh.getRoot();

		partRoot.addOrReplaceChild("mortar", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.ZERO);

		return LayerDefinition.create(mesh, 32, 32);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void setupAnim(HydraMortarHead entity, float v, float v1, float v2, float v3, float v4) { }
}
