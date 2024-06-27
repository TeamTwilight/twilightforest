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

public class FireflyModel extends Model {
	//fields
	public final ModelPart legs;
	public final ModelPart fatbody;
	public final ModelPart skinnybody;
	public final ModelPart glow;

	public FireflyModel(ModelPart root) {
		super(RenderType::entityCutoutNoCull);

		this.legs = root.getChild("legs");
		this.fatbody = root.getChild("fat_body");
		this.skinnybody = root.getChild("skinny_body");
		this.glow = root.getChild("glow");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();
		partdefinition.addOrReplaceChild("legs",
			CubeListBuilder.create()
				.texOffs(0, 21)
				.addBox(-4.0F, 7.9F, -5.0F, 8.0F, 1.0F, 10.0F),
			PartPose.ZERO);
		partdefinition.addOrReplaceChild("fat_body",
			CubeListBuilder.create()
				.texOffs(0, 11)
				.addBox(-2.0F, 6.0F, -4.0F, 4.0F, 2.0F, 6.0F),
			PartPose.ZERO);
		partdefinition.addOrReplaceChild("skinny_body",
			CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-1.0F, 6.9F, -5.0F, 2.0F, 1.0F, 8.0F),
			PartPose.ZERO);
		partdefinition.addOrReplaceChild("glow",
			CubeListBuilder.create()
				.texOffs(20, 0)
				.addBox(-5.0F, 5.9F, -9.0F, 10.0F, 0.0F, 10.0F),
			PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack stack, VertexConsumer consumer, int light, int overlay, float red, float green, float blue, float alpha) {
		this.legs.render(stack, consumer, light, overlay, red, green, blue, alpha);
		this.fatbody.render(stack, consumer, light, overlay, red, green, blue, alpha);
		this.skinnybody.render(stack, consumer, light, overlay, red, green, blue, alpha);
	}
}
