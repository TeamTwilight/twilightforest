package twilightforest.client.model.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.util.Mth;

public class IceCrystalModel extends EntityModel<LivingEntityRenderState> {

	private final ModelPart[] spikes = new ModelPart[16];

	public IceCrystalModel(ModelPart root) {
		super(root, RenderType::entityTranslucent);

		for (int i = 0; i < spikes.length; i++) {
			this.spikes[i] = root.getChild("spike_" + i);
		}
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		for (int i = 0; i < 16; i++) {

			int spikeLength = i % 2 == 0 ? 6 : 8;

			var spike = partdefinition.addOrReplaceChild("spike_" + i, CubeListBuilder.create()
					.texOffs(0, 16)
					.addBox(-1.0F, -1.0F, -1.0F, 2.0F, spikeLength, 2.0F),
				PartPose.ZERO);

			spike.addOrReplaceChild("cube_" + i, CubeListBuilder.create()
					.texOffs(8, 16)
					.addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
				PartPose.offsetAndRotation(0.0F, spikeLength, 0.0F, 0.0F, 0.0F, (Mth.PI / 4.0F)));
		}

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(LivingEntityRenderState state) {
		for (int i = 0; i < this.spikes.length; i++) {
			// rotate the spikes
			this.spikes[i].xRot = Mth.sin((state.ageInTicks) / 5.0F) / 4.0F;
			this.spikes[i].yRot = (state.ageInTicks) / 5.0F;
			this.spikes[i].zRot = Mth.cos((state.ageInTicks) / 5.0F) / 4.0F;

			this.spikes[i].xRot += i * (Mth.PI / 8.0F);

			if (i % 4 == 0) {
				this.spikes[i].yRot += 1.0F;
			} else if (i % 4 == 2) {
				this.spikes[i].yRot -= 1.0F;
			}
		}
	}
}
