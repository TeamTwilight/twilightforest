package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;

import java.util.Random;

public class MazeEntranceShaftComponent extends TFStructureComponentOld {

	public MazeEntranceShaftComponent(ServerLevel level, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMES, nbt);
	}

	private int averageGroundLevel = Integer.MIN_VALUE;

	public MazeEntranceShaftComponent(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(MinotaurMazePieces.TFMMES, feature, i, new BoundingBox(x, y, z, x + 6 - 1, y, z + 6 - 1).encapsulate(new BlockPos(x, 0, z))); // FIXME Swap 0 for Worldgen Sea Level
		this.setOrientation(Direction.Plane.HORIZONTAL.getRandomDirection(rand));
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void addChildren(StructurePiece structurecomponent, StructurePieceAccessor list, Random random) {
		// NO-OP
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		BlockPos.MutableBlockPos pos = chunkPosIn.getWorldPosition().mutable().setX(this.boundingBox.minX()).setZ(this.boundingBox.minZ());

		this.boundingBox.encapsulate(pos.setY(generator.getSeaLevel() - 9));

		this.generateBox(world, sbb, 0, 0, 0, 5, this.boundingBox.getYSpan(), 5, TFBlocks.MAZESTONE_BRICK.get().defaultBlockState(), AIR, true);
		this.generateAirBox(world, sbb, 1, 0, 1, 4, this.boundingBox.getYSpan(), 4);

		return true;
	}
}
