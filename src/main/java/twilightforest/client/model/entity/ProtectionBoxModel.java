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
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.ProtectionBox;

public class ProtectionBoxModel<T extends ProtectionBox> extends ListModel<T> {

	@Nullable
	private T entity;

	public ModelPart box;
	private int lastPixelsX;
	private int lastPixelsY;
	private int lastPixelsZ;

	public ProtectionBoxModel(ModelPart root) {
		this.box = root.getChild("box");
	}

	public static MeshDefinition createMesh() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("box", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 16.0F),
			PartPose.ZERO);

		return meshdefinition;
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(this.box);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, int color) {
		if (this.entity != null) {
			int pixelsX = this.entity.sizeX * 16 + 2;
			int pixelsY = this.entity.sizeY * 16 + 2;
			int pixelsZ = this.entity.sizeZ * 16 + 2;

			if (pixelsX != this.lastPixelsX || pixelsY != this.lastPixelsY || pixelsZ != this.lastPixelsZ) {
				this.resizeBoxElement(pixelsX, pixelsY, pixelsZ);
			}
		}

		super.renderToBuffer(stack, consumer, light, overlay, color);
		this.entity = null;
	}

	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.entity = entity;
	}

	private void resizeBoxElement(int pixelsX, int pixelsY, int pixelsZ) {

		MeshDefinition meshdefinition = createMesh();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("box", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-1.0F, -1.0F, -1.0F, pixelsX, pixelsY, pixelsZ),
			PartPose.ZERO);
		this.box = partdefinition.getChild("box").bake(16, 16);

		this.lastPixelsX = pixelsX;
		this.lastPixelsY = pixelsY;
		this.lastPixelsZ = pixelsZ;
	}
}
