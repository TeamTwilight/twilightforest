package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.IceCrystal;

public class IceCrystalModel extends HierarchicalModel<IceCrystal> {

	private final ModelPart root;
	private final ModelPart[] spikes = new ModelPart[16];

	private boolean alive;

	public IceCrystalModel(ModelPart root) {
		super(RenderType::entityTranslucent);
		this.root = root;

		for (int i = 0; i < spikes.length; i++) {
			this.spikes[i] = root.getChild("spike_" + i);
		}

	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		for (int i = 0; i < 16; i++) {

			int spikeLength = i % 2 == 0 ? 6 : 8;

			var spike = definition.addOrReplaceChild("spike_" + i, CubeListBuilder.create()
							.texOffs(0, 16)
							.addBox(-1.0F, -1.0F, -1.0F, 2.0F, spikeLength, 2.0F),
					PartPose.ZERO);

			spike.addOrReplaceChild("cube_" + i, CubeListBuilder.create()
							.texOffs(8, 16)
							.addBox(-1.5F, -1.5F, -1.5F, 3.0F, 3.0F, 3.0F),
					PartPose.offsetAndRotation(0.0F, spikeLength, 0.0F, 0.0F, 0.0F, (Mth.PI / 4F)));
		}

		return LayerDefinition.create(mesh, 32, 32);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float alpha) {
		for (ModelPart spike : spikes) {
			spike.render(stack, builder, light, overlay, red, green, blue, alive ? 0.6F : alpha);
		}
	}

	@Override
	public void setupAnim(IceCrystal entity, float v, float v1, float v2, float v3, float v4) {
	}

	@Override
	public void prepareMobModel(IceCrystal entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		this.alive = entity.isAlive();
		for (int i = 0; i < spikes.length; i++) {
			// rotate the spikes
			this.spikes[i].xRot = Mth.sin((entity.tickCount + partialTicks) / 5.0F) / 4.0F;
			this.spikes[i].yRot = (entity.tickCount + partialTicks) / 5.0F;
			this.spikes[i].zRot = Mth.cos((entity.tickCount + partialTicks) / 5.0F) / 4.0F;

			this.spikes[i].xRot += i * (Math.PI / 8F);

			if (i % 4 == 0) {
				this.spikes[i].yRot += 1;
			} else if (i % 4 == 2) {
				this.spikes[i].yRot -= 1;
			}
		}
	}
}
