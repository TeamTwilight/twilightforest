package twilightforest.structures.icetower;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponentOld;

import java.util.List;
import java.util.Random;

public class ComponentTFIceTowerEntrance extends ComponentTFIceTowerWing {

	public ComponentTFIceTowerEntrance() {
	}

	public ComponentTFIceTowerEntrance(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, EnumFacing direction) {
		super(feature, i, x, y, z, pSize, pHeight, direction);
	}


	@Override
	protected boolean shouldHaveBase(Random rand) {
		return true;
	}


	@Override
	public void buildComponent(StructureComponent parent, List<StructureComponent> list, Random rand) {
		if (parent != null && parent instanceof StructureTFComponentOld) {
			this.deco = ((StructureTFComponentOld) parent).deco;
		}

		// we should have a door where we started
		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);

		// stairs
		addStairs(list, rand, this.getComponentType() + 1, this.size - 1, 1, size / 2, Rotation.NONE);
		addStairs(list, rand, this.getComponentType() + 1, this.size / 2, 1, 0, Rotation.COUNTERCLOCKWISE_90);
		addStairs(list, rand, this.getComponentType() + 1, this.size / 2, 1, this.size - 1, Rotation.CLOCKWISE_90);

		// should we build a base
		this.hasBase = this.shouldHaveBase(rand);

		// add a roof?
		makeARoof(parent, list, rand);
	}

	/**
	 * Add some stairs leading to this tower
	 */
	private boolean addStairs(List<StructureComponent> list, Random rand, int index, int x, int y, int z, Rotation rotation) {
		// add door
		this.addOpening(x, y, z, rotation);

		EnumFacing direction = getStructureRelativeRotation(rotation);
		BlockPos dx = offsetTowerCCoords(x, y, z, this.size, direction);

		ComponentTFIceTowerStairs entrance = new ComponentTFIceTowerStairs(getFeatureType(), index, dx.getX(), dx.getY(), dx.getZ(), this.size, this.height, direction);

		list.add(entrance);
		entrance.buildComponent(list.get(0), list, rand);
		return true;
	}

	/**
	 * Make a new wing
	 */
	@Override
	public boolean makeTowerWing(List<StructureComponent> list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		return false;
	}

	/**
	 * No floors
	 */
	@Override
	protected void makeFloorsForTower(World world, Random rand, StructureBoundingBox sbb) {

		decoratePillarsCornersHigh(world, rand, 0, 11, Rotation.NONE, sbb);

	}


	protected void decoratePillarsCornersHigh(World world, Random rand, int bottom, int top, Rotation rotation, StructureBoundingBox sbb) {
		final IBlockState pillarXAxis = deco.pillarState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.X);
		final IBlockState pillarZAxis = deco.pillarState.withProperty(BlockRotatedPillar.AXIS, EnumFacing.Axis.Z);
		this.fillBlocksRotated(world, sbb, 3, bottom + 5, 1, 3, bottom + 5, 9, pillarZAxis, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 5, 1, 7, bottom + 5, 9, pillarZAxis, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 5, 3, 9, bottom + 5, 3, pillarXAxis, rotation);
		this.fillBlocksRotated(world, sbb, 1, bottom + 5, 7, 9, bottom + 5, 7, pillarXAxis, rotation);

		this.fillAirRotated(world, sbb, 3, bottom + 5, 3, 7, bottom + 5, 7, rotation);

		// pillars connected only to ceiling
		this.fillBlocksRotated(world, sbb, 3, bottom + 5, 3, 3, top - 1, 3, deco.pillarState, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 5, 3, 7, top - 1, 3, deco.pillarState, rotation);
		this.fillBlocksRotated(world, sbb, 3, bottom + 5, 7, 3, top - 1, 7, deco.pillarState, rotation);
		this.fillBlocksRotated(world, sbb, 7, bottom + 5, 7, 7, top - 1, 7, deco.pillarState, rotation);
	}
}
