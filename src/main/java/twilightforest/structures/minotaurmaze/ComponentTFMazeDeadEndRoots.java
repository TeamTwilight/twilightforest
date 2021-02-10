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

public class ComponentTFMazeDeadEndRoots extends ComponentTFMazeDeadEnd {

	public ComponentTFMazeDeadEndRoots(TemplateManager manager, CompoundNBT nbt) {
		this(TFMinotaurMazePieces.TFMMDER, nbt);
	}

	public ComponentTFMazeDeadEndRoots(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
	}

	public ComponentTFMazeDeadEndRoots(IStructurePieceType type, TFFeature feature, int i, int x, int y, int z, Direction rotation) {
		super(type, feature, i, x, y, z, rotation);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// no door
		for (int x = 1; x < 5; x++) {
			for (int z = 0; z < 5; z++) {
				// as we go +z, there are more vines
				if (rand.nextInt(z + 2) > 0) {
					int length = rand.nextInt(6);

					//place dirt above ceiling
					this.setBlockState(world, Blocks.DIRT.getDefaultState(), x, 6, z, sbb);

					// roots
					for (int y = 6 - length; y < 6; y++) {
						this.setBlockState(world, TFBlocks.root_strand.get().getDefaultState(), x, y, z, sbb);
					}

					// occasional gravel
					if (rand.nextInt(z + 1) > 1) {
						this.setBlockState(world, Blocks.GRAVEL.getDefaultState(), x, 1, z, sbb);

						if (rand.nextInt(z + 1) > 1) {
							this.setBlockState(world, Blocks.GRAVEL.getDefaultState(), x, 2, z, sbb);
						}
					}
				}
			}
		}

		return true;
	}
}

