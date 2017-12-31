package twilightforest.structures.lichtower;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;

import java.util.List;
import java.util.Random;


public class ComponentTFTowerBridge extends ComponentTFTowerWing {


	int dSize;
	int dHeight;

	public ComponentTFTowerBridge() {
		super();
	}


	protected ComponentTFTowerBridge(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, EnumFacing direction) {
		super(feature, i, x, y, z, 3, 3, direction);

		this.dSize = pSize;
		this.dHeight = pHeight;
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		int[] dest = new int[]{2, 1, 1};//getValidOpening(rand, 0);
		makeTowerWing(list, rand, 1, dest[0], dest[1], dest[2], dSize, dHeight, Rotation.NONE);
	}

	/**
	 * Gets the bounding box of the tower wing we would like to make.
	 *
	 * @return
	 */
	public StructureBoundingBox getWingBB() {
		int[] dest = offsetTowerCoords(2, 1, 1, dSize, this.getCoordBaseMode());
		return StructureTFComponentOld.getComponentToAddBoundingBox(dest[0], dest[1], dest[2], 0, 0, 0, dSize - 1, dHeight - 1, dSize - 1, this.getCoordBaseMode());
	}


	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {

		// make walls
		for (int x = 0; x < 3; x++) {
			setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), x, 2, 0, sbb);
			setBlockState(world, Blocks.OAK_FENCE.getDefaultState(), x, 2, 2, sbb);
			setBlockState(world, Blocks.STONEBRICK.getDefaultState(), x, 1, 0, sbb);
			setBlockState(world, Blocks.STONEBRICK.getDefaultState(), x, 1, 2, sbb);
			setBlockState(world, Blocks.STONEBRICK.getDefaultState(), x, 0, 0, sbb);
			setBlockState(world, Blocks.STONEBRICK.getDefaultState(), x, 0, 1, sbb);
			setBlockState(world, Blocks.STONEBRICK.getDefaultState(), x, 0, 2, sbb);
			setBlockState(world, Blocks.STONEBRICK.getDefaultState(), x, -1, 1, sbb);
		}

		// try two blocks outside the boundries
		setBlockState(world, Blocks.STONEBRICK.getDefaultState(), -1, -1, 1, sbb);
		setBlockState(world, Blocks.STONEBRICK.getDefaultState(), 3, -1, 1, sbb);

		// clear bridge walkway
		this.fillWithAir(world, sbb, 0, 1, 1, 2, 2, 1);


		// marker blocks
//        setBlockState(world, Blocks.WOOL, this.coordBaseMode, size / 2, 2, size / 2, sbb);
//        setBlockState(world, Blocks.GOLD_BLOCK, 0, 0, 0, 0, sbb);

		// door opening?
//        makeDoorOpening(world, sbb);


		return true;
	}
}
