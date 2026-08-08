package twilightforest.world.components.structures.markerhandler;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.RandomizableContainer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.init.custom.TemplateMarkerHandlers;

public record LootMarkerHandler(BlockStateProvider provider, ResourceKey<LootTable> lootTable) implements TemplateMarkerHandler {
	public static final MapCodec<LootMarkerHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC.fieldOf("loot_block").forGetter(LootMarkerHandler::provider),
		ResourceKey.codec(Registries.LOOT_TABLE).fieldOf("loot_table").forGetter(LootMarkerHandler::lootTable)
	).apply(instance, LootMarkerHandler::new));

	@Override
	public boolean handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen, Rotation rotation) {
		BlockState state = this.provider.getState(level, random, pos).rotate(rotation);

		if (level.setBlock(pos, state, Block.UPDATE_ALL) && level.getBlockEntity(pos) instanceof RandomizableContainer) {
			RandomizableContainer.setBlockEntityLootTable(level, random, pos, this.lootTable);

			return true;
		}

		return false;
	}

	@Override
	public TemplateMarkerHandlerType getType() {
		return TemplateMarkerHandlers.LOOT.value();
	}
}
