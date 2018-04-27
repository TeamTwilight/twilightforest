package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.entity.Entity;


public class ModelTFSkeletonDruid extends ModelSkeleton {
	private ModelRenderer dress;

	public ModelTFSkeletonDruid() {
		bipedBody = new ModelRenderer(this, 8, 16);
		bipedBody.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4, 0.0F);
		bipedBody.setRotationPoint(0.0F, 0.0F, 0.0F);
		bipedRightArm = new ModelRenderer(this, 0, 16);
		bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
		bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
		bipedLeftArm = new ModelRenderer(this, 0, 16);
		bipedLeftArm.mirror = true;
		bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, 0.0F);
		bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
		
		dress = new ModelRenderer(this, 32, 16);
		dress.addBox(-4F, 12.0F, -2F, 8, 12, 4, 0);
		dress.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);

		dress.render(f5);
	}
}
