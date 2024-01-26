package twilightforest.client.model.entity;

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
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.boss.Naga;
import twilightforest.entity.boss.NagaSegment;

public class NagaModel<T extends Entity> extends ListModel<T> {

	public final ModelPart head;
	public final ModelPart body;
	@Nullable
	private T entity;

	public NagaModel(ModelPart root) {
		this.head = root.getChild("head");
		this.body = root.getChild("body");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-8.0F, -12.0F, -8.0F, 16.0F, 16.0F, 16.0F),
				PartPose.ZERO);

		definition.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-8.0F, -12.0F, -8.0F, 16.0F, 16.0F, 16.0F),
				PartPose.ZERO);

		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(this.head, this.body);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float scale) {
		if (this.entity instanceof Naga naga) {
			this.head.render(stack, builder, light, overlay, red, green - naga.stunlessRedOverlayProgress, blue - naga.stunlessRedOverlayProgress, scale * 2);
		} else if (this.entity instanceof NagaSegment) {
			this.body.render(stack, builder, light, overlay, red, green, blue, scale * 2);
		} else {
			this.head.render(stack, builder, light, overlay, red, green, blue, scale * 2);
		}
		this.entity = null;
	}

	@Override
	public void setupAnim(T entity, float v, float v1, float v2, float v3, float v4) {
		this.entity = entity;
	}
}
