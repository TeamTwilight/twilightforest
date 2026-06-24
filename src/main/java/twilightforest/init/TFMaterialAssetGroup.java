package twilightforest.init;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.equipment.EquipmentAsset;
import net.minecraft.world.item.equipment.trim.MaterialAssetGroup;

import java.util.Map;

public class TFMaterialAssetGroup {

	public static final MaterialAssetGroup IRONWOOD = create("twilightforest_ironwood");
	public static final MaterialAssetGroup STEELEAF = create("twilightforest_steeleaf");
	public static final MaterialAssetGroup KNIGHTMETAL = create("twilightforest_knightmetal");
	public static final MaterialAssetGroup FIERY = create("twilightforest_fiery");
	public static final MaterialAssetGroup NAGA_SCALE = create("twilightforest_naga_scale");
	public static final MaterialAssetGroup CARMINITE = create("twilightforest_carminite");

	public static MaterialAssetGroup create(String base) {
		return create(base, Map.of());
	}

	public static MaterialAssetGroup create(String base, Map<ResourceKey<EquipmentAsset>, String> overrides) {
		return new MaterialAssetGroup(new MaterialAssetGroup.AssetInfo(base), Map.copyOf(Maps.transformValues(overrides, MaterialAssetGroup.AssetInfo::new)));
	}
}
