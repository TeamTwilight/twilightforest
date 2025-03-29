package twilightforest.client.renderer.armor;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class TFArmorRenderer implements IClientItemExtensions {
	public static final List<TFSimpleArmorRenderer> INSTANCES = new ArrayList<>();
	protected final Map<ModelLayerLocation, Lazy<ModelPart>> ARMOR_MODELS = new HashMap<>();

	public TFArmorRenderer(ModelLayerLocation... layerLocations) {
		for (ModelLayerLocation layerLocation : layerLocations) {
			this.ARMOR_MODELS.put(
				layerLocation,
				Lazy.of(() -> Minecraft.getInstance().getEntityModels().bakeLayer(layerLocation))
			);
		}
	}

	public void resetModelCache() {
		ARMOR_MODELS.values().forEach(Lazy::invalidate);
	}

	public static void resetAllModelCache() {
		INSTANCES.forEach(TFSimpleArmorRenderer::resetModelCache);
	}

	protected ModelPart getModelPart(ModelLayerLocation layerLocation) {
		return ARMOR_MODELS.get(layerLocation).get();
	}

	public static final class ResourceReloadListener implements ResourceManagerReloadListener {
		@Override
		public void onResourceManagerReload(@NotNull ResourceManager resourceManager) {
			TFArmorRenderer.resetAllModelCache();
		}
	}
}
