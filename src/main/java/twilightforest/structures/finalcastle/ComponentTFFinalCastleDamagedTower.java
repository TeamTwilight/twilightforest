package twilightforest.structures.finalcastle;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFCastleMagic;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleDamagedTower extends ComponentTFFinalCastleMazeTower13 {
	public ComponentTFFinalCastleDamagedTower() {
	}

	public ComponentTFFinalCastleDamagedTower(TFFeature feature, Random rand, int i, int x, int y, int z, EnumFacing direction) {
		super(feature, rand, i, x, y, z, BlockTFCastleMagic.VALID_COLORS.get(2), direction);  //TODO: change rune color
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}

		// add foundation
		ComponentTFFinalCastleFoundation13 foundation = new ComponentTFFinalCastleFoundation13(getFeatureType(), rand, 0, this);
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
	protected ComponentTFFinalCastleMazeTower13 makeNewDamagedTower(Random rand, EnumFacing facing, BlockPos tc) {
		return new ComponentTFFinalCastleWreckedTower(getFeatureType(), rand, this.getComponentType() + 1, tc.getX(), tc.getY(), tc.getZ(), facing);
	}


	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		super.addComponentParts(world, rand, sbb);
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		this.destroyTower(world, decoRNG, sbb);

		return true;
	}

	public void destroyTower(World world, Random rand, StructureBoundingBox sbb) {

		// make list of destroyed areas
		ArrayList<DestroyArea> areas = makeInitialDestroyList(rand);

		boolean hitDeadRock = false;

		// go down from the top of the tower to the ground, taking out rectangular chunks
		//for (int y = this.boundingBox.maxY; y > this.boundingBox.minY; y--) {
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		for (int y = this.boundingBox.maxY; !hitDeadRock && y > 64; y--) {
			for (int x = this.boundingBox.minX - 2; x <= this.boundingBox.maxX + 2; x++) {
				for (int z = this.boundingBox.minZ - 2; z <= this.boundingBox.maxZ + 2; z++) {
					pos.setPos(x, y, z);
					if (sbb.isVecInside(pos)) {
						if (world.getBlockState(pos).getBlock() == TFBlocks.deadrock) {
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

	protected void determineBlockDestroyed(World world, ArrayList<DestroyArea> areas, int y, int x, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		for (DestroyArea dArea : areas) {
			if (dArea != null && dArea.isVecInside(pos)) {
				world.setBlockToAir(pos);
			}
		}
	}

}
