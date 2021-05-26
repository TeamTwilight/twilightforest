package twilightforest.structures.icetower;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BossVariant;

import java.util.Random;

public class ComponentTFIceTowerBossWing extends ComponentTFIceTowerWing {

	public ComponentTFIceTowerBossWing(TemplateManager manager, CompoundNBT nbt) {
		super(TFIceTowerPieces.TFITBoss, nbt);
		// no spawns
		this.spawnListIndex = -1;
	}

	public ComponentTFIceTowerBossWing(TFFeature feature, int index, int x, int y, int z, int wingSize, int wingHeight, Direction direction) {
		super(TFIceTowerPieces.TFITBoss, feature, index, x, y, z, wingSize, wingHeight, direction);
		// no spawns
		this.spawnListIndex = -1;
	}

	@Override
	protected boolean shouldHaveBase(Random rand) {
		return false;
	}

	/**
	 * Put down planks or whatevs for a floor
	 */
	@Override
	protected void placeFloor(ISeedReader world, Random rand, MutableBoundingBox sbb, int floorHeight, int floor) {
		for (int x = 1; x < size - 1; x++) {
			for (int z = 1; z < size - 1; z++) {

				BlockState ice = (rand.nextInt(4) == 0 ? Blocks.ICE : Blocks.PACKED_ICE).getDefaultState();
				int thickness = 1 + rand.nextInt(2) + rand.nextInt(2) + rand.nextInt(2);

				for (int y = 0; y < thickness; y++) {
					setBlockState(world, ice, x, (floor * floorHeight) + floorHeight - y, z, sbb);
				}
			}
		}
	}

	/**
	 * Called to decorate each floor.  This is responsible for adding a ladder up, the stub of the ladder going down, then picking a theme for each floor and executing it.
	 *
	 * @param floor
	 * @param bottom
	 * @param top
	 * @param ladderUpDir
	 * @param ladderDownDir
	 */
	@Override
	protected void decorateFloor(ISeedReader world, Random rand, int floor, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, MutableBoundingBox sbb) {

		Rotation r = ladderDownDir;
		for (int y = 0; y < 3; y++) {
			placeIceStairs(world, sbb, rand, bottom + (y * 3), r);
			placeIceStairs(world, sbb, rand, bottom + (y * 3), r.add(Rotation.CLOCKWISE_180));
			r = r.add(Rotation.CLOCKWISE_90);
		}
	}

	private void placeIceStairs(ISeedReader world, MutableBoundingBox sbb, Random rand, int y, Rotation rotation) {
		final BlockState packedIce = Blocks.PACKED_ICE.getDefaultState();
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
	protected void decorateTopFloor(ISeedReader world, Random rand, int floor, int bottom, int top, Rotation ladderUpDir, Rotation ladderDownDir, MutableBoundingBox sbb) {
		for (int x = 1; x < size - 1; x++) {
			for (int z = 1; z < size - 1; z++) {

				BlockState ice = (rand.nextInt(10) == 0 ? Blocks.GLOWSTONE : Blocks.PACKED_ICE).getDefaultState();
				int thickness = rand.nextInt(2) + rand.nextInt(2);

				for (int y = 0; y < thickness; y++) {
					setBlockState(world, ice, x, top - 1 - y, z, sbb);
				}
			}
		}

		final BlockState snowQueenSpawner = TFBlocks.boss_spawner_snow_queen.get().getDefaultState();

		this.setBlockStateRotated(world, snowQueenSpawner, 7, top - 6, 7, Rotation.NONE, sbb);

		this.fillWithAir(world, sbb, 5, top - 3, 5, 9, top - 1, 9);
	}
}
