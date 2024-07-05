package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import twilightforest.init.TFBlocks;
import twilightforest.init.TFStructurePieceTypes;
import twilightforest.util.RotationUtil;
import twilightforest.world.components.structures.TFStructureComponentOld;


public class FinalCastleBellTower21Component extends FinalCastleMazeTower13Component {

	private static final int FLOORS = 8;

	public FinalCastleBellTower21Component(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(TFStructurePieceTypes.TFFCBelTo.get(), nbt);
	}

	@SuppressWarnings("this-escape")
	public FinalCastleBellTower21Component(int i, int x, int y, int z, Direction direction) {
		super(TFStructurePieceTypes.TFFCBelTo.get(), i, x, y, z, FLOORS, 1, TFBlocks.BLUE_CASTLE_RUNE_BRICK.get().defaultBlockState(), direction);
		this.size = 21;
		this.height = FLOORS * 8 + 1;
		this.boundingBox = TFStructureComponentOld.getComponentToAddBoundingBox2(x, y, z, -6, -8, -this.size / 2, this.size - 1, this.height, this.size - 1, direction);
		this.openings.clear();
		addOpening(0, 9, size / 2, Rotation.CLOCKWISE_180);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, RandomSource rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

		// add foundation
		FinalCastleBellFoundation21Component foundation = new FinalCastleBellFoundation21Component(4, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(foundation);
		foundation.addChildren(this, list, rand);

		// add roof
		TFStructureComponentOld roof = new FinalCastleRoof13CrenellatedComponent(4, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(roof);
		roof.addChildren(this, list, rand);
	}

	@Override
	public void postProcess(WorldGenLevel world, StructureManager manager, ChunkGenerator generator, RandomSource rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);

		// openings!
		BlockState fieldBlock = TFBlocks.BLUE_FORCE_FIELD.get().defaultBlockState();
		for (Rotation rotation : RotationUtil.ROTATIONS) {
			int y = 48;
			for (int x = 5; x < this.size - 4; x += 2) {
				this.fillBlocksRotated(world, sbb, x, y, 0, x, y + 14, 0, fieldBlock, rotation);
			}
			y = 24;
			for (int x = 1; x < this.size - 1; x += 8) {
				this.fillBlocksRotated(world, sbb, x, y, 0, x, y + 14, 0, fieldBlock, rotation);
//	        		fieldMeta = rand.nextInt(5);
				this.fillBlocksRotated(world, sbb, x + 2, y, 0, x + 2, y + 14, 0, fieldBlock, rotation);
			}
		}

		// sign
		this.placeSignAtCurrentPosition(world, 7, 9, 8, "Parkour area 2", "mini-boss 1", sbb);
	}
}
