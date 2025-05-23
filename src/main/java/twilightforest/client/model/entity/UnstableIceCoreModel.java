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

public class UnstableIceCoreModel extends EntityModel<LivingEntityRenderState> {

	protected final ModelPart[] spikes = new ModelPart[16];
	protected final ModelPart[] cubes = new ModelPart[16];

	public UnstableIceCoreModel(ModelPart root) {
		super(root, RenderType::entityTranslucent);

		for (int i = 0; i < this.spikes.length; i++) {
			this.spikes[i] = root.getChild("spike_" + i);
		}

		for (int i = 0; i < this.cubes.length; i++) {
			this.cubes[i] = this.spikes[i].getChild("cube_" + i);
		}
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
			PartPose.ZERO);

		for (int i = 0; i < 16; i++) {
			float spikeLength = i % 2 == 0 ? 6.0F : 8.0F;

			var spike = partdefinition.addOrReplaceChild("spike_" + i, CubeListBuilder.create()
					.texOffs(0, 16)
					.addBox(-1.0F, 4.0F, -1.0F, 2.0F, spikeLength, 2.0F),
				PartPose.offset(0.0F, 4.0F, 0.0F));

			spike.addOrReplaceChild("cube_" + i, CubeListBuilder.create()
					.texOffs(8, 16)
					.addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
				PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, Mth.PI / 4.0F));
		}

		return LayerDefinition.create(meshdefinition, 32, 32);
	}

	@Override
	public void setupAnim(LivingEntityRenderState state) {
		super.setupAnim(state);

		for (int i = 0; i < this.spikes.length; i++) {
			// rotate the spikes
			this.spikes[i].yRot = state.ageInTicks / 5.0F;
			this.spikes[i].xRot = Mth.sin((state.ageInTicks) / 5.0F) / 4.0F;
			this.spikes[i].zRot = Mth.cos((state.ageInTicks) / 5.0F) / 4.0F;

			this.spikes[i].xRot += i * 5.0F;
			this.spikes[i].yRot += i * 2.5F;
			this.spikes[i].zRot += i * 3.0F;

			this.spikes[i].x = Mth.cos((state.ageInTicks) / i) * 3.0F;
			this.spikes[i].y = 5.0F + Mth.sin((state.ageInTicks) / i) * 3.0F;
			this.spikes[i].z = Mth.sin((state.ageInTicks) / i) * 3.0F;

			this.cubes[i].y = 10.0F + Mth.sin((i + state.ageInTicks) / i) * 3.0F;
		}
	}
}
