package twilightforest.client.model.entity;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;

public class SpikeBlockModel extends Model {

	public SpikeBlockModel(ModelPart root) {
		super(root, RenderType::entityCutoutNoCull);
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var block = partdefinition.addOrReplaceChild("block", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F),
			PartPose.ZERO);

		// Rotation constants
		final float QUARTER_PI = 0.25F * Mth.PI;
		final float ANGLE_MINOR = -35.0F * Mth.DEG_TO_RAD;
		final float ANGLE_MAJOR = -55.0F * Mth.DEG_TO_RAD;

		block.addOrReplaceChild("spikes_0", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offset(0.0F, -9.0F, 0.0F));

		block.addOrReplaceChild("spikes_1", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(0.0F, -8.0F, 4.0F, QUARTER_PI, 0.0F, 0.0F));

		block.addOrReplaceChild("spikes_2", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(4.0F, -8.0F, 4.0F, ANGLE_MAJOR, QUARTER_PI, 0.0F));

		block.addOrReplaceChild("spikes_3", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(4.0F, -8.0F, 0.0F, 0.0F, 0.0F, QUARTER_PI));

		block.addOrReplaceChild("spikes_4", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(4.0F, -8.0F, -4.0F, ANGLE_MINOR, -QUARTER_PI, 0.0F));

		block.addOrReplaceChild("spikes_5", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(0.0F, -8.0F, -4.0F, QUARTER_PI, 0.0F, 0.0F));

		block.addOrReplaceChild("spikes_6", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-4.0F, -8.0F, -4.0F, ANGLE_MINOR, QUARTER_PI, 0.0F));

		block.addOrReplaceChild("spikes_7", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-4.0F, -8.0F, 0.0F, 0.0F, 0.0F, QUARTER_PI));

		block.addOrReplaceChild("spikes_8", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-4.0F, -8.0F, 4.0F, ANGLE_MAJOR, -QUARTER_PI, 0.0F));

		block.addOrReplaceChild("spikes_9", CubeListBuilder.create() // this spike is not really there
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offset(0.0F, -4, 0.0F));

		block.addOrReplaceChild("spikes_10", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(0.0F, -4.0F, 4.0F, 0.0F, 0.0F, 0.0F));

		block.addOrReplaceChild("spikes_11", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(4.0F, -4.0F, 5.0F, 0.0F, QUARTER_PI, 0.0F));

		block.addOrReplaceChild("spikes_12", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offset(5.0F, -4.0F, 0.0F));

		block.addOrReplaceChild("spikes_13", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(4.0F, -4.0F, -4.0F, 0.0F, QUARTER_PI, 0.0F));

		block.addOrReplaceChild("spikes_14", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offset(0.0F, -4.0F, -5.0F));

		block.addOrReplaceChild("spikes_15", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-4.0F, -4.0F, -4.0F, 0.0F, QUARTER_PI, 0.0F));

		block.addOrReplaceChild("spikes_16", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offset(-5.0F, -4.0F, 0.0F));

		block.addOrReplaceChild("spikes_17", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-4.0F, -4.0F, 4.0F, 0.0F, QUARTER_PI, 0.0F));

		block.addOrReplaceChild("spikes_18", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offset(0.0F, 1.0F, 0.0F));

		block.addOrReplaceChild("spikes_19", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(0.0F, 0.0F, 4.0F, QUARTER_PI, 0.0F, 0.0F));

		block.addOrReplaceChild("spikes_20", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(4.0F, 0.0F, 4.0F, ANGLE_MINOR, QUARTER_PI, 0.0F));

		block.addOrReplaceChild("spikes_21", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(4.0F, 0.0F, 0.0F, 0.0F, 0.0F, QUARTER_PI));

		block.addOrReplaceChild("spikes_22", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(4.0F, 0.0F, -4.0F, ANGLE_MAJOR, -QUARTER_PI, 0.0F));

		block.addOrReplaceChild("spikes_23", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(0.0F, 0.0F, -4.0F, QUARTER_PI, 0.0F, 0.0F));

		block.addOrReplaceChild("spikes_24", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-4.0F, 0.0F, -4.0F, ANGLE_MAJOR, QUARTER_PI, 0.0F));

		block.addOrReplaceChild("spikes_25", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-4.0F, 0.0F, 0.0F, 0.0F, 0.0F, QUARTER_PI));

		block.addOrReplaceChild("spikes_26", CubeListBuilder.create()
				.texOffs(24, 0)
				.addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F),
			PartPose.offsetAndRotation(-4.0F, 0.0F, 4.0F, ANGLE_MINOR, -QUARTER_PI, 0.0F));

		return LayerDefinition.create(meshdefinition, 32, 16);
	}
}
