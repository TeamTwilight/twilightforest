package twilightforest.structures.stronghold;

import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.template.TemplateManager;
import twilightforest.TFTreasure;

import java.util.List;
import java.util.Random;

public class ComponentTFStrongholdDeadEnd extends StructureTFStrongholdComponent {


	private boolean chestTrapped;

	public ComponentTFStrongholdDeadEnd() {
	}

	public ComponentTFStrongholdDeadEnd(int i, EnumFacing facing, int x, int y, int z) {
		super(i, facing, x, y, z);
	}

	@Override
	protected void writeStructureToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeStructureToNBT(par1NBTTagCompound);

		par1NBTTagCompound.setBoolean("chestTrapped", this.chestTrapped);
	}

	@Override
	protected void readStructureFromNBT(NBTTagCompound par1NBTTagCompound, TemplateManager templateManager) {
		super.readStructureFromNBT(par1NBTTagCompound, templateManager);

		this.chestTrapped = par1NBTTagCompound.getBoolean("chestTrapped");
	}

	@Override
	public StructureBoundingBox generateBoundingBox(EnumFacing facing, int x, int y, int z) {
		return StructureTFStrongholdComponent.getComponentToAddBoundingBox(x, y, z, -4, -1, 0, 9, 6, 9, facing);
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random random) {
		super.buildComponent(parent, list, random);

		// entrance
		this.addDoor(4, 1, 0);

		this.chestTrapped = random.nextInt(3) == 0;
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		placeStrongholdWalls(world, sbb, 0, 0, 0, 8, 6, 8, rand, deco.randomBlocks);

		// statues
		this.placeWallStatue(world, 1, 1, 4, Rotation.CLOCKWISE_90, sbb);
		this.placeWallStatue(world, 7, 1, 4, Rotation.COUNTERCLOCKWISE_90, sbb);
		this.placeWallStatue(world, 4, 1, 7, Rotation.NONE, sbb);

		// doors
		placeDoors(world, rand, sbb);

		// treasure
		this.placeTreasureAtCurrentPosition(world, rand, 4, 1, 3, TFTreasure.stronghold_cache, this.chestTrapped, sbb);
		if (this.chestTrapped) {
			this.setBlockState(world, Blocks.TNT.getDefaultState(), 4, 0, 3, sbb);
		}

		for (int z = 2; z < 5; z++) {
			this.setBlockState(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.WEST), 3, 1, z, sbb);
			this.setBlockState(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.EAST), 5, 1, z, sbb);
		}

		this.setBlockState(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.NORTH), 4, 1, 2, sbb);
		this.setBlockState(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.SOUTH), 4, 1, 4, sbb);
		this.setBlockState(world, deco.stairState.withProperty(BlockStairs.FACING, EnumFacing.NORTH), 4, 2, 3, sbb);

		return true;
	}


}
