package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class ModelTFMosquitoSwarm extends ModelBase {

	ModelRenderer core;
	ModelRenderer node1;
	ModelRenderer node2;
	ModelRenderer node3;
	ModelRenderer node4;
	ModelRenderer node5;
	ModelRenderer node6;

	Random rand = new Random();

	public ModelTFMosquitoSwarm() {
		core = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));
		core.addBox(-4F, 0.0F, -2F, 1, 1, 1);
		core.setRotationPoint(0.0F, -4.0F, 0.0F);

		node1 = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));
		node1.addBox(-5.5F, -5F, -13F, 1, 1, 1);
		node1.setRotationPoint(2F, -1F, -6F);
		core.addChild(node1);

		node2 = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));
		node2.addBox(-5.5F, -13F, -5F, 1, 1, 1);
		node2.setRotationPoint(0F, -7F, -1F);
		core.addChild(node2);

		node3 = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));
		node3.addBox(-13F, -5F, -5F, 1, 1, 1);
		node3.setRotationPoint(5F, -2F, -1F);
		core.addChild(node3);

		node4 = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));
		node4.addBox(-5.5F, -5F, -13F, 1, 1, 1);
		node4.setRotationPoint(2F, -1F, -6F);
		core.addChild(node4);

		node5 = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));
		node5.addBox(-5.5F, -13F, -5F, 1, 1, 1);
		node5.setRotationPoint(0F, -7F, -1F);
		core.addChild(node5);

		node6 = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));
		node6.addBox(-13F, -5F, -5F, 1, 1, 1);
		node6.setRotationPoint(5F, -2F, -1F);
		core.addChild(node6);

		addBugsToNodes(node1);
		addBugsToNodes(node2);
		addBugsToNodes(node3);
		addBugsToNodes(node4);
		addBugsToNodes(node5);
		addBugsToNodes(node6);
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {
		core.render(par7 / 2.0F);
//		node1.render(par7);
//		node2.render(par7);
//		node3.render(par7);
	}

	/**
	 * Add the proper number of mosquitoes to the nodes
	 */
	public void addBugsToNodes(ModelRenderer node) {
		int bugs = 16;

		for (int i = 0; i < bugs; i++) {
			Vec3d vec = new Vec3d(11, 0, 0);
			float rotateY = ((i * (360F / bugs)) * 3.141593F) / 180F;
			vec.rotateYaw(rotateY);
			ModelRenderer bug = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));

			float bugX = (rand.nextFloat() - rand.nextFloat()) * 4.0f;
			float bugY = (rand.nextFloat() - rand.nextFloat()) * 4.0f;
			float bugZ = (rand.nextFloat() - rand.nextFloat()) * 4.0f;

			bug.addBox(bugX, bugY, bugZ, 1, 1, 1);

			bug.setRotationPoint((float) vec.x, (float) vec.y, (float) vec.z);
			bug.rotateAngleY = rotateY;
			node.addChild(bug);
		}
	}

	@Override
	public void setLivingAnimations(EntityLivingBase par1EntityLiving, float par2, float par3, float time) {
		core.rotateAngleY = (par1EntityLiving.ticksExisted + time) / 5.0F;
		core.rotateAngleX = MathHelper.sin((par1EntityLiving.ticksExisted + time) / 5.0F) / 4.0F;
		core.rotateAngleZ = MathHelper.cos((par1EntityLiving.ticksExisted + time) / 5.0F) / 4.0F;

		node1.rotateAngleY = (par1EntityLiving.ticksExisted + time) / 2.0F;
		node1.rotateAngleX = MathHelper.sin((par1EntityLiving.ticksExisted + time) / 6.0F) / 2.0F;
		node1.rotateAngleZ = MathHelper.cos((par1EntityLiving.ticksExisted + time) / 5.0F) / 4.0F;

		node2.rotateAngleY = MathHelper.sin((par1EntityLiving.ticksExisted + time) / 2.0F) / 3.0F;
		node2.rotateAngleX = (par1EntityLiving.ticksExisted + time) / 5.0F;
		node2.rotateAngleZ = MathHelper.cos((par1EntityLiving.ticksExisted + time) / 5.0F) / 4.0F;

		node3.rotateAngleY = MathHelper.sin((par1EntityLiving.ticksExisted + time) / 7.0F) / 3.0F;
		node3.rotateAngleX = MathHelper.cos((par1EntityLiving.ticksExisted + time) / 4.0F) / 2.0F;
		node3.rotateAngleZ = (par1EntityLiving.ticksExisted + time) / 5.0F;

		node4.rotateAngleX = (par1EntityLiving.ticksExisted + time) / 2.0F;
		node4.rotateAngleZ = MathHelper.sin((par1EntityLiving.ticksExisted + time) / 6.0F) / 2.0F;
		node4.rotateAngleY = MathHelper.sin((par1EntityLiving.ticksExisted + time) / 5.0F) / 4.0F;

		node5.rotateAngleZ = MathHelper.sin((par1EntityLiving.ticksExisted + time) / 2.0F) / 3.0F;
		node5.rotateAngleY = MathHelper.cos((par1EntityLiving.ticksExisted + time) / 5.0F) / 4.0F;
		node5.rotateAngleX = MathHelper.cos((par1EntityLiving.ticksExisted + time) / 5.0F) / 4.0F;

		node6.rotateAngleZ = MathHelper.cos((par1EntityLiving.ticksExisted + time) / 7.0F) / 3.0F;
		node6.rotateAngleX = MathHelper.cos((par1EntityLiving.ticksExisted + time) / 4.0F) / 2.0F;
		node6.rotateAngleY = (par1EntityLiving.ticksExisted + time) / 5.0F;

	}


}
