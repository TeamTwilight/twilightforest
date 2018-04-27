package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;


public class ModelTFHydraNeck extends ModelBase {

	ModelRenderer neck;

	public ModelTFHydraNeck() {
		textureWidth = 512;
		textureHeight = 256;

		setTextureOffset("neck.box", 128, 136);
		setTextureOffset("neck.fin", 128, 200);
		neck = new ModelRenderer(this, "neck");
		neck.addBox("box", -16F, -16F, -16F, 32, 32, 32);
		neck.addBox("fin", -2F, -23F, 0F, 4, 24, 24);
		neck.setRotationPoint(0F, 0F, 0F);

	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		neck.render(f5);
	}

	@Override
	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity) {
		neck.rotateAngleY = f3 / 57.29578F;
		neck.rotateAngleX = f4 / 57.29578F;
	}


}
