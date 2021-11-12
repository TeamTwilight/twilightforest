package twilightforest.world.components.structures.darktower;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import twilightforest.world.registration.TFFeature;
import twilightforest.block.TFBlocks;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.util.RotationUtil;

import java.util.Random;

public class DarkTowerBossTrapComponent extends DarkTowerWingComponent {

	public DarkTowerBossTrapComponent(ServerLevel level, CompoundTag nbt) {
		super(DarkTowerPieces.TFDTBT, nbt);
	}

	protected DarkTowerBossTrapComponent(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		super(DarkTowerPieces.TFDTBT, feature, i, x, y, z, pSize, pHeight, direction);

		// no spawns
		this.spawnListIndex = -1;
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
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
			makeTowerBalcony(list, rand, this.getGenDepth(), dest[0], dest[1], dest[2], i);
		}
	}

	/**
	 * Attach a roof to this tower.
	 */
	@Override
	public void makeARoof(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		//nope;
	}

	@Override
	public boolean postProcess(WorldGenLevel world, StructureFeatureManager manager, ChunkGenerator generator, Random rand, BoundingBox sbb, ChunkPos chunkPosIn, BlockPos blockPos) {
		Random decoRNG = new Random(world.getSeed() + (this.boundingBox.minX() * 321534781L) ^ (this.boundingBox.minZ() * 756839L));

		// make walls
		makeEncasedWalls(world, rand, sbb, 0, 0, 0, size - 1, height - 1, size - 1);

		// clear inside
		generateAirBox(world, sbb, 1, 1, 1, size - 2, height - 2, size - 2);

		// openings
		makeOpenings(world, sbb);

		// half floors, always starting at y = 4
		addBossTrapFloors(world, sbb);

		// demolish some
		destroyTower(world, decoRNG, 5, height + 2, 5, 4, sbb);
		destroyTower(world, decoRNG, 0, height, 0, 3, sbb);
		destroyTower(world, decoRNG, 0, height, 8, 4, sbb);

		// hole for boss trap beam
		destroyTower(world, decoRNG, 5, 6, 5, 2, sbb);

		// redraw some of the floor in case we destroyed it
		this.generateBox(world, sbb, 1, 0, 1, size / 2, 0, size - 2, deco.blockState, Blocks.AIR.defaultBlockState(), false);
		this.generateBox(world, sbb, 1, 1, 1, size / 2, 1, size - 2, Blocks.AIR.defaultBlockState(), Blocks.AIR.defaultBlockState(), false);

		// add boss trap
		this.placeBlock(world, TFBlocks.GHAST_TRAP.get().defaultBlockState(), 5, 1, 5, sbb);
		this.placeBlock(world, Blocks.REDSTONE_WIRE.defaultBlockState(), 5, 1, 6, sbb);
		this.placeBlock(world, Blocks.REDSTONE_WIRE.defaultBlockState(), 5, 1, 7, sbb);
		this.placeBlock(world, Blocks.REDSTONE_WIRE.defaultBlockState(), 5, 1, 8, sbb);
		this.placeBlock(world, Blocks.REDSTONE_WIRE.defaultBlockState(), 4, 1, 8, sbb);
		this.placeBlock(world, Blocks.REDSTONE_WIRE.defaultBlockState(), 3, 1, 8, sbb);
		this.placeBlock(world, Blocks.OAK_PRESSURE_PLATE.defaultBlockState(), 2, 1, 8, sbb);

		return true;
	}

	/**
	 * Add specific boss trap floors
	 */
	protected void addBossTrapFloors(WorldGenLevel world, BoundingBox sbb) {

		makeFullFloor(world, sbb, 4);

		addStairsDown(world, sbb, Rotation.COUNTERCLOCKWISE_90, 4, size - 2, 4);
		addStairsDown(world, sbb, Rotation.COUNTERCLOCKWISE_90, 4, size - 3, 4);

		// stairs to roof
		addStairsDown(world, sbb, Rotation.CLOCKWISE_90, this.height - 1, size - 2, 4);
		addStairsDown(world, sbb, Rotation.CLOCKWISE_90, this.height - 1, size - 3, 4);
	}
}
