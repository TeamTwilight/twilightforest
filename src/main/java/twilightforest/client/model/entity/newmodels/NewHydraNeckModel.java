package twilightforest.client.model.entity.newmodels;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import twilightforest.entity.boss.HydraNeck;

public class NewHydraNeckModel extends ListModel<HydraNeck> {

	final ModelPart neck;

	public NewHydraNeckModel(ModelPart root) {
		this.neck = root.getChild("neck");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("neck", CubeListBuilder.create()
						.texOffs(260, 0)
						.addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
						.texOffs(0, 0)
						.addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
				PartPose.ZERO);

		return LayerDefinition.create(mesh, 512, 256);
	}

	@Override
	public void setupAnim(HydraNeck entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		neck.yRot = netHeadYaw / 57.29578F;
		neck.xRot = headPitch / 57.29578F;
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(neck);
	}
}
