package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import twilightforest.entity.EntityTFProtectionBox;

public class ModelTFProtectionBox extends ModelBase {

	@Override
	public void setLivingAnimations(EntityLivingBase par1EntityLivingBase, float par2, float par3, float par4) {
	}

	public ModelRenderer box;
	private int lastPixelsX;
	private int lastPixelsY;
	private int lastPixelsZ;

	public ModelTFProtectionBox() {
		textureWidth = 16;
		textureHeight = 16;
		box = new ModelRenderer(this, 0, 0);
		box.addBox(0F, 0F, 0F, 16, 16, 16, 0F);
		box.setRotationPoint(0F, 0F, 0F);
	}

	@Override
	public void render(Entity par1Entity, float par2, float par3, float par4, float par5, float par6, float par7) {

		EntityTFProtectionBox boxEntity = (EntityTFProtectionBox) par1Entity;

		int pixelsX = boxEntity.sizeX * 16 + 2;
		int pixelsY = boxEntity.sizeY * 16 + 2;
		int pixelsZ = boxEntity.sizeZ * 16 + 2;

		if (pixelsX != this.lastPixelsX || pixelsY != this.lastPixelsY || pixelsZ != this.lastPixelsZ) {
			resizeBoxElement(pixelsX, pixelsY, pixelsZ);
		}

		box.render(par7);
	}

	private void resizeBoxElement(int pixelsX, int pixelsY, int pixelsZ) {
		box = new ModelRenderer(this, 0, 0);
		box.addBox(-1F, -1F, -1F, pixelsX, pixelsY, pixelsZ, 0F);
		box.setRotationPoint(0F, 0F, 0F);

		this.lastPixelsX = pixelsX;
		this.lastPixelsY = pixelsY;
		this.lastPixelsZ = pixelsZ;
	}


}
