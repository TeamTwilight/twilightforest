package twilightforest.structures.minotaurmaze;

import net.minecraft.block.BlockState;
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

public class ComponentTFMazeDeadEndTripwireChest extends ComponentTFMazeDeadEndChest {

	public ComponentTFMazeDeadEndTripwireChest(TemplateManager manager, CompoundNBT nbt) {
		super(TFMinotaurMazePieces.TFMMDETC, nbt);
	}

	public ComponentTFMazeDeadEndTripwireChest(TFFeature feature, int i, int x, int y, int z, Direction rotation) {
		super(TFMinotaurMazePieces.TFMMDETC, feature, i, x, y, z, rotation);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// normal chest room
		super.func_230383_a_(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// add tripwire
		this.placeTripwire(world, 1, 1, 2, 3, Direction.EAST, sbb);

		// TNT!
		BlockState tnt = Blocks.TNT.getDefaultState();
		this.setBlockState(world, tnt, 0,  0, 2, sbb);

		// Air blocks are required underneath to maximize TNT destruction of chest
		this.setBlockState(world, AIR, 0, -1, 2, sbb);
		this.setBlockState(world, AIR, 1, -1, 2, sbb);

		this.setBlockState(world, tnt, 2,  0, 4, sbb);
		this.setBlockState(world, tnt, 3,  0, 4, sbb);
		this.setBlockState(world, tnt, 2,  0, 3, sbb);
		this.setBlockState(world, tnt, 3,  0, 3, sbb);

		return true;
	}
}
