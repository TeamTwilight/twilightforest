package twilightforest.client.model.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import twilightforest.client.state.TFGhastRenderState;

public class TFGhastModel extends EntityModel<TFGhastRenderState> {

	protected final static int tentacleCount = 9;
	private final ModelPart body;
	private final ModelPart[] tentacles = new ModelPart[tentacleCount];

	public TFGhastModel(ModelPart root) {
		super(root);
		this.body = root.getChild("body");

		for (int i = 0; i < this.tentacles.length; i++) {
			this.tentacles[i] = this.body.getChild("tentacle_" + i);
		}
	}

	public static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create()
				.texOffs(0, 0)
				.addBox(-8.0F, -8.0F, -8.0F, 16.0F, 16.0F, 16.0F),
			PartPose.offset(0.0F, 8.0F, 0.0F));

		RandomSource rand = RandomSource.create(1660L);

		for (int i = 0; i < TFGhastModel.tentacleCount; ++i) {
			makeTentacle(body, "tentacle_" + i, rand, i);
		}

		return LayerDefinition.create(meshdefinition, 64, 32);
	}

	private static void makeTentacle(PartDefinition parent, String name, RandomSource random, int i) {
		final float length = random.nextInt(7) + 8.0F;

		// Please ensure the model is working accurately before we port
		float xPoint = ((i % 3 - i / 3.0F % 2 * 0.5F + 0.25F) / 2.0F * 2.0F - 1.0F) * 5.0F;
		float zPoint = ((i % 3 - i)) * 1.5F + 4.0F;

		parent.addOrReplaceChild(name, CubeListBuilder.create()
				.addBox(-1.0F, 0.0F, -1.0F, 2.0F, length, 2.0F),
			PartPose.offset(xPoint, 7, zPoint));
	}

	@Override
	public void setupAnim(TFGhastRenderState state) {
		super.setupAnim(state);
		// wave tentacles
		for (int i = 0; i < this.tentacles.length; ++i) {
			this.tentacles[i].xRot = 0.2F * Mth.sin(state.ageInTicks * 0.3F + i) + 0.4F;
		}

		// make body face what we're looking at
		this.body.xRot = state.xRot * Mth.DEG_TO_RAD;
		this.body.yRot = state.yRot * Mth.DEG_TO_RAD;
	}
}
