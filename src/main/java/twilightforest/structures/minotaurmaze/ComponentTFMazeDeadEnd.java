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
import twilightforest.structures.StructureTFComponentOld;

import java.util.Random;

public class ComponentTFMazeDeadEnd extends StructureTFComponentOld {

	public ComponentTFMazeDeadEnd(TemplateManager manager, CompoundNBT nbt) {
		this(TFMinotaurMazePieces.TFMMDE, nbt);
	}

	public ComponentTFMazeDeadEnd(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
	}

	public ComponentTFMazeDeadEnd(IStructurePieceType type, TFFeature feature, int i, int x, int y, int z, Direction rotation) {
		super(type, feature, i);
		this.setCoordBaseMode(rotation);
		this.boundingBox = new MutableBoundingBox(x, y, z, x + 5, y + 5, z + 5);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		this.fillWithBlocks(world, sbb, 1, 1, 0, 4, 4, 0, Blocks.OAK_FENCE.getDefaultState(), AIR, false);
		this.fillWithAir(world, sbb, 2, 1, 0, 3, 3, 0);
		return true;
	}
}
