package twilightforest.world.components.structures.util;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.block.Blocks;
import twilightforest.init.TFBlocks;

import java.util.List;

public class SimpleRandomBlockSelectorFactory {
	public static SimpleRandomBlockSelector getStrongholdStones() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), 0.2F),
				Pair.of(Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), 0.3F),
				Pair.of(Blocks.INFESTED_STONE_BRICKS.defaultBlockState(), 0.05F),
				Pair.of(Blocks.STONE_BRICKS.defaultBlockState(), 0.45F)
			)
		);
	}

	public static SimpleRandomBlockSelector getMazestone() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(TFBlocks.MOSSY_MAZESTONE.get().defaultBlockState(), 0.2F),
				Pair.of(TFBlocks.CRACKED_MAZESTONE.get().defaultBlockState(), 0.3F),
				Pair.of(TFBlocks.MAZESTONE_BRICK.get().defaultBlockState(), 0.5F)
			)
		);
	}

	public static SimpleRandomBlockSelector getKnightStones() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(TFBlocks.CRACKED_UNDERBRICK.get().defaultBlockState(), 0.2F),
				Pair.of(TFBlocks.MOSSY_UNDERBRICK.get().defaultBlockState(), 0.3F),
				Pair.of(TFBlocks.UNDERBRICK.get().defaultBlockState(), 0.5F)
			)
		);
	}

	public static SimpleRandomBlockSelector getTowerwood() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(TFBlocks.CRACKED_TOWERWOOD.get().defaultBlockState(), 0.1F),
				Pair.of(TFBlocks.MOSSY_TOWERWOOD.get().defaultBlockState(), 0.1F),
				Pair.of(TFBlocks.INFESTED_TOWERWOOD.get().defaultBlockState(), 0.025F),
				Pair.of(TFBlocks.TOWERWOOD.get().defaultBlockState(), 0.775F)
			)
		);
	}

	public static SimpleRandomBlockSelector getIceTower() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(TFBlocks.AURORA_BLOCK.get().defaultBlockState(), 1F)
			)
		);
	}

	public static SimpleRandomBlockSelector getCastleBlocks() {
		return new SimpleRandomBlockSelector(
			List.of(
				Pair.of(TFBlocks.WORN_CASTLE_BRICK.get().defaultBlockState(), 0.1F),
				Pair.of(TFBlocks.CRACKED_CASTLE_BRICK.get().defaultBlockState(), 0.1F),
				Pair.of(TFBlocks.CASTLE_BRICK.get().defaultBlockState(), 0.8F)
			)
		);
	}
}
