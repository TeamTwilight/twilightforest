package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import twilightforest.entity.projectile.CubeOfAnnihilation;

public class CubeOfAnnihilationModel extends ListModel<CubeOfAnnihilation> {
	private final ModelPart box, boxX, boxY, boxZ;

	public CubeOfAnnihilationModel(ModelPart root) {
		this.box = root.getChild("box");
		this.boxX = root.getChild("box_x");
		this.boxY = root.getChild("box_y");
		this.boxZ = root.getChild("box_z");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("box", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-8F, -8F, -8F, 16, 16, 16),
				PartPose.ZERO);

		definition.addOrReplaceChild("box_x", CubeListBuilder.create()
						.texOffs(0, 32)
						.addBox(-8F, -8F, -8F, 16, 16, 16),
				PartPose.ZERO);

		definition.addOrReplaceChild("box_y", CubeListBuilder.create()
						.texOffs(0, 32)
						.addBox(-8F, -8F, -8F, 16, 16, 16),
				PartPose.ZERO);

		definition.addOrReplaceChild("box_z", CubeListBuilder.create()
						.texOffs(0, 32)
						.addBox(-8F, -8F, -8F, 16, 16, 16),
				PartPose.ZERO);


		return LayerDefinition.create(mesh, 64, 64);
	}

    @Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(
				this.box,
				this.boxX,
				this.boxY,
				this.boxZ
		);
	}

	@Override
	public void setupAnim(CubeOfAnnihilation entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.boxX.xRot = Mth.sin((entity.tickCount + headPitch)) / 5F;
		this.boxY.yRot = Mth.sin((entity.tickCount + headPitch)) / 5F;
		this.boxZ.zRot = Mth.sin((entity.tickCount + headPitch)) / 5F;
	}
}
