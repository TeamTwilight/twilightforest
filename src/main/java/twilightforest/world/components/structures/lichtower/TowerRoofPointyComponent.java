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


public class TowerRoofPointyComponent extends TowerRoofComponent {

	public TowerRoofPointyComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFLTRP.get(), nbt);
	}

	public TowerRoofPointyComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
	}

	public TowerRoofPointyComponent(StructurePieceType piece, int i, TowerWingComponent wing, int x, int y, int z) {
		super(piece, i, x, y, z);

		// same facing, but it doesn't matter
		this.setOrientation(wing.getOrientation());

		this.size = wing.size; // assuming only square towers and roofs right now.
		this.height = size;

		// just hang out at the very top of the tower
		makeCapBB(wing);
	}

	/**
	 * Makes a pointy roof out of stuff
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		BlockState birchSlab = Blocks.BIRCH_SLAB.defaultBlockState();
		BlockState birchPlanks = Blocks.BIRCH_PLANKS.defaultBlockState();

		for (int y = 0; y <= height; y++) {
			int min, mid, max;
			int slopeChange = slopeChangeForSize();
			if (y < slopeChange) {
				min = y;
				max = size - y - 1;
			} else {
				min = (y + slopeChange) / 2;
				max = size - ((y + slopeChange) / 2) - 1;
			}
			mid = min + ((max - min) / 2);
			for (int x = min; x <= max; x++) {
				for (int z = min; z <= max; z++) {
					placeBlock(world, birchPlanks, x, y, z, sbb);
					// some of these are unnecessary and will just be overwritten by a normal block, but whatevs.
					if ((x == min && (z == min || z == max)) || (x == max && (z == min || z == max))) {
						placeBlock(world, birchSlab, x, y + 1, z, sbb);
					}
					// mid blocks
					if (((((x == min || x == max) && z == mid) && x % 2 == 0) || (((z == min || z == max) && x == mid) && z % 2 == 0)) && mid != min + 1) {
						placeBlock(world, birchSlab, x, y + 1, z, sbb);
					}
				}
			}
		}
	}

	public int slopeChangeForSize() {
		if (size > 10) {
			return 3;
		} else if (size > 6) {
			return 2;
		} else {
			return 1;
		}
	}
}
