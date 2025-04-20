package twilightforest.datagen.assets;

import net.minecraft.client.data.models.EquipmentAssetProvider;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import twilightforest.init.TFEquipmentAssets;

import java.util.function.BiConsumer;

public class EquipmentAssetsGenerator extends EquipmentAssetProvider {
	public EquipmentAssetsGenerator(PackOutput output) {
		super(output);
	}

	@Override
	protected void registerModels(BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> output) {
		TFEquipmentAssets.bootstrap(output);
	}
}
