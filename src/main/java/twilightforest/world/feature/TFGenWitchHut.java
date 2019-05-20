package twilightforest.world.feature;

import net.minecraft.entity.EntityList;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.entity.EntityTFSkeletonDruid;

import java.util.Random;


public class TFGenWitchHut extends TFGenerator {

	/**
	 * Make a cute witch's hut
	 */
	@Override
	public boolean generate(World world, Random rand, BlockPos pos) {
		return generateTinyHut(world, rand, pos);
	}

	/**
	 * Make the smallest size hut
	 */
	public boolean generateTinyHut(World world, Random rand, BlockPos pos) {
		if (!isAreaSuitable(world, rand, pos, 5, 7, 6)) {
			return false;
		}

		// walls!

		setBlockAndNotifyAdequately(world, pos.add(1, 0, 1), randStone(rand, 1));
		setBlockAndNotifyAdequately(world, pos.add(2, 0, 1), randStone(rand, 1));
		setBlockAndNotifyAdequately(world, pos.add(3, 0, 1), randStone(rand, 1));
		setBlockAndNotifyAdequately(world, pos.add(5, 0, 1), randStone(rand, 1));
		setBlockAndNotifyAdequately(world, pos.add(0, 0, 2), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 0, 2), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 0, 2), randStone(rand, 1));
		setBlockAndNotifyAdequately(world, pos.add(0, 0, 3), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 0, 3), randStone(rand, 1));
		setBlockAndNotifyAdequately(world, pos.add(0, 0, 4), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 0, 4), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 0, 4), randStone(rand, 1));
		setBlockAndNotifyAdequately(world, pos.add(1, 0, 5), randStone(rand, 1));
		setBlockAndNotifyAdequately(world, pos.add(2, 0, 5), randStone(rand, 1));
		setBlockAndNotifyAdequately(world, pos.add(3, 0, 5), randStone(rand, 1));
		setBlockAndNotifyAdequately(world, pos.add(5, 0, 5), randStone(rand, 1));

		setBlockAndNotifyAdequately(world, pos.add(1, 1, 1), randStone(rand, 2));
		setBlockAndNotifyAdequately(world, pos.add(3, 1, 1), randStone(rand, 2));
		setBlockAndNotifyAdequately(world, pos.add(5, 1, 1), randStone(rand, 2));
		setBlockAndNotifyAdequately(world, pos.add(0, 1, 2), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 1, 2), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 1, 2), randStone(rand, 2));
		setBlockAndNotifyAdequately(world, pos.add(0, 1, 3), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 1, 4), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 1, 4), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 1, 4), randStone(rand, 2));
		setBlockAndNotifyAdequately(world, pos.add(1, 1, 5), randStone(rand, 2));
		setBlockAndNotifyAdequately(world, pos.add(3, 1, 5), randStone(rand, 2));
		setBlockAndNotifyAdequately(world, pos.add(5, 1, 5), randStone(rand, 2));

		setBlockAndNotifyAdequately(world, pos.add(1, 2, 1), randStone(rand, 3));
		setBlockAndNotifyAdequately(world, pos.add(2, 2, 1), randStone(rand, 3));
		setBlockAndNotifyAdequately(world, pos.add(3, 2, 1), randStone(rand, 3));
		setBlockAndNotifyAdequately(world, pos.add(4, 2, 1), randStone(rand, 3));
		setBlockAndNotifyAdequately(world, pos.add(5, 2, 1), randStone(rand, 3));
		setBlockAndNotifyAdequately(world, pos.add(0, 2, 2), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 2, 2), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 2, 2), randStone(rand, 3));
		setBlockAndNotifyAdequately(world, pos.add(0, 2, 3), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 2, 3), randStone(rand, 3));
		setBlockAndNotifyAdequately(world, pos.add(0, 2, 4), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 2, 4), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 2, 4), randStone(rand, 1));
		setBlockAndNotifyAdequately(world, pos.add(1, 2, 5), randStone(rand, 3));
		setBlockAndNotifyAdequately(world, pos.add(2, 2, 5), randStone(rand, 3));
		setBlockAndNotifyAdequately(world, pos.add(3, 2, 5), randStone(rand, 3));
		setBlockAndNotifyAdequately(world, pos.add(4, 2, 5), randStone(rand, 3));
		setBlockAndNotifyAdequately(world, pos.add(5, 2, 5), randStone(rand, 3));

		setBlockAndNotifyAdequately(world, pos.add(0, 3, 2), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 3, 3), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 3, 4), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 3, 1), randStone(rand, 4));
		setBlockAndNotifyAdequately(world, pos.add(3, 3, 1), randStone(rand, 4));
		setBlockAndNotifyAdequately(world, pos.add(4, 3, 1), randStone(rand, 4));
		setBlockAndNotifyAdequately(world, pos.add(2, 3, 5), randStone(rand, 4));
		setBlockAndNotifyAdequately(world, pos.add(3, 3, 5), randStone(rand, 4));
		setBlockAndNotifyAdequately(world, pos.add(4, 3, 5), randStone(rand, 4));

		setBlockAndNotifyAdequately(world, pos.add(0, 4, 3), Blocks.BRICK_BLOCK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 4, 1), randStone(rand, 5));
		setBlockAndNotifyAdequately(world, pos.add(3, 4, 5), randStone(rand, 5));

		setBlockAndNotifyAdequately(world, pos.add(0, 5, 3), Blocks.BRICK_BLOCK.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(0, 6, 3), Blocks.BRICK_BLOCK.getDefaultState());

		// roof!
		setBlockAndNotifyAdequately(world, pos.add(0, 2, 0), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 2, 1), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 2, 5), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(0, 2, 6), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(6, 2, 0), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(6, 2, 1), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(6, 2, 2), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(6, 2, 3), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(6, 2, 4), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(6, 2, 5), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(6, 2, 6), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(1, 3, 0), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 3, 1), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 3, 2), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 3, 4), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 3, 5), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 3, 6), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 3, 0), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 3, 1), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 3, 2), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 3, 3), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 3, 4), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 3, 5), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 3, 6), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(1, 4, 0), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 4, 0), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 4, 1), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 4, 2), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 4, 3), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 4, 4), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 4, 5), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 4, 6), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 4, 6), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 4, 0), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(4, 4, 0), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(4, 4, 1), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(4, 4, 2), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(4, 4, 3), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(4, 4, 4), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(4, 4, 5), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(4, 4, 6), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(5, 4, 6), Blocks.WOODEN_SLAB.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(2, 5, 0), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 5, 1), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(4, 5, 0), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(4, 5, 1), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 5, 0), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 5, 1), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 5, 2), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 5, 3), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 5, 4), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 5, 5), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 5, 6), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 5, 5), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(2, 5, 6), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(4, 5, 5), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(4, 5, 6), Blocks.WOODEN_SLAB.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(3, 6, 0), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 6, 1), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 6, 2), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 6, 4), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 6, 5), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 6, 6), Blocks.DOUBLE_WOODEN_SLAB.getDefaultState());

		setBlockAndNotifyAdequately(world, pos.add(3, 7, 0), Blocks.WOODEN_SLAB.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(3, 7, 6), Blocks.WOODEN_SLAB.getDefaultState());

		// fire in chimney!
		setBlockAndNotifyAdequately(world, pos.add(1, -1, 3), Blocks.NETHERRACK.getDefaultState());
		setBlockAndNotifyAdequately(world, pos.add(1, 0, 3), Blocks.FIRE.getDefaultState()); // oh god the roof!

		// skeleton spawner!
		world.setBlockState(pos.add(3, 1, 3), Blocks.MOB_SPAWNER.getDefaultState(), 16 | 2);
		TileEntityMobSpawner ms = (TileEntityMobSpawner) world.getTileEntity(pos.add(3, 1, 3));
		if (ms != null) {
			ms.getSpawnerBaseLogic().setEntityId(EntityList.getKey(EntityTFSkeletonDruid.class));
		}

		return true;
	}

}
