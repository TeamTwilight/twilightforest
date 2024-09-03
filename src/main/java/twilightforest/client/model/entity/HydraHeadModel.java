package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.ListModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import twilightforest.client.JappaPackReloadListener;
import twilightforest.client.renderer.entity.HydraRenderer;
import twilightforest.entity.boss.HydraHead;
import twilightforest.entity.boss.HydraPart;

public class HydraHeadModel<T extends HydraHead> extends ListModel<T> implements TrophyBlockModel {

	private final ModelPart head;
	private final ModelPart jaw;

	public HydraHeadModel(ModelPart root) {
		this.head = root.getChild("head");
		this.jaw = this.head.getChild("jaw");
	}

	public static LayerDefinition checkForPack() {
		return JappaPackReloadListener.INSTANCE.isJappaPackLoaded() ? createJappaModel() : create();
	}

	private static LayerDefinition create() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(272, 0)
				.addBox(-16.0F, -14.0F, -16.0F, 32.0F, 24.0F, 32.0F)
				.texOffs(272, 56)
				.addBox(-15.0F, -2.0F, -40.0F, 30.0F, 12.0F, 24.0F)
				.texOffs(272, 132)
				.addBox(-15F, 10F, -4F, 30, 8, 16)
				.texOffs(128, 200)
				.addBox(-2.0F, -30.0F, 4.0F, 4.0F, 24.0F, 24.0F)
				.texOffs(272, 156)
				.addBox(-12.0F, 10.0F, -33.0F, 2.0F, 5.0F, 2.0F)
				.texOffs(272, 156)
				.addBox(10.0F, 10.0F, -33.0F, 2.0F, 5.0F, 2.0F)
				.texOffs(280, 156)
				.addBox(-8.0F, 9.0F, -33.0F, 16.0F, 2.0F, 2.0F)
				.texOffs(280, 160)
				.addBox(-10.0F, 9.0F, -29.0F, 2.0F, 2.0F, 16.0F)
				.texOffs(280, 160)
				.addBox(8.0F, 9.0F, -29.0F, 2.0F, 2.0F, 16.0F),
			PartPose.ZERO);

		head.addOrReplaceChild("jaw", CubeListBuilder.create()
				.texOffs(272, 92)
				.addBox(-15.0F, 0.0F, -16.0F, 30.0F, 8.0F, 32.0F)
				.texOffs(272, 156)
				.addBox(-10.0F, -5.0F, -13.0F, 2.0F, 5.0F, 2.0F)
				.texOffs(272, 156)
				.addBox(8.0F, -5.0F, -13.0F, 2.0F, 5.0F, 2.0F)
				.texOffs(280, 156)
				.addBox(-8.0F, -1.0F, -13.0F, 16.0F, 2.0F, 2.0F)
				.texOffs(280, 160)
				.addBox(-10.0F, -1.0F, -9.0F, 2.0F, 2.0F, 16.0F)
				.texOffs(280, 160)
				.addBox(8.0F, -1.0F, -9.0F, 2.0F, 2.0F, 16.0F),
			PartPose.offset(0.0F, 10.0F, -20.0F));

		head.addOrReplaceChild("frill", CubeListBuilder.create()
				.texOffs(272, 200)
				.addBox(-24.0F, -50.0F, 16.0F, 48.0F, 48.0F, 4.0F),
			PartPose.offsetAndRotation(0.0F, 0.0F, -14.0F, -0.5235988F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 512, 256);
	}

	private static LayerDefinition createJappaModel() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		var head = partdefinition.addOrReplaceChild("head", CubeListBuilder.create()
				.texOffs(260, 64)
				.addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F)
				.texOffs(236, 128)
				.addBox(-16.0F, -2.0F, -40.0F, 32.0F, 10.0F, 24.0F)
				.texOffs(356, 70)
				.addBox(-12.0F, 8.0F, -36.0F, 24.0F, 6.0F, 20.0F),
			PartPose.ZERO);

		head.addOrReplaceChild("jaw", CubeListBuilder.create()
				.texOffs(240, 162)
				.addBox(-15.0F, 0.0F, -24.0F, 30.0F, 8.0F, 24.0F),
			PartPose.offset(0.0F, 10.0F, -14.0F));

		head.addOrReplaceChild("plate", CubeListBuilder.create()
				.texOffs(388, 0)
				.addBox(-24.0F, -48.0F, 0.0F, 48.0F, 48.0F, 6.0F)
				.texOffs(220, 0)
				.addBox(-4.0F, -32.0F, -8.0F, 8.0F, 32.0F, 8.0F),
			PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.7853981633974483F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 512, 256);
	}

	@Override
	public Iterable<ModelPart> parts() {
		return ImmutableList.of(this.head);
	}

	@Override
	public void setupAnim(HydraHead entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.head.z = -16.0F;
	}

	@Override
	public void prepareMobModel(T entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		this.head.yRot = this.getRotationY(entity, partialTicks);
		this.head.xRot = this.getRotationX(entity, partialTicks);

		float mouthOpenLast = entity.getMouthOpenLast();
		float mouthOpenReal = entity.getMouthOpen();
		float mouthOpen = Mth.lerp(partialTicks, mouthOpenLast, mouthOpenReal);
		this.head.xRot -= mouthOpen * (Mth.PI / 12.0F);
		this.jaw.xRot = mouthOpen * (Mth.PI / 3.0F);
	}

	public float getRotationY(HydraPart whichHead, float time) {
		float yaw = whichHead.yRotO + (whichHead.getYRot() - whichHead.yRotO) * time;

		return yaw * Mth.DEG_TO_RAD;
	}

	public float getRotationX(HydraPart whichHead, float time) {
		return (whichHead.xRotO + (whichHead.getXRot() - whichHead.xRotO) * time) * Mth.DEG_TO_RAD;
	}

	@Override
	public void setupRotationsForTrophy(float x, float y, float z, float mouthAngle) {
		this.head.yRot = y * Mth.DEG_TO_RAD;
		this.head.xRot = z * Mth.DEG_TO_RAD;

		this.head.xRot -= mouthAngle * (Mth.PI / 12.0F);
		this.jaw.xRot = mouthAngle * (Mth.PI / 3.0F);
	}

	@Override
	public void renderTrophy(PoseStack stack, MultiBufferSource buffer, int light, int overlay, int color, ItemDisplayContext context) {
		boolean itemForm = context != ItemDisplayContext.NONE;
		stack.scale(0.25F, 0.25F, 0.25F);
		if (itemForm) {
			stack.scale(0.9F, 0.9F, 0.9F);
		}
		if (context == ItemDisplayContext.GUI) {
			stack.translate(0.0F, 0.0F, 0.75f);
		}
		stack.translate(0.0F, -1.0F, itemForm && !JappaPackReloadListener.INSTANCE.isJappaPackLoaded() ? -1.0F : 0.0F);
		VertexConsumer consumer = buffer.getBuffer(RenderType.entityCutoutNoCull(HydraRenderer.TEXTURE));
		this.head.render(stack, consumer, light, overlay, color);
	}
}