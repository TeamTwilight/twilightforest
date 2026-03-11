package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import twilightforest.client.JappaPackReloadListener;
import twilightforest.entity.boss.HydraNeck;

public class HydraNeckModel extends ListModel<HydraNeck> {

	private final ModelPart neck;

	public HydraNeckModel(ModelPart root) {
		this.neck = root.getChild("neck");
	}

	public static LayerDefinition checkForPack() {
		return JappaPackReloadListener.INSTANCE.isJappaPackLoaded() ? createJappaModel() : create();
	}

	private static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("neck", CubeListBuilder.create()
				.texOffs(128, 136)
				.addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
				.texOffs(128, 200)
				.addBox(-2.0F, -23.0F, 0.0F, 4.0F, 24.0F, 24.0F),
			PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 512, 256);
	}

	private static LayerDefinition createJappaModel() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		partdefinition.addOrReplaceChild("neck", CubeListBuilder.create()
				.texOffs(260, 0)
				.addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
				.texOffs(0, 0)
				.addBox(-2.0F, -24.0F, 0.0F, 4.0F, 8.0F, 16.0F),
			PartPose.ZERO);

		return LayerDefinition.create(meshdefinition, 512, 256);
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(this.neck);
	}

	@Override
	public void setupAnim(HydraNeck entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.neck.yRot = netHeadYaw * Mth.DEG_TO_RAD;
		this.neck.xRot = headPitch * Mth.DEG_TO_RAD;
	}
}