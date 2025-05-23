package twilightforest.client.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidArmorModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.common.util.Lazy;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class TFSimpleArmorRenderer implements IClientItemExtensions {
	public static final List<TFSimpleArmorRenderer> INSTANCES = new ArrayList<>();
	protected final Lazy<HumanoidModel<?>> INNER_ARMOR_MODEL;
	protected final Lazy<HumanoidModel<?>> OUTER_ARMOR_MODEL;

	public TFSimpleArmorRenderer(Function<ModelPart, HumanoidArmorModel<?>> createModelInstance, ModelLayerLocation innerLayerLocation, ModelLayerLocation outerLayerLocation) {
		INSTANCES.add(this);
		this.INNER_ARMOR_MODEL = Lazy.of(() -> {
			ModelPart baked = Minecraft.getInstance().getEntityModels().bakeLayer(innerLayerLocation);
			return createModelInstance.apply(baked);
		});
		this.OUTER_ARMOR_MODEL = Lazy.of(() -> {
			ModelPart baked = Minecraft.getInstance().getEntityModels().bakeLayer(outerLayerLocation);
			return createModelInstance.apply(baked);
		});
	}

	// can be overridden
	public void resetModelCache() {
		INNER_ARMOR_MODEL.invalidate();
		OUTER_ARMOR_MODEL.invalidate();
	}

	public static void resetAllModelCache() {
		INSTANCES.forEach(TFSimpleArmorRenderer::resetModelCache);
	}

	@Override
	public Model getHumanoidArmorModel(ItemStack stack, EquipmentClientInfo.LayerType type, Model original) {
		return type == EquipmentClientInfo.LayerType.HUMANOID_LEGGINGS ? INNER_ARMOR_MODEL.get() : OUTER_ARMOR_MODEL.get();
	}

	public static final class ResourceReloadListener implements ResourceManagerReloadListener {
		@Override
		public void onResourceManagerReload(ResourceManager resourceManager) {
			TFSimpleArmorRenderer.resetAllModelCache();
		}
	}
}
