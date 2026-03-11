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
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import twilightforest.entity.monster.BaseIceMob;

public class UnstableIceCoreModel<T extends BaseIceMob> extends HierarchicalModel<T> {

	protected final ModelPart[] spikes = new ModelPart[16];
	protected final ModelPart[] cubes = new ModelPart[16];

	private final ModelPart root;
	protected boolean alive;

	public UnstableIceCoreModel(ModelPart root) {
		super(RenderType::entityTranslucent);
		this.root = root;

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
	public ModelPart root() {
		return this.root;
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, int color) {
		this.root().render(stack, builder, light, overlay, FastColor.ARGB32.color((int) (FastColor.ARGB32.alpha(color) * (alive ? 0.6F : 1.0F)), FastColor.ARGB32.red(color), FastColor.ARGB32.green(color), FastColor.ARGB32.blue(color)));
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {

	}

	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		this.alive = entity.isAlive();

		for (int i = 0; i < this.spikes.length; i++) {
			// rotate the spikes
			this.spikes[i].yRot = (entity.tickCount + partialTicks) / 5.0F;
			this.spikes[i].xRot = Mth.sin((entity.tickCount + partialTicks) / 5.0F) / 4.0F;
			this.spikes[i].zRot = Mth.cos((entity.tickCount + partialTicks) / 5.0F) / 4.0F;

			this.spikes[i].xRot += i * 5.0F;
			this.spikes[i].yRot += i * 2.5F;
			this.spikes[i].zRot += i * 3.0F;

			this.spikes[i].x = Mth.cos((entity.tickCount + partialTicks) / i) * 3.0F;
			this.spikes[i].y = 5.0F + Mth.sin((entity.tickCount + partialTicks) / i) * 3.0F;
			this.spikes[i].z = Mth.sin((entity.tickCount + partialTicks) / i) * 3.0F;

			this.cubes[i].y = 10.0F + Mth.sin((i + entity.tickCount + partialTicks) / i) * 3.0F;
		}
	}
}
