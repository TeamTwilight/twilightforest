package twilightforest.block;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.transfer.ResourceStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.resource.ResourceStack;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.MockedStatic;
import tamaized.beanification.junit.MockitoFixer;
import twilightforest.config.TFConfig;
import twilightforest.network.ParticlePacket;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.doAnswer;
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
			TreeTestFixture fixture = treeTestFixture(64);

			try (MockedStatic<PacketDistributor> packets = mockStatic(PacketDistributor.class)) {
				SortLogCoreBlock block = runTreeEffect(fixture);
				packets.verify(() -> PacketDistributor.sendToPlayersNear(
					same(fixture.level()), isNull(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(ParticlePacket.class)
				));
				verify(block, times(Direction.values().length)).getItemHandler(
					same(fixture.level()), same(fixture.inputBlockEntity()), any(Direction.class)
				);
				verify(block, times(Direction.values().length)).getItemHandler(
					same(fixture.level()), same(fixture.outputBlockEntity()), any(Direction.class)
				);
			}

			assertEquals(1, fixture.input().getAmountAsInt(0));
			assertEquals(2, fixture.output().getAmountAsInt(0));
		} finally {
			TFConfig.sortingCoreRange = originalRange;
		}
	}

	@Test
	public void rollsBackExtractionWhenTheOutputIsFull() {
		int originalRange = TFConfig.sortingCoreRange;
		TFConfig.sortingCoreRange = 4;

		try {
			TreeTestFixture fixture = treeTestFixture(1);

			try (MockedStatic<PacketDistributor> packets = mockStatic(PacketDistributor.class)) {
				runTreeEffect(fixture);
				packets.verifyNoInteractions();
			}

			assertEquals(2, fixture.input().getAmountAsInt(0));
			assertEquals(1, fixture.output().getAmountAsInt(0));
		} finally {
			TFConfig.sortingCoreRange = originalRange;
		}
	}

	private static TreeTestFixture treeTestFixture(int outputCapacity) {
		ServerLevel level = mock(ServerLevel.class);
		ServerChunkCache chunkSource = mock(ServerChunkCache.class);
		LevelChunk inputChunk = mock(LevelChunk.class);
		LevelChunk outputChunk = mock(LevelChunk.class);
		BlockPos corePos = new BlockPos(15, 64, 0);
		BlockPos inputPos = new BlockPos(16, 64, 0);
		BlockPos outputPos = new BlockPos(11, 64, 0);
		BlockEntity inputBlockEntity = blockEntityAt(inputPos);
		BlockEntity outputBlockEntity = blockEntityAt(outputPos);
		ItemResource empty = mock(ItemResource.class);
		ItemResource diamonds = mock(ItemResource.class);
		when(empty.isEmpty()).thenReturn(true);
		when(diamonds.isEmpty()).thenReturn(false);
		TestItemHandler input = new TestItemHandler(empty, 64);
		TestItemHandler output = new TestItemHandler(empty, outputCapacity);
		input.set(0, diamonds, 2);
		output.set(0, diamonds, 1);

		when(level.getChunkSource()).thenReturn(chunkSource);
		when(chunkSource.getChunkNow(0, 0)).thenReturn(outputChunk);
		when(chunkSource.getChunkNow(1, 0)).thenReturn(inputChunk);
		when(inputChunk.getBlockEntities()).thenReturn(Map.of(inputPos, inputBlockEntity));
		when(outputChunk.getBlockEntities()).thenReturn(Map.of(outputPos, outputBlockEntity));
		when(level.getEntities(
			isNull(Entity.class),
			any(AABB.class),
			org.mockito.ArgumentMatchers.<Predicate<? super Entity>>any()
		)).thenReturn(List.of());

		return new TreeTestFixture(
			level,
			corePos,
			inputBlockEntity,
			outputBlockEntity,
			input,
			output
		);
	}

	private static SortLogCoreBlock runTreeEffect(TreeTestFixture fixture) {
		// Constructing an unregistered block after Minecraft freezes its registries is not allowed in unit tests.
		SortLogCoreBlock block = mock(SortLogCoreBlock.class, Answers.CALLS_REAL_METHODS);
		doAnswer(invocation -> invocation.getArgument(1) == fixture.inputBlockEntity() ? fixture.input() : fixture.output())
			.when(block).getItemHandler(same(fixture.level()), any(BlockEntity.class), any(Direction.class));
		block.performTreeEffect(fixture.level(), fixture.corePos(), RandomSource.create(0L));
		return block;
	}

	private static BlockEntity blockEntityAt(BlockPos pos) {
		BlockEntity blockEntity = mock(BlockEntity.class);
		when(blockEntity.getBlockPos()).thenReturn(pos);
		when(blockEntity.isRemoved()).thenReturn(false);
		return blockEntity;
	}

	private static final class TestItemHandler extends ResourceStacksResourceHandler<ItemResource> {

		private final int capacity;

		private TestItemHandler(ItemResource emptyResource, int capacity) {
			super(1, emptyResource, Codec.STRING.xmap(ignored -> new ResourceStack<>(emptyResource, 0), ignored -> ""));
			this.capacity = capacity;
		}

		@Override
		protected int getCapacity(int index, ItemResource resource) {
			return this.capacity;
		}
	}

	private record TreeTestFixture(
		ServerLevel level,
		BlockPos corePos,
		BlockEntity inputBlockEntity,
		BlockEntity outputBlockEntity,
		TestItemHandler input,
		TestItemHandler output
	) {
	}
}
