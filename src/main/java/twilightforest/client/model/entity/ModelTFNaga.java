package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import twilightforest.entity.boss.EntityTFNaga;
import twilightforest.entity.boss.EntityTFNagaSegment;


public class ModelTFNaga extends ModelBase {
	public ModelTFNaga() {
		head = new ModelRenderer(this, 0, 0);
		head.addBox(-8F, -12F, -8F, 16, 16, 16, 0F);
		head.setRotationPoint(0F, 0F, 0F);

		body = new ModelRenderer(this, 0, 0);
		body.addBox(-8F, -16F, -8F, 16, 16, 16, 0F);
		body.setRotationPoint(0F, 0F, 0F);
	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);

		if (entity instanceof EntityTFNaga) {
			head.render(f5 * 2);
		} else if (entity instanceof EntityTFNagaSegment) {
			body.render(f5 * 2);
		} else {
			head.render(f5 * 2);
		}

	}

	//fields
	public ModelRenderer head;
	public ModelRenderer body;
}
