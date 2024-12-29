package twilightforest.client.model.block;

// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


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

public class BrazierModel extends Model {
	private final ModelPart leg1;
	private final ModelPart leg2;
	private final ModelPart leg3;
	private final ModelPart leg4;
	private final ModelPart basket;
	private final ModelPart charcoal;
	private final ModelPart rope;

	public BrazierModel(ModelPart root) {
		super(RenderType::entityCutoutNoCull);

		this.leg1 = root.getChild("leg1");
		this.leg2 = root.getChild("leg2");
		this.leg3 = root.getChild("leg3");
		this.leg4 = root.getChild("leg4");
		this.basket = root.getChild("basket");
		this.charcoal = root.getChild("charcoal");
		this.rope = root.getChild("rope");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition part = mesh.getRoot();

		PartDefinition leg1 = part.addOrReplaceChild("leg1", CubeListBuilder.create()
				.texOffs(24, 13)
				.addBox(-1.0F, -32.0F, -1.0F, 2.0F, 32.0F, 2.0F),
			PartPose.offsetAndRotation(-5.0F, 24.0F, 5.0F, 0.3655F, 0.7119F, 0.5299F));
		PartDefinition leg2 = part.addOrReplaceChild("leg2", CubeListBuilder.create()
				.texOffs(16, 13)
				.addBox(-1.0F, -32.0F, -1.0F, 2.0F, 32.0F, 2.0F),
			PartPose.offsetAndRotation(5.0F, 24.0F, 5.0F, 0.3655F, -0.7119F, -0.5299F));
		PartDefinition leg3 = part.addOrReplaceChild("leg3", CubeListBuilder.create()
				.texOffs(8, 13)
				.addBox(-1.0F, -32.0F, -1.0F, 2.0F, 32.0F, 2.0F),
			PartPose.offsetAndRotation(-5.0F, 24.0F, -5.0F, -0.3655F, -0.7119F, 0.5299F));
		PartDefinition leg4 = part.addOrReplaceChild("leg4", CubeListBuilder.create()
				.texOffs(0, 13)
				.addBox(-1.0F, -32.0F, -1.0F, 2.0F, 32.0F, 2.0F),
			PartPose.offsetAndRotation(5.0F, 24.0F, -5.0F, -0.3655F, 0.7119F, -0.5299F));
		PartDefinition basket = part.addOrReplaceChild("basket", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-4.0F, -5.0F, -4.0F, 8.0F, 5.0F, 8.0F),
			PartPose.offsetAndRotation(0.0F, -2.0F, 0.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition charcoal = part.addOrReplaceChild("charcoal", CubeListBuilder.create()
				.texOffs(32, 6)
				.addBox(-3.0F, -1.0F, -3.0F, 6.0F, 2.0F, 6.0F),
			PartPose.offsetAndRotation(0.0F, -3.25F, 0.0F, 0.0F, 0.7854F, 0.0F));
		PartDefinition rope = part.addOrReplaceChild("rope", CubeListBuilder.create()
				.texOffs(32, 0)
				.addBox(-2.0F, -1.0F, -2.0F, 4.0F, 2.0F, 4.0F),
			PartPose.offsetAndRotation(0.0F, 7.0F, 0.0F, 0.0F, 0.7854F, 0.0F));

		return LayerDefinition.create(mesh, 64, 64);
	}

	@Override
	public void renderToBuffer(PoseStack pose, VertexConsumer vc, int light, int overlay, int color) {
		leg1.render(pose, vc, light, overlay, color);
		leg2.render(pose, vc, light, overlay, color);
		leg3.render(pose, vc, light, overlay, color);
		leg4.render(pose, vc, light, overlay, color);
		basket.render(pose, vc, light, overlay, color);
		charcoal.render(pose, vc, light, overlay, color);
		rope.render(pose, vc, light, overlay, color);
	}
}