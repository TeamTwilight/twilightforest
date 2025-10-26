package twilightforest.gametests;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.registries.DeferredHolder;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;

import java.util.Iterator;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

@GameTestHolder(TwilightForestMod.ID)
@PrefixGameTestTemplate(false)
public class TFGameTests {

	private static Stream<? extends BlockState> extractBlockStates(DeferredHolder<Block, ? extends Block> holder) {
		return holder.value().getStateDefinition().getPossibleStates().stream();
	}

	/**
	 * Fundamental test that sets every single block.
	 * <br>
	 * Helps catch any fundamental problems such as using clientside-specific code that would crash on a dedicated server.
	 */
	@GameTest(templateNamespace = TwilightForestMod.ID, template = "empty")
	public static void setTFBlocks(final GameTestHelper test) {

		Set<DeferredHolder<Block, ? extends Block>> excludeBlocks = Set.of(
			TFBlocks.TWILIGHT_PORTAL, // Twilight Portal instantly reverts without supporting blocks
			TFBlocks.UNCRAFTING_TABLE, // FIXME What's going on with its powered on state?
			TFBlocks.CANDELABRA, // FIXME Candle property behavior
			TFBlocks.CINDER_FURNACE // Unimplemented block
		);

		Iterator<? extends BlockState> blockStatesForTesting = TFBlocks.BLOCKS.getEntries().stream()
			.filter(Predicate.not(excludeBlocks::contains))
			.flatMap(TFGameTests::extractBlockStates)
			.iterator();

		while (blockStatesForTesting.hasNext()) {
			test.setBlock(BlockPos.ZERO, Blocks.AIR);

			BlockState state = blockStatesForTesting.next();
			test.setBlock(BlockPos.ZERO, state);
			test.assertBlockState(BlockPos.ZERO, state::equals, () -> "Expected placement of " + state + ", detected " + test.getBlockState(BlockPos.ZERO));
		}

		test.succeed(); // All assertions passed

	}

}
