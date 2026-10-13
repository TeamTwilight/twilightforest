package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.network.PacketDistributor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import tamaized.beanification.junit.MockitoFixer;
import twilightforest.config.TFConfig;
import twilightforest.init.TFBlocks;
import twilightforest.network.ParticlePacket;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoFixer.class)
public class SortLogCoreBlockTests {

	@Test
	public void transfersItemsAcrossAChunkBoundary() {
		int originalRange = TFConfig.sortingCoreRange;
		TFConfig.sortingCoreRange = 4;

		try {
			ServerLevel level = mock(ServerLevel.class);
			ServerChunkCache chunkSource = mock(ServerChunkCache.class);
			LevelChunk inputChunk = mock(LevelChunk.class);
			LevelChunk outputChunk = mock(LevelChunk.class);
			BlockPos corePos = new BlockPos(15, 64, 0);
			BlockPos inputPos = new BlockPos(16, 64, 0);
			BlockPos outputPos = new BlockPos(11, 64, 0);
			BlockEntity inputBlockEntity = blockEntityAt(inputPos);
			BlockEntity outputBlockEntity = blockEntityAt(outputPos);
			BlockState inputState = inputBlockEntity.getBlockState();
			BlockState outputState = outputBlockEntity.getBlockState();
			ItemStackHandler input = new ItemStackHandler(1);
			ItemStackHandler output = new ItemStackHandler(1);
			input.setStackInSlot(0, new ItemStack(Items.DIAMOND, 2));
			output.setStackInSlot(0, new ItemStack(Items.DIAMOND));

			when(level.getChunkSource()).thenReturn(chunkSource);
			when(chunkSource.getChunkNow(0, 0)).thenReturn(outputChunk);
			when(chunkSource.getChunkNow(1, 0)).thenReturn(inputChunk);
			when(inputChunk.getBlockEntities()).thenReturn(Map.of(inputPos, inputBlockEntity));
			when(outputChunk.getBlockEntities()).thenReturn(Map.of(outputPos, outputBlockEntity));
			when(level.getCapability(
				eq(Capabilities.ItemHandler.BLOCK),
				eq(inputPos),
				same(inputState),
				same(inputBlockEntity),
				any(Direction.class)
			)).thenReturn(input);
			when(level.getCapability(
				eq(Capabilities.ItemHandler.BLOCK),
				eq(outputPos),
				same(outputState),
				same(outputBlockEntity),
				any(Direction.class)
			)).thenReturn(output);
			when(level.getEntities(
				isNull(Entity.class),
				any(AABB.class),
				org.mockito.ArgumentMatchers.<Predicate<? super Entity>>any()
			)).thenReturn(List.of());

			try (MockedStatic<PacketDistributor> packets = mockStatic(PacketDistributor.class)) {
				((SortLogCoreBlock) TFBlocks.SORTING_LOG_CORE.get()).performTreeEffect(level, corePos, RandomSource.create(0L));
				packets.verify(() -> PacketDistributor.sendToPlayersNear(
					same(level), isNull(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(ParticlePacket.class)
				));
			}

			assertEquals(1, input.getStackInSlot(0).getCount());
			assertEquals(2, output.getStackInSlot(0).getCount());
			verify(level, times(Direction.values().length)).getCapability(
				eq(Capabilities.ItemHandler.BLOCK), eq(inputPos), same(inputState), same(inputBlockEntity), any(Direction.class)
			);
			verify(level, times(Direction.values().length)).getCapability(
				eq(Capabilities.ItemHandler.BLOCK), eq(outputPos), same(outputState), same(outputBlockEntity), any(Direction.class)
			);
		} finally {
			TFConfig.sortingCoreRange = originalRange;
		}
	}

	private static BlockEntity blockEntityAt(BlockPos pos) {
		BlockEntity blockEntity = mock(BlockEntity.class);
		BlockState state = mock(BlockState.class);
		when(blockEntity.getBlockPos()).thenReturn(pos);
		when(blockEntity.getBlockState()).thenReturn(state);
		when(blockEntity.isRemoved()).thenReturn(false);
		return blockEntity;
	}
}
