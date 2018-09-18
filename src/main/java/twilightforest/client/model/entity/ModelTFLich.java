package twilightforest.client.model.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.ForgeHooksClient;
import org.lwjgl.opengl.GL11;
import twilightforest.capabilities.CapabilityList;
import twilightforest.capabilities.shield.IShieldCapability;
import twilightforest.entity.boss.EntityTFLich;
import twilightforest.item.TFItems;

public class ModelTFLich extends ModelBiped {

	ModelRenderer collar;
	ModelRenderer cloak;
	ModelRenderer shieldBelt;

	boolean renderPass = false;

	public ModelTFLich() {
		this.renderPass = false;

		textureWidth = 64;
		textureHeight = 64;


		bipedBody = new ModelRenderer(this, 8, 16);
		bipedBody.addBox(-4F, 0.0F, -2F, 8, 24, 4);
		bipedBody.setRotationPoint(0.0F, -4.0F, 0.0F);
		bipedBody.setTextureSize(64, 64);

		bipedRightArm = new ModelRenderer(this, 0, 16);
		bipedRightArm.addBox(-2F, -2F, -1F, 2, 12, 2);
		bipedRightArm.setTextureSize(64, 64);
		bipedRightArm.setRotationPoint(-5F, -2.0F, 0.0F);

		bipedLeftArm = new ModelRenderer(this, 0, 16);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(-2F, -2F, -1F, 2, 12, 2);
		bipedLeftArm.setRotationPoint(5F, -2.0F, 0.0F);
		bipedLeftArm.setTextureSize(64, 64);

		bipedHeadwear = new ModelRenderer(this, 32, 0);
		bipedHeadwear.addBox(-4F, -12F, -4F, 8, 8, 8, 0.5F);
		bipedHeadwear.setRotationPoint(0.0F, -4.0F, 0.0F);
		bipedHeadwear.setTextureSize(64, 64);

		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addBox(-4F, -8F, -4F, 8, 8, 8);
		bipedHead.setRotationPoint(0F, -4F, 0F);
		bipedHead.setTextureSize(64, 64);

		bipedRightLeg = new ModelRenderer(this, 0, 16);
		bipedRightLeg.addBox(-1F, 0F, -1F, 2, 12, 2);
		bipedRightLeg.setRotationPoint(-2F, 9.5F, 0F);
		bipedRightLeg.setTextureSize(64, 64);

		bipedLeftLeg = new ModelRenderer(this, 0, 16);
		bipedLeftLeg.addBox(-1F, 0F, -1F, 2, 12, 2);
		bipedLeftLeg.setRotationPoint(2F, 9.5F, 0F);
		bipedLeftLeg.setTextureSize(64, 64);
		bipedLeftLeg.mirror = true;

		collar = new ModelRenderer(this, 32, 16);
		collar.addBox(-6F, 0F, 0F, 12, 12, 1);
		collar.setRotationPoint(0F, -3F, -1F);
		collar.setTextureSize(64, 64);
		setRotation(collar, 2.164208F, 0F, 0F);


		cloak = new ModelRenderer(this, 0, 44);
		cloak.addBox(-6F, 0F, 0F, 12, 19, 1);
		cloak.setRotationPoint(0F, -4F, 2.5F);
		cloak.setTextureSize(64, 64);
		setRotation(cloak, 0F, 0F, 0F);

		shieldBelt = new ModelRenderer(this);
		shieldBelt.setRotationPoint(0F, 0F, 0F);

	}

