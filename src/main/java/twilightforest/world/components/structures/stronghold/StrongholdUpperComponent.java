package twilightforest.world.components.structures.stronghold;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.List;

public abstract class StrongholdUpperComponent extends KnightStrongholdComponent {

	protected StrongholdUpperComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
	}

	protected StrongholdUpperComponent(StructurePieceType type, int i, Direction facing, int x, int y, int z) {
		super(type, i, facing, x, y, z);
	}

	protected abstract List<SmallDoorway> getSmallDoorways();

	@Override
	public final void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		this.placeUpperStrongholdWalls(world, sbb, 0, 0, 0, this.getXSize(), this.boundingBox.getYSpan() - 1, this.getZSize(), rand, this.deco.randomBlocks, this.getSmallDoorways());
		this.postProcessAfterWalls(world, sbb);
	}

	protected void postProcessAfterWalls(WorldGenLevel world, BoundingBox sbb) {
	}

	private int getZSize() {
		Direction orientation = this.getOrientation();
		if (orientation == null)
			return 0;  // Should never happen
		return switch (orientation) {
			case WEST, EAST -> this.boundingBox.getXSpan() - 1;
			default -> this.boundingBox.getZSpan() - 1;
		};
	}
}