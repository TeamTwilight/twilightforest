package twilightforest.world.components.structures.finalcastle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class FinalCastleDungeonEntranceComponent extends FinalCastleDungeonRoom31Component {

	public boolean hasExit = false;

	public FinalCastleDungeonEntranceComponent(ServerLevel level, CompoundTag nbt) {
		super(FinalCastlePieces.TFFCDunEn, nbt);
	}

	public FinalCastleDungeonEntranceComponent(TFFeature feature, Random rand, int i, int x, int y, int z, Direction direction, int level) {
		super(FinalCastlePieces.TFFCDunEn, feature, rand, i, x, y, z, direction, level);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		this.deco = new StructureTFDecoratorCastle();
		this.deco.blockState = TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get().defaultBlockState();
		this.deco.fenceState = TFBlocks.PINK_FORCE_FIELD.get().defaultBlockState();

		// this is going to be the parent for all rooms on this level
		super.addChildren(this, list, rand);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		if (!super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos)) {
			return false;
		}

		final BlockState stairs = deco.stairState.setValue(StairBlock.FACING, Direction.SOUTH);
		final BlockState deadRock = TFBlocks.DEADROCK.get().defaultBlockState();
		// stairs
		for (int y = 0; y <= this.height; y++) {
			int x = (this.size / 2) - 2;
			int z = (this.size / 2) - y + 2;

			this.generateBox(world, sbb, x, 0, z, x + 4, y - 1, z, deadRock, deadRock, false);
			this.generateBox(world, sbb, x, y, z, x + 4, y, z, stairs, stairs, false);
			this.generateAirBox(world, sbb, x, y + 1, z, x + 4, y + 6, z);
		}

		// door
		final BlockState castleDoor = TFBlocks.PINK_CASTLE_DOOR.get().defaultBlockState();
		this.generateBox(world, sbb, 23, 0, 12, 23, 3, 14, castleDoor, AIR, false);
		this.generateBox(world, sbb, 23, 4, 12, 23, 4, 14, deco.blockState, deco.blockState, false);

		return true;
	}

	@Override
	protected BlockState getForceFieldColor(Random decoRNG) {
		return TFBlocks.PINK_FORCE_FIELD.get().defaultBlockState();
	}

	@Override
	protected BlockState getRuneColor(BlockState fieldColor) {
		return TFBlocks.PINK_CASTLE_RUNE_BRICK.get().defaultBlockState();
	}
}
