package twilightforest.client.model.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.util.Mth;

public class CubeOfAnnihilationModel extends EntityModel<EntityRenderState> {

	private final ModelPart boxX;
	private final ModelPart boxY;
	private final ModelPart boxZ;

	public CubeOfAnnihilationModel(ModelPart root) {
		super(root);
		this.boxX = root.getChild("box_x");
		this.boxY = root.getChild("box_y");
		this.boxZ = root.getChild("box_z");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("box", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("box_x", CubeListBuilder.create()
				.texOffs(0, 32)
				.addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("box_y", CubeListBuilder.create()
				.texOffs(0, 32)
				.addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("box_z", CubeListBuilder.create()
				.texOffs(0, 32)
				.addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F),
			PartPose.ZERO);


		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(EntityRenderState state) {
		this.boxX.xRot = Mth.sin((state.ageInTicks)) / 5.0F;
		this.boxY.yRot = Mth.sin((state.ageInTicks)) / 5.0F;
		this.boxZ.zRot = Mth.sin((state.ageInTicks)) / 5.0F;
	}
}
