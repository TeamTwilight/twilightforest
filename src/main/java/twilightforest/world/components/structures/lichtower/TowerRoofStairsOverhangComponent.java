package twilightforest.world.components.structures.lichtower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;


public class TowerRoofStairsOverhangComponent extends TowerRoofComponent {

	public TowerRoofStairsOverhangComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFLTRStO.get(), nbt);
	}

	public TowerRoofStairsOverhangComponent(int i, TowerWingComponent wing, int x, int y, int z) {
		super(TFStructurePieceTypes.TFLTRStO.get(), i, x, y, z);

		// always facing = 0.  This roof cannot rotate, due to stair facing issues.
		this.setOrientation(Direction.SOUTH);

		this.size = wing.size + 2; // assuming only square towers and roofs right now.
		this.height = size / 2;

		// just hang out at the very top of the tower
		this.boundingBox = new BoundingBox(wing.getBoundingBox().minX() - 1, wing.getBoundingBox().maxY(), wing.getBoundingBox().minZ() - 1, wing.getBoundingBox().maxX() + 1, wing.getBoundingBox().maxY() + this.height - 1, wing.getBoundingBox().maxZ() + 1);
	}

	/**
	 * Makes a pyramid-shaped roof out of stairs
	 */
	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		BlockState woodenSlab = Blocks.BIRCH_SLAB.defaultBlockState();
		BlockState woodenPlanks = Blocks.BIRCH_PLANKS.defaultBlockState();

		BlockState birchStairsNorth = Blocks.BIRCH_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.NORTH);
		BlockState birchStairsSouth = Blocks.BIRCH_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.SOUTH);
		BlockState birchStairsEast = Blocks.BIRCH_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.EAST);
		BlockState birchStairsWest = Blocks.BIRCH_STAIRS.defaultBlockState().setValue(StairBlock.FACING, Direction.WEST);

		for (int y = 0; y <= height; y++) {
			int min = y;
			int max = size - y - 1;
			for (int x = min; x <= max; x++) {
				for (int z = min; z <= max; z++) {
					if (x == min) {
						if (z == min || z == max) {
							placeBlock(world, woodenSlab, x, y, z, sbb);
						} else {
							placeBlock(world, birchStairsWest, x, y, z, sbb);
						}
					} else if (x == max) {
						if (z == min || z == max) {
							placeBlock(world, woodenSlab, x, y, z, sbb);
						} else {
							placeBlock(world, birchStairsEast, x, y, z, sbb);
						}
					} else if (z == max) {
						placeBlock(world, birchStairsSouth, x, y, z, sbb);
					} else if (z == min) {
						placeBlock(world, birchStairsNorth, x, y, z, sbb);
					} else {
						placeBlock(world, woodenPlanks, x, y, z, sbb);
					}
				}
			}
		}
	}
}
