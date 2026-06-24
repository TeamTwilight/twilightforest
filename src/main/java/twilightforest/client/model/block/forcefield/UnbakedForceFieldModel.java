package twilightforest.client.model.block.forcefield;

import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.resources.model.cuboid.ItemTransforms;
import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.StandardModelParameters;
import java.util.Map;

public class UnbakedForceFieldModel extends AbstractUnbakedModel {

	private final Map<String, ForceFieldModelLoader.Condition> elementsAndConditions;

	public UnbakedForceFieldModel(Map<String, ForceFieldModelLoader.Condition> elementsAndConditions, StandardModelParameters parameters) {
		super(parameters);
		this.elementsAndConditions = elementsAndConditions;
	}

	@Override
	public UnbakedGeometry geometry() {
		return new ForceFieldModel(
			this.elementsAndConditions,
			(String textureKey) -> textureKey,
			Boolean.TRUE.equals(this.parameters.ambientOcclusion()),
			this.parameters.guiLight().lightLikeBlock(),
			ItemTransforms.NO_TRANSFORMS,
			java.util.Set.of(RenderTypes.translucentMovingBlock())
		);
	}
}
