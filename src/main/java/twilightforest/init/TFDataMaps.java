package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import twilightforest.TwilightForestMod;
import twilightforest.util.datamaps.CrumbledBlock;
import twilightforest.util.datamaps.EntityTransformation;
import twilightforest.util.datamaps.MagicMapBiomeColor;
import twilightforest.util.datamaps.OreMapOreColor;

public class TFDataMaps {

	public static final DataMapType<EntityType<?>, EntityTransformation> TRANSFORMATION_POWDER = DataMapType.builder(
		TwilightForestMod.prefix("transformation_powder"), Registries.ENTITY_TYPE, EntityTransformation.CODEC).synced(EntityTransformation.CODEC, false).build();

	public static final DataMapType<EntityType<?>, EntityTransformation> OMINOUS_FIRE = DataMapType.builder(
		TwilightForestMod.prefix("ominous_fire"), Registries.ENTITY_TYPE, EntityTransformation.CODEC).synced(EntityTransformation.CODEC, false).build();

	public static final DataMapType<Block, CrumbledBlock> CRUMBLE_HORN = DataMapType.builder(
		TwilightForestMod.prefix("crumble_horn"), Registries.BLOCK, CrumbledBlock.CODEC).synced(CrumbledBlock.CODEC, false).build();

	public static final DataMapType<Biome, MagicMapBiomeColor> MAGIC_MAP_BIOME_COLOR = DataMapType.builder(
		TwilightForestMod.prefix("magic_map_color"), Registries.BIOME, MagicMapBiomeColor.CODEC).synced(MagicMapBiomeColor.CODEC, false).build();

	public static final DataMapType<Block, OreMapOreColor> ORE_MAP_ORE_COLOR = DataMapType.builder(
		TwilightForestMod.prefix("ore_map_color"), Registries.BLOCK, OreMapOreColor.CODEC).synced(OreMapOreColor.CODEC, false).build();
}
