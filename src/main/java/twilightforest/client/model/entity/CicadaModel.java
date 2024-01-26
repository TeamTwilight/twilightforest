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

	public final ModelPart legs;
	public final ModelPart fatbody;
	public final ModelPart skinnybody;
	public final ModelPart eye1;
	public final ModelPart eye2;
	public final ModelPart wings;

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

		partdefinition.addOrReplaceChild("legs",
				CubeListBuilder.create()
						.texOffs(0, 21)
						.addBox(-4.0F, 7.9F, -5.0F, 8.0F, 1.0F, 9.0F),
				PartPose.ZERO);
		partdefinition.addOrReplaceChild("fat_body",
				CubeListBuilder.create()
						.texOffs(0, 11)
						.addBox(-2F, 6F, -4F, 4, 2, 6),
				PartPose.ZERO);
		partdefinition.addOrReplaceChild("skinny_body",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-1F, 7F, -5F, 2, 1, 8),
				PartPose.ZERO);
		partdefinition.addOrReplaceChild("eye_1",
				CubeListBuilder.create()
						.texOffs(20, 15)
						.addBox(1F, 5F, 2F, 2, 2, 2),
				PartPose.ZERO);
		partdefinition.addOrReplaceChild("eye_2",
				CubeListBuilder.create()
						.texOffs(20, 15)
						.addBox(-3F, 5F, 2F, 2, 2, 2),
				PartPose.ZERO);
		partdefinition.addOrReplaceChild("wings",
				CubeListBuilder.create()
						.texOffs(20, 0)
						.addBox(-4F, 5F, -7F, 8, 1, 8),
				PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	@Override
	public void renderToBuffer(PoseStack ms, VertexConsumer buffer, int light, int overlay, float r, float g, float b, float a) {
		this.legs.render(ms, buffer, light, overlay, r, g, b, a);
		this.fatbody.render(ms, buffer, light, overlay, r, g, b, a);
		this.skinnybody.render(ms, buffer, light, overlay, r, g, b, a);
		this.eye1.render(ms, buffer, light, overlay, r, g, b, a);
		this.eye2.render(ms, buffer, light, overlay, r, g, b, a);
		this.wings.render(ms, buffer, light, overlay, r, g, b, a);
	}
}
