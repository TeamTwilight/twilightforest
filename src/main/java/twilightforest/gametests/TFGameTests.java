package twilightforest.gametests;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
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

	/**
	 * Fundamental test that places every single block with a mock player.
	 * <br>
	 * Triggers `Block#getStateForPlacement`
	 * <br>
	 * Helps catch any fundamental problems such as using clientside-specific code that would crash on a dedicated server.
	 */
	@GameTest(templateNamespace = TwilightForestMod.ID, template = "empty")
	public static void playerPlaceTFBlocks(final GameTestHelper test) {

		Set<DeferredHolder<Block, ? extends Block>> excludeBlocks = Set.of(
			// TODO Requires dirt above
			TFBlocks.TORCHBERRY_PLANT,
			TFBlocks.ROOT_STRAND,
			TFBlocks.TROLLVIDR,
			TFBlocks.UNRIPE_TROLLBER,
			TFBlocks.TROLLBER,
			// TODO Requires water below
			TFBlocks.HUGE_LILY_PAD,
			TFBlocks.HUGE_WATER_LILY,
			// TODO Requires adjacent support
			TFBlocks.IRON_LADDER,
			TFBlocks.ROPE,
			TFBlocks.THORN_ROSE,
			// TODO Requires block above
			TFBlocks.TWILIGHT_OAK_HANGING_SIGN,
			TFBlocks.CANOPY_HANGING_SIGN,
			TFBlocks.MANGROVE_HANGING_SIGN,
			TFBlocks.DARK_HANGING_SIGN,
			TFBlocks.TIME_HANGING_SIGN,
			TFBlocks.TRANSFORMATION_HANGING_SIGN,
			TFBlocks.MINING_HANGING_SIGN,
			TFBlocks.SORTING_HANGING_SIGN,
			// TODO Requires block sideways
			TFBlocks.TWILIGHT_OAK_WALL_HANGING_SIGN,
			TFBlocks.CANOPY_WALL_HANGING_SIGN,
			TFBlocks.MANGROVE_WALL_HANGING_SIGN,
			TFBlocks.DARK_WALL_HANGING_SIGN,
			TFBlocks.TIME_WALL_HANGING_SIGN,
			TFBlocks.TRANSFORMATION_WALL_HANGING_SIGN,
			TFBlocks.MINING_WALL_HANGING_SIGN,
			TFBlocks.SORTING_WALL_HANGING_SIGN,
			// FIXME Crashes game, cherry-pick 44c2d5650e41ade9cbca70be7ce9b5b9e402b5ac into 1.21.1
			TFBlocks.TROLLSTEINN,
			// FIXME why do these fail?
			TFBlocks.FALLEN_LEAVES,
			TFBlocks.GIANT_COBBLESTONE,
			TFBlocks.GIANT_LOG,
			TFBlocks.GIANT_LEAVES,
			TFBlocks.GIANT_OBSIDIAN,
			// NYI
			TFBlocks.CINDER_FURNACE
		);

		Iterator<? extends Block> blockForTesting = TFBlocks.BLOCKS.getEntries().stream()
			.filter(Predicate.not(excludeBlocks::contains))
			.map(DeferredHolder::value)
			.iterator();

		BlockPos worldPos = test.absolutePos(BlockPos.ZERO);

		Player player = test.makeMockPlayer(GameType.CREATIVE);
		{ // Positions the player and makes them look at the block
			Vec3 worldVecPos = Vec3.atBottomCenterOf(worldPos);
			player.teleportTo(worldVecPos.x + 1, worldVecPos.y, worldVecPos.z + 1);
			player.lookAt(EntityAnchorArgument.Anchor.EYES, worldVecPos);
		}

		test.setBlock(BlockPos.ZERO.below(), Blocks.DIRT); // Plant support

		while (blockForTesting.hasNext()) {
			test.setBlock(BlockPos.ZERO, Blocks.AIR);

			Item blockItem = blockForTesting.next().asItem();
			if (blockItem.getDefaultInstance().isEmpty()) continue;
			ItemStack stack = new ItemStack(blockItem);
			player.setItemInHand(InteractionHand.MAIN_HAND, stack);

			test.placeAt(player, stack, BlockPos.ZERO.above(), Direction.DOWN);
			test.assertBlockState(BlockPos.ZERO, state -> !state.is(Blocks.AIR), () -> "Expected placement of " + blockItem + ", detected " + test.getBlockState(BlockPos.ZERO));
		}

		test.succeed(); // All assertions passed

	}

}
