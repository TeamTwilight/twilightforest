package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import twilightforest.world.registration.TFFeature;

import java.util.Random;

public class MazeDeadEndTripwireChestComponent extends MazeDeadEndChestComponent {

	public MazeDeadEndTripwireChestComponent(ServerLevel level, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMDETC, nbt);
	}

	public MazeDeadEndTripwireChestComponent(TFFeature feature, int i, int x, int y, int z, Direction rotation) {
		super(MinotaurMazePieces.TFMMDETC, feature, i, x, y, z, rotation);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// normal chest room
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// add tripwire
		this.placeTripwire(world, 1, 1, 2, 3, Direction.EAST, sbb);

		// TNT! Since the trap requires tripwire, we'll make punching the tnt NOT the solution
		BlockState tnt = Blocks.TNT.defaultBlockState().setValue(TntBlock.UNSTABLE, true);
		this.placeBlock(world, tnt, 0,  0, 2, sbb);

		// Air blocks are required underneath to maximize TNT destruction of chest
		this.placeBlock(world, AIR, 0, -1, 2, sbb);
		this.placeBlock(world, AIR, 1, -1, 2, sbb);

		this.placeBlock(world, tnt, 2,  0, 4, sbb);
		this.placeBlock(world, tnt, 3,  0, 4, sbb);
		this.placeBlock(world, tnt, 2,  0, 3, sbb);
		this.placeBlock(world, tnt, 3,  0, 3, sbb);

		return true;
	}
}
