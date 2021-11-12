package twilightforest.world.components.structures.minotaurmaze;

import net.minecraft.server.level.ServerLevel;
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
import twilightforest.block.TFBlocks;

import java.util.Random;

public class MazeCorridorIronFenceComponent extends MazeCorridorComponent {

	public MazeCorridorIronFenceComponent(ServerLevel level, CompoundTag nbt) {
		super(MinotaurMazePieces.TFMMCIF, nbt);
	}

	public MazeCorridorIronFenceComponent(TFFeature feature, int i, int x, int y, int z, Direction rotation) {
		super(MinotaurMazePieces.TFMMCIF, feature, i, x, y, z, rotation);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		this.generateBox(world, sbb, 1, 4, 2, 4, 4, 3, TFBlocks.DECORATIVE_MAZESTONE.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 1, 1, 2, 4, 3, 3, TFBlocks.CUT_MAZESTONE.get().defaultBlockState(), AIR, false);
		this.generateBox(world, sbb, 2, 1, 2, 3, 3, 3, Blocks.IRON_BARS.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);
		return true;
	}
}
