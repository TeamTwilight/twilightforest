package twilightforest.structures.stronghold;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFBossSpawner;
import twilightforest.block.TFBlocks;
import twilightforest.enums.BossVariant;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdBossRoom extends StructureTFStrongholdComponent {

	public ComponentTFStrongholdBossRoom() {
	}

	public ComponentTFStrongholdBossRoom(TFFeature feature, int i, EnumFacing facing, int x, int y, int z) {
		super(feature, i, facing, x, y, z);
		this.spawnListIndex = Integer.MAX_VALUE;
	}

	@Override
	public StructureBoundingBox generateBoundingBox(EnumFacing facing, int x, int y, int z) {
		return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -13, -1, 0, 27, 7, 27, facing);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random random) {
		super.buildComponent(parent, list, random);

		this.addDoor(13, 1, 0);
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 26, 6, 26, rand, deco.randomBlocks);

		// inner walls
		this.fillWithRandomizedBlocks(world, sbb, 1, 1, 1, 3, 5, 25, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 23, 1, 1, 25, 5, 25, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 4, 1, 1, 22, 5, 3, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 4, 1, 23, 22, 5, 25, false, rand, deco.randomBlocks);

		// obsidian filler
		this.fillWithBlocks(world, sbb, 1, 1, 1, 2, 5, 25, Blocks.OBSIDIAN.getDefaultState(), Blocks.OBSIDIAN.getDefaultState(), false);
		this.fillWithBlocks(world, sbb, 24, 1, 1, 25, 5, 25, Blocks.OBSIDIAN.getDefaultState(), Blocks.OBSIDIAN.getDefaultState(), false);
		this.fillWithBlocks(world, sbb, 4, 1, 1, 22, 5, 2, Blocks.OBSIDIAN.getDefaultState(), Blocks.OBSIDIAN.getDefaultState(), false);
		this.fillWithBlocks(world, sbb, 4, 1, 24, 22, 5, 25, Blocks.OBSIDIAN.getDefaultState(), Blocks.OBSIDIAN.getDefaultState(), false);

		// corner pillars
		this.fillWithRandomizedBlocks(world, sbb, 4, 1, 4, 4, 5, 7, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 5, 1, 4, 5, 5, 5, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 6, 1, 4, 7, 5, 4, false, rand, deco.randomBlocks);

		this.fillWithRandomizedBlocks(world, sbb, 4, 1, 19, 4, 5, 22, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 5, 1, 21, 5, 5, 22, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 6, 1, 22, 7, 5, 22, false, rand, deco.randomBlocks);

		this.fillWithRandomizedBlocks(world, sbb, 22, 1, 4, 22, 5, 7, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 21, 1, 4, 21, 5, 5, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 19, 1, 4, 20, 5, 4, false, rand, deco.randomBlocks);

		this.fillWithRandomizedBlocks(world, sbb, 22, 1, 19, 22, 5, 22, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 21, 1, 21, 21, 5, 22, false, rand, deco.randomBlocks);
		this.fillWithRandomizedBlocks(world, sbb, 19, 1, 22, 20, 5, 22, false, rand, deco.randomBlocks);

		// pillar decorations (stairs)
		placePillarDecorations(world, sbb, Rotation.NONE);
		placePillarDecorations(world, sbb, Rotation.CLOCKWISE_90);
		placePillarDecorations(world, sbb, Rotation.CLOCKWISE_180);
		placePillarDecorations(world, sbb, Rotation.COUNTERCLOCKWISE_90);

		// sarcophagi
		placeSarcophagus(world, sbb, 8, 1, 8, Rotation.NONE);
		placeSarcophagus(world, sbb, 13, 1, 8, Rotation.NONE);
		placeSarcophagus(world, sbb, 18, 1, 8, Rotation.NONE);

		placeSarcophagus(world, sbb, 8, 1, 15, Rotation.NONE);
		placeSarcophagus(world, sbb, 13, 1, 15, Rotation.NONE);
		placeSarcophagus(world, sbb, 18, 1, 15, Rotation.NONE);


		// doorway
		this.fillWithAir(world, sbb, 12, 1, 1, 14, 4, 2);
		this.fillWithBlocks(world, sbb, 12, 1, 3, 14, 4, 3, Blocks.IRON_BARS.getDefaultState(), Blocks.IRON_BARS.getDefaultState(), false);

		//spawner
		setBlockState(world, TFBlocks.bossSpawner.getDefaultState().withProperty(BlockTFBossSpawner.VARIANT, BossVariant.KNIGHT_PHANTOM), 13, 2, 13, sbb);

		// doors
		placeDoors(world, rand, sbb);

		return true;
	}


	private void placeSarcophagus(World world, StructureBoundingBox sbb, int x, int y, int z, Rotation rotation) {

		this.setBlockStateRotated(world, deco.pillarState, x - 1, y, z + 0, rotation, sbb);
		this.setBlockStateRotated(world, deco.pillarState, x + 1, y, z + 3, rotation, sbb);
		this.setBlockStateRotated(world, deco.pillarState, x + 1, y, z + 0, rotation, sbb);
		this.setBlockStateRotated(world, deco.pillarState, x - 1, y, z + 3, rotation, sbb);

		// make either torches or fence posts

		if (world.rand.nextInt(7) == 0) {
			this.setBlockStateRotated(world, Blocks.TORCH.getDefaultState(), x + 1, y + 1, z + 0, rotation, sbb);
		} else {
			this.setBlockStateRotated(world, deco.fenceState, x + 1, y + 1, z + 0, rotation, sbb);
		}
		if (world.rand.nextInt(7) == 0) {
			this.setBlockStateRotated(world, Blocks.TORCH.getDefaultState(), x - 1, y + 1, z + 0, rotation, sbb);
		} else {
			this.setBlockStateRotated(world, deco.fenceState, x - 1, y + 1, z + 0, rotation, sbb);
		}
		if (world.rand.nextInt(7) == 0) {
			this.setBlockStateRotated(world, Blocks.TORCH.getDefaultState(), x + 1, y + 1, z + 3, rotation, sbb);
		} else {
			this.setBlockStateRotated(world, deco.fenceState, x + 1, y + 1, z + 3, rotation, sbb);
		}
		if (world.rand.nextInt(7) == 0) {
			this.setBlockStateRotated(world, Blocks.TORCH.getDefaultState(), x - 1, y + 1, z + 3, rotation, sbb);
		} else {
			this.setBlockStateRotated(world, deco.fenceState, x - 1, y + 1, z + 3, rotation, sbb);
		}

		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_90.rotate(EnumFacing.WEST), rotation, false), x + 0, y, z + 0, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, false), x + 0, y, z + 3, rotation, sbb);

		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, false), x + 1, y, z + 1, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, false), x + 1, y, z + 2, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(EnumFacing.WEST), rotation, false), x - 1, y, z + 1, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.NONE.rotate(EnumFacing.WEST), rotation, false), x - 1, y, z + 2, rotation, sbb);

		this.setBlockStateRotated(world, Blocks.STONE_SLAB.getDefaultState(), x + 0, y + 1, z + 1, rotation, sbb);
		this.setBlockStateRotated(world, Blocks.STONE_SLAB.getDefaultState(), x + 0, y + 1, z + 2, rotation, sbb);

	}

	protected void placePillarDecorations(World world, StructureBoundingBox sbb, Rotation rotation) {
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, false), 4, 1, 8, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, false), 8, 1, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 4, 5, 8, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, true), 8, 5, 4, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, false), 5, 1, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, false), 6, 1, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, false), 6, 1, 5, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.COUNTERCLOCKWISE_90.rotate(EnumFacing.WEST), rotation, true), 5, 5, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, true), 6, 5, 6, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Rotation.CLOCKWISE_180.rotate(EnumFacing.WEST), rotation, true), 6, 5, 5, rotation, sbb);
	}

	@Override
	protected void placeDoorwayAt(World world, Random rand, int x, int y, int z, StructureBoundingBox sbb) {
		if (x == 0 || x == getXSize()) {
			this.fillWithBlocks(world, sbb, x, y, z - 1, x, y + 3, z + 1, Blocks.IRON_BARS.getDefaultState(), Blocks.AIR.getDefaultState(), false);
		} else {
			this.fillWithBlocks(world, sbb, x - 1, y, z, x + 1, y + 3, z, Blocks.IRON_BARS.getDefaultState(), Blocks.AIR.getDefaultState(), false);
		}
	}

	@Override
	protected boolean isValidBreakInPoint(int wx, int wy, int wz) {
		return false;
	}
}
