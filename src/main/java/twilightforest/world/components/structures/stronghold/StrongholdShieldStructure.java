package twilightforest.world.components.structures.stronghold;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import twilightforest.block.TFBlocks;

import java.util.Random;

public class StrongholdShieldStructure extends StructureTFStrongholdComponent {

	public StrongholdShieldStructure(ServerLevel level, CompoundTag nbt) {
		super(StrongholdPieces.TFSShield, nbt);
		this.spawnListIndex = -1;
	}

	@Override
	public BoundingBox generateBoundingBox(Direction facing, int x, int y, int z) {
		return null;
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random randomIn, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		BlockState down  = TFBlocks.STRONGHOLD_SHIELD.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.DOWN);
		BlockState up    = TFBlocks.STRONGHOLD_SHIELD.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.UP);
		BlockState north = TFBlocks.STRONGHOLD_SHIELD.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.NORTH);
		BlockState south = TFBlocks.STRONGHOLD_SHIELD.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.SOUTH);
		BlockState west  = TFBlocks.STRONGHOLD_SHIELD.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.WEST);
		BlockState east  = TFBlocks.STRONGHOLD_SHIELD.get().defaultBlockState().setValue(DirectionalBlock.FACING, Direction.EAST);

		// +x
		this.generateBox(world, sbb, this.boundingBox.getXSpan(), 0, 0, this.boundingBox.getXSpan(), this.boundingBox.getYSpan(), this.boundingBox.getZSpan(), west, west, false);
		// -x
		this.generateBox(world, sbb, 0, 0, 0, 0, this.boundingBox.getYSpan(), this.boundingBox.getZSpan(), east, east, false);
		// +z
		this.generateBox(world, sbb, 0, 0, this.boundingBox.getZSpan(), this.boundingBox.getXSpan(), this.boundingBox.getYSpan(), this.boundingBox.getZSpan(), north, north, false);
		// -z
		this.generateBox(world, sbb, 0, 0, 0, this.boundingBox.getXSpan(), this.boundingBox.getYSpan(), 0, south, south, false);
		// top
		this.generateBox(world, sbb, 0, 0, 0, this.boundingBox.getXSpan(), 0, this.boundingBox.getZSpan(), up, up, false);
		// bottom
		this.generateBox(world, sbb, 0, this.boundingBox.getYSpan(), 0, this.boundingBox.getXSpan(), this.boundingBox.getYSpan(), this.boundingBox.getZSpan(), down, down, false);

		return true;
	}
}
