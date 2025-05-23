package twilightforest.client.model.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import twilightforest.client.state.ProtectionBoxRenderState;

public class ProtectionBoxModel extends EntityModel<ProtectionBoxRenderState> {

	public ModelPart box;
	private int lastPixelsX;
	private int lastPixelsY;
	private int lastPixelsZ;

	public ProtectionBoxModel(ModelPart root) {
		super(root);
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
	public void setupAnim(ProtectionBoxRenderState state) {
		super.setupAnim(state);
		int pixelsX = state.sizeX * 16 + 2;
		int pixelsY = state.sizeY * 16 + 2;
		int pixelsZ = state.sizeZ * 16 + 2;

		if (pixelsX != this.lastPixelsX || pixelsY != this.lastPixelsY || pixelsZ != this.lastPixelsZ) {
			this.resizeBoxElement(pixelsX, pixelsY, pixelsZ);
		}
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
