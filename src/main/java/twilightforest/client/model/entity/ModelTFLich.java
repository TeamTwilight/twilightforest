package twilightforest.client.model.entity;

import com.google.common.collect.Iterables;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.boss.EntityTFLich;

import java.util.Arrays;

public class ModelTFLich extends BipedModel<EntityTFLich> {
	private final ModelRenderer collar;
	private final ModelRenderer cloak;

	private boolean shadowClone;

	public ModelTFLich() {
		super(0.0F, 0.0F, 64, 64);

		bipedBody = new ModelRenderer(this, 8, 16);
		bipedBody.addBox(-4F, 0.0F, -2F, 8, 24, 4);
		bipedBody.setRotationPoint(0.0F, -4.0F, 0.0F);
		bipedBody.setTextureSize(64, 64);

		bipedRightArm = new ModelRenderer(this, 0, 16);
		bipedRightArm.addBox(-2.0F, -2.0F, -2.0F, 2.0F, 12.0F, 2.0F);
		bipedRightArm.setTextureSize(64, 64);
		bipedRightArm.setRotationPoint(-5.0F, -2.0F, 0.0F);

		bipedLeftArm = new ModelRenderer(this, 0, 16);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(-1.0F, -2.0F, -2.0F, 2.0F, 12.0F, 2.0F);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		bipedLeftArm.setTextureSize(64, 64);

		bipedHeadwear = new ModelRenderer(this, 32, 0);
		bipedHeadwear.addBox(-4F, -8F, -4F, 8, 8, 8, 0.5F);
		bipedHeadwear.setRotationPoint(0.0F, -4.0F, 0.0F);
		bipedHeadwear.setTextureSize(64, 64);

		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4F, -4F, -4F, 8, 8, 8);
		bipedHead.setRotationPoint(0F, -4F, 0F);
		bipedHead.setTextureSize(64, 64);

		bipedRightLeg = new ModelRenderer(this, 0, 16);
		bipedRightLeg.addBox(-1F, 2F, -1F, 2, 12, 2);
		bipedRightLeg.setRotationPoint(-2F, 9.5F, 0F);
		bipedRightLeg.setTextureSize(64, 64);

		bipedLeftLeg = new ModelRenderer(this, 0, 16);
		bipedLeftLeg.addBox(-1F, 2F, -1F, 2, 12, 2);
		bipedLeftLeg.setRotationPoint(2F, 9.5F, 0F);
		bipedLeftLeg.setTextureSize(64, 64);
		bipedLeftLeg.mirror = true;

		collar = new ModelRenderer(this, 32, 16);
		collar.addBox(-6F, -2F, -4F, 12, 12, 1);
		collar.setRotationPoint(0F, -3F, -1F);
		collar.setTextureSize(64, 64);
		setRotation(collar, 2.164208F, 0F, 0F);

		cloak = new ModelRenderer(this, 0, 44);
		cloak.addBox(-6F, 2F, 0F, 12, 19, 1);
		cloak.setRotationPoint(0F, -4F, 2.5F);
		cloak.setTextureSize(64, 64);
		setRotation(cloak, 0F, 0F, 0F);
	}

	@Override
	public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float alpha) {
		if (!shadowClone) {
			super.render(stack, builder, light, overlay, red, green, blue, alpha);
		} else {
			float shadow = 0.33f;
			super.render(stack, builder, light, overlay, red * shadow, green * shadow, blue * shadow, 0.8F);
		}
	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		if (shadowClone) {
			return super.getBodyParts();
		} else {
			return Iterables.concat(Arrays.asList(cloak, collar), super.getBodyParts());
		}
	}

	@Override
	public void setRotationAngles(EntityTFLich entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.shadowClone = entity.isShadowClone();
		super.setRotationAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		float ogSin = MathHelper.sin(swingProgress * 3.141593F);
		float otherSin = MathHelper.sin((1.0F - (1.0F - swingProgress) * (1.0F - swingProgress)) * 3.141593F);
		bipedRightArm.rotateAngleZ = 0.0F;
		bipedLeftArm.rotateAngleZ = 0.5F;
		bipedRightArm.rotateAngleY = -(0.1F - ogSin * 0.6F);
		bipedLeftArm.rotateAngleY = 0.1F - ogSin * 0.6F;
		bipedRightArm.rotateAngleX = -1.570796F;
		bipedLeftArm.rotateAngleX = -3.141593F;
		bipedRightArm.rotateAngleX -= ogSin * 1.2F - otherSin * 0.4F;
		bipedLeftArm.rotateAngleX -= ogSin * 1.2F - otherSin * 0.4F;
		bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.26F) * 0.15F + 0.05F;
		bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.26F) * 0.15F + 0.05F;
		bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.167F) * 0.15F;
		bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.167F) * 0.15F;

		bipedHead.rotationPointY = -4.0F;
		bipedHeadwear.rotationPointY = -4.0F;
		bipedRightLeg.rotationPointY = 9.5F;
		bipedLeftLeg.rotationPointY = 9.5F;
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
