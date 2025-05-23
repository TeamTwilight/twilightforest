package twilightforest.client.model.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;

public class RedThreadModel extends Model {

	private final ModelPart center;
	private final ModelPart up;
	private final ModelPart down;
	private final ModelPart left;
	private final ModelPart right;

	public RedThreadModel(ModelPart root) {
		super(root, RenderType::entityCutoutNoCull);
		this.center = root.getChild("center");
		this.up = root.getChild("up");
		this.down = root.getChild("down");
		this.left = root.getChild("left");
		this.right = root.getChild("right");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("center", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-1.0F, 0.2F, -1.0F, 2.0F, 0.0F, 2.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("up", CubeListBuilder.create()
				.texOffs(0, 2)
				.addBox(-1.0F, 0.2F, -8.0F, 2.0F, 0.0F, 7.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("down", CubeListBuilder.create()
				.texOffs(4, 2)
				.addBox(-1.0F, 0.2F, 1.0F, 2.0F, 0.0F, 7.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("left", CubeListBuilder.create()
				.texOffs(0, 10)
				.addBox(-8.0F, 0.2F, -1.0F, 7.0F, 0.0F, 2.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("right", CubeListBuilder.create()
				.texOffs(0, 14)
				.addBox(1.0F, 0.2F, -1.0F, 7.0F, 0.0F, 2.0F),
			PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	public void renderCenterPiece(PoseStack stack, VertexConsumer consumer, int light) {
		this.center.render(stack, consumer, light, OverlayTexture.NO_OVERLAY);
	}

	/**
	 * Helper method for getting the part of the model that needs to be rendered,
	 * based on what face the part is sitting on and what direction it's pointing towards.
	 */
	public ModelPart getPart(Direction face, Direction direction) {
		return switch (face) {
			case DOWN -> switch (direction) {
				case NORTH -> this.up;
				case EAST -> this.right;
				case SOUTH -> this.down;
				default -> this.left;
			};
			case UP -> switch (direction) {
				case NORTH -> this.up;
				case WEST -> this.right;
				case SOUTH -> this.down;
				default -> this.left;
			};
			case NORTH -> switch (direction) {
				case UP -> this.up;
				case EAST -> this.right;
				case DOWN -> this.down;
				default -> this.left;
			};
			case SOUTH -> switch (direction) {
				case UP -> this.down;
				case WEST -> this.left;
				case DOWN -> this.up;
				default -> this.right;
			};
			case WEST -> switch (direction) {
				case UP -> this.left;
				case NORTH -> this.up;
				case DOWN -> this.right;
				default -> this.down;
			};
			case EAST -> switch (direction) {
				case UP -> this.right;
				case SOUTH -> this.down;
				case DOWN -> this.left;
				default -> this.up;
			};
		};
	}
}
