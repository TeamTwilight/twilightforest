package twilightforest.client.model.entity;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.renderer.entity.model.SegmentedModel;
import net.minecraft.client.renderer.model.ModelRenderer;
import twilightforest.entity.EntityTFCubeOfAnnihilation;

public class ModelTFCubeOfAnnihilation extends SegmentedModel<EntityTFCubeOfAnnihilation> {

	public ModelRenderer box;
	public ModelRenderer boxX;
	public ModelRenderer boxY;
	public ModelRenderer boxZ;

	public ModelTFCubeOfAnnihilation() {
		textureWidth = 64;
		textureHeight = 64;
		box = new ModelRenderer(this, 0, 0);
		box.addBox(-8F, -8F, -8F, 16, 16, 16, 0F);
		box.setRotationPoint(0F, 0F, 0F);

		boxX = new ModelRenderer(this, 0, 32);
		boxX.addBox(-8F, -8F, -8F, 16, 16, 16, 0F);
		boxX.setRotationPoint(0F, 0F, 0F);

		boxY = new ModelRenderer(this, 0, 32);
		boxY.addBox(-8F, -8F, -8F, 16, 16, 16, 0F);
		boxY.setRotationPoint(0F, 0F, 0F);

		boxZ = new ModelRenderer(this, 0, 32);
		boxZ.addBox(-8F, -8F, -8F, 16, 16, 16, 0F);
		boxZ.setRotationPoint(0F, 0F, 0F);
	}

    @Override
	public Iterable<ModelRenderer> getParts() {
		return ImmutableList.of(
				box,
				boxX,
				boxY,
				boxZ
		);
	}

	@Override
	public void setRotationAngles(EntityTFCubeOfAnnihilation entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		boxX.rotateAngleX = (float) Math.sin((entity.ticksExisted + headPitch)) / 5F;
		boxY.rotateAngleY = (float) Math.sin((entity.ticksExisted + headPitch)) / 5F;
		boxZ.rotateAngleZ = (float) Math.sin((entity.ticksExisted + headPitch)) / 5F;
	}
}
