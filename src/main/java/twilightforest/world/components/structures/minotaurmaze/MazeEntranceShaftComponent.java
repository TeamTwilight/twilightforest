package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFDimensionData;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class MazeEntranceShaftComponent extends TFStructureComponentOld {

	public MazeEntranceShaftComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFMMES.get(), nbt);
	}

	public MazeEntranceShaftComponent(int i, RandomSource rand, int x, int y, int z) {
		super(TFStructurePieceTypes.TFMMES.get(), i, new BoundingBox(x, y, z, x + 6 - 1, y, z + 6 - 1).encapsulate(new BlockPos(x, TFDimensionData.SEALEVEL, z)));
		this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, RandomSource random) {
		// NO-OP
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		BlockPos.MutableBlockPos pos = chunkPosIn.getWorldPosition().mutable().setX(this.boundingBox.minX()).setZ(this.boundingBox.minZ());

		this.boundingBox.encapsulate(pos.setY(generator.getSeaLevel() - 9));

		this.generateBox(world, sbb, 0, 0, 0, 5, this.boundingBox.getYSpan(), 5, TFBlocks.MAZESTONE_BRICK.get().defaultBlockState(), AIR, true);
		this.generateAirBox(world, sbb, 1, 0, 1, 4, (generator.getSeaLevel() + 11) - this.boundingBox.minY(), 4);
	}
}
