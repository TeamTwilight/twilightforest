package twilightforest.world.components.structures.lichtower;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.init.TFStructurePieceTypes;


public class TowerOutbuildingComponent extends TowerWingComponent {

	public TowerOutbuildingComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFLTOut.get(), nbt);
	}

	protected TowerOutbuildingComponent(int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		super(TFStructurePieceTypes.TFLTOut.get(), i, x, y, z, pSize, pHeight, direction);
	}

	/**
	 * NO BEARDS!
	 */
	@Override
	public void makeABeard(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
	}

	/**
	 * Outbuildings should not make new wings close to the ground.
	 */
	@Override
	public boolean makeTowerWing(StructurePieceAccessor list, RandomSource rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation direction) {
		if (y > 7) {
			return super.makeTowerWing(list, rand, index, x, y, z, wingSize, wingHeight, direction);
		} else {
			return false;
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		final BlockState cobblestone = Blocks.COBBLESTONE.defaultBlockState();
		for (int x = 0; x < this.size; x++) {
			for (int z = 0; z < this.size; z++) {
				this.fillColumnDown(world, cobblestone, x, -1, z, sbb);
			}
		}
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);
	}
}
