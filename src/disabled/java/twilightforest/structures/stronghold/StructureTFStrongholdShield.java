package twilightforest.structures.stronghold;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import twilightforest.block.BlockTFShield;
import twilightforest.block.TFBlocks;

public class StructureTFStrongholdShield extends StructureTFStrongholdComponent {

	public StructureTFStrongholdShield() {}

	public StructureTFStrongholdShield(int minX, int minY, int minZ, int maxX, int maxY, int maxZ) {
		super(0, 0, minX, minY, minZ);
		this.boundingBox = new StructureBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
		this.spawnListIndex = -1;
	}

	@Override
	public StructureBoundingBox generateBoundingBox(int facing, int x, int y, int z) {
		return null;
	}

	@Override
	public boolean addComponentParts(World world, Random random, StructureBoundingBox sbb) {
		IBlockState down = TFBlocks.shield.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.DOWN);
		IBlockState up = TFBlocks.shield.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.UP);
		IBlockState north = TFBlocks.shield.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.NORTH);
		IBlockState south = TFBlocks.shield.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.SOUTH);
		IBlockState west = TFBlocks.shield.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.WEST);
		IBlockState east = TFBlocks.shield.getDefaultState().withProperty(TFBlockProperties.FACING, EnumFacing.EAST);
		
        // +x
        this.fillWithBlocks(world, sbb, this.boundingBox.getXSize(), 0, 0, this.boundingBox.getXSize(), this.boundingBox.getYSize(), this.boundingBox.getZSize(), west, west, false);
        // -x
        this.fillWithBlocks(world, sbb, 0, 0, 0, 0, this.boundingBox.getYSize(), this.boundingBox.getZSize(), east, east, false);
        // +z
        this.fillWithBlocks(world, sbb, 0, 0, this.boundingBox.getZSize(), this.boundingBox.getXSize(), this.boundingBox.getYSize(), this.boundingBox.getZSize(), north, north, false);
        // -z
        this.fillWithBlocks(world, sbb, 0, 0, 0, this.boundingBox.getXSize(), this.boundingBox.getYSize(), 0, south, south, false);
		// top
        this.fillWithBlocks(world, sbb, 0, 0, 0, this.boundingBox.getXSize(), 0, this.boundingBox.getZSize(), up, up, false);
        // bottom
        this.fillWithBlocks(world, sbb, 0, this.boundingBox.getYSize(), 0, this.boundingBox.getXSize(), this.boundingBox.getYSize(), this.boundingBox.getZSize(), down, down, false);
        
        return true;
	}

}
