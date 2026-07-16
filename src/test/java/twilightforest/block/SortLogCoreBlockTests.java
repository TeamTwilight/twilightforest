package twilightforest.block;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
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
			TreeTestFixture fixture = treeTestFixture(64);

			try (MockedStatic<PacketDistributor> packets = mockStatic(PacketDistributor.class)) {
				runTreeEffect(fixture);
				packets.verify(() -> PacketDistributor.sendToPlayersNear(
					same(fixture.level()), isNull(), anyDouble(), anyDouble(), anyDouble(), anyDouble(), any(ParticlePacket.class)
				));
			}

			assertEquals(1, fixture.input().getAmountAsInt(0));
			assertEquals(2, fixture.output().getAmountAsInt(0));
			verify(fixture.level(), times(Direction.values().length)).getCapability(
				eq(Capabilities.Item.BLOCK),
				eq(fixture.inputPos()),
				same(fixture.inputState()),
				same(fixture.inputBlockEntity()),
				any(Direction.class)
			);
			verify(fixture.level(), times(Direction.values().length)).getCapability(
				eq(Capabilities.Item.BLOCK),
				eq(fixture.outputPos()),
				same(fixture.outputState()),
				same(fixture.outputBlockEntity()),
				any(Direction.class)
			);
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
		BlockState inputState = inputBlockEntity.getBlockState();
		BlockState outputState = outputBlockEntity.getBlockState();
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
		when(level.getCapability(
			eq(Capabilities.Item.BLOCK),
			eq(inputPos),
			same(inputState),
			same(inputBlockEntity),
			any(Direction.class)
		)).thenReturn(input);
		when(level.getCapability(
			eq(Capabilities.Item.BLOCK),
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

		return new TreeTestFixture(
			level,
			corePos,
			inputPos,
			outputPos,
			inputBlockEntity,
			outputBlockEntity,
			inputState,
			outputState,
			input,
			output
		);
	}

	private static void runTreeEffect(TreeTestFixture fixture) {
		// Constructing an unregistered block after Minecraft freezes its registries is not allowed in unit tests.
		mock(SortLogCoreBlock.class, Answers.CALLS_REAL_METHODS)
			.performTreeEffect(fixture.level(), fixture.corePos(), RandomSource.create(0L));
	}

	private static BlockEntity blockEntityAt(BlockPos pos) {
		BlockEntity blockEntity = mock(BlockEntity.class);
		BlockState state = mock(BlockState.class);
		when(blockEntity.getBlockPos()).thenReturn(pos);
		when(blockEntity.getBlockState()).thenReturn(state);
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
		BlockPos inputPos,
		BlockPos outputPos,
		BlockEntity inputBlockEntity,
		BlockEntity outputBlockEntity,
		BlockState inputState,
		BlockState outputState,
		TestItemHandler input,
		TestItemHandler output
	) {
	}
}
