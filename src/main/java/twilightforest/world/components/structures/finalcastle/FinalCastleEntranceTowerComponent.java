package twilightforest.world.components.structures.finalcastle;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.util.BoundingBoxUtils;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.registration.TFFeature;

import java.util.Random;

public class FinalCastleEntranceTowerComponent extends FinalCastleMazeTower13Component {

	public FinalCastleEntranceTowerComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(FinalCastlePieces.TFFCEnTo, nbt);
	}

	public FinalCastleEntranceTowerComponent(TFFeature feature, Random rand, int i, int x, int y, int z, Direction direction) {
		super(FinalCastlePieces.TFFCEnTo, feature, rand, i, x, y, z, 3, 2, TFBlocks.PINK_CASTLE_RUNE_BRICK.get().defaultBlockState(), direction);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

		// add foundation
		FinalCastleFoundation13Component foundation = new FinalCastleFoundation13Component(FinalCastlePieces.TFFCToF13, getFeatureType(), rand, 4, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(foundation);
		foundation.addChildren(this, list, rand);

		// add roof
		TFStructureComponentOld roof = new FinalCastleRoof13PeakedComponent(getFeatureType(), rand, 4, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(roof);
		roof.addChildren(this, list, rand);

		// how many floors until the bottom?
		int missingFloors = (this.boundingBox.minY() - 127) / 8;

		// place half on the bottom
		int bottomFloors = missingFloors / 2;
		// how many are left for the middle?
		int middleFloors = missingFloors - bottomFloors;

		// what direction can we put the side tower in, if any?
		Direction facing = Rotation.CLOCKWISE_90.rotate(this.getOrientation());
		int howFar = 20;
		if (!this.buildSideTower(list, rand, middleFloors + 1, facing, howFar)) {
			facing = Rotation.COUNTERCLOCKWISE_90.rotate(this.getOrientation());
			if (!this.buildSideTower(list, rand, middleFloors + 1, facing, howFar)) {
				facing = Rotation.NONE.rotate(this.getOrientation());
				if (!this.buildSideTower(list, rand, middleFloors + 1, facing, howFar)) {
					// side tower no worky
				}
			}
		}

		// add bottom tower
		FinalCastleEntranceBottomTowerComponent eTower = new FinalCastleEntranceBottomTowerComponent(getFeatureType(), rand, this.getGenDepth() + 1, this.boundingBox.minX() + 6, this.boundingBox.minY() - (middleFloors) * 8, this.boundingBox.minZ() + 6, bottomFloors + 1, bottomFloors, facing.getOpposite());
		list.addPiece(eTower);
		eTower.addChildren(this, list, rand);

		// add bridge to bottom
		BlockPos opening = this.getValidOpeningCC(rand, facing);
		opening = opening.below(middleFloors * 8);

		BlockPos bc = this.offsetTowerCCoords(opening.getX(), opening.getY(), opening.getZ(), 1, facing);
		FinalCastleBridgeComponent bridge = new FinalCastleBridgeComponent(getFeatureType(), this.getGenDepth() + 1, bc.getX(), bc.getY(), bc.getZ(), howFar - 7, facing);
		list.addPiece(bridge);
		bridge.addChildren(this, list, rand);
	}

	private boolean buildSideTower(StructurePieceAccessor list, Random rand, int middleFloors, Direction facing, int howFar) {
		BlockPos opening = this.getValidOpeningCC(rand, facing);

		// build towards
		BlockPos tc = this.offsetTowerCCoords(opening.getX(), opening.getY(), opening.getZ(), howFar, facing);

		FinalCastleEntranceSideTowerComponent eTower = new FinalCastleEntranceSideTowerComponent(getFeatureType(), rand, this.getGenDepth() + 1, tc.getX(), tc.getY(), tc.getZ(), middleFloors, middleFloors - 1, facing);

		BoundingBox largerBB = BoundingBoxUtils.cloneWithAdjustments(eTower.getBoundingBox(), -6, 0, -6, 6, 0, 6);

		StructurePiece intersect = list.findCollisionPiece(largerBB);

		if (intersect == null) {
			list.addPiece(eTower);
			eTower.addChildren(this, list, rand);
			// add bridge
			BlockPos bc = this.offsetTowerCCoords(opening.getX(), opening.getY(), opening.getZ(), 1, facing);
			FinalCastleBridgeComponent bridge = new FinalCastleBridgeComponent(getFeatureType(), this.getGenDepth() + 1, bc.getX(), bc.getY(), bc.getZ(), howFar - 7, facing);
			list.addPiece(bridge);
			bridge.addChildren(this, list, rand);

			// opening
			addOpening(opening.getX(), opening.getY() + 1, opening.getZ(), facing);

			return true;
		} else {
			TwilightForestMod.LOGGER.info("side entrance tower blocked");
			return false;
		}

	}

	/**
	 * Gets a random position in the specified direction that connects to a floor currently in the tower.
	 */
	@Override
	public BlockPos getValidOpeningCC(Random rand, Direction facing) {
		BlockPos opening = super.getValidOpeningCC(rand, facing);
		return new BlockPos(opening.getX(), 0, opening.getZ());
	}


}
