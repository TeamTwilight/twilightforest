package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class FinalCastleBridgeComponent extends TFStructureComponentOld {

	public FinalCastleBridgeComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCBri.get(), nbt);
	}

	public FinalCastleBridgeComponent(int i, int x, int y, int z, int length, Direction direction) {
		super(TFStructurePieceTypes.TFFCBri.get(), i, x, y, z);
		this.setOrientation(direction);
		this.boundingBox = TFStructureComponentOld.getComponentToAddBoundingBox2(x, y, z, 0, -1, -3, length - 1, 5, 6, direction);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		int length = (this.getOrientation() == Direction.SOUTH || this.getOrientation() == Direction.NORTH) ? this.boundingBox.maxX() - this.boundingBox.minX() : this.boundingBox.maxZ() - this.boundingBox.minZ();

		// span
		generateBox(world, sbb, 0, 0, 0, length, 1, 6, false, rand, deco.randomBlocks);
		// rails
		BlockState castlePillar = TFBlocks.BOLD_CASTLE_BRICK_PILLAR.get().defaultBlockState().setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);

		generateBox(world, sbb, 0, 2, 0, length, 2, 0, castlePillar, castlePillar, false);
		generateBox(world, sbb, 0, 2, 6, length, 2, 6, castlePillar, castlePillar, false);

		// supports
		int l3 = length / 3;
		for (int i = 0; i < l3; i++) {
			int sl = l3 - (int) (Mth.cos((float) (l3 - i) / (float) l3 * 1.6F) * l3); // this could be better, maybe?
			generateBox(world, sbb, i, -sl, 0, i, 0, 0, false, rand, deco.randomBlocks);
			generateBox(world, sbb, i, -sl, 6, i, 0, 6, false, rand, deco.randomBlocks);
			generateBox(world, sbb, length - i, -sl, 0, length - i, 0, 0, false, rand, deco.randomBlocks);
			generateBox(world, sbb, length - i, -sl, 6, length - i, 0, 6, false, rand, deco.randomBlocks);
		}

		// doorframes
		this.generateBox(world, sbb, 0, 2, 1, 0, 7, 1, deco.pillarState, deco.pillarState, false);
		this.generateBox(world, sbb, 0, 2, 5, 0, 7, 5, deco.pillarState, deco.pillarState, false);
		this.generateBox(world, sbb, 0, 6, 2, 0, 6, 4, deco.accentState, deco.accentState, false);
		this.placeBlock(world, deco.pillarState, 0, 7, 3, sbb);

		this.generateBox(world, sbb, length, 2, 1, length, 7, 1, deco.pillarState, deco.pillarState, false);
		this.generateBox(world, sbb, length, 2, 5, length, 7, 5, deco.pillarState, deco.pillarState, false);
		this.generateBox(world, sbb, length, 6, 2, length, 6, 4, deco.accentState, deco.accentState, false);
		this.placeBlock(world, deco.pillarState, length, 7, 3, sbb);
	}
}
