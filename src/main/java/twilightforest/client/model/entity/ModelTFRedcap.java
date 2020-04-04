package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import twilightforest.entity.EntityTFRedcap;

public class ModelTFRedcap<T extends EntityTFRedcap> extends BipedModel<T> {

	public ModelTFRedcap() {
		super(0.0F);

		bipedHead = new ModelRenderer(this, 0, 0);
		bipedHead.addCuboid(-3.4F, 1F, -4F, 7, 7, 7, 0F);
		bipedHead.setRotationPoint(0F, 0F, 0F);

		bipedHeadwear = new ModelRenderer(this, 32, 0);
		bipedHeadwear.addCuboid(-2F, 0F, -3F, 4, 5, 7, 0F);
		bipedHeadwear.setRotationPoint(0F, 0F, 0F);

		bipedBody = new ModelRenderer(this, 12, 19);
		bipedBody.addCuboid(-4F, 6F, -2F, 8, 9, 4, 0F);
		bipedBody.setRotationPoint(0F, 0F, 0F);

		bipedRightArm = new ModelRenderer(this, 36, 17);
		bipedRightArm.addCuboid(-2F, -2F, -2F, 3, 12, 3, 0F);
		bipedRightArm.setRotationPoint(-5F, 8F, 0F);

		bipedLeftArm = new ModelRenderer(this, 36, 17);
		bipedLeftArm.addCuboid(-1F, -2F, -2F, 3, 12, 3, 0F);
		bipedLeftArm.setRotationPoint(5F, 8F, 0F);

		bipedRightLeg = new ModelRenderer(this, 0, 20);
		bipedRightLeg.addCuboid(-2F, 2F, -1F, 3, 9, 3, 0F);
		bipedRightLeg.setRotationPoint(-2F, 12F, 0F);

		bipedLeftLeg = new ModelRenderer(this, 0, 20);
		bipedLeftLeg.addCuboid(-1F, 3F, -1F, 3, 9, 3, 0F);
		bipedLeftLeg.setRotationPoint(2F, 12F, 0F);

		goblinRightEar = new ModelRenderer(this, 48, 20);
		goblinRightEar.addCuboid(3F, -2F, -1F, 2, 3, 1, 0F);
		goblinRightEar.setRotationPoint(0F, 3F, 0F);

		goblinLeftEar = new ModelRenderer(this, 48, 24);
		goblinLeftEar.addCuboid(-5F, -2F, -1F, 2, 3, 1, 0F);
		goblinLeftEar.setRotationPoint(0F, 3F, 0F);

		goblinLeftEar.mirror = true;
	}

	@Override
	public void setAngles(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		super.setAngles(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);

		goblinRightEar.rotateAngleX = bipedHead.rotateAngleX;
		goblinRightEar.rotateAngleY = bipedHead.rotateAngleY;
		goblinRightEar.rotateAngleZ = bipedHead.rotateAngleZ;

		goblinLeftEar.rotateAngleX = bipedHead.rotateAngleX;
		goblinLeftEar.rotateAngleY = bipedHead.rotateAngleY;
		goblinLeftEar.rotateAngleZ = bipedHead.rotateAngleZ;
	}

	@Override
	protected Iterable<ModelRenderer> getBodyParts() {
		return Iterables.concat(super.getBodyParts(), ImmutableList.of(goblinRightEar, goblinLeftEar));
	}

	ModelRenderer goblinRightEar;
	ModelRenderer goblinLeftEar;
}
