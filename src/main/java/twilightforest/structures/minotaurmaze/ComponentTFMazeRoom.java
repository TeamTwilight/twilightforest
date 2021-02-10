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
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;

import java.util.List;
import java.util.Random;

public class ComponentTFMazeRoom extends StructureTFComponentOld {

	public ComponentTFMazeRoom(TemplateManager manager, CompoundNBT nbt) {
		this(TFMinotaurMazePieces.TFMMR, nbt);
	}

	public ComponentTFMazeRoom(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
	}

	public ComponentTFMazeRoom(IStructurePieceType type, TFFeature feature, int i, Random rand, int x, int y, int z) {
		super(type, feature, i);
		this.setCoordBaseMode(Direction.Plane.HORIZONTAL.random(rand));

		this.boundingBox = new MutableBoundingBox(x, y, z, x + 15, y + 4, z + 15);
	}

	/**
	 * Initiates construction of the Structure Component picked, at the current Location of StructGen
	 */
	@Override
	public void buildComponent(StructurePiece structurecomponent, List<StructurePiece> list, Random random) {
		// NO-OP
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// floor border
		fillWithBlocks(world, sbb, 1, 0, 1, 14, 0, 14, TFBlocks.maze_stone_border.get().getDefaultState(), AIR, true);
		fillWithBlocks(world, sbb, 2, 0, 2, 13, 0, 13, TFBlocks.maze_stone_mosaic.get().getDefaultState(), AIR, true);

		// doorways
		if (this.getBlockStateFromPos(world, 7, 1, 0, sbb).getBlock() == Blocks.AIR) {
			fillWithBlocks(world, sbb, 6, 1, 0, 9, 4, 0, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
			fillWithAir(world, sbb, 7, 1, 0, 8, 3, 0);
		}

		if (this.getBlockStateFromPos(world, 7, 1, 15, sbb).getBlock() == Blocks.AIR) {
			fillWithBlocks(world, sbb, 6, 1, 15, 9, 4, 15, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
			fillWithAir(world, sbb, 7, 1, 15, 8, 3, 15);
		}

		if (this.getBlockStateFromPos(world, 0, 1, 7, sbb).getBlock() == Blocks.AIR) {
			fillWithBlocks(world, sbb, 0, 1, 6, 0, 4, 9, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
			fillWithAir(world, sbb, 0, 1, 7, 0, 3, 8);
		}

		if (this.getBlockStateFromPos(world, 15, 1, 7, sbb).getBlock() == Blocks.AIR) {
			fillWithBlocks(world, sbb, 15, 1, 6, 15, 4, 9, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
			fillWithAir(world, sbb, 15, 1, 7, 15, 3, 8);
		}
		return true;
	}
}
