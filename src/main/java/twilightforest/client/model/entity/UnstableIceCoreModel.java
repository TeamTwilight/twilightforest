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
import twilightforest.entity.monster.BaseIceMob;

public class UnstableIceCoreModel<T extends BaseIceMob> extends HierarchicalModel<T> {

	public final ModelPart[] spikes = new ModelPart[16];
	public final ModelPart[] cubes = new ModelPart[16];

	private final ModelPart root;
	protected boolean alive;

	public UnstableIceCoreModel(ModelPart root) {
		super(RenderType::entityTranslucent);
		this.root = root;

		for (int i = 0; i < spikes.length; i++) {
			this.spikes[i] = root.getChild("spike_" + i);
		}

		for (int i = 0; i < cubes.length; i++) {
			this.cubes[i] = spikes[i].getChild("cube_" + i);
		}
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-4.0F, 0.0F, -4.0F, 8.0F, 8.0F, 8.0F),
				PartPose.ZERO);

		for (int i = 0; i < 16; i++) {
			int spikeLength = i % 2 == 0 ? 6 : 8;

			var spike = definition.addOrReplaceChild("spike_" + i, CubeListBuilder.create()
							.texOffs(0, 16)
							.addBox(-1.0F, 4.0F, -1.0F, 2, spikeLength, 2),
					PartPose.offset(0.0F, 4.0F, 0.0F));

			spike.addOrReplaceChild("cube_" + i, CubeListBuilder.create()
							.texOffs(8, 16)
							.addBox(-1.5F, -1.5F, -1.5F, 3, 3, 3),
					PartPose.offsetAndRotation(0.0F, 8.0F, 0.0F, 0.0F, 0.0F, Mth.PI / 4.0F));
		}

		return LayerDefinition.create(mesh, 32, 32);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float alpha) {
		this.root().render(stack, builder, light, overlay, red, green, blue, alive ? 0.6F : alpha);
	}

	@Override
	public void setupAnim(T t, float v, float v1, float v2, float v3, float v4) {

	}

	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		this.alive = entity.isAlive();

		for (int i = 0; i < this.spikes.length; i++) {
			// rotate the spikes
			this.spikes[i].yRot = (entity.tickCount + partialTicks) / 5.0F;
			this.spikes[i].xRot = Mth.sin((entity.tickCount + partialTicks) / 5.0F) / 4.0F;
			this.spikes[i].zRot = Mth.cos((entity.tickCount + partialTicks) / 5.0F) / 4.0F;

			this.spikes[i].xRot += i * 5;
			this.spikes[i].yRot += i * 2.5f;
			this.spikes[i].zRot += i * 3;

			this.spikes[i].x = Mth.cos((entity.tickCount + partialTicks) / i) * 3F;
			this.spikes[i].y = 5F + Mth.sin((entity.tickCount + partialTicks) / i) * 3F;
			this.spikes[i].z = Mth.sin((entity.tickCount + partialTicks) / i) * 3F;

			this.cubes[i].y = 10 + Mth.sin((i + entity.tickCount + partialTicks) / i) * 3F;
		}
	}
}
