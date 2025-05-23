package twilightforest.world.components.structures.lichtower;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;


public class TowerRoofGableForwardsComponent extends TowerRoofComponent {

	public TowerRoofGableForwardsComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFLTRGF.get(), nbt);
	}

	public TowerRoofGableForwardsComponent(int i, TowerWingComponent wing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFLTRGF.get(), i, x, y, z);

		// same facing
		this.setOrientation(wing.getOrientation());

		this.size = wing.size + 2; // assuming only square towers and roofs right now.
		this.height = size;

		// just hang out at the very top of the tower
		this.makeAttachedOverhangBB(wing);
	}

	/**
	 * Makes a pointy roof out of stuff
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		BlockState birchSlab = Blocks.BIRCH_SLAB.defaultBlockState();
		BlockState birchPlanks = Blocks.BIRCH_PLANKS.defaultBlockState();

		int slopeChange = slopeChangeForSize();
		for (int y = 0; y <= height; y++) {
			int min, max;
			if (y < slopeChange) {
				min = y;
				max = size - y - 1;
			} else {
				min = (y + slopeChange) / 2;
				max = size - ((y + slopeChange) / 2) - 1;
			}
			for (int x = 0; x <= size - 2; x++) {
				for (int z = min; z <= max; z++) {
					if (z == min || z == max) {
						placeBlock(world, birchPlanks, x, y, z, sbb);
					} else if (x < size - 2) {
						placeBlock(world, birchPlanks, x, y, z, sbb);
					}
				}
			}
		}

		// put on the little figurehead-like "cap"

		// where is even the top of our roof?
		int top = (size + 1) - slopeChange;
		int zMid = size / 2;

		placeBlock(world, birchSlab.setValue(SlabBlock.TYPE, SlabType.TOP), size - 1, top - 1, zMid, sbb);
		placeBlock(world, birchSlab, 0, top, zMid, sbb);
		placeBlock(world, birchSlab, size - 3, top, zMid, sbb);
		placeBlock(world, birchPlanks, size - 2, top, zMid, sbb);
		placeBlock(world, birchPlanks, size - 1, top, zMid, sbb);
		placeBlock(world, birchPlanks, size - 1, top + 1, zMid, sbb);
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
