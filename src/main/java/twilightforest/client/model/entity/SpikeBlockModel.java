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
import twilightforest.entity.SpikeBlock;

public class SpikeBlockModel extends ListModel<SpikeBlock> {
	final ModelPart block;

	public SpikeBlockModel(ModelPart root) {
		this.block = root.getChild("block");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		var block = definition.addOrReplaceChild("block", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4F, -8F, -4F, 8, 8, 8),
				PartPose.ZERO);

		// Rotation constants
		final float QUARTER_PI = 0.25F * Mth.PI;
		final float ANGLE_MINOR = -35F * Mth.DEG_TO_RAD;
		final float ANGLE_MAJOR = -55F * Mth.DEG_TO_RAD;

		block.addOrReplaceChild("spikes_0", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(0, -9, 0, 0, 0, 0));

		block.addOrReplaceChild("spikes_1", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(0, -8, 4, QUARTER_PI, 0, 0));

		block.addOrReplaceChild("spikes_2", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(4, -8, 4, ANGLE_MAJOR, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_3", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(4, -8, 0, 0, 0, QUARTER_PI));

		block.addOrReplaceChild("spikes_4", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(4, -8, -4, ANGLE_MINOR, -QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_5", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(0, -8, -4, QUARTER_PI, 0, 0));

		block.addOrReplaceChild("spikes_6", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(-4, -8, -4, ANGLE_MINOR, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_7", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(-4, -8, 0, 0, 0, QUARTER_PI));

		block.addOrReplaceChild("spikes_8", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(-4, -8, 4, ANGLE_MAJOR, -QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_9", CubeListBuilder.create() // this spike is not really there
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(0, -4, 0, 0, 0, 0));

		block.addOrReplaceChild("spikes_10", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(0, -4, 4, 0, 0, 0));

		block.addOrReplaceChild("spikes_11", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(4, -4, 5, 0, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_12", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(5, -4, 0, 0, 0, 0));

		block.addOrReplaceChild("spikes_13", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(4, -4, -4, 0, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_14", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(0, -4, -5, 0, 0, 0));

		block.addOrReplaceChild("spikes_15", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(-4, -4, -4, 0, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_16", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(-5, -4, 0, 0, 0, 0));

		block.addOrReplaceChild("spikes_17", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(-4, -4, 4, 0, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_18", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(0, 1, 0, 0, 0, 0));

		block.addOrReplaceChild("spikes_19", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(0, 0, 4, QUARTER_PI, 0, 0));

		block.addOrReplaceChild("spikes_20", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(4, 0, 4, ANGLE_MINOR, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_21", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(4, 0, 0, 0, 0, QUARTER_PI));

		block.addOrReplaceChild("spikes_22", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(4, 0, -4, ANGLE_MAJOR, -QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_23", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(0, 0, -4, QUARTER_PI, 0, 0));

		block.addOrReplaceChild("spikes_24", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(-4, 0, -4, ANGLE_MAJOR, QUARTER_PI, 0));

		block.addOrReplaceChild("spikes_25", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(-4, 0, 0, 0, 0, QUARTER_PI));

		block.addOrReplaceChild("spikes_26", CubeListBuilder.create()
						.texOffs(24, 0)
						.addBox(-1F, -1F, -1F, 2, 2, 2),
				PartPose.offsetAndRotation(-4, 0, 4, ANGLE_MINOR, -QUARTER_PI, 0));

		return LayerDefinition.create(mesh, 32, 16);
	}

	@Override
	public void setupAnim(SpikeBlock entity, float v, float v1, float v2, float v3, float v4) {

	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(this.block);
	}
}