	public ModelTFLich(boolean specialRenderModel) {
		this();
		this.renderPass = specialRenderModel;
	}

	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		if(entity instanceof EntityTFLich) {
			EntityTFLich lich = (EntityTFLich) entity;

			// on regular pass, render everything about the master lich except the shield
			if (!renderPass) {
				if (!lich.isShadowClone()) {
					super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale * 1.125F);
					collar.render(scale * 1.125F);
					cloak.render(scale * 1.125F);
				}
			} else {
				// on the special render pass, render the shadow clone and the shield
				if (lich.isShadowClone()) {
					//    	        GL11.glDisable(GL11.GL_DEPTH_TEST);

					super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale * 1.125F);
					//               	GL11.glEnable(GL11.GL_DEPTH_TEST);

				} else {
					if (lich.getShieldStrength() > 0) {
						//shieldBelt.render(scale * 1.125F);
						renderShields(scale * 13, lich.getShieldStrength(), lich);
					}
				}
			}
		} else if (entity.hasCapability(CapabilityList.SHIELDS, null)) {
			IShieldCapability cap = entity.getCapability(CapabilityList.SHIELDS, null);
			if (cap != null && cap.shieldsLeft() > 0 && (entity instanceof EntityLivingBase)) {
				//shieldBelt.render(scale * 1.125F);
				renderShields(scale * 13, cap.shieldsLeft(), (EntityLivingBase) entity);
			}
		}
	}

	private static final float PI = (float) Math.PI;

	private void renderShields(float scale, int count, EntityLivingBase entityLivingBase) {

		ItemStack shieldStack = new ItemStack(TFItems.experiment_115, 1, 3);
		IBakedModel model = Minecraft.getMinecraft().getRenderItem().getItemModelWithOverrides(shieldStack, entityLivingBase.world, entityLivingBase);

		// Texture was bound to the Lich texture, re-bind to the blocks texture
		Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

		float prevX = OpenGlHelper.lastBrightnessX, prevY = OpenGlHelper.lastBrightnessY;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240f, 240f);
		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(1f, 1f, 1f, 1f));

		for (int c = 0; c < count; c++) {
			GlStateManager.pushMatrix();

			// GL Calls should be similar to those found in ModelRenderer.render
			GlStateManager.translate(shieldBelt.offsetX, shieldBelt.offsetY, shieldBelt.offsetZ);
			GlStateManager.translate(shieldBelt.rotationPointX * scale, shieldBelt.rotationPointY * scale, shieldBelt.rotationPointZ * scale);
			GlStateManager.rotate(shieldBelt.rotateAngleZ * (180F / PI)                       , 0.0F, 0.0F, 1.0F);
			GlStateManager.rotate(shieldBelt.rotateAngleY * (180F / PI) + (c * (360F / count)), 0.0F, 1.0F, 0.0F);
			GlStateManager.rotate(shieldBelt.rotateAngleX * (180F / PI)                       , 1.0F, 0.0F, 0.0F);

			// It's upside-down, gotta make it upside-up
			GlStateManager.scale(scale, -scale, scale);

			// Move the draw away from the entity being drawn around
			GlStateManager.translate(0F, 0F, 1F);

			Minecraft.getMinecraft().getRenderItem().renderItem(shieldStack, ForgeHooksClient.handleCameraTransforms(model, ItemCameraTransforms.TransformType.NONE, false));

			GlStateManager.popMatrix();
		}

		GL11.glMaterial(GL11.GL_FRONT_AND_BACK, GL11.GL_EMISSION, RenderHelper.setColorBuffer(0f, 0f, 0f, 1f));
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, prevX, prevY);
	}

	@Override
	public void setModelAttributes(ModelBase model) {
		super.setModelAttributes(model);
		if (model instanceof ModelTFLich) {
			shieldBelt = ((ModelTFLich) model).shieldBelt;
		}
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	 * and third as in the setRotationAngles method.
	 */
	@Override
	public void setLivingAnimations(EntityLivingBase entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		EntityTFLich lich = null;
		if (entity instanceof EntityTFLich)
			lich = (EntityTFLich) entity;

		IShieldCapability cap = null;
		if (entity.hasCapability(CapabilityList.SHIELDS, null))
			cap = entity.getCapability(CapabilityList.SHIELDS, null);

		// make the shield belt
		int shields = lich != null ? lich.getShieldStrength() : cap != null ? cap.shieldsLeft() : 0;
		if ((lich == null || !lich.isShadowClone()) && shields > 0) {
			//if (shieldBelt.childModels == null || shieldBelt.childModels.size() != shields) {
			//	// clear shields if we have the wrong number
			//	if (shieldBelt.childModels != null) {
			//		shieldBelt.childModels.clear();
			//	}
			//	// make or remake a belt of shields around the lich
			//	Vec3d vec;
			//	for (int i = 0; i < shields; i++) {
			//		vec = new Vec3d(11, 0, 0);
			//		float rotateY = ((i * (360F / shields)) * 3.141593F) / 180F;
			//		vec = vec.rotateYaw(rotateY);
			//		ModelRenderer shield = new ModelRenderer(this, 26, 40);
			//		shield.addBox(0.5F, -6F, -6F, 1, 12, 12);
			//		shield.setRotationPoint((float) vec.x, (float) vec.y, (float) vec.z);
			//		shield.setTextureSize(64, 64);
			//		shield.rotateAngleY = rotateY;
			//		shieldBelt.addChild(shield);
			//	}
			//}

			// rotate the belt
			shieldBelt.rotateAngleY = (entity.ticksExisted + partialTicks) / 5.0F;
			shieldBelt.rotateAngleX = MathHelper.sin((entity.ticksExisted + partialTicks) / 5.0F) / 4.0F;
			shieldBelt.rotateAngleZ = MathHelper.cos((entity.ticksExisted + partialTicks) / 5.0F) / 4.0F;
		}
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entity) {
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entity);
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
