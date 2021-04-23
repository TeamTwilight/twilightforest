package twilightforest.structures.finalcastle;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleDamagedTower extends ComponentTFFinalCastleMazeTower13 {

	public ComponentTFFinalCastleDamagedTower(TemplateManager manager, CompoundNBT nbt) {
		this(TFFinalCastlePieces.TFFCDamT, nbt);
	}

	public ComponentTFFinalCastleDamagedTower(IStructurePieceType piece, CompoundNBT nbt) {
		super(piece, nbt);
	}

	public ComponentTFFinalCastleDamagedTower(IStructurePieceType piece, TFFeature feature, Random rand, int i, int x, int y, int z, Direction direction) {
		super(piece, feature, rand, i, x, y, z, TFBlocks.castle_rune_brick_yellow.get().getDefaultState(), direction);  //TODO: change rune color
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}

		// add foundation
		ComponentTFFinalCastleFoundation13 foundation = new ComponentTFFinalCastleFoundation13(TFFinalCastlePieces.TFFCToF13, getFeatureType(), rand, 0, this);
		list.add(foundation);
		foundation.buildComponent(this, list, rand);

		// add thorns
		ComponentTFFinalCastleFoundation13 thorns = new ComponentTFFinalCastleFoundation13Thorns(getFeatureType(), rand, 0, this);
		list.add(thorns);
		thorns.buildComponent(this, list, rand);

//    		// add roof
//    		StructureTFComponentOld roof = rand.nextBoolean() ? new Roof13Conical(rand, 4, this) :  new Roof13Crenellated(rand, 4, this);
//    		list.add(roof);
//    		roof.buildComponent(this, list, rand);


		// keep on building?
		this.buildNonCriticalTowers(parent, list, rand);
	}

	@Override
	protected ComponentTFFinalCastleMazeTower13 makeNewDamagedTower(Random rand, Direction facing, BlockPos tc) {
		return new ComponentTFFinalCastleWreckedTower(getFeatureType(), rand, this.getComponentType() + 1, tc.getX(), tc.getY(), tc.getZ(), facing);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		super.func_230383_a_(world, manager, generator, rand, sbb, chunkPosIn, blockPos);
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		this.destroyTower(world, decoRNG, sbb);

		return true;
	}

	public void destroyTower(ISeedReader world, Random rand, MutableBoundingBox sbb) {

		// make list of destroyed areas
		ArrayList<DestroyArea> areas = makeInitialDestroyList(rand);

		boolean hitDeadRock = false;

		// go down from the top of the tower to the ground, taking out rectangular chunks
		//for (int y = this.boundingBox.maxY; y > this.boundingBox.minY; y--) {
		BlockPos.Mutable pos = new BlockPos.Mutable();
		for (int y = this.boundingBox.maxY; !hitDeadRock && y > 64; y--) {
			for (int x = this.boundingBox.minX - 2; x <= this.boundingBox.maxX + 2; x++) {
				for (int z = this.boundingBox.minZ - 2; z <= this.boundingBox.maxZ + 2; z++) {
					pos.setPos(x, y, z);
					if (sbb.isVecInside(pos)) {
						if (world.getBlockState(pos).getBlock() == TFBlocks.deadrock.get()) {
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

		areas.add(DestroyArea.createNonIntersecting(this.getBoundingBox(), rand, this.getBoundingBox().maxY - 1, areas));
		areas.add(DestroyArea.createNonIntersecting(this.getBoundingBox(), rand, this.getBoundingBox().maxY - 1, areas));
		areas.add(DestroyArea.createNonIntersecting(this.getBoundingBox(), rand, this.getBoundingBox().maxY - 1, areas));
		return areas;
	}

	protected void determineBlockDestroyed(ISeedReader world, ArrayList<DestroyArea> areas, int y, int x, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		for (DestroyArea dArea : areas) {
			if (dArea != null && dArea.isVecInside(pos)) {
				world.removeBlock(pos, false);
			}
		}
	}
}
