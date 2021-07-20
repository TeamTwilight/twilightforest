package twilightforest.structures.stronghold;

import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class StructureTFStrongholdShield extends StructureTFStrongholdComponent {

	public StructureTFStrongholdShield(TemplateManager manager, CompoundNBT nbt) {
		super(TFStrongholdPieces.TFSShield, nbt);
		this.spawnListIndex = -1;
	}

	@Override
	public MutableBoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return null;
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random randomIn, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		BlockState down  = TFBlocks.stronghold_shield.get().getDefaultState().with(DirectionalBlock.FACING, Direction.DOWN);
		BlockState up    = TFBlocks.stronghold_shield.get().getDefaultState().with(DirectionalBlock.FACING, Direction.UP);
		BlockState north = TFBlocks.stronghold_shield.get().getDefaultState().with(DirectionalBlock.FACING, Direction.NORTH);
		BlockState south = TFBlocks.stronghold_shield.get().getDefaultState().with(DirectionalBlock.FACING, Direction.SOUTH);
		BlockState west  = TFBlocks.stronghold_shield.get().getDefaultState().with(DirectionalBlock.FACING, Direction.WEST);
		BlockState east  = TFBlocks.stronghold_shield.get().getDefaultState().with(DirectionalBlock.FACING, Direction.EAST);

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
