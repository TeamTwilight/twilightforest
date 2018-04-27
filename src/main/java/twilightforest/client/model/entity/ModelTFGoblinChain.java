package twilightforest.client.model.entity;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;


public class ModelTFGoblinChain extends ModelBase {
	ModelRenderer chain;

	public ModelTFGoblinChain() {

		chain = new ModelRenderer(this, 56, 16);
		chain.addBox(-1F, -1F, -1F, 2, 2, 2, 0F);
		chain.setRotationPoint(0F, 0F, 0F);

	}

	@Override
	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5) {
		chain.render(f5);
	}

}
