package twilightforest.structures.minotaurmaze;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.TFFeature;
import twilightforest.TFTreasure;
import twilightforest.block.BlockTFMazestone;
import twilightforest.block.TFBlocks;
import twilightforest.enums.MazestoneVariant;
import twilightforest.util.TFEntityNames;

import java.util.Random;

public class ComponentTFMazeRoomSpawnerChests extends ComponentTFMazeRoom {

	public ComponentTFMazeRoomSpawnerChests() {
		super();
	}

	public ComponentTFMazeRoomSpawnerChests(TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(feature, i, rand, x, y, z);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		super.addComponentParts(world, rand, sbb);

		// 4 pillar enclosures
		placePillarEnclosure(world, sbb, 3, 3);
		placePillarEnclosure(world, sbb, 10, 3);
		placePillarEnclosure(world, sbb, 3, 10);
		placePillarEnclosure(world, sbb, 10, 10);

		// spawner
		setSpawner(world, 4, 2, 4, sbb, TFEntityNames.MINOTAUR);

		// treasure
		this.placeTreasureAtCurrentPosition(world, rand, 4, 2, 11, TFTreasure.labyrinth_room, sbb);

		// treasure
		this.placeTreasureAtCurrentPosition(world, rand, 11, 2, 4, TFTreasure.labyrinth_room, sbb);

		// trap
		setBlockState(world, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 11, 1, 11, sbb);
		setBlockState(world, Blocks.TNT.getDefaultState(), 10, 0, 11, sbb);
		setBlockState(world, Blocks.TNT.getDefaultState(), 11, 0, 10, sbb);
		setBlockState(world, Blocks.TNT.getDefaultState(), 11, 0, 12, sbb);
		setBlockState(world, Blocks.TNT.getDefaultState(), 12, 0, 11, sbb);

		return true;
	}

	private void placePillarEnclosure(World world, StructureBoundingBox sbb,
									  int dx, int dz) {
		for (int y = 1; y < 5; y++) {
			final IBlockState chiselledMazeBlock = TFBlocks.maze_stone.getDefaultState().withProperty(BlockTFMazestone.VARIANT, MazestoneVariant.CHISELED);
			setBlockState(world, chiselledMazeBlock, dx + 0, y, dz + 0, sbb);
			setBlockState(world, chiselledMazeBlock, dx + 2, y, dz + 0, sbb);
			setBlockState(world, chiselledMazeBlock, dx + 0, y, dz + 2, sbb);
			setBlockState(world, chiselledMazeBlock, dx + 2, y, dz + 2, sbb);
		}
		setBlockState(world, Blocks.PLANKS.getDefaultState(), dx + 1, 1, dz + 1, sbb);
		setBlockState(world, Blocks.PLANKS.getDefaultState(), dx + 1, 4, dz + 1, sbb);

		final IBlockState defaultState = Blocks.OAK_STAIRS.getDefaultState();


		setBlockState(world, getStairState(defaultState, EnumFacing.NORTH, rotation, false), dx + 1, 1, dz + 0, sbb);
		setBlockState(world, getStairState(defaultState, EnumFacing.WEST, rotation, false), dx + 0, 1, dz + 1, sbb);
		setBlockState(world, getStairState(defaultState, EnumFacing.EAST, rotation, false), dx + 2, 1, dz + 1, sbb);
		setBlockState(world, getStairState(defaultState, EnumFacing.SOUTH, rotation, false), dx + 1, 1, dz + 2, sbb);

		setBlockState(world, getStairState(defaultState, EnumFacing.NORTH, rotation, true), dx + 1, 4, dz + 0, sbb);
		setBlockState(world, getStairState(defaultState, EnumFacing.WEST, rotation, true), dx + 0, 4, dz + 1, sbb);
		setBlockState(world, getStairState(defaultState, EnumFacing.EAST, rotation, true), dx + 2, 4, dz + 1, sbb);
		setBlockState(world, getStairState(defaultState, EnumFacing.SOUTH, rotation, true), dx + 1, 4, dz + 2, sbb);

		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 1, 2, dz + 0, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 0, 2, dz + 1, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 2, 2, dz + 1, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 1, 2, dz + 2, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 1, 3, dz + 0, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 0, 3, dz + 1, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 2, 3, dz + 1, sbb);
		setBlockState(world, Blocks.IRON_BARS.getDefaultState(), dx + 1, 3, dz + 2, sbb);


	}

}
