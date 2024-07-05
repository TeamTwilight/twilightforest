package twilightforest.init;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import twilightforest.TwilightForestMod;
import twilightforest.util.datamaps.CrumbledBlock;
import twilightforest.util.datamaps.EntityTransformation;

public class TFDataMaps {

	public static final DataMapType<EntityType<?>, EntityTransformation> TRANSFORMATION_POWDER = DataMapType.builder(
		TwilightForestMod.prefix("transformation_powder"), Registries.ENTITY_TYPE, EntityTransformation.CODEC).synced(EntityTransformation.CODEC, false).build();

	public static final DataMapType<Block, CrumbledBlock> CRUMBLE_HORN = DataMapType.builder(
		TwilightForestMod.prefix("crumble_horn"), Registries.BLOCK, CrumbledBlock.CODEC).synced(CrumbledBlock.CODEC, false).build();
}
