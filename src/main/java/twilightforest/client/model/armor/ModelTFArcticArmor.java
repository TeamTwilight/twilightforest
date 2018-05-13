package twilightforest.client.model.armor;

import net.minecraft.client.model.ModelRenderer;
import twilightforest.client.model.entity.ModelTFArmor;

public class ModelTFArcticArmor extends ModelTFArmor {

	public ModelTFArcticArmor(float expand) {
		super(expand);

		ModelRenderer rightHood = new ModelRenderer(this, 0, 0);
		rightHood.addBox(-1.0F, -2.0F, -1.0F, 1, 4, 1, expand);
		rightHood.setRotationPoint(-2.5F, -3.0F, -5.0F);
		this.bipedHead.addChild(rightHood);

		ModelRenderer leftHood = new ModelRenderer(this, 0, 0);
		leftHood.addBox(0.0F, -2.0F, -1.0F, 1, 4, 1, expand);
		leftHood.setRotationPoint(2.5F, -3.0F, -5.0F);
		this.bipedHead.addChild(leftHood);

		ModelRenderer topHood = new ModelRenderer(this, 24, 0);
		topHood.addBox(-2.0F, -1.0F, -1.0F, 4, 1, 1, expand);
		topHood.setRotationPoint(0.0F, -5.5F, -5.0F);
		this.bipedHead.addChild(topHood);

		ModelRenderer bottomHood = new ModelRenderer(this, 24, 0);
		bottomHood.addBox(-2.0F, -1.0F, -1.0F, 4, 1, 1, expand);
		bottomHood.setRotationPoint(0.0F, 0.5F, -5.0F);
		this.bipedHead.addChild(bottomHood);
	}
}
