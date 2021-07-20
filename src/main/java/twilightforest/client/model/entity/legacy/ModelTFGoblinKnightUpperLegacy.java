package twilightforest.client.model.entity.legacy;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import twilightforest.entity.EntityTFGoblinKnightUpper;

public class ModelTFGoblinKnightUpperLegacy extends BipedModel<EntityTFGoblinKnightUpper> {

	public ModelRenderer breastplate;
	public ModelRenderer helmet;
	public ModelRenderer righthorn1;
	public ModelRenderer righthorn2;
	public ModelRenderer lefthorn1;
	public ModelRenderer lefthorn2;

	public ModelRenderer shield;
	public ModelRenderer spear;


	public ModelTFGoblinKnightUpperLegacy() {
		super(0.0F, 0.0F, 128, 64);
		this.isSneak = false;
		this.textureWidth = 128;
		this.textureHeight = 64;

//FIXME: AtomicBlom: Replace with something like LayerCape
/*
		this.bipedCloak = new ModelRenderer(this, 0, 0);
        this.bipedCloak.addBox(-5.0F, 0.0F, -1.0F, 10, 16, 1);
*/
//FIXME: AtomicBlom replace with some variant of LayerDeadmau5Head
/*
        this.bipedEars = new ModelRenderer(this, 24, 0);
        this.bipedEars.addBox(-3.0F, -6.0F, -1.0F, 6, 6, 1);
*/

		this.bipedHead = new ModelRenderer(this, 0, 0);
		this.bipedHead.addBox(0, 0, 0, 0, 0, 0);
		this.bipedHead.setRotationPoint(0.0F, 12.0F, 0.0F);

		this.bipedHeadwear = new ModelRenderer(this, 0, 0);
		this.bipedHeadwear.addBox(0, 0, 0, 0, 0, 0);
		this.bipedHeadwear.setRotationPoint(0.0F, 12.0F, 0.0F);

		this.helmet = new ModelRenderer(this, 0, 0);
		this.helmet.addBox(-3.5F, -11.0F, -3.5F, 7, 11, 7);
		this.helmet.rotateAngleY = 45F / (180F / (float) Math.PI);

		this.righthorn1 = new ModelRenderer(this, 28, 0);
		this.righthorn1.addBox(-6F, -1.5F, -1.5F, 7, 3, 3);
		this.righthorn1.setRotationPoint(-3.5F, -9F, 0.0F);
		this.righthorn1.rotateAngleY = 15F / (180F / (float) Math.PI);
		this.righthorn1.rotateAngleZ = 10F / (180F / (float) Math.PI);

		this.righthorn2 = new ModelRenderer(this, 28, 6);
		this.righthorn2.addBox(-3.0F, -1.0F, -1.0F, 3, 2, 2);
		this.righthorn2.setRotationPoint(-5.5F, 0.0F, 0.0F);
		this.righthorn2.rotateAngleZ = 10F / (180F / (float) Math.PI);

		this.righthorn1.addChild(righthorn2);

		this.lefthorn1 = new ModelRenderer(this, 28, 0);
		this.lefthorn1.mirror = true;
		this.lefthorn1.addBox(-1F, -1.5F, -1.5F, 7, 3, 3);
		this.lefthorn1.setRotationPoint(3.5F, -9F, 0.0F);
		this.lefthorn1.rotateAngleY = -15F / (180F / (float) Math.PI);
		this.lefthorn1.rotateAngleZ = -10F / (180F / (float) Math.PI);

		this.lefthorn2 = new ModelRenderer(this, 28, 6);
		this.lefthorn2.addBox(0.0F, -1.0F, -1.0F, 3, 2, 2);
		this.lefthorn2.setRotationPoint(5.5F, 0.0F, 0.0F);
		this.lefthorn2.rotateAngleZ = -10F / (180F / (float) Math.PI);

		this.lefthorn1.addChild(lefthorn2);

		this.bipedHeadwear.addChild(helmet);
		this.bipedHeadwear.addChild(righthorn1);
		this.bipedHeadwear.addChild(lefthorn1);

		this.bipedBody = new ModelRenderer(this, 0, 18);
		this.bipedBody.setRotationPoint(0.0F, 12.0F, 0.0F);
		this.bipedBody.addBox(-5.5F, 0.0F, -2.0F, 11, 8, 4);
		this.bipedBody.setTextureOffset(30, 24).addBox(-6.5F, 0F, -2F, 1, 4, 4); // right shoulder
		this.bipedBody.setTextureOffset(30, 24).addBox(5.5F, 0F, -2F, 1, 4, 4); // left shoulder

		this.bipedRightArm = new ModelRenderer(this, 44, 16);
		this.bipedRightArm.addBox(-4.0F, -2.0F, -2.0F, 4, 12, 4);
		this.bipedRightArm.setRotationPoint(-6.5F, 14.0F, 0.0F);

		this.bipedLeftArm = new ModelRenderer(this, 44, 16);
		this.bipedLeftArm.mirror = true;
		this.bipedLeftArm.addBox(0.0F, -2.0F, -2.0F, 4, 12, 4);
		this.bipedLeftArm.setRotationPoint(6.5F, 14.0F, 0.0F);

		this.bipedRightLeg = new ModelRenderer(this, 30, 16);
		this.bipedRightLeg.addBox(-1.5F, 0.0F, -2.0F, 3, 4, 4);
		this.bipedRightLeg.setRotationPoint(-4F, 20.0F, 0.0F);

		this.bipedLeftLeg = new ModelRenderer(this, 30, 16);
		this.bipedLeftLeg.mirror = true;
		this.bipedLeftLeg.addBox(-1.5F, 0.0F, -2.0F, 3, 4, 4);
		this.bipedLeftLeg.setRotationPoint(4F, 20.0F, 0.0F);

		this.shield = new ModelRenderer(this, 63, 36);
		this.shield.addBox(-6.0F, -6.0F, -2.0F, 12, 20, 2);
		this.shield.setRotationPoint(0F, 12F, 0.0F);
		this.shield.rotateAngleX = 90F / (180F / (float) Math.PI);

		this.bipedLeftArm.addChild(shield);

		this.spear = new ModelRenderer(this, 108, 0);
		this.spear.addBox(-1.0F, -19.0F, -1.0F, 2, 40, 2);
		this.spear.setRotationPoint(-2F, 8.5F, 0.0F);
		this.spear.rotateAngleX = 90F / (180F / (float) Math.PI);

		this.bipedRightArm.addChild(spear);

		this.breastplate = new ModelRenderer(this, 64, 0);
		this.breastplate.addBox(-6.5F, 0.0F, -3.0F, 13, 12, 6);
		this.breastplate.setRotationPoint(0F, 11.5F, 0.0F);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(MatrixStack stack, IVertexBuilder builder, int light, int overlay, float red, float green, float blue, float scale) {
		super.render(stack, builder, light, overlay, red, green, blue, scale);

		this.breastplate.render(stack, builder, light, overlay, red, green, blue, scale);
	}

	@Override
	public void setRotationAngles(EntityTFGoblinKnightUpper entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		boolean hasShield = entity.hasShield();

		this.bipedHead.rotateAngleY = netHeadYaw / (180F / (float) Math.PI);
		this.bipedHead.rotateAngleX = headPitch / (180F / (float) Math.PI);
		this.bipedHead.rotateAngleZ = 0;
		this.bipedHeadwear.rotateAngleY = this.bipedHead.rotateAngleY;
		this.bipedHeadwear.rotateAngleX = this.bipedHead.rotateAngleX;
		this.bipedHeadwear.rotateAngleZ = this.bipedHead.rotateAngleZ;

		this.bipedRightArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 2.0F * limbSwingAmount * 0.5F;

		float leftConstraint = hasShield ? 0.2F : limbSwingAmount;

		this.bipedLeftArm.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 2.0F * leftConstraint * 0.5F;
		this.bipedRightArm.rotateAngleZ = 0.0F;
		this.bipedLeftArm.rotateAngleZ = 0.0F;

		this.bipedRightLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
		this.bipedLeftLeg.rotateAngleX = MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
		this.bipedRightLeg.rotateAngleY = 0.0F;
		this.bipedLeftLeg.rotateAngleY = 0.0F;

		if (this.isSitting) {
			this.bipedRightArm.rotateAngleX += -((float) Math.PI / 5F);
			this.bipedLeftArm.rotateAngleX += -((float) Math.PI / 5F);
			this.bipedRightLeg.rotateAngleX = 0;
			this.bipedLeftLeg.rotateAngleX = 0;
//            this.bipedRightLeg.rotateAngleY = ((float)Math.PI / 10F);
//            this.bipedLeftLeg.rotateAngleY = -((float)Math.PI / 10F);
		}

		if (this.leftArmPose != ArmPose.EMPTY) {
			this.bipedLeftArm.rotateAngleX = this.bipedLeftArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
		}

		this.rightArmPose = ArmPose.ITEM;

		if (this.rightArmPose != ArmPose.EMPTY) {
			this.bipedRightArm.rotateAngleX = this.bipedRightArm.rotateAngleX * 0.5F - ((float) Math.PI / 10F);
		}

		bipedRightArm.rotateAngleX -= (Math.PI * 0.66);

		// during swing move arm forward
		if (entity.heavySpearTimer > 0) {
			bipedRightArm.rotateAngleX -= this.getArmRotationDuringSwing(60 - entity.heavySpearTimer) / (180F / (float) Math.PI);
		}

		this.bipedRightArm.rotateAngleY = 0.0F;
		this.bipedLeftArm.rotateAngleY = 0.0F;

		this.bipedRightArm.rotateAngleZ += MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedLeftArm.rotateAngleZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.05F + 0.05F;
		this.bipedRightArm.rotateAngleX += MathHelper.sin(ageInTicks * 0.067F) * 0.05F;
		this.bipedLeftArm.rotateAngleX -= MathHelper.sin(ageInTicks * 0.067F) * 0.05F;

		// shield arm points somewhat inward
		this.bipedLeftArm.rotateAngleZ = -this.bipedLeftArm.rotateAngleZ;

		// fix shield so that it's always perpendicular to the floor
		this.shield.rotateAngleX = (float) (Math.PI * 2 - this.bipedLeftArm.rotateAngleX);

		this.breastplate.showModel = entity.hasArmor();
		this.shield.showModel = entity.hasShield();
	}

	/**
	 *
	 */
	private float getArmRotationDuringSwing(float attackTime) {
		if (attackTime <= 10) {
			// rock back
			return attackTime;
		}
		if (attackTime > 10 && attackTime <= 30) {
			// hang back
			return 10F;
		}
		if (attackTime > 30 && attackTime <= 33) {
			// slam forward
			return (attackTime - 30) * -8F + 10F;
		}
		if (attackTime > 33 && attackTime <= 50) {
			// stay forward
			return -15F;
		}
		if (attackTime > 50 && attackTime <= 60) {
			// back to normal
			return (10 - (attackTime - 50)) * -1.5F;
		}

		return 0;
	}
}
