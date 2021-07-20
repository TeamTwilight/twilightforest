package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.boss.EntityTFHydraHead;
import twilightforest.entity.boss.EntityTFHydraPart;

public class ModelTFHydraHead extends SegmentedModel<EntityTFHydraHead> {

	ModelRenderer head;
	ModelRenderer mouth;
	ModelRenderer plate;

	public ModelTFHydraHead() {
		textureWidth = 512;
		textureHeight = 256;

		this.head = new ModelRenderer(this, 0, 0);
		this.head.setRotationPoint(0F, 0F, 0F);
		this.head.setTextureOffset(260, 64).addBox(-16.0F, -16.0F, -16.0F, 32.0F, 32.0F, 32.0F, 0.0F, 0.0F, 0.0F);
		this.head.setTextureOffset(236, 128).addBox(-16.0F, -2.0F, -40.0F, 32.0F, 10.0F, 24.0F, 0.0F, 0.0F, 0.0F);
		this.head.setTextureOffset(356, 70).addBox(-12.0F, 8.0F, -36.0F, 24.0F, 6.0F, 20.0F, 0.0F, 0.0F, 0.0F);


		this.plate = new ModelRenderer(this, 0, 0);
		this.plate.setRotationPoint(0.0F, 0.0F, 0.0F);
		this.plate.setTextureOffset(388, 0).addBox(-24.0F, -48.0F, 0.0F, 48.0F, 48.0F, 6.0F, 0.0F, 0.0F, 0.0F);
		this.plate.setTextureOffset(220, 0).addBox(-4.0F, -32.0F, -8.0F, 8.0F, 32.0F, 8.0F, 0.0F, 0.0F, 0.0F);
		this.setRotateAngle(plate, -0.7853981633974483F, 0.0F, 0.0F);

		head.addChild(plate);

		this.mouth = new ModelRenderer(this, 0, 0);
		this.mouth.setRotationPoint(0.0F, 10.0F, -14.0F);
		this.mouth.setTextureOffset(240, 162).addBox(-15.0F, 0.0F, -24.0F, 30.0F, 8.0F, 24.0F, 0.0F, 0.0F, 0.0F);

		head.addChild(mouth);
	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(head);
	}

	@Override
	public void setRotationAngles(EntityTFHydraHead entity, float v, float v1, float v2, float v3, float v4) { }

	@Override
	public void setLivingAnimations(EntityTFHydraHead entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		head.rotateAngleY = getRotationY(entity, partialTicks);
		head.rotateAngleX = getRotationX(entity, partialTicks);

		float mouthOpenLast = entity.getMouthOpenLast();
		float mouthOpenReal = entity.getMouthOpen();
		float mouthOpen = MathHelper.lerp(partialTicks, mouthOpenLast, mouthOpenReal);
		head.rotateAngleX -= (float) (mouthOpen * (Math.PI / 12.0));
		mouth.rotateAngleX = (float) (mouthOpen * (Math.PI / 3.0));
	}

	public float getRotationY(EntityTFHydraPart whichHead, float time) {
		float yaw = whichHead.prevRotationYaw + (whichHead.rotationYaw - whichHead.prevRotationYaw) * time;

		return yaw / 57.29578F;
	}

	public float getRotationX(EntityTFHydraPart whichHead, float time) {
		return (whichHead.prevRotationPitch + (whichHead.rotationPitch - whichHead.prevRotationPitch) * time) / 57.29578F;
	}

	public void setRotateAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}
