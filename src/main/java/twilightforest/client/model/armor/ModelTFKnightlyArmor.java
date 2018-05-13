package twilightforest.client.model.armor;

import net.minecraft.client.model.ModelRenderer;
import twilightforest.client.model.entity.ModelTFArmor;

public class ModelTFKnightlyArmor extends ModelTFArmor {

	public ModelRenderer righthorn1;
	public ModelRenderer righthorn2;
	public ModelRenderer lefthorn1;
	public ModelRenderer lefthorn2;

	public ModelRenderer shoulderSpike1;
	public ModelRenderer shoulderSpike2;

	public ModelRenderer shoeSpike1;
	public ModelRenderer shoeSpike2;


	public ModelTFKnightlyArmor(float expand) {
		super(expand);

		this.righthorn1 = new ModelRenderer(this, 24, 0);
		this.righthorn1.addBox(-5.5F, -1.5F, -1.5F, 5, 3, 3);
		this.righthorn1.setRotationPoint(-4.0F, -6.5F, 0.0F);
		this.righthorn1.rotateAngleY = -15F / (180F / (float) Math.PI);
		this.righthorn1.rotateAngleZ = 10F / (180F / (float) Math.PI);

		this.righthorn2 = new ModelRenderer(this, 54, 16);
		this.righthorn2.addBox(-3.5F, -1.0F, -1.0F, 3, 2, 2);
		this.righthorn2.setRotationPoint(-4.5F, 0.0F, 0.0F);
		this.righthorn2.rotateAngleZ = 10F / (180F / (float) Math.PI);

		this.bipedHead.addChild(righthorn1);
		this.righthorn1.addChild(righthorn2);

		this.lefthorn1 = new ModelRenderer(this, 24, 0);
		this.lefthorn1.mirror = true;
		this.lefthorn1.addBox(0.5F, -1.5F, -1.5F, 5, 3, 3);
		this.lefthorn1.setRotationPoint(4.0F, -6.5F, 0.0F);
		this.lefthorn1.rotateAngleY = 15F / (180F / (float) Math.PI);
		this.lefthorn1.rotateAngleZ = -10F / (180F / (float) Math.PI);

		this.lefthorn2 = new ModelRenderer(this, 54, 16);
		this.lefthorn2.addBox(0.5F, -1.0F, -1.0F, 3, 2, 2);
		this.lefthorn2.setRotationPoint(4.5F, 0.0F, 0.0F);
		this.lefthorn2.rotateAngleZ = -10F / (180F / (float) Math.PI);

		this.bipedHead.addChild(lefthorn1);
		this.lefthorn1.addChild(lefthorn2);

		this.shoulderSpike1 = new ModelRenderer(this, 0, 0);
		this.shoulderSpike1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.5F);
		this.shoulderSpike1.setRotationPoint(-3.75F, -2.5F, 0.0F);
		this.shoulderSpike1.rotateAngleX = 45F / (180F / (float) Math.PI);
		this.shoulderSpike1.rotateAngleY = 10F / (180F / (float) Math.PI);
		this.shoulderSpike1.rotateAngleZ = 35F / (180F / (float) Math.PI);

		this.bipedRightArm.addChild(shoulderSpike1);

		this.shoulderSpike2 = new ModelRenderer(this, 0, 0);
		this.shoulderSpike2.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.5F);
		this.shoulderSpike2.setRotationPoint(3.75F, -2.5F, 0.0F);
		this.shoulderSpike2.rotateAngleX = -45F / (180F / (float) Math.PI);
		this.shoulderSpike2.rotateAngleY = -10F / (180F / (float) Math.PI);
		this.shoulderSpike2.rotateAngleZ = 55F / (180F / (float) Math.PI);

		this.bipedLeftArm.addChild(shoulderSpike2);

		this.shoeSpike1 = new ModelRenderer(this, 0, 0);
		this.shoeSpike1.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.5F);
		this.shoeSpike1.setRotationPoint(-2.5F, 11F, 2.0F);
		this.shoeSpike1.rotateAngleY = -45F / (180F / (float) Math.PI);

		this.bipedRightLeg.addChild(shoeSpike1);

		this.shoeSpike2 = new ModelRenderer(this, 0, 0);
		this.shoeSpike2.addBox(-1.0F, -1.0F, -1.0F, 2, 2, 2, 0.5F);
		this.shoeSpike2.setRotationPoint(2.5F, 11F, 2.0F);
		this.shoeSpike2.rotateAngleY = 45F / (180F / (float) Math.PI);

		this.bipedLeftLeg.addChild(shoeSpike2);
	}
}
