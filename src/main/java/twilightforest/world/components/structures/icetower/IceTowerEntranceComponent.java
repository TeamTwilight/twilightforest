package twilightforest.world.components.structures.icetower;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.components.structures.TFStructureComponent;
import twilightforest.world.registration.TFFeature;
import twilightforest.world.components.structures.TFStructureComponentOld;

import java.util.Random;

public class IceTowerEntranceComponent extends IceTowerWingComponent {

	public IceTowerEntranceComponent(ServerLevel level, CompoundTag nbt) {
		super(IceTowerPieces.TFITEnt, nbt);
	}

	public IceTowerEntranceComponent(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		super(IceTowerPieces.TFITEnt, feature, i, x, y, z, pSize, pHeight, direction);
	}

	@Override
	protected boolean shouldHaveBase(Random rand) {
		return true;
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		if (parent != null && parent instanceof TFStructureComponent tfStructureComponent) {
			this.deco = tfStructureComponent.deco;
		}

		// we should have a door where we started
		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);

		// stairs
		addStairs(list, rand, this.getGenDepth() + 1, this.size - 1, 1, size / 2, Rotation.NONE);
		addStairs(list, rand, this.getGenDepth() + 1, this.size / 2, 1, 0, Rotation.COUNTERCLOCKWISE_90);
		addStairs(list, rand, this.getGenDepth() + 1, this.size / 2, 1, this.size - 1, Rotation.CLOCKWISE_90);

		// should we build a base
		this.hasBase = this.shouldHaveBase(rand);

		// add a roof?
		makeARoof(parent, list, rand);
	}

	/**
	 * Add some stairs leading to this tower
	 */
	private boolean addStairs(StructurePieceAccessor list, Random rand, int index, int x, int y, int z, Rotation rotation) {
		// add door
		this.addOpening(x, y, z, rotation);

		Direction direction = getStructureRelativeRotation(rotation);
		BlockPos dx = offsetTowerCCoords(x, y, z, this.size, direction);

		IceTowerStairsComponent entrance = new IceTowerStairsComponent(getFeatureType(), index, dx.getX(), dx.getY(), dx.getZ(), this.size, this.height, direction);

		list.addPiece(entrance);
		entrance.addChildren(this, list, rand);
		return true;
	}

	/**
	 * Make a new wing
	 */
	@Override
	public boolean makeTowerWing(StructurePieceAccessor list, Random rand, int index, int x, int y, int z, int wingSize, int wingHeight, Rotation rotation) {
		return false;
	}

	/**
	 * No floors
	 */
	@Override
	protected void makeFloorsForTower(WorldGenLevel world, Random rand, BoundingBox sbb) {
		decoratePillarsCornersHigh(world, 0, 11, Rotation.NONE, sbb);
	}

	protected void decoratePillarsCornersHigh(WorldGenLevel world, int bottom, int top, Rotation rotation, BoundingBox sbb) {
		final BlockState pillarXAxis = deco.pillarState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.X);
		final BlockState pillarZAxis = deco.pillarState.setValue(RotatedPillarBlock.AXIS, Direction.Axis.Z);
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
