package twilightforest.world.components.structures.finalcastle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;

import java.util.Random;

public class FinalCastleBridgeComponent extends TFStructureComponentOld {

	public FinalCastleBridgeComponent(ServerLevel level, CompoundTag nbt) {
		super(FinalCastlePieces.TFFCBri, nbt);
	}

	public FinalCastleBridgeComponent(TFFeature feature, int i, int x, int y, int z, int length, Direction direction) {
		super(FinalCastlePieces.TFFCBri, feature, i, x, y, z);
		this.setOrientation(direction);
		this.boundingBox = TFStructureComponentOld.getComponentToAddBoundingBox2(x, y, z, 0, -1, -3, length - 1, 5, 6, direction);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
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

		return true;
	}
}
