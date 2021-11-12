package twilightforest.client.model.entity.legacy;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.Entity;
import twilightforest.entity.boss.Naga;
import twilightforest.entity.boss.NagaSegment;

public class NagaLegacyModel<T extends Entity> extends ListModel<T> {

	public ModelPart head;
	public ModelPart body;
	private T entity;

	public NagaLegacyModel(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition partRoot = mesh.getRoot();

		partRoot.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-8F, -12F, -8F, 16, 16, 16),
				PartPose.ZERO);

		partRoot.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-8F, -12F, -8F, 16, 16, 16),
				PartPose.ZERO);

		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(head, body);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {

		//setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);

		if (entity instanceof Naga) {
			head.render(stack, builder, light, overlay, red, green, blue, scale * 2);
		} else if (entity instanceof NagaSegment) {
			body.render(stack, builder, light, overlay, red, green, blue, scale * 2);
		} else {
			head.render(stack, builder, light, overlay, red, green, blue, scale * 2);
		}
	}

	@Override
	public void setupAnim(T entity, float v, float v1, float v2, float v3, float v4) {
		this.entity = entity;
	}
}
