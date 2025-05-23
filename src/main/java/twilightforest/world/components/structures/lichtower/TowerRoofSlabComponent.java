package twilightforest.world.components.structures.lichtower;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.init.TFStructurePieceTypes;


public class TowerRoofSlabComponent extends TowerRoofComponent {

	public TowerRoofSlabComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFLTRS.get(), nbt);
	}

	public TowerRoofSlabComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
	}

	public TowerRoofSlabComponent(StructurePieceType piece, int i, TowerWingComponent wing, int x, int y, int z) {
		super(piece, i, x, y, z);

		// same alignment
		this.setOrientation(wing.getOrientation());
		// same size
		this.size = wing.size; // assuming only square towers and roofs right now.
		this.height = size / 2;

		// just hang out at the very top of the tower
		makeCapBB(wing);
	}

	/**
	 * Makes a flat, pyramid-shaped roof
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		makePyramidCap(world, Blocks.BIRCH_SLAB.defaultBlockState(), Blocks.BIRCH_PLANKS.defaultBlockState(), sbb);
	}

	protected void makePyramidCap(WorldGenLevel world, BlockState slabType, BlockState woodType, BoundingBox sbb) {
		for (int y = 0; y <= height; y++) {
			int min = 2 * y;
			int max = size - (2 * y) - 1;
			for (int x = min; x <= max; x++) {
				for (int z = min; z <= max; z++) {
					if (x == min || x == max || z == min || z == max) {

						placeBlock(world, slabType, x, y, z, sbb);
					} else {
						placeBlock(world, woodType, x, y, z, sbb);
					}
				}
			}
		}
	}

	protected void makeConnectedCap(WorldGenLevel world, BlockState slabType, BlockState woodType, BoundingBox sbb) {
		for (int y = 0; y < height; y++) {
			int min = 2 * y;
			int max = size - (2 * y) - 1;
			for (int x = 0; x <= max; x++) {
				for (int z = min; z <= max; z++) {
					if (x == max || z == min || z == max) {
						placeBlock(world, slabType, x, y, z, sbb);
					} else {
						placeBlock(world, woodType, x, y, z, sbb);
					}
				}
			}
		}
	}
}
