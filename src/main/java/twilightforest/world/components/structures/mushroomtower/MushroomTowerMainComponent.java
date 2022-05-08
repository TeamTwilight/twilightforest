package twilightforest.world.components.structures.mushroomtower;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructurePieceAccessor;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import twilightforest.world.registration.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.world.components.structures.TFStructureComponentOld;
import twilightforest.world.components.structures.lichtower.TowerRoofComponent;
import twilightforest.util.RotationUtil;

import java.util.Random;

public class MushroomTowerMainComponent extends MushroomTowerWingComponent {

	public MushroomTowerMainComponent(StructurePieceSerializationContext ctx, CompoundTag nbt) {
		super(MushroomTowerPieces.TFMTMai, nbt);
	}

	public MushroomTowerMainComponent(TFFeature feature, Random rand, int index, int x, int y, int z) {
		this(feature, rand, index, x + MAIN_SIZE, y + 4, z + MAIN_SIZE, Direction.NORTH);
	}

	public MushroomTowerMainComponent(TFFeature feature, Random rand, int index, int x, int y, int z, Direction rotation) {
		super(MushroomTowerPieces.TFMTMai, feature, index, x, y, z, MAIN_SIZE, 8 + (rand.nextInt(3) * FLOOR_HEIGHT), rotation);

//		// check to make sure we can build the whole tower
//		if (this.boundingBox.maxY > 245)
//		{
//			int amtToLower = (((this.boundingBox.maxY - 245) / 5) * 5) + 5;
//
//			FMLLog.info("[TwilightForest] Lowering Dark Tower max height by %d to be within world bounds", amtToLower);
//			
//			this.height -= amtToLower;
//			this.boundingBox.maxY -= amtToLower;
//		}

		// decorator
		if (this.deco == null) {
			this.deco = new MushroomTowerDecorator();
		}
	}

	protected MushroomTowerMainComponent(TFFeature feature, int i, int x, int y, int z, int pSize, int pHeight, Direction direction) {
		super(MushroomTowerPieces.TFMTMai, feature, i, x, y, z, pSize, pHeight, direction);
	}

	@Override
	public void addChildren(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		if (parent != null && parent instanceof TFStructureComponentOld) {
			this.deco = ((TFStructureComponentOld) parent).deco;
		}

		// we should have a door where we started
		addOpening(0, 1, size / 2, Rotation.CLOCKWISE_180);

		// should we build a base?
		this.hasBase = true;

		Rotation mainDir = null;

		// limit sprawl to a reasonable amount
		if (this.getGenDepth() < 3) {
			// make a special sub-tower that will lead back here 
			// try 6 times
			for (int i = 0; i < 6; i++) {
				mainDir = makeAscenderTower(list, rand);

				if (mainDir != null) {
					break;
				}
			}

			// make sub towers
			//for (int i = 0; i < 4; i++) {
			for (Rotation i : RotationUtil.ROTATIONS) {

				if (i == mainDir) {
					continue;
				}

				int[] dest = getValidOpening(rand, i);

				int childHeight = (rand.nextInt(2) + rand.nextInt(2) + 2) * FLOOR_HEIGHT + 1;

				makeBridge(list, rand, this.getGenDepth() + 1, dest[0], dest[1], dest[2], size - 4, childHeight, i);
			}
		} else {
			// add a roof?
			makeARoof(parent, list, rand);
		}
	}

	/**
	 * Make a new ascender tower.  Returns direction if successful, null if not.
	 */
	private Rotation makeAscenderTower(StructurePieceAccessor list, Random rand) {

		Rotation mainDir = RotationUtil.ROTATIONS[rand.nextInt(4)];
		int[] dest = getValidOpening(rand, mainDir);
		int childHeight = (height - dest[1]) + ((rand.nextInt(2) + rand.nextInt(2) + 3) * FLOOR_HEIGHT) + 1;
		boolean madeIt = makeBridge(list, rand, this.getGenDepth() + 1, dest[0], dest[1], dest[2], size - 4, childHeight, mainDir, true);

		if (madeIt) {
			TwilightForestMod.LOGGER.debug("Main tower made a bridge to another tower");
			return mainDir;
		} else {
			TwilightForestMod.LOGGER.info("Main tower failed to branch off at index {}", this.genDepth);
			return null;
		}
	}

	/**
	 * Make a mushroom roof!
	 */
	@Override
	public void makeARoof(StructurePiece parent, StructurePieceAccessor list, Random rand) {
		TowerRoofComponent roof = new TowerRoofMushroomComponent(getFeatureType(), this.getGenDepth() + 1, this, 1.6F, getLocatorPosition().getX(), getLocatorPosition().getY(), getLocatorPosition().getZ());
		list.addPiece(roof);
		roof.addChildren(this, list, rand);
	}

	/**
	 * Make an opening in this tower for a door.  This now only makes one opening, so you need two
	 */
	@Override
	protected void makeDoorOpening(WorldGenLevel world, int dx, int dy, int dz, BoundingBox sbb) {
		super.makeDoorOpening(world, dx, dy, dz, sbb);

		// try to remove blocks inside this door
		if (dx == 0) {
			placeBlock(world, AIR, dx + 1, dy, dz, sbb);
			placeBlock(world, AIR, dx + 1, dy + 1, dz, sbb);
		}
		if (dx == size - 1) {
			placeBlock(world, AIR, dx - 1, dy, dz, sbb);
			placeBlock(world, AIR, dx - 1, dy + 1, dz, sbb);
		}
		if (dz == 0) {
			placeBlock(world, AIR, dx, dy, dz + 1, sbb);
			placeBlock(world, AIR, dx, dy + 1, dz + 1, sbb);
		}
		if (dz == size - 1) {
			placeBlock(world, AIR, dx, dy, dz - 1, sbb);
			placeBlock(world, AIR, dx, dy + 1, dz - 1, sbb);
		}
	}
}
