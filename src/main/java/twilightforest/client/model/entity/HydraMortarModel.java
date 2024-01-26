package twilightforest.client.model.entity;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import twilightforest.entity.boss.HydraMortar;

public class HydraMortarModel extends HierarchicalModel<HydraMortar> {

	public final ModelPart root;

	public HydraMortarModel(ModelPart root) {
		this.root = root;
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("mortar", CubeListBuilder.create()
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
	public void setupAnim(HydraMortar entity, float v, float v1, float v2, float v3, float v4) { }
}
