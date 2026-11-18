package twilightforest.client.model.block;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Unit;

public class BrazierModel extends Model<Unit> {

	public BrazierModel(ModelPart root) {
		super(root, RenderTypes::entityCutout);
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition part = mesh.getRoot();

		part.addOrReplaceChild("leg1", CubeListBuilder.create()
				.texOffs(24, 15)
				.addBox(-1.0F, -30.0F, -1.0F, 2.0F, 30.0F, 2.0F),
			PartPose.offsetAndRotation(-5.0F, 24.0F, 5.0F, 0.3655F, 0.7119F, 0.5299F));

		part.addOrReplaceChild("leg2", CubeListBuilder.create()
				.texOffs(16, 15)
				.addBox(-1.0F, -30.0F, -1.0F, 2.0F, 30.0F, 2.0F),
			PartPose.offsetAndRotation(5.0F, 24.0F, 5.0F, 0.3655F, -0.7119F, -0.5299F));

		part.addOrReplaceChild("leg3", CubeListBuilder.create()
				.texOffs(8, 15)
				.addBox(-1.0F, -30.0F, -1.0F, 2.0F, 30.0F, 2.0F),
			PartPose.offsetAndRotation(-5.0F, 24.0F, -5.0F, -0.3655F, -0.7119F, 0.5299F));

		part.addOrReplaceChild("leg4", CubeListBuilder.create()
				.texOffs(0, 15)
				.addBox(-1.0F, -30.0F, -1.0F, 2.0F, 30.0F, 2.0F),
			PartPose.offsetAndRotation(5.0F, 24.0F, -5.0F, -0.3655F, 0.7119F, -0.5299F));

		part.addOrReplaceChild("basket", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -5.0F, -4.0F, 8.0F, 5.0F, 8.0F),
			PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		part.addOrReplaceChild("charcoal", CubeListBuilder.create()
				.texOffs(32, 6)
				.addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F),
			PartPose.offsetAndRotation(0.0F, -1.25F, 0.0F, 0.0F, 0.7854F, 0.0F));

		part.addOrReplaceChild("rope", CubeListBuilder.create()
				.texOffs(32, 0)
				.addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F),
			PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}
}