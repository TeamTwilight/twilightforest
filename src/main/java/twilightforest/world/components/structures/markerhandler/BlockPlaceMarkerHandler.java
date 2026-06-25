package twilightforest.world.components.structures.markerhandler;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import twilightforest.init.custom.TemplateMarkerHandlers;

public record BlockPlaceMarkerHandler(BlockStateProvider provider) implements TemplateMarkerHandler {
	public static final MapCodec<BlockPlaceMarkerHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockStateProvider.CODEC.fieldOf("block_state").forGetter(BlockPlaceMarkerHandler::provider)
	).apply(instance, BlockPlaceMarkerHandler::new));

	@Override
	public boolean handleDataMarker(String label, BlockPos pos, WorldGenLevel level, RandomSource random, BoundingBox chunkBounds, ChunkGenerator chunkGen, Rotation rotation) {
		BlockState state = this.provider.getState(level, random, pos).rotate(rotation);
		return level.setBlock(pos, state, Block.UPDATE_ALL);
	}

	@Override
	public TemplateMarkerHandlerType getType() {
		return TemplateMarkerHandlers.BLOCK_PLACEMENT.value();
	}
}
