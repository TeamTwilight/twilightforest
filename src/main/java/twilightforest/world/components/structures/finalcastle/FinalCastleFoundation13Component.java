package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.RotationUtil;
import twilightforest.world.components.structures.TFStructureComponentOld;

import java.util.function.Predicate;

public class FinalCastleFoundation13Component extends TFStructureComponentOld {

	protected int groundLevel = -1;

	public FinalCastleFoundation13Component(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		this(TFStructurePieceTypes.TFFCToF13.get(), nbt);
	}

	public FinalCastleFoundation13Component(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
	}

	public FinalCastleFoundation13Component(StructurePieceType type, int i, TFStructureComponentOld sideTower, int x, int y, int z) {
		super(type, i, x, y, z);

		this.setOrientation(sideTower.getOrientation());
		this.boundingBox = new BoundingBox(sideTower.getBoundingBox().minX() - 2, sideTower.getBoundingBox().minY() - 1, sideTower.getBoundingBox().minZ() - 2, sideTower.getBoundingBox().minX() + 2, sideTower.getBoundingBox().minY(), sideTower.getBoundingBox().maxZ() + 2);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource randomIn, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// offset bounding box to average ground level
		if (this.groundLevel < 0) {
			this.groundLevel = this.findGroundLevel(world, sbb, 150, isDeadrock); // is 150 a good place to start? :)

			if (this.groundLevel < 0) {
				return;
			}
		}

		// how tall are we
		int height = this.boundingBox.maxY() - this.groundLevel;
		int mid = height / 2;

		// assume square
		int size = this.boundingBox.maxX() - this.boundingBox.minX();

		for (Rotation rotation : RotationUtil.ROTATIONS) {
			// do corner
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 1, -1, 1, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 2, -1, 1, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 2, -mid, 0, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 1, -1, 2, rotation, sbb);
			this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, 0, -mid, 2, rotation, sbb);

			for (int x = 6; x < (size - 3); x += 4) {
				this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, x, -1, 1, rotation, sbb);
				this.replaceAirAndLiquidDownwardsRotated(world, deco.blockState, x, -mid, 0, rotation, sbb);
			}
		}
	}

	protected static final Predicate<BlockState> isDeadrock = state -> state.getBlock() == TFBlocks.DEADROCK.get();
}
