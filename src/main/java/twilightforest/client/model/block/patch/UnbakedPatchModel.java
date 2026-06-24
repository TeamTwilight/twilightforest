package twilightforest.client.model.block.patch;

import net.minecraft.client.resources.model.geometry.UnbakedGeometry;
import net.neoforged.neoforge.client.model.AbstractUnbakedModel;
import net.neoforged.neoforge.client.model.StandardModelParameters;

public class UnbakedPatchModel extends AbstractUnbakedModel {
	private final boolean shaggify;

	public UnbakedPatchModel(boolean shaggify, StandardModelParameters parameters) {
		super(parameters);
		this.shaggify = shaggify;
	}

	@Override
	public UnbakedGeometry geometry() {
		return new PatchModel(this.shaggify);
	}
}
