package twilightforest.client.model.tileentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import twilightforest.TwilightForestMod;

public class UrGhastTrophyModel extends GenericTrophyModel {

	public final ModelPart body;
	private final ModelPart[][] tentacles = new ModelPart[9][3];
	
	public UrGhastTrophyModel(ModelPart part) {
		this.body = part.getChild("body");

		for (int i = 0; i < this.tentacles.length; i++) {
			this.tentacles[i][0] = body.getChild("tentacle_" + i + "_base");
			this.tentacles[i][1] = this.tentacles[i][0].getChild("tentacle_" + i + "_extension");
			this.tentacles[i][2] = this.tentacles[i][1].getChild("tentacle_" + i + "_tip");
		}
	}

	public static LayerDefinition createHead() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var body = partdefinition.addOrReplaceChild("body",
				CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F),
				PartPose.offset(0.0F, 8.0F, 0.0F));

		for (int i = 0; i < 9; ++i) {
			makeTentacle(body, "tentacle_" + i, i);
		}

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	private static void makeTentacle(PartDefinition parent, String name, int iteration) {
		var tentacleBase = parent.addOrReplaceChild(name + "_base", CubeListBuilder.create()
						.addBox(-1.5F, 0.0F, -1.5F, 3.333F, 5.333F, 3.333F),
				switch (iteration) {
					case 0 -> PartPose.offset(4.5F, 7, 4.5F);
					case 1 -> PartPose.offset(-4.5F, 7, 4.5F);
					case 2 -> PartPose.offset(0F, 7, 0F);
					case 3 -> PartPose.offset(5.5F, 7, -4.5F);
					case 4 -> PartPose.offset(-5.5F, 7, -4.5F);
					case 5 -> PartPose.offsetAndRotation(-7.5F, 3.5F, -1F, 0F, 0F, Mth.PI / 4.0F);
					case 6 -> PartPose.offsetAndRotation(-7.5F, -1.5F, 3.5F, 0F, 0F, Mth.PI / 3.0F);
					case 7 -> PartPose.offsetAndRotation(7.5F, 3.5F, -1F, 0F, 0F, -Mth.PI / 4.0F);
					case 8 -> PartPose.offsetAndRotation(7.5F, -1.5F, 3.5F, 0F, 0F, -Mth.PI / 3.0F);
					default -> {
						TwilightForestMod.LOGGER.warn("Out of bounds with Ur-Ghast Trophy limb creation: Iteration " + iteration);
						yield PartPose.ZERO;
					}
				});

		var tentacleExtension = tentacleBase.addOrReplaceChild(name + "_extension", CubeListBuilder.create()
						.texOffs(0, 3)
						.addBox(-1.5F, -1.35F, -1.5F, 3.333F, 6.66F, 3.333F),
				PartPose.offset(0, 6.66F, 0));

		tentacleExtension.addOrReplaceChild(name + "_tip", CubeListBuilder.create()
						.texOffs(0, 9)
						.addBox(-1.5F, 1.3F, -1.5F, 3.333F, 4, 3.333F),
				PartPose.offset(0, 4, 0));

	}
	
	@Override
	public void setRotations(float x, float y, float z) {
		this.body.yRot = y * ((float) Math.PI / 180F);
		this.body.xRot = z * ((float) Math.PI / 180F);
		for (int i = 0; i < this.tentacles.length; ++i) {

			float wiggle = Math.min(x, 0.6F);

			float time = ((x * .35F) + (i * 9)) / 2.0F;

			this.tentacles[i][1].xRot = (Mth.cos(time * 0.6662F) - (float) Math.PI / 3.0F) * wiggle;
			this.tentacles[i][2].xRot = Mth.cos(time * 0.7774F) * 1.2F * wiggle;

			this.tentacles[i][1].xRot = 0.2F + Mth.cos(time * 0.3335F) * 0.15F;
			this.tentacles[i][2].xRot = 0.1F + Mth.cos(time * 0.4445F) * 0.20F;

			float yTwist = 0.4F;

			this.tentacles[i][0].xRot = 0.2F * Mth.sin(time * 0.3F + i) + 0.4F;
			this.tentacles[i][0].yRot = yTwist * Mth.sin(time * 0.3F);
		}
	}

	public void setTranslate(PoseStack matrix, float x, float y, float z) {
		matrix.translate(x, y, z);
	}

	@Override
	public void renderToBuffer(PoseStack matrix, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		this.body.render(matrix, buffer, packedLight, packedOverlay, red, green, blue, alpha);
	}

}
