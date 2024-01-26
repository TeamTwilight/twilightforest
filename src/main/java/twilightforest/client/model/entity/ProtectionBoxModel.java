package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import twilightforest.entity.ProtectionBox;

public class ProtectionBoxModel<T extends ProtectionBox> extends ListModel<T> {

	private T entity;

	public ModelPart box;
	private int lastPixelsX;
	private int lastPixelsY;
	private int lastPixelsZ;

	public ProtectionBoxModel(ModelPart root) {
		this.box = root.getChild("box");
	}

	public static MeshDefinition createMesh() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("box", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F),
				PartPose.ZERO);

		return mesh;
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(this.box);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer builder, int light, int overlay, float red, float green, float blue, float alpha) {
		ProtectionBox boxEntity = entity;

		int pixelsX = boxEntity.sizeX * 16 + 2;
		int pixelsY = boxEntity.sizeY * 16 + 2;
		int pixelsZ = boxEntity.sizeZ * 16 + 2;

		if (pixelsX != this.lastPixelsX || pixelsY != this.lastPixelsY || pixelsZ != this.lastPixelsZ) {
			resizeBoxElement(pixelsX, pixelsY, pixelsZ);
		}

		super.renderToBuffer(stack, builder, light, overlay, red, green, blue, alpha);
	}

	@Override
	public void setupAnim(T entity, float v, float v1, float v2, float v3, float v4) {

	}

	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		this.entity = entity;
	}

	private void resizeBoxElement(int pixelsX, int pixelsY, int pixelsZ) {

		MeshDefinition mesh = createMesh();
		PartDefinition definition = mesh.getRoot();

		definition.addOrReplaceChild("box", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-1.0F, -1.0F, -1.0F, pixelsX, pixelsY, pixelsZ),
				PartPose.ZERO);
		box = definition.getChild("box").bake(16, 16);

		this.lastPixelsX = pixelsX;
		this.lastPixelsY = pixelsY;
		this.lastPixelsZ = pixelsZ;
	}
}
