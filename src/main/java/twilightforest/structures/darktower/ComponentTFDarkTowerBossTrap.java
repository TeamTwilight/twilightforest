package twilightforest.structures.darktower;

import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.block.BlockTFTowerDevice;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;

import java.util.List;
import java.util.Random;

import static twilightforest.enums.TowerDeviceVariant.GHASTTRAP_INACTIVE;

public class ComponentTFDarkTowerBossTrap extends ComponentTFDarkTowerWing {

	public ComponentTFDarkTowerBossTrap() {
	}

	protected ComponentTFDarkTowerBossTrap(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, EnumFacing direction) {
		super(feature, i, x, y, z, pSize, pHeight, direction);

		// no spawns
		this.spawnListIndex = -1;
	}

	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}

		// we should have a door where we started
		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);

		// add a beard
		makeABeard(parent, list, rand);

		for (Rotation i : RotationUtil.ROTATIONS) {
			if (i == Rotation.CLOCKWISE_180 || rand.nextBoolean()) {
				continue;
			}
			// occasional balcony
			int[] dest = getValidOpening(rand, i);
			// move opening to tower base
			dest[1] = 1;
			makeTowerBalcony(list, rand, this.getComponentType(), dest[0], dest[1], dest[2], i);
		}
	}

	/**
	 * Attach a roof to this tower.
	 */
	@Override
	public void makeARoof(StructureComponent parent, List<StructureComponent> list, Random rand) {
		//nope;
	}

	@Override
	public boolean addComponentParts(World world, Random rand, StructureBoundingBox sbb) {
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX * 321534781) ^ (this.boundingBox.minZ * 756839));

		// make walls
		makeEncasedWalls(world, rand, sbb, 0, 0, 0, size - 1, height - 1, size - 1);

		// clear inside
		fillWithAir(world, sbb, 1, 1, 1, size - 2, height - 2, size - 2);

		// openings
		makeOpenings(world, sbb);

		// half floors, always starting at y = 4
		addBossTrapFloors(world, decoRNG, sbb, 4, height - 1);

		// demolish some
		destroyTower(world, decoRNG, 5, height + 2, 5, 4, sbb);
		destroyTower(world, decoRNG, 0, height, 0, 3, sbb);
		destroyTower(world, decoRNG, 0, height, 8, 4, sbb);

		// hole for boss trap beam
		destroyTower(world, decoRNG, 5, 6, 5, 2, sbb);

		// redraw some of the floor in case we destroyed it
		this.fillWithBlocks(world, sbb, 1, 0, 1, size / 2, 0, size - 2, deco.blockState, Blocks.AIR.getDefaultState(), false);
		this.fillWithBlocks(world, sbb, 1, 1, 1, size / 2, 1, size - 2, Blocks.AIR.getDefaultState(), Blocks.AIR.getDefaultState(), false);

		// add boss trap
		this.setBlockState(world, TFBlocks.tower_device.getDefaultState().withProperty(BlockTFTowerDevice.VARIANT, GHASTTRAP_INACTIVE), 5, 1, 5, sbb);
		this.setBlockState(world, Blocks.REDSTONE_WIRE.getDefaultState(), 5, 1, 6, sbb);
		this.setBlockState(world, Blocks.REDSTONE_WIRE.getDefaultState(), 5, 1, 7, sbb);
		this.setBlockState(world, Blocks.REDSTONE_WIRE.getDefaultState(), 5, 1, 8, sbb);
		this.setBlockState(world, Blocks.REDSTONE_WIRE.getDefaultState(), 4, 1, 8, sbb);
		this.setBlockState(world, Blocks.REDSTONE_WIRE.getDefaultState(), 3, 1, 8, sbb);
		this.setBlockState(world, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 2, 1, 8, sbb);


		return true;
	}

	/**
	 * Add specific boss trap floors
	 */
	protected void addBossTrapFloors(World world, Random rand, StructureBoundingBox sbb, int bottom, int top) {

		makeFullFloor(world, sbb, Rotation.COUNTERCLOCKWISE_90, 4, 4);

		addStairsDown(world, sbb, Rotation.COUNTERCLOCKWISE_90, 4, size - 2, 4);
		addStairsDown(world, sbb, Rotation.COUNTERCLOCKWISE_90, 4, size - 3, 4);

		// stairs to roof
		addStairsDown(world, sbb, Rotation.CLOCKWISE_90, this.height - 1, size - 2, 4);
		addStairsDown(world, sbb, Rotation.CLOCKWISE_90, this.height - 1, size - 3, 4);
	}


}
