package twilightforest.structures.minotaurmaze;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class ComponentTFMazeDeadEndFountain extends ComponentTFMazeDeadEnd {

	public ComponentTFMazeDeadEndFountain(TemplateManager manager, CompoundNBT nbt) {
		super(TFMinotaurMazePieces.TFMMDEF, nbt);
	}

	public ComponentTFMazeDeadEndFountain(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
	}

	public ComponentTFMazeDeadEndFountain(IStructurePieceType type, TFFeature feature, int i, int x, int y, int z, Direction rotation) {
		super(type, feature, i, x, y, z, rotation);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// normal doorway
		super.func_230383_a_(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// back wall brick
		this.fillWithBlocks(world, sbb, 1, 1, 4, 4, 4, 4, TFBlocks.maze_stone_brick.get().getDefaultState(), AIR, false);

		// water
		this.setBlockState(world, Blocks.WATER.getDefaultState(), 2, 3, 4, sbb);
		this.setBlockState(world, Blocks.WATER.getDefaultState(), 3, 3, 4, sbb);

		// receptacle
		this.setBlockState(world, AIR, 2, 0, 3, sbb);
		this.setBlockState(world, AIR, 3, 0, 3, sbb);

		return true;
	}
}
