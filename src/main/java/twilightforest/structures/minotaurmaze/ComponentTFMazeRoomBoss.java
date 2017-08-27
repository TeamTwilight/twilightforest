package twilightforest.structures.minotaurmaze;

import net.minecraft.block.BlockHugeMushroom;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFTreasure;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.block.enums.BossVariant;

import java.util.Random;

public class ComponentTFMazeRoomBoss extends ComponentTFMazeRoom {


	public ComponentTFMazeRoomBoss() {
		super();
		// TODO Auto-generated constructor stub
	}


	public ComponentTFMazeRoomBoss(int i, Random rand, int x, int y, int z) {
		super(i, rand, x, y, z);
	}


	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		// doorways
		if (this.getBlockStateFromPos(world, 7, 1, 0, sbb).getBlock() == Blocks.AIR) {
			fillWithBlocks(world, sbb, 6, 1, 0, 9, 4, 0, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
		}

		if (this.getBlockStateFromPos(world, 7, 1, 15, sbb).getBlock() == Blocks.AIR) {
			fillWithBlocks(world, sbb, 6, 1, 15, 9, 4, 15, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
		}

		if (this.getBlockStateFromPos(world, 0, 1, 7, sbb).getBlock() == Blocks.AIR) {
			fillWithBlocks(world, sbb, 0, 1, 6, 0, 4, 9, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
		}

		if (this.getBlockStateFromPos(world, 15, 1, 7, sbb).getBlock() == Blocks.AIR) {
			fillWithBlocks(world, sbb, 15, 1, 6, 15, 4, 9, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
		}

		// mycelium / small mushrooms on floor
		for (int x = 1; x < 14; x++) {
			for (int z = 1; z < 14; z++) {
				// calculate distance from middle
				int dist = (int) Math.round(7 / Math.sqrt((7.5 - x) * (7.5 - x) + (7.5 - z) * (7.5 - z)));
				boolean mycelium = rand.nextInt(dist + 1) > 0;
				boolean mushroom = rand.nextInt(dist) > 0;
				boolean mushRed = rand.nextBoolean();

				// make part of the floor mycelium
				if (mycelium) {
					this.setBlockState(world, Blocks.MYCELIUM.getDefaultState(), x, 0, z, sbb);
				}
				// add small mushrooms all over
				if (mushroom) {
					this.setBlockState(world, (mushRed ? Blocks.RED_MUSHROOM : Blocks.BROWN_MUSHROOM).getDefaultState(), x, 1, z, sbb);
				}
			}
		}

		// mushroom chest shelves in corner
		final IBlockState redMushroom = Blocks.RED_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_OUTSIDE);
		final IBlockState brownMushroom = Blocks.BROWN_MUSHROOM_BLOCK.getDefaultState().withProperty(BlockHugeMushroom.VARIANT, BlockHugeMushroom.EnumType.ALL_OUTSIDE);

		fillWithBlocks(world, sbb, 1, 1, 1, 3, 1, 3, redMushroom, AIR, false);
		fillWithBlocks(world, sbb, 1, 2, 1, 1, 3, 4, redMushroom, AIR, false);
		fillWithBlocks(world, sbb, 2, 2, 1, 4, 3, 1, redMushroom, AIR, false);
		fillWithBlocks(world, sbb, 1, 4, 1, 3, 4, 3, redMushroom, AIR, false);
		placeTreasureAtCurrentPosition(world, rand, 3, 2, 3, TFTreasure.labyrinth_room, sbb);

		fillWithBlocks(world, sbb, 12, 1, 12, 14, 1, 14, redMushroom, AIR, false);
		fillWithBlocks(world, sbb, 14, 2, 11, 14, 3, 14, redMushroom, AIR, false);
		fillWithBlocks(world, sbb, 11, 2, 14, 14, 3, 14, redMushroom, AIR, false);
		fillWithBlocks(world, sbb, 12, 4, 12, 14, 4, 14, redMushroom, AIR, false);
		placeTreasureAtCurrentPosition(world, rand, 12, 2, 12, TFTreasure.labyrinth_room, sbb);

		fillWithBlocks(world, sbb, 1, 1, 12, 3, 1, 14, redMushroom, AIR, false);
		fillWithBlocks(world, sbb, 1, 2, 11, 1, 3, 14, redMushroom, AIR, false);
		fillWithBlocks(world, sbb, 2, 2, 14, 4, 3, 14, redMushroom, AIR, false);
		fillWithBlocks(world, sbb, 1, 4, 12, 3, 4, 14, redMushroom, AIR, false);
		placeTreasureAtCurrentPosition(world, rand, 3, 2, 12, TFTreasure.labyrinth_room, sbb);

		fillWithBlocks(world, sbb, 12, 1, 1, 14, 1, 3, brownMushroom, AIR, false);
		fillWithBlocks(world, sbb, 11, 2, 1, 14, 3, 1, brownMushroom, AIR, false);
		fillWithBlocks(world, sbb, 14, 2, 2, 14, 3, 4, brownMushroom, AIR, false);
		fillWithBlocks(world, sbb, 12, 4, 1, 14, 4, 3, brownMushroom, AIR, false);
		placeTreasureAtCurrentPosition(world, rand, 12, 2, 3, TFTreasure.labyrinth_room, sbb);

		// a few more ceilingshrooms
		fillWithBlocks(world, sbb, 5, 4, 5, 7, 5, 7, brownMushroom, AIR, false);
		fillWithBlocks(world, sbb, 8, 4, 8, 10, 5, 10, redMushroom, AIR, false);

		// the moo-cen-mino-shrom-taur!
		final IBlockState taurSpawner = TFBlocks.bossSpawner.getDefaultState().withProperty(BlockTFBossSpawner.VARIANT, BossVariant.MINOSHROOM);
		setBlockStateRotated(world, taurSpawner, 7, 1, 7, Rotation.NONE, sbb);

		return true;
	}
}
