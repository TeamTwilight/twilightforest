package twilightforest.client.model.block.forcefield;

import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.block.model.TextureSlots;
import net.minecraft.client.resources.model.*;
import net.minecraft.util.context.ContextMap;
import net.neoforged.neoforge.client.RenderTypeGroup;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.ExtendedUnbakedModel;
import net.neoforged.neoforge.client.model.StandardModelParameters;

import java.util.Map;

public class UnbakedForceFieldModel extends AbstractUnbakedModel {

	private final Map<BlockElement, ForceFieldModelLoader.Condition> elementsAndConditions;

	public UnbakedForceFieldModel(Map<BlockElement, ForceFieldModelLoader.Condition> elementsAndConditions, StandardModelParameters parameters) {
		super(parameters);
		this.elementsAndConditions = elementsAndConditions;
	}

	@Override
	public BakedModel bake(TextureSlots textures, ModelBaker baker, ModelState modelState, boolean useAmbientOcclusion, boolean usesBlockLight, ItemTransforms itemTransforms, ContextMap additionalProperties) {
		return new ForceFieldModel(this.elementsAndConditions, s -> baker.findSprite(textures, s), useAmbientOcclusion, usesBlockLight, itemTransforms, this.parameters.renderTypeGroup());
	}
}
