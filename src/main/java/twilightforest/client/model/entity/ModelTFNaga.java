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
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale) {
		super.render(entity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
		setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entity);

		if (entity instanceof EntityTFNaga) {
			head.render(scale * 2);
		} else if (entity instanceof EntityTFNagaSegment) {
			body.render(scale * 2);
		} else {
			head.render(scale * 2);
		}

	}

	//fields
	public ModelRenderer head;
	public ModelRenderer body;
}
