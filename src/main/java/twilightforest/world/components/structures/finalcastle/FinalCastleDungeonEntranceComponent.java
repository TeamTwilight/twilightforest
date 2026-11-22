package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;


public class FinalCastleDungeonEntranceComponent extends FinalCastleDungeonRoom31Component {

	public boolean hasExit = false;

	public FinalCastleDungeonEntranceComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCDunEn.get(), nbt);
	}

	public FinalCastleDungeonEntranceComponent(int i, int x, int y, int z, Direction direction, int level) {
		super(TFStructurePieceTypes.TFFCDunEn.get(), i, x, y, z, direction, level);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePiecesBuilder list, RandomSource rand) {
		this.deco = new StructureTFDecoratorCastle();
		this.deco.blockState = TFBlocks.PINK_CASTLE_RUNE_BRICK.get().defaultBlockState();
		this.deco.fenceState = TFBlocks.PINK_FORCE_FIELD.get().defaultBlockState();

		// this is going to be the parent for all rooms on this level
		super.addChildren(this, list, rand);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {

		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

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
	}

	@Override
	protected BlockState getForceFieldColor(RandomSource decoRNG) {
		return TFBlocks.PINK_FORCE_FIELD.get().defaultBlockState();
	}

	@Override
	protected BlockState getRuneColor(BlockState fieldColor) {
		return TFBlocks.PINK_CASTLE_RUNE_BRICK.get().defaultBlockState();
	}
}
