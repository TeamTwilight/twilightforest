package twilightforest.world.components.structures.finalcastle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.util.RotationUtil;

import java.util.Random;

public class FinalCastleBellTower21Component extends FinalCastleMazeTower13Component {

	private static final int FLOORS = 8;

	public FinalCastleBellTower21Component(ServerLevel level, CompoundTag nbt) {
		super(FinalCastlePieces.TFFCBelTo, nbt);
	}

	public FinalCastleBellTower21Component(TFFeature feature, Random rand, int i, int x, int y, int z, Direction direction) {
		super(FinalCastlePieces.TFFCBelTo, feature, rand, i, x, y, z, FLOORS, 1, TFBlocks.BLUE_CASTLE_RUNE_BRICK.get().defaultBlockState(), direction);
		this.size = 21;
		int floors = FLOORS;
		this.height = floors * 8 + 1;
		this.boundingBox = TFStructureComponentOld.getComponentToAddBoundingBox2(x, y, z, -6, -8, -this.size / 2, this.size - 1, this.height, this.size - 1, direction);
		this.openings.clear();
		addOpening(0, 9, size / 2, Rotation.CLOCKWISE_180);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

		// add foundation
		FinalCastleBellFoundation21Component foundation = new FinalCastleBellFoundation21Component(getFeatureType(), rand, 4, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(foundation);
		foundation.addChildren(this, list, rand);

		// add roof
		TFStructureComponentOld roof = new FinalCastleRoof13CrenellatedComponent(getFeatureType(), rand, 4, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(roof);
		roof.addChildren(this, list, rand);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
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

		return true;
	}
}
