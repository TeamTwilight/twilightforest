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

public class CicadaModel extends Model {

	private final ModelPart legs;
	private final ModelPart fatbody;
	private final ModelPart skinnybody;
	private final ModelPart eye1;
	private final ModelPart eye2;
	private final ModelPart wings;

	public CicadaModel(ModelPart root) {
		super(RenderType::entityCutoutNoCull);

		this.legs = root.getChild("legs");
		this.fatbody = root.getChild("fat_body");
		this.skinnybody = root.getChild("skinny_body");
		this.eye1 = root.getChild("eye_1");
		this.eye2 = root.getChild("eye_2");
		this.wings = root.getChild("wings");
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("legs", CubeListBuilder.create()
				.texOffs(0, 21)
				.addBox(-4.0F, 7.9F, -5.0F, 8.0F, 1.0F, 9.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("fat_body", CubeListBuilder.create()
				.texOffs(0, 11)
				.addBox(-2.0F, 6.0F, -4.0F, 4.0F, 2.0F, 6.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("skinny_body", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-1.0F, 7.0F, -5.0F, 2.0F, 1.0F, 8.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("eye_1", CubeListBuilder.create()
				.texOffs(20, 15)
				.addBox(1.0F, 5.0F, 2.0F, 2.0F, 2.0F, 2.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("eye_2", CubeListBuilder.create()
				.texOffs(20, 15)
				.addBox(-3.0F, 5.0F, 2.0F, 2.0F, 2.0F, 2.0F),
			PartPose.ZERO);

		partdefinition.addOrReplaceChild("wings", CubeListBuilder.create()
				.texOffs(20, 0)
				.addBox(-4.0F, 5.0F, -7.0F, 8.0F, 1.0F, 8.0F),
			PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack ms, VertexConsumer buffer, int light, int overlay, int color) {
		this.legs.render(ms, buffer, light, overlay, color);
		this.fatbody.render(ms, buffer, light, overlay, color);
		this.skinnybody.render(ms, buffer, light, overlay, color);
		this.eye1.render(ms, buffer, light, overlay, color);
		this.eye2.render(ms, buffer, light, overlay, color);
		this.wings.render(ms, buffer, light, overlay, color);
	}
}
