package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import twilightforest.entity.EntityTFMosquitoSwarm;

import java.util.Random;

public class ModelTFMosquitoSwarm extends SegmentedModel<EntityTFMosquitoSwarm> {

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
		core.addBox(-0.5F, 2F, -0.5F, 1, 1, 1);
		core.setRotationPoint(0.0F, -4.0F, 0.0F);

		node1 = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));
		node1.addBox(0F, 0F, -11F, 1, 1, 1);
		node1.setRotationPoint(-0.5F, -2F, -0.5F);
		core.addChild(node1);

		node2 = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));
		node2.addBox(0F, -11F, 0F, 1, 1, 1);
		node2.setRotationPoint(-0.5F, -2F, -0.5F);
		core.addChild(node2);

		node3 = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));
		node3.addBox(-11F, 0F, 0F, 1, 1, 1);
		node3.setRotationPoint(-0.5F, -2F, -0.5F);
		core.addChild(node3);

		node4 = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));
		node4.addBox(0F, 0F, 11F, 1, 1, 1);
		node4.setRotationPoint(-0.5F, -2F, -0.5F);
		core.addChild(node4);

		node5 = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));
		node5.addBox(0F, 11F, 0F, 1, 1, 1);
		node5.setRotationPoint(-0.5F, -2F, -0.5F);
		core.addChild(node5);

		node6 = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));
		node6.addBox(11F, 0F, 0F, 1, 1, 1);
		node6.setRotationPoint(-0.5F, -2F, -0.5F);
		core.addChild(node6);

		addBugsToNodes(node1);
		addBugsToNodes(node2);
		addBugsToNodes(node3);
		addBugsToNodes(node4);
		addBugsToNodes(node5);
		addBugsToNodes(node6);
	}

	@Override
	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(core, node1, node2, node3, node4, node5, node6);
	}

	/**
	 * Add the proper number of mosquitoes to the nodes
	 */
	public void addBugsToNodes(ModelRenderer node) {
		int bugs = 16;

		for (int i = 0; i < bugs; i++) {
			Vector3d vec = new Vector3d(0, 0, 0);
			float rotateY = ((i * (360F / bugs)) * 3.141593F) / 180F;
			vec.rotatePitch(rotateY);
			ModelRenderer bug = new ModelRenderer(this, rand.nextInt(28), rand.nextInt(28));

			float bugX = (rand.nextFloat() - rand.nextFloat()) * 16.0f;
			float bugY = (rand.nextFloat() - rand.nextFloat()) * 16.0f;
			float bugZ = (rand.nextFloat() - rand.nextFloat()) * 16.0f;

			bug.addBox(bugX, bugY, bugZ, 1, 1, 1);

			bug.setRotationPoint((float) vec.x, (float) vec.y, (float) vec.z);
			bug.rotateAngleY = rotateY;
			node.addChild(bug);
		}
	}

	@Override
	public void setRotationAngles(EntityTFMosquitoSwarm entity, float v, float v1, float v2, float v3, float v4) { }

	@Override
	public void setLivingAnimations(EntityTFMosquitoSwarm entity, float limbSwing, float limbSwingAmount, float partialTicks) {
		core.rotateAngleY = (entity.ticksExisted + partialTicks) / 5.0F;
		core.rotateAngleX = MathHelper.sin((entity.ticksExisted + partialTicks) / 5.0F) / 4.0F;
		core.rotateAngleZ = MathHelper.cos((entity.ticksExisted + partialTicks) / 5.0F) / 4.0F;

		node1.rotateAngleY = (entity.ticksExisted + partialTicks) / 2.0F;
		node1.rotateAngleX = MathHelper.sin((entity.ticksExisted + partialTicks) / 6.0F) / 2.0F;
		node1.rotateAngleZ = MathHelper.cos((entity.ticksExisted + partialTicks) / 5.0F) / 4.0F;

		node2.rotateAngleY = MathHelper.sin((entity.ticksExisted + partialTicks) / 2.0F) / 3.0F;
		node2.rotateAngleX = (entity.ticksExisted + partialTicks) / 5.0F;
		node2.rotateAngleZ = MathHelper.cos((entity.ticksExisted + partialTicks) / 5.0F) / 4.0F;

		node3.rotateAngleY = MathHelper.sin((entity.ticksExisted + partialTicks) / 7.0F) / 3.0F;
		node3.rotateAngleX = MathHelper.cos((entity.ticksExisted + partialTicks) / 4.0F) / 2.0F;
		node3.rotateAngleZ = (entity.ticksExisted + partialTicks) / 5.0F;

		node4.rotateAngleX = (entity.ticksExisted + partialTicks) / 2.0F;
		node4.rotateAngleZ = MathHelper.sin((entity.ticksExisted + partialTicks) / 6.0F) / 2.0F;
		node4.rotateAngleY = MathHelper.sin((entity.ticksExisted + partialTicks) / 5.0F) / 4.0F;

		node5.rotateAngleZ = (entity.ticksExisted + partialTicks) / 2.0F;
		node5.rotateAngleY = MathHelper.cos((entity.ticksExisted + partialTicks) / 5.0F) / 4.0F;
		node5.rotateAngleX = MathHelper.cos((entity.ticksExisted + partialTicks) / 5.0F) / 4.0F;

		node6.rotateAngleZ = MathHelper.cos((entity.ticksExisted + partialTicks) / 7.0F) / 3.0F;
		node6.rotateAngleX = MathHelper.cos((entity.ticksExisted + partialTicks) / 4.0F) / 2.0F;
		node6.rotateAngleY = (entity.ticksExisted + partialTicks) / 5.0F;
	}
}
