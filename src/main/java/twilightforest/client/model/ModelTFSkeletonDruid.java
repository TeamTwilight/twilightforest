package twilightforest.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.entity.Entity;


public class ModelTFSkeletonDruid extends ModelSkeleton {
	private ModelRenderer dress;

	public ModelTFSkeletonDruid() {
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
