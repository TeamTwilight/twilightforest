package twilightforest.world.components.structures.finalcastle;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;

import java.util.ArrayList;
import java.util.Random;

public class FinalCastleWreckedTowerComponent extends FinalCastleDamagedTowerComponent {

	public FinalCastleWreckedTowerComponent(ServerLevel level, CompoundTag nbt) {
		super(FinalCastlePieces.TFFCWrT, nbt);
	}

	public FinalCastleWreckedTowerComponent(TFFeature feature, Random rand, int i, int x, int y, int z, Direction direction) {
		super(FinalCastlePieces.TFFCWrT, feature, rand, i, x, y, z, direction);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

//    		// add foundation
//    		Foundation13 foundation = new Foundation13(rand, 4, this);
//    		list.add(foundation);
//    		foundation.buildComponent(this, list, rand);

		// add thorns
		FinalCastleFoundation13Component thorns = new FinalCastleFoundation13ComponentThorns(getFeatureType(), rand, 0, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(thorns);
		thorns.addChildren(this, list, rand);

//    		// add roof
//    		StructureTFComponentOld roof = rand.nextBoolean() ? new Roof13Conical(rand, 4, this) :  new Roof13Crenellated(rand, 4, this);
//    		list.add(roof);
//    		roof.buildComponent(this, list, rand);

		// keep on building?
//    		this.buildNonCriticalTowers(parent, list, rand);
	}

	@Override
	public BlockState getGlyphColour() {
		return TFBlocks.BLUE_CASTLE_RUNE_BRICK.get().defaultBlockState();
	}

	@Override
	protected void determineBlockDestroyed(WorldGenLevel world, ArrayList<DestroyArea> areas, int y, int x, int z) {
		boolean isInside = false;

		BlockPos pos = new BlockPos(x, y, z);

		for (DestroyArea dArea : areas) {
			if (dArea != null && dArea.isVecInside(pos)) {
				isInside = true;
			}
		}

		if (!isInside) {
			world.removeBlock(pos, false);
		}
	}

	@Override
	protected ArrayList<DestroyArea> makeInitialDestroyList(Random rand) {
		ArrayList<DestroyArea> areas = new ArrayList<DestroyArea>(2);

		areas.add(DestroyArea.createNonIntersecting(this.getBoundingBox(), rand, this.getBoundingBox().maxY() - 1, areas));
		areas.add(DestroyArea.createNonIntersecting(this.getBoundingBox(), rand, this.getBoundingBox().maxY() - 1, areas));
		areas.add(DestroyArea.createNonIntersecting(this.getBoundingBox(), rand, this.getBoundingBox().maxY() - 1, areas));
		areas.add(DestroyArea.createNonIntersecting(this.getBoundingBox(), rand, this.getBoundingBox().maxY() - 1, areas));
		return areas;
	}
}
