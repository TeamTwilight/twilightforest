package twilightforest.world.components.structures.icetower;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;


public class IceTowerBossWingComponent extends IceTowerWingComponent {

	public IceTowerBossWingComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFITBoss.get(), nbt);
		// no spawns
		this.spawnListIndex = -1;
	}

	public IceTowerBossWingComponent(int index, int x, int y, int z, int wingSize, int wingHeight, Direction direction) {
		super(TFStructurePieceTypes.TFITBoss.get(), index, x, y, z, wingSize, wingHeight, direction);
		// no spawns
		this.spawnListIndex = -1;
	}

	@Override
	protected boolean shouldHaveBase(RandomSource rand) {
		return false;
	}

	/**
	 * Put down planks or whatevs for a floor
	 */
	@Override
	protected void placeFloor(WorldGenLevel world, RandomSource rand, BoundingBox sbb, int floorHeight, int floor) {
		for (int x = 1; x < size - 1; x++) {
			for (int z = 1; z < size - 1; z++) {

				BlockState ice = (rand.nextInt(4) == 0 ? Blocks.ICE : Blocks.PACKED_ICE).defaultBlockState();
				int thickness = 1 + rand.nextInt(2) + rand.nextInt(2) + rand.nextInt(2);

				for (int y = 0; y < thickness; y++) {
					placeBlock(world, ice, x, (floor * floorHeight) + floorHeight - y, z, sbb);
				}
			}
		}
	}

	/**
	 * Called to decorate each floor.  This is responsible for adding a ladder up, the stub of the ladder going down, then picking a theme for each floor and executing it.
	 */
	@Override
	protected void decorateFloor(WorldGenLevel world, RandomSource rand, int floor, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, BoundingBox sbb) {

		Rotation r = ladderDownDir;
		for (int y = 0; y < 3; y++) {
			placeIceStairs(world, sbb, rand, bottom + (y * 3), r);
			placeIceStairs(world, sbb, rand, bottom + (y * 3), r.getRotated(Rotation.CLOCKWISE_180));
			r = r.getRotated(Rotation.CLOCKWISE_90);
		}
	}

	private void placeIceStairs(WorldGenLevel world, BoundingBox sbb, RandomSource rand, int y, Rotation rotation) {
		final BlockState packedIce = Blocks.PACKED_ICE.defaultBlockState();
		this.fillBlocksRotated(world, sbb, 8, y + 1, 1, 10, y + 1, 3, packedIce, rotation);
		if (y > 1) {
			this.randomlyFillBlocksRotated(world, sbb, rand, 0.5F, 8, y, 1, 10, y, 3, packedIce, AIR, rotation);
		}
		this.fillBlocksRotated(world, sbb, 11, y + 2, 1, 13, y + 2, 3, packedIce, rotation);
		this.randomlyFillBlocksRotated(world, sbb, rand, 0.5F, 11, y + 1, 1, 13, y + 1, 3, packedIce, AIR, rotation);
		this.fillBlocksRotated(world, sbb, 11, y + 3, 4, 13, y + 3, 6, packedIce, rotation);
		this.randomlyFillBlocksRotated(world, sbb, rand, 0.5F, 11, y + 2, 4, 13, y + 2, 6, packedIce, AIR, rotation);
	}

	@Override
	protected void decorateTopFloor(WorldGenLevel world, RandomSource rand, int floor, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, BoundingBox sbb) {
		for (int x = 1; x < size - 1; x++) {
			for (int z = 1; z < size - 1; z++) {

				BlockState ice = (rand.nextInt(10) == 0 ? Blocks.GLOWSTONE : Blocks.PACKED_ICE).defaultBlockState();
				int thickness = rand.nextInt(2) + rand.nextInt(2);

				for (int y = 0; y < thickness; y++) {
					placeBlock(world, ice, x, top - 1 - y, z, sbb);
				}
			}
		}

		final BlockState snowQueenSpawner = TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.get().defaultBlockState();

		this.setBlockStateRotated(world, snowQueenSpawner, 7, top - 6, 7, Rotation.NONE, sbb);

		this.generateAirBox(world, sbb, 5, top - 3, 5, 9, top - 1, 9);
	}
}
