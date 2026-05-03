package twilightforest.client.model.entity;

import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.util.Mth;

/**
 * Keepsake Casket Model - MCVinnyq
 * Created using Tabula 8.0.0
 */
public class KeepsakeCasketModel extends Model<Float> {

	private final ModelPart lid;

	public KeepsakeCasketModel(ModelPart root) {
		super(root, RenderTypes::entityCutout);
		this.lid = root.getChild("lid");
	}

	public static LayerDefinition create(boolean addSpikes) {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		var lid = partdefinition.addOrReplaceChild("lid", CubeListBuilder.create()
				.texOffs(0, 0).addBox(-8.0F, -8.0F, -13.0F, 16.0F, 10.0F, 14.0F),
			PartPose.offset(0.0F, -6.0F, 6.0F));

		if (addSpikes) {
			lid.addOrReplaceChild("spikes", CubeListBuilder.create()
					.texOffs(0, 46).addBox(-8.0F, -10.0F, -13.0F, 16.0F, 2.0F, 0.0F)
					.texOffs(2, 34).addBox(-7.99F, -10.0F, -12.0F, 0.0F, 2.0F, 14.0F)
					.texOffs(2, 36).addBox(7.99F, -10.0F, -12.0F, 0.0F, 2.0F, 14.0F),
				PartPose.ZERO);
		}

		partdefinition.addOrReplaceChild("base", CubeListBuilder.create()
				.texOffs(1, 28).addBox(-7.0F, -10.0F, -2.0F, 14.0F, 10.0F, 8.0F)
				.texOffs(0, 26).addBox(-7.0F, -10.0F, -6.0F, 1.0F, 6.0F, 4.0F)
				.texOffs(40, 26).addBox(6.0F, -10.0F, -6.0F, 1.0F, 6.0F, 4.0F)
				.texOffs(0, 56).addBox(-7.0F, -4.0F, -6.0F, 14.0F, 4.0F, 4.0F),
			PartPose.offset(0.0F, -0.01F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(Float state) {
		super.setupAnim(state);
		this.lid.xRot = state * -Mth.HALF_PI;
	}
}
