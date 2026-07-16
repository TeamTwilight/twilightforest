package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import tamaized.beanification.junit.MockitoFixer;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoFixer.class)
public class WorldUtilTests {

	@Test
	public void filtersLoadedBlockEntitiesAcrossChunkBoundaries() {
		ServerLevel level = mock(ServerLevel.class);
		ServerChunkCache chunkSource = mock(ServerChunkCache.class);
		LevelChunk chunk00 = mock(LevelChunk.class);
		LevelChunk chunk10 = mock(LevelChunk.class);
		LevelChunk chunk01 = mock(LevelChunk.class);

		BlockEntity inside = blockEntityAt(14, 63, 14, false);
		BlockEntity xEdge = blockEntityAt(16, 65, 14, false);
		BlockEntity zEdge = blockEntityAt(14, 64, 16, false);
		BlockEntity outsideHorizontal = blockEntityAt(13, 64, 15, false);
		BlockEntity outsideVertical = blockEntityAt(15, 66, 15, false);
		BlockEntity removed = blockEntityAt(15, 64, 15, true);

		when(level.getChunkSource()).thenReturn(chunkSource);
		when(chunkSource.getChunkNow(0, 0)).thenReturn(chunk00);
		when(chunkSource.getChunkNow(1, 0)).thenReturn(chunk10);
		when(chunkSource.getChunkNow(0, 1)).thenReturn(chunk01);
		when(chunkSource.getChunkNow(1, 1)).thenReturn(null);
		when(chunk00.getBlockEntities()).thenReturn(Map.of(
			new BlockPos(14, 63, 14), inside,
			new BlockPos(13, 64, 15), outsideHorizontal,
			new BlockPos(15, 66, 15), outsideVertical,
			new BlockPos(15, 64, 15), removed
		));
		when(chunk10.getBlockEntities()).thenReturn(Map.of(new BlockPos(16, 65, 14), xEdge));
		when(chunk01.getBlockEntities()).thenReturn(Map.of(new BlockPos(14, 64, 16), zEdge));

		List<BlockEntity> result = WorldUtil.getLoadedBlockEntitiesInRange(level, new BlockPos(15, 64, 15), 1).toList();

		assertEquals(3, result.size());
		assertEquals(Set.of(inside, xEdge, zEdge), new HashSet<>(result));
		verify(chunkSource).getChunkNow(0, 0);
		verify(chunkSource).getChunkNow(1, 0);
		verify(chunkSource).getChunkNow(0, 1);
		verify(chunkSource).getChunkNow(1, 1);
		verifyNoMoreInteractions(chunkSource);
	}

	@Test
	public void handlesNegativeChunkBoundaries() {
		ServerLevel level = mock(ServerLevel.class);
		ServerChunkCache chunkSource = mock(ServerChunkCache.class);
		LevelChunk chunkNegativeTwo = mock(LevelChunk.class);
		LevelChunk chunkNegativeOne = mock(LevelChunk.class);
		BlockEntity negativeEdge = blockEntityAt(-17, 64, -16, false);
		BlockEntity positiveEdge = blockEntityAt(-15, 64, -16, false);

		when(level.getChunkSource()).thenReturn(chunkSource);
		when(chunkSource.getChunkNow(-2, -2)).thenReturn(null);
		when(chunkSource.getChunkNow(-1, -2)).thenReturn(null);
		when(chunkSource.getChunkNow(-2, -1)).thenReturn(chunkNegativeTwo);
		when(chunkSource.getChunkNow(-1, -1)).thenReturn(chunkNegativeOne);
		when(chunkNegativeTwo.getBlockEntities()).thenReturn(Map.of(new BlockPos(-17, 64, -16), negativeEdge));
		when(chunkNegativeOne.getBlockEntities()).thenReturn(Map.of(new BlockPos(-15, 64, -16), positiveEdge));

		List<BlockEntity> result = WorldUtil.getLoadedBlockEntitiesInRange(level, new BlockPos(-16, 64, -16), 1).toList();

		assertEquals(Set.of(negativeEdge, positiveEdge), new HashSet<>(result));
		verify(chunkSource).getChunkNow(-2, -2);
		verify(chunkSource).getChunkNow(-1, -2);
		verify(chunkSource).getChunkNow(-2, -1);
		verify(chunkSource).getChunkNow(-1, -1);
		verifyNoMoreInteractions(chunkSource);
	}

	@Test
	public void supportsZeroRange() {
		ServerLevel level = mock(ServerLevel.class);
		ServerChunkCache chunkSource = mock(ServerChunkCache.class);
		LevelChunk chunk = mock(LevelChunk.class);
		BlockEntity center = blockEntityAt(1, 64, 1, false);
		BlockEntity neighbor = blockEntityAt(2, 64, 1, false);

		when(level.getChunkSource()).thenReturn(chunkSource);
		when(chunkSource.getChunkNow(0, 0)).thenReturn(chunk);
		when(chunk.getBlockEntities()).thenReturn(Map.of(
			new BlockPos(1, 64, 1), center,
			new BlockPos(2, 64, 1), neighbor
		));

		List<BlockEntity> result = WorldUtil.getLoadedBlockEntitiesInRange(level, new BlockPos(1, 64, 1), 0).toList();

		assertEquals(List.of(center), result);
		verify(chunkSource).getChunkNow(0, 0);
		verifyNoMoreInteractions(chunkSource);
	}

	private static BlockEntity blockEntityAt(int x, int y, int z, boolean removed) {
		BlockEntity blockEntity = mock(BlockEntity.class);
		when(blockEntity.getBlockPos()).thenReturn(new BlockPos(x, y, z));
		when(blockEntity.isRemoved()).thenReturn(removed);
		return blockEntity;
	}
}
