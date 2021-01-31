package twilightforest.structures.minotaurmaze;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;

import java.util.Random;

public class ComponentTFMazeDeadEndFountainLava extends ComponentTFMazeDeadEndFountain {

	public ComponentTFMazeDeadEndFountainLava(TemplateManager manager, CompoundNBT nbt) {
		super(TFMinotaurMazePieces.TFMMDEFL, nbt);
	}

	public ComponentTFMazeDeadEndFountainLava(TFFeature feature, int i, int x, int y, int z, Direction rotation) {
		super(TFMinotaurMazePieces.TFMMDEFL, feature, i, x, y, z, rotation);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// normal fountain
		super.func_230383_a_(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// remove water
		this.setBlockState(world, AIR, 2, 3, 4, sbb);
		this.setBlockState(world, AIR, 3, 3, 4, sbb);

		// lava instead of water
		this.setBlockState(world, Blocks.LAVA.getDefaultState(), 2, 3, 4, sbb);
		this.setBlockState(world, Blocks.LAVA.getDefaultState(), 3, 3, 4, sbb);

		return true;
	}
}
