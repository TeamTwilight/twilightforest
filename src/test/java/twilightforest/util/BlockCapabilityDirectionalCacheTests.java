package twilightforest.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.BlockCapabilityCache;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import tamaized.beanification.junit.MockitoFixer;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoFixer.class)
public class BlockCapabilityDirectionalCacheTests {

	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void reusesCacheForTheSameSideAndSeparatesDirections() {
		BlockCapability<Object, @Nullable Direction> capability = mock(BlockCapability.class);
		BlockCapabilityDirectionalCache<Object> instance = new BlockCapabilityDirectionalCache<>(capability);
		ServerLevel level = mock(ServerLevel.class);
		BlockEntity blockEntity = blockEntityAt(new BlockPos(4, 8, 15));
		BlockCapabilityCache<Object, @Nullable Direction> northCache = mock(BlockCapabilityCache.class);
		BlockCapabilityCache<Object, @Nullable Direction> southCache = mock(BlockCapabilityCache.class);
		Object northHandler = new Object();
		Object southHandler = new Object();

		try (MockedStatic<BlockCapabilityCache> caches = mockStatic(BlockCapabilityCache.class)) {
			caches.when(() -> BlockCapabilityCache.create(capability, level, blockEntity.getBlockPos(), Direction.NORTH)).thenReturn(northCache);
			caches.when(() -> BlockCapabilityCache.create(capability, level, blockEntity.getBlockPos(), Direction.SOUTH)).thenReturn(southCache);
			when(northCache.level()).thenReturn(level);
			when(northCache.pos()).thenReturn(blockEntity.getBlockPos());
			when(northCache.getCapability()).thenReturn(northHandler);
			when(southCache.getCapability()).thenReturn(southHandler);

			assertSame(northHandler, instance.get(level, blockEntity, Direction.NORTH));
			assertSame(northHandler, instance.get(level, blockEntity, Direction.NORTH));
			assertSame(southHandler, instance.get(level, blockEntity, Direction.SOUTH));

			caches.verify(() -> BlockCapabilityCache.create(capability, level, blockEntity.getBlockPos(), Direction.NORTH), times(1));
			caches.verify(() -> BlockCapabilityCache.create(capability, level, blockEntity.getBlockPos(), Direction.SOUTH), times(1));
			verify(northCache, times(2)).getCapability();
			verify(southCache).getCapability();
		}
	}

	@Test
	@SuppressWarnings({"unchecked", "rawtypes"})
	public void keepsLevelsIndependentAndClearsOnlyTheUnloadedLevel() {
		BlockCapability<Object, @Nullable Direction> capability = mock(BlockCapability.class);
		BlockCapabilityDirectionalCache<Object> instance = new BlockCapabilityDirectionalCache<>(capability);
		ServerLevel firstLevel = mock(ServerLevel.class);
		ServerLevel secondLevel = mock(ServerLevel.class);
		BlockPos sharedPos = new BlockPos(4, 8, 15);
		BlockEntity firstBlockEntity = blockEntityAt(sharedPos);
		BlockEntity secondBlockEntity = blockEntityAt(sharedPos);
		BlockCapabilityCache<Object, @Nullable Direction> firstCache = mock(BlockCapabilityCache.class);
		BlockCapabilityCache<Object, @Nullable Direction> secondCache = mock(BlockCapabilityCache.class);
		BlockCapabilityCache<Object, @Nullable Direction> recreatedFirstCache = mock(BlockCapabilityCache.class);
		Object firstHandler = new Object();
		Object secondHandler = new Object();
		Object recreatedFirstHandler = new Object();

		try (MockedStatic<BlockCapabilityCache> caches = mockStatic(BlockCapabilityCache.class)) {
			caches.when(() -> BlockCapabilityCache.create(capability, firstLevel, sharedPos, Direction.NORTH)).thenReturn(firstCache, recreatedFirstCache);
			caches.when(() -> BlockCapabilityCache.create(capability, secondLevel, sharedPos, Direction.NORTH)).thenReturn(secondCache);
			when(firstCache.level()).thenReturn(firstLevel);
			when(firstCache.pos()).thenReturn(sharedPos);
			when(secondCache.level()).thenReturn(secondLevel);
			when(secondCache.pos()).thenReturn(sharedPos);
			when(recreatedFirstCache.level()).thenReturn(firstLevel);
			when(recreatedFirstCache.pos()).thenReturn(sharedPos);
			when(firstCache.getCapability()).thenReturn(firstHandler);
			when(secondCache.getCapability()).thenReturn(secondHandler);
			when(recreatedFirstCache.getCapability()).thenReturn(recreatedFirstHandler);

			assertSame(firstHandler, instance.get(firstLevel, firstBlockEntity, Direction.NORTH));
			assertSame(secondHandler, instance.get(secondLevel, secondBlockEntity, Direction.NORTH));

			instance.clear(firstLevel);

			assertSame(recreatedFirstHandler, instance.get(firstLevel, firstBlockEntity, Direction.NORTH));
			assertSame(secondHandler, instance.get(secondLevel, secondBlockEntity, Direction.NORTH));

			caches.verify(() -> BlockCapabilityCache.create(capability, firstLevel, sharedPos, Direction.NORTH), times(2));
			caches.verify(() -> BlockCapabilityCache.create(capability, secondLevel, sharedPos, Direction.NORTH), times(1));
			verify(secondCache, times(2)).getCapability();
		}
	}

	private static BlockEntity blockEntityAt(BlockPos pos) {
		BlockEntity blockEntity = mock(BlockEntity.class);
		when(blockEntity.getBlockPos()).thenReturn(pos);
		return blockEntity;
	}
}
