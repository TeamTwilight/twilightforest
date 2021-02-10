package twilightforest.structures.finalcastle;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.template.TemplateManager;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.block.TFBlocks;
import twilightforest.structures.StructureTFComponentOld;
import twilightforest.util.RotationUtil;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class ComponentTFFinalCastleMain extends StructureTFComponentOld {

	public ComponentTFFinalCastleMain(TemplateManager manager, CompoundNBT nbt) {
		super(TFFinalCastlePieces.TFFCMain, nbt);
	}

	public ComponentTFFinalCastleMain(TFFeature feature, Random rand, int i, int x, int y, int z) {
		super(TFFinalCastlePieces.TFFCMain, feature, i);
		this.setCoordBaseMode(Direction.SOUTH);
		this.spawnListIndex = 1; // main monsters

		x = ((x + 127) >> 8) << 8;
		z = ((z + 127) >> 8) << 8;

		this.boundingBox = feature.getComponentToAddBoundingBox(x, y, z, -24, 120, -24, 48, 40, 48, Direction.SOUTH);

		BlockPos cc = TFFeature.getNearestCenterXYZ(x >> 4, z >> 4);

		int cx = (x >> 8) << 8;
		int cz = (z >> 8) << 8;

		TwilightForestMod.LOGGER.debug("Making castle at {}, {}. center is {}, {}", x, z, cc.getX(), cc.getZ());
		TwilightForestMod.LOGGER.debug("Natural center at {}, {}", cx, cz);

		// decorator
		if (this.deco == null) {
			this.deco = new StructureTFDecoratorCastle();
		}
	}

	@Override
	public void buildComponent(StructurePiece parent, List<StructurePiece> list, Random rand) {
		// add foundation
		ComponentTFFinalCastleFoundation48 foundation = new ComponentTFFinalCastleFoundation48(getFeatureType(), rand, 4, this);
		list.add(foundation);
		foundation.buildComponent(this, list, rand);

		// add roof
		StructureTFComponentOld roof = new ComponentTFFinalCastleRoof48Crenellated(getFeatureType(), rand, 4, this);
		list.add(roof);
		roof.buildComponent(this, list, rand);

		// boss gazebo on roof
		StructureTFComponentOld gazebo = new ComponentTFFinalCastleBossGazebo(getFeatureType(), rand, 5, this);
		list.add(gazebo);
		gazebo.buildComponent(this, list, rand);


		// build 4 towers on sides
		ComponentTFFinalCastleStairTower tower0 = new ComponentTFFinalCastleStairTower(getFeatureType(), rand, 3, boundingBox.minX, boundingBox.minY + 3, boundingBox.minZ, Direction.NORTH);
		list.add(tower0);
		tower0.buildComponent(this, list, rand);

		ComponentTFFinalCastleLargeTower tower1 = new ComponentTFFinalCastleLargeTower(getFeatureType(), rand, 3, boundingBox.maxX, boundingBox.minY + 3, boundingBox.minZ, Direction.EAST);
		list.add(tower1);
		tower1.buildComponent(this, list, rand);

		ComponentTFFinalCastleStairTower tower2 = new ComponentTFFinalCastleStairTower(getFeatureType(), rand, 3, boundingBox.minX, boundingBox.minY + 3, boundingBox.maxZ, Direction.WEST);
		list.add(tower2);
		tower2.buildComponent(this, list, rand);

		ComponentTFFinalCastleStairTower tower3 = new ComponentTFFinalCastleStairTower(getFeatureType(), rand, 3, boundingBox.maxX, boundingBox.minY + 3, boundingBox.maxZ, Direction.SOUTH);
		list.add(tower3);
		tower3.buildComponent(this, list, rand);

		// tower maze towards entrance
		BlockPos dest = new BlockPos(boundingBox.minX - 4, boundingBox.maxY, boundingBox.minZ - 24);
		buildTowerMaze(list, rand, 48, 0, 24, 60, Direction.SOUTH, TFBlocks.castle_rune_brick_pink.get().getDefaultState(), dest);


		// another tower/bridge maze towards the clock tower
		dest = new BlockPos(boundingBox.maxX + 4, boundingBox.minY, boundingBox.maxZ + 24);
		buildTowerMaze(list, rand, 0, 30, 24, 60, Direction.NORTH, TFBlocks.castle_rune_brick_blue.get().getDefaultState(), dest);


		// initial stairs down towards dungeon
		ComponentTFFinalCastleDungeonSteps steps0 = new ComponentTFFinalCastleDungeonSteps(getFeatureType(), rand, 5, boundingBox.minX + 18, boundingBox.minY + 1, boundingBox.minZ + 18, Direction.SOUTH);
		list.add(steps0);
		steps0.buildComponent(this, list, rand);

		// continued steps
		ComponentTFFinalCastleDungeonSteps steps1 = steps0.buildMoreStepsTowards(parent, list, rand, Rotation.COUNTERCLOCKWISE_90);
		ComponentTFFinalCastleDungeonSteps steps2 = steps1.buildMoreStepsTowards(parent, list, rand, Rotation.COUNTERCLOCKWISE_90);
		ComponentTFFinalCastleDungeonSteps steps3 = steps2.buildMoreStepsTowards(parent, list, rand, Rotation.COUNTERCLOCKWISE_90);

		// start dungeon
		steps3.buildLevelUnder(parent, list, rand, 1);

		// mural on front
		BlockPos mc = this.offsetTowerCCoords(48, 23, 25, 1, Direction.SOUTH);
		ComponentTFFinalCastleMural mural0 = new ComponentTFFinalCastleMural(getFeatureType(), rand, 7, mc.getX(), mc.getY(), mc.getZ(), 35, 30, Direction.SOUTH);
		list.add(mural0);
		mural0.buildComponent(this, list, rand);

		// mural inside
		BlockPos mc1 = this.offsetTowerCCoords(48, 33, 24, -1, Direction.SOUTH);
		ComponentTFFinalCastleMural mural1 = new ComponentTFFinalCastleMural(getFeatureType(), rand, 7, mc1.getX(), mc1.getY(), mc.getZ(), 19, 12, Direction.NORTH);
		list.add(mural1);
		mural1.buildComponent(this, list, rand);

	}

	/**
	 * Build a side tower, then tell it to start building towards the destination
	 */
	private void buildTowerMaze(List<StructurePiece> list, Random rand, int x, int y, int z, int howFar, Direction direction, BlockState type, BlockPos dest) {
		boolean complete = false;
		int iterations = 0;
		while (!complete && iterations < 15) {
			iterations++;
			// duplicate list
			List<StructurePiece> before = new LinkedList<>(list);

			// build
			BlockPos tc = this.offsetTowerCCoords(x, y, z, howFar, direction);
			ComponentTFFinalCastleMazeTower13 sTower = new ComponentTFFinalCastleMazeTower13(TFFinalCastlePieces.TFFCSiTo, getFeatureType(), rand, 3, tc.getX(), tc.getY(), tc.getZ(), type, direction);

			// add bridge
			BlockPos bc = this.offsetTowerCCoords(x, y, z, 1, direction);
			ComponentTFFinalCastleBridge bridge = new ComponentTFFinalCastleBridge(getFeatureType(), this.getComponentType() + 1, bc.getX(), bc.getY(), bc.getZ(), howFar - 7, direction);

			list.add(bridge);
			bridge.buildComponent(this, list, rand);

			// don't check if the bounding box is clear, there's either nothing there or we've made a terrible mistake
			list.add(sTower);
			sTower.buildTowards(this, list, rand, dest);

			// check if we've successfully built the end tower
			TwilightForestMod.LOGGER.debug("Working towards {},{},{}", dest.getX(), dest.getY(), dest.getZ());
			if (this.isMazeComplete(list, type)) {
				TwilightForestMod.LOGGER.debug("Tower maze color {} complete!", type);
				complete = true;
			} else {
				// TODO: add limit on retrying, in case of infinite loop?
				TwilightForestMod.LOGGER.info("Tower maze color {} INCOMPLETE, retrying!", type);
				list.clear();
				list.addAll(before);
				//this.buildTowerMaze(list, rand, x, y, z, howFar, direction, color, dest);
			}
		}
	}

	private boolean isMazeComplete(List<StructurePiece> list, BlockState type) {
		if (list.size() > 60) {
			TwilightForestMod.LOGGER.warn("Maze of color {} is getting a bit excessive.", type);
		}
		for (StructurePiece structurecomponent : list) {
			MutableBoundingBox boundingBox = structurecomponent.getBoundingBox();
			int x = (boundingBox.maxX - boundingBox.minX / 2) + boundingBox.minX;
			int y = (boundingBox.maxY - boundingBox.minY / 2) + boundingBox.minY;
			int z = (boundingBox.maxZ - boundingBox.minZ / 2) + boundingBox.minZ;
			TwilightForestMod.LOGGER.debug("Component {} at {},{},{}", structurecomponent.getClass().getSimpleName(), x, y, z);
			if (type == TFBlocks.castle_rune_brick_pink.get().getDefaultState() && structurecomponent instanceof ComponentTFFinalCastleEntranceTower) {
				return true;
			}
			if (type == TFBlocks.castle_rune_brick_blue.get().getDefaultState() && structurecomponent instanceof ComponentTFFinalCastleBellTower21) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Provides coordinates to make a tower such that it will open into the parent tower at the provided coordinates.
	 */
	@Override
	protected BlockPos offsetTowerCCoords(int x, int y, int z, int howFar, Direction direction) {

		int dx = getXWithOffset(x, z);
		int dy = getYWithOffset(y);
		int dz = getZWithOffset(x, z);

		switch (direction) {
			case SOUTH:
				dx += howFar;
				break;
			case WEST:
				dz += howFar;
				break;
			case NORTH:
				dx -= howFar;
				break;
			case EAST:
				dz -= howFar;
				break;
			default:
				break;
		}

		// ugh?
		return new BlockPos(dx, dy, dz);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager manager, ChunkGenerator generator, Random rand, MutableBoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		// walls
		fillWithRandomizedBlocks(world, sbb, 0, 0, 0, 48, 40, 48, false, rand, deco.randomBlocks);

		// 2M
		fillWithRandomizedBlocks(world, sbb, 13, 30, 1, 47, 30, 12, false, rand, deco.randomBlocks);
		this.fillWithBlocks(world, sbb, 13, 31, 12, 36, 31, 12, deco.fenceState, deco.fenceState, false);
		fillWithRandomizedBlocks(world, sbb, 13, 30, 36, 47, 30, 47, false, rand, deco.randomBlocks);
		this.fillWithBlocks(world, sbb, 13, 31, 36, 36, 31, 36, deco.fenceState, deco.fenceState, false);
		fillWithRandomizedBlocks(world, sbb, 1, 30, 1, 12, 30, 47, false, rand, deco.randomBlocks);
		this.fillWithBlocks(world, sbb, 12, 31, 12, 12, 31, 36, deco.fenceState, deco.fenceState, false);

		// second floor stairs to mezzanine
		fillWithRandomizedBlocks(world, sbb, 38, 25, 13, 47, 25, 35, false, rand, deco.randomBlocks);

		for (int i = 0; i < 5; i++) {
			int y = 30 - i;

			makeMezzTopStairs(world, sbb, y, 10 + i, Direction.SOUTH);
			makeMezzTopStairs(world, sbb, y, 38 - i, Direction.NORTH);

			y = 25 - i;
			int x = 37 - i;
			final BlockState stairState = getStairState(deco.stairState, Direction.WEST, rotation, false);
			this.fillWithBlocks(world, sbb, x, y, 14, x, y, 22, stairState, stairState, false);
			this.fillWithBlocks(world, sbb, x, y - 1, 14, x, y - 1, 22, deco.blockState, deco.blockState, false);
			this.fillWithBlocks(world, sbb, x, y, 26, x, y, 34, stairState, stairState, false);
			this.fillWithBlocks(world, sbb, x, y - 1, 26, x, y - 1, 34, deco.blockState, deco.blockState, false);
		}

		// pillars
		for (int x = 11; x < 47; x += 12) {
			for (int z = 11; z < 47; z += 12) {
				this.fillWithBlocks(world, sbb, x, 1, z, x + 2, 40, z + 2, deco.pillarState, deco.blockState, false);

				makePillarBase(world, sbb, x, z, 1, false);
				makePillarBase(world, sbb, x, z, 19, true);
				makePillarBase(world, sbb, x, z, 21, false);
				makePillarBase(world, sbb, x, z, 39, true);
			}
		}

		// side pillars
		for (Rotation rotation : RotationUtil.ROTATIONS) {
			for (int z = 11; z < 47; z += 12) {

				// no middle pillars on walls with entrances
				if (z == 23 && (rotation == Rotation.NONE || rotation == Rotation.CLOCKWISE_180)) {
					continue;
				}

				this.fillBlocksRotated(world, sbb, 1, 1, z, 1, 40, z + 2, deco.pillarState, rotation);
				makeHalfPillarBase(world, sbb, rotation, 1, z, false);
				makeHalfPillarBase(world, sbb, rotation, 19, z, true);
				makeHalfPillarBase(world, sbb, rotation, 21, z, false);
				makeHalfPillarBase(world, sbb, rotation, 39, z, true);
			}
		}

		// second floor
		fillWithRandomizedBlocks(world, sbb, 1, 20, 1, 47, 20, 47, false, rand, deco.randomBlocks);

		// force field around dungeon stairs
		BlockState fieldBlock = TFBlocks.force_field_pink.get().getDefaultState();
		this.fillWithBlocks(world, sbb, 12, 1, 12, 24, 10, 12, fieldBlock, fieldBlock, false);
		this.fillWithBlocks(world, sbb, 12, 1, 12, 12, 10, 24, fieldBlock, fieldBlock, false);
		this.fillWithBlocks(world, sbb, 24, 1, 12, 24, 10, 24, fieldBlock, fieldBlock, false);
		this.fillWithBlocks(world, sbb, 12, 1, 24, 24, 10, 24, fieldBlock, fieldBlock, false);

		this.fillWithBlocks(world, sbb, 12, 10, 12, 24, 10, 24, fieldBlock, fieldBlock, false);

		// doors in dungeon force field
		this.fillWithBlocks(world, sbb, 17, 1, 12, 19, 4, 12, TFBlocks.castle_door_pink.get().getDefaultState(), AIR, false);
		this.fillWithBlocks(world, sbb, 17, 1, 24, 19, 4, 24, TFBlocks.castle_door_pink.get().getDefaultState(), AIR, false);

		// stairs to stair towers
		makeSmallTowerStairs(world, sbb, Rotation.NONE);
		makeSmallTowerStairs(world, sbb, Rotation.CLOCKWISE_90);
		makeSmallTowerStairs(world, sbb, Rotation.COUNTERCLOCKWISE_90);
		makeLargeTowerStairs(world, sbb, Rotation.CLOCKWISE_180);

		// door, first floor
		this.fillWithBlocks(world, sbb, 48, 1, 23, 48, 4, 25, TFBlocks.castle_door_yellow.get().getDefaultState(), AIR, false);

		// door, second floor
		this.fillWithBlocks(world, sbb, 0, 31, 23, 0, 34, 25, TFBlocks.castle_door_purple.get().getDefaultState(), AIR, false);

		return true;
	}

	private void makeSmallTowerStairs(ISeedReader world, MutableBoundingBox sbb, Rotation rotation) {
		for (int y = 1; y < 4; y++) {
			int z = 40 + y;
			this.fillBlocksRotated(world, sbb, 1, 1, z, 4, y, z, deco.blockState, rotation);

			this.fillBlocksRotated(world, sbb, 2, y, z, 3, y, z, getStairState(deco.stairState, Direction.NORTH, rotation, false), rotation);
		}
	}

	private void makeLargeTowerStairs(ISeedReader world, MutableBoundingBox sbb, Rotation rotation) {
		final BlockState stairState = getStairState(deco.stairState, Direction.NORTH, rotation, false);
		for (int y = 1; y < 4; y++) {
			int z = 38 + y;
			this.fillBlocksRotated(world, sbb, 2, 1, z, 6, y, z, deco.blockState, rotation);
			this.fillBlocksRotated(world, sbb, 3, y, z, 5, y, z, stairState, rotation);
		}
	}

	private void makeMezzTopStairs(ISeedReader world, MutableBoundingBox sbb, int y, int z, Direction stairMeta) {
		final BlockState stairState = getStairState(deco.stairState, stairMeta, rotation, false);
		this.fillWithBlocks(world, sbb, 38, y, z, 46, y, z, stairState, stairState, false);
		this.fillWithBlocks(world, sbb, 38, y - 1, z, 46, y - 1, z, deco.blockState, deco.blockState, false);
		this.fillWithAir(world, sbb, 38, y + 1, z, 46, y + 3, z);
	}

	private void makeHalfPillarBase(ISeedReader world, MutableBoundingBox sbb, Rotation rotation, int y, int z, boolean isFlipped) {
		this.fillBlocksRotated(world, sbb, 2, y, z - 1, 2, y, z + 3, getStairState(deco.stairState, Direction.EAST, rotation, isFlipped), rotation);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.NORTH, rotation, isFlipped), 1, y, z - 1, rotation, sbb);
		this.setBlockStateRotated(world, getStairState(deco.stairState, Direction.SOUTH, rotation, isFlipped), 1, y, z + 3, rotation, sbb);
	}

	private void makePillarBase(ISeedReader world, MutableBoundingBox sbb, int x, int z, int y, boolean isFlipped) {
		this.fillWithBlocks(world, sbb, x + 0, y, z + 3, x + 3, y, z + 3, getStairState(deco.stairState, Direction.SOUTH, rotation, isFlipped), AIR, false);
		this.fillWithBlocks(world, sbb, x - 1, y, z - 1, x + 2, y, z - 1, getStairState(deco.stairState, Direction.NORTH, rotation, isFlipped), AIR, false);

		this.fillWithBlocks(world, sbb, x + 3, y, z - 1, x + 3, y, z + 2, getStairState(deco.stairState, Direction.EAST, rotation, isFlipped), AIR, false);
		this.fillWithBlocks(world, sbb, x - 1, y, z + 0, x - 1, y, z + 3, getStairState(deco.stairState, Direction.WEST, rotation, isFlipped), AIR, false);
	}
}
