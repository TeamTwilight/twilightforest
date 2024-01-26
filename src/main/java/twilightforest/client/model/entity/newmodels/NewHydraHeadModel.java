package twilightforest.client.model.entity.newmodels;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import twilightforest.entity.boss.HydraHead;
import twilightforest.entity.boss.HydraPart;

public class NewHydraHeadModel extends ListModel<HydraHead> {

	public final ModelPart head;
	public final ModelPart mouth;

	public NewHydraHeadModel(ModelPart root) {
		this.head = root.getChild("head");
		this.mouth = head.getChild("mouth");
	}

	public static LayerDefinition create() {
		MeshDefinition mesh = new MeshDefinition();
		PartDefinition definition = mesh.getRoot();

		var head = definition.addOrReplaceChild("head", CubeListBuilder.create()
						.texOffs(260, 64)
						.addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
						.texOffs(236, 128)
						.addBox(-16.0F, -2.0F, -40.0F, 32.0F, 10.0F, 24.0F)
						.texOffs(356, 70)
						.addBox(-12.0F, 8.0F, -36.0F, 24.0F, 6.0F, 20.0F),
				PartPose.ZERO);

		head.addOrReplaceChild("mouth", CubeListBuilder.create()
						.texOffs(240, 162)
						.addBox(-15.0F, 0.0F, -24.0F, 30.0F, 8.0F, 24.0F),
				PartPose.offset(0.0F, 10.0F, -14.0F));

		head.addOrReplaceChild("plate", CubeListBuilder.create()
						.texOffs(388, 0)
						.addBox(-24.0F, -48.0F, 0.0F, 48.0F, 48.0F, 6.0F)
						.texOffs(220, 0)
						.addBox(-4.0F, -32.0F, -8.0F, 8.0F, 32.0F, 8.0F),
				PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7853981633974483F, 0.0F, 0.0F));

		return LayerDefinition.create(mesh, 512, 256);
	}

	@Override
	public void setupAnim(HydraHead entity, float v, float v1, float v2, float v3, float v4) { }

	@Override
	public void prepareMobModel(HydraHead entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		head.yRot = getRotationY(entity, partialTicks);
		head.xRot = getRotationX(entity, partialTicks);

		float mouthOpenLast = entity.getMouthOpenLast();
		float mouthOpenReal = entity.getMouthOpen();
		float mouthOpen = Mth.lerp(partialTicks, mouthOpenLast, mouthOpenReal);
		head.xRot -= (float) (mouthOpen * (Math.PI / 12.0));
		mouth.xRot = (float) (mouthOpen * (Math.PI / 3.0));
	}

	public float getRotationY(HydraPart whichHead, float time) {
		float yaw = whichHead.yRotO + (whichHead.getYRot() - whichHead.yRotO) * time;

		return yaw / 57.29578F;
	}

	public float getRotationX(HydraPart whichHead, float time) {
		return (whichHead.xRotO + (whichHead.getXRot() - whichHead.xRotO) * time) / 57.29578F;
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(head);
	}
}
