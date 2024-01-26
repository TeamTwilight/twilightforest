package twilightforest.client.model.entity;

import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import twilightforest.entity.monster.CarminiteGhastguard;

import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class TFGhastModel<T extends CarminiteGhastguard> extends HierarchicalModel<T> {
	protected final static int tentacleCount = 9;
	private final ModelPart root, body;
	private final ModelPart[] tentacles = new ModelPart[tentacleCount];

	public TFGhastModel(ModelPart root) {
		this.root = root;
		this.body = this.root.getChild("body");

		for (int i = 0; i < this.tentacles.length; i++) {
			this.tentacles[i] = this.body.getChild("tentacle_" + i);
		}
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		var body = definition.addOrReplaceChild("body", CubeListBuilder.create()
						.texOffs(0, 0)
						.addBox(-8.0F, -8.0F, -8.0F, 16, 16, 16),
				PartPose.offset(0, 8, 0));

		Random rand = new Random(1660L);

		for (int i = 0; i < TFGhastModel.tentacleCount; ++i) {
			makeTentacle(body, "tentacle_" + i, rand, i);
		}

		return LayerDefinition.create(mesh, 64, 32);
	}

	@Override
	public ModelPart root() {
		return this.root;
	}

	private static PartDefinition makeTentacle(PartDefinition parent, String name, Random random, int i) {
		final int length = random.nextInt(7) + 8;

		// Please ensure the model is working accurately before we port
		float xPoint = ((i % 3 - i / 3.0F % 2 * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
		float zPoint = ((i % 3 - i)) * 1.5F + 4.0F;

		return parent.addOrReplaceChild(name, CubeListBuilder.create()
						.addBox(-1.0F, 0.0F, -1.0F, 2, length, 2),
				PartPose.offset(xPoint, 7, zPoint));
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		// wave tentacles
		for (int i = 0; i < this.tentacles.length; ++i) {
			this.tentacles[i].xRot = 0.2F * Mth.sin(ageInTicks * 0.3F + i) + 0.4F;
		}

		// make body face what we're looking at
		this.body.xRot = headPitch / (180F / (float) Math.PI);
		this.body.yRot = netHeadYaw / (180F / (float) Math.PI);
	}
}
