package twilightforest.client.model.entity;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;

public class HydraMortarModel extends Model {

	public HydraMortarModel(ModelPart root) {
		super(root, RenderType::entityTranslucent);
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
}
