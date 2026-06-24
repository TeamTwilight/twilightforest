package twilightforest.client.model.block.patch;

import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.*;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.StandardModelParameters;

public class UnbakedPatchModel extends AbstractUnbakedModel {

	private final boolean shaggify;

	public UnbakedPatchModel(boolean shaggify, StandardModelParameters parameters) {
		super(parameters);
		this.shaggify = shaggify;
	}

	@Override
	public BakedModel bake(TextureSlots textureSlots, ModelBaker baker, ModelState modelState, boolean hasAmbientOcclusion, boolean useBlockLight, ItemTransforms transforms, ContextMap additionalProperties) {
		return new PatchModel(baker.findSprite(textureSlots, "texture"), this.shaggify, baker.findSprite(textureSlots, "particle"), hasAmbientOcclusion, useBlockLight, transforms, this.parameters.renderTypeGroup());
	}
}
