package twilightforest.world.components.structures.finalcastle;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;

import java.util.ArrayList;
import java.util.Random;

public class FinalCastleDamagedTowerComponent extends FinalCastleMazeTower13Component {

	public FinalCastleDamagedTowerComponent(ServerLevel level, CompoundTag nbt) {
		this(FinalCastlePieces.TFFCDamT, nbt);
	}

	public FinalCastleDamagedTowerComponent(StructurePieceType piece, CompoundTag nbt) {
		super(piece, nbt);
	}

	public FinalCastleDamagedTowerComponent(StructurePieceType piece, TFFeature feature, Random rand, int i, int x, int y, int z, Direction direction) {
		super(piece, feature, rand, i, x, y, z, TFBlocks.YELLOW_CASTLE_RUNE_BRICK.get().defaultBlockState(), direction);  //TODO: change rune color
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

		// add foundation
		FinalCastleFoundation13Component foundation = new FinalCastleFoundation13Component(FinalCastlePieces.TFFCToF13, getFeatureType(), rand, 0, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(foundation);
		foundation.addChildren(this, list, rand);

		// add thorns
		FinalCastleFoundation13Component thorns = new FinalCastleFoundation13ComponentThorns(getFeatureType(), rand, 0, this, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(thorns);
		thorns.addChildren(this, list, rand);

//    		// add roof
//    		StructureTFComponentOld roof = rand.nextBoolean() ? new Roof13Conical(rand, 4, this) :  new Roof13Crenellated(rand, 4, this);
//    		list.add(roof);
//    		roof.buildComponent(this, list, rand);


		// keep on building?
		this.buildNonCriticalTowers(parent, list, rand);
	}

	@Override
	protected FinalCastleMazeTower13Component makeNewDamagedTower(Random rand, Direction facing, BlockPos tc) {
		return new FinalCastleWreckedTowerComponent(getFeatureType(), rand, this.getGenDepth() + 1, tc.getX(), tc.getY(), tc.getZ(), facing);
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		super.postProcess(world, manager, generator, rand, sbb, chunkPosIn, blockPos);
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		this.destroyTower(world, decoRNG, sbb);

		return true;
	}

	public void destroyTower(WorldGenLevel world, Random rand, BoundingBox sbb) {

		// make list of destroyed areas
		ArrayList<DestroyArea> areas = makeInitialDestroyList(rand);

		boolean hitDeadRock = false;

		// go down from the top of the tower to the ground, taking out rectangular chunks
		//for (int y = this.boundingBox.maxY; y > this.boundingBox.minY; y--) {
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for (int y = this.boundingBox.maxY(); !hitDeadRock && y > 64; y--) {
			for (int x = this.boundingBox.minX() - 2; x <= this.boundingBox.maxX() + 2; x++) {
				for (int z = this.boundingBox.minZ() - 2; z <= this.boundingBox.maxZ() + 2; z++) {
					pos.set(x, y, z);
					if (sbb.isInside(pos)) {
						if (world.getBlockState(pos).getBlock() == TFBlocks.DEADROCK.get()) {
							hitDeadRock = true;
						}
						determineBlockDestroyed(world, areas, y, x, z);
					}
				}
			}

			// check to see if any of our DestroyAreas are entirely above the current y value
			DestroyArea removeArea = null;

			for (DestroyArea dArea : areas) {
				if (dArea == null || dArea.isEntirelyAbove(y)) {
					removeArea = dArea;
				}
			}
			// if so, replace them with new ones
			if (removeArea != null) {
				areas.remove(removeArea);
				areas.add(DestroyArea.createNonIntersecting(this.getBoundingBox(), rand, y, areas));

			}
		}
	}

	protected ArrayList<DestroyArea> makeInitialDestroyList(Random rand) {
		ArrayList<DestroyArea> areas = new ArrayList<DestroyArea>(2);

		areas.add(DestroyArea.createNonIntersecting(this.getBoundingBox(), rand, this.getBoundingBox().maxY() - 1, areas));
		areas.add(DestroyArea.createNonIntersecting(this.getBoundingBox(), rand, this.getBoundingBox().maxY() - 1, areas));
		areas.add(DestroyArea.createNonIntersecting(this.getBoundingBox(), rand, this.getBoundingBox().maxY() - 1, areas));
		return areas;
	}

	protected void determineBlockDestroyed(WorldGenLevel world, ArrayList<DestroyArea> areas, int y, int x, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		for (DestroyArea dArea : areas) {
			if (dArea != null && dArea.isVecInside(pos)) {
				world.removeBlock(pos, false);
			}
		}
	}
}
