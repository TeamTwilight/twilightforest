package twilightforest.structures;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraft.world.gen.structure.StructureStrongholdPieces;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.biomes.TFBiomes;
import twilightforest.block.TFBlockProperties;
import twilightforest.block.TFBlocks;
import twilightforest.structures.darktower.ComponentTFDarkTowerMain;
import twilightforest.structures.darktower.TFDarkTowerPieces;
import twilightforest.structures.finalcastle.ComponentTFFinalCastleMain;
import twilightforest.structures.finalcastle.TFFinalCastlePieces;
import twilightforest.structures.hollowtree.StructureTFHollowTreeStart;
import twilightforest.structures.hollowtree.TFHollowTreePieces;
import twilightforest.structures.icetower.ComponentTFIceTowerMain;
import twilightforest.structures.icetower.TFIceTowerPieces;
import twilightforest.structures.lichtower.ComponentTFTowerMain;
import twilightforest.structures.lichtower.TFLichTowerPieces;
import twilightforest.structures.minotaurmaze.ComponentTFMazeRuins;
import twilightforest.structures.minotaurmaze.TFMinotaurMazePieces;
import twilightforest.structures.mushroomtower.ComponentTFMushroomTowerMain;
import twilightforest.structures.mushroomtower.TFMushroomTowerPieces;
import twilightforest.structures.stronghold.ComponentTFStrongholdEntrance;
import twilightforest.structures.stronghold.TFStrongholdPieces;
import twilightforest.structures.trollcave.ComponentTFTrollCaveMain;
import twilightforest.structures.trollcave.TFTrollCavePieces;
import twilightforest.world.TFBiomeProvider;
import twilightforest.world.TFWorld;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class StructureTFMajorFeatureStart extends StructureStart {

	public static int NUM_LOCKS = 4;

	public TFFeature feature;
	public boolean isConquered;
	public byte[] lockBytes = new byte[NUM_LOCKS];

	static {
		MapGenStructureIO.registerStructure(StructureTFMajorFeatureStart.class, "TFFeature");
		MapGenStructureIO.registerStructure(StructureTFHollowTreeStart.class, "TFHollowTree");

		TFFinalCastlePieces.registerFinalCastlePieces();
		TFStrongholdPieces.registerPieces();
		TFMushroomTowerPieces.registerPieces();
		TFMinotaurMazePieces.registerPieces();
		TFDarkTowerPieces.registerPieces();
		TFIceTowerPieces.registerPieces();
		TFTrollCavePieces.registerPieces();
		TFHollowTreePieces.registerPieces();
		TFLichTowerPieces.registerPieces();

		// register one-off pieces here
		MapGenStructureIO.registerStructureComponent(ComponentTFHedgeMaze.class, "TFHedge");
		MapGenStructureIO.registerStructureComponent(ComponentTFHollowHill.class, "TFHill");
		MapGenStructureIO.registerStructureComponent(ComponentTFHydraLair.class, "TFHydra");
		MapGenStructureIO.registerStructureComponent(ComponentTFNagaCourtyard.class, "TFNaga");
		MapGenStructureIO.registerStructureComponent(ComponentTFQuestGrove.class, "TFQuest1");
		MapGenStructureIO.registerStructureComponent(ComponentTFYetiCave.class, "TFYeti");
	}

	public StructureTFMajorFeatureStart() {
	}

	public StructureTFMajorFeatureStart(World world, Random rand, int chunkX, int chunkZ) {
		StructureStrongholdPieces.prepareStructurePieces();
		//TFStrongholdPieces.prepareStructurePieces();

		int x = (chunkX << 4) + 8;
		int z = (chunkZ << 4) + 8;
		int y = TFWorld.SEALEVEL + 1; //TODO: maybe a biome-specific altitude for some of them?

		this.feature = TFFeature.getFeatureDirectlyAt(chunkX, chunkZ, world);
		this.isConquered = false;

		StructureComponent firstComponent = makeFirstComponent(world, rand, feature, x, y, z);
		if (firstComponent != null) {
			components.add(firstComponent);
			firstComponent.buildComponent(firstComponent, components, rand);
		}

		updateBoundingBox();

		if (firstComponent instanceof StructureStrongholdPieces.Stairs2) {
			List<StructureComponent> var6 = ((StructureStrongholdPieces.Stairs2) firstComponent).pendingChildren;

			while (!var6.isEmpty()) {
				int var7 = rand.nextInt(var6.size());
				StructureComponent var8 = (StructureComponent) var6.remove(var7);
				var8.buildComponent(firstComponent, this.components, rand);
			}

			updateBoundingBox();

			int offY = -33;

			boundingBox.offset(0, offY, 0);

			for (StructureComponent com : getComponents()) {
				com.getBoundingBox().offset(0, offY, 0);
			}
		}


		if (firstComponent instanceof ComponentTFTowerMain || firstComponent instanceof ComponentTFDarkTowerMain) {
			moveToAvgGroundLevel(world, x, z);
		}
	}

	/**
	 * @return The first component we should add to our structure
	 */
	public StructureComponent makeFirstComponent(World world, Random rand, TFFeature feature, int x, int y, int z) {
		/*if (feature != null) {
			//FIXME: Debug, force only one kind of feature to spawn.
			TwilightForestMod.LOGGER.info("Selected Debug Feature @ {} {} {}", x, y, z);
			return new ComponentTFFinalCastleMain(world, rand, 0, x, y, z);
		}*/

		if (feature == TFFeature.nagaCourtyard) {
			TwilightForestMod.LOGGER.info("Naga Courtyard @ {} {} {}", x, y, z);
			return new ComponentTFNagaCourtyard(world, rand, 0, x, y, z);
		}
		if (feature == TFFeature.hedgeMaze) {
			TwilightForestMod.LOGGER.info("Hedge Maze @ {} {} {}", x, y, z);
			return new ComponentTFHedgeMaze(world, rand, 0, x, y, z);
		}
		if (feature == TFFeature.hill1) {
			TwilightForestMod.LOGGER.info("Hill 1 @ {} {} {}", x, y, z);
			return new ComponentTFHollowHill(world, rand, 0, 1, x, y, z);
		}
		if (feature == TFFeature.hill2) {
			TwilightForestMod.LOGGER.info("Hill 2 @ {} {} {}", x, y, z);
			return new ComponentTFHollowHill(world, rand, 0, 2, x, y, z);
		}
		if (feature == TFFeature.hill3) {
			TwilightForestMod.LOGGER.info("Hill 3 @ {} {} {}", x, y, z);
			return new ComponentTFHollowHill(world, rand, 0, 3, x, y, z);
		}
		if (feature == TFFeature.questGrove) {
			TwilightForestMod.LOGGER.info("Quest Grove @ {} {} {}", x, y, z);
			return new ComponentTFQuestGrove(world, rand, 0, x, y, z);
		}
		if (feature == TFFeature.hydraLair) {
			TwilightForestMod.LOGGER.info("Hydra Lair @ {} {} {}", x, y, z);
			return new ComponentTFHydraLair(world, rand, 0, x, y, z);
		}
		if (feature == TFFeature.yetiCave) {
			TwilightForestMod.LOGGER.info("Yeti Cave @ {} {} {}", x, y, z);
			return new ComponentTFYetiCave(world, rand, 0, x, y, z);
		}

		if (feature == TFFeature.lichTower) {
			TwilightForestMod.LOGGER.info("Lich Tower @ {} {} {}", x, y, z);
			return new ComponentTFTowerMain(world, rand, 0, x, y, z);
		}
		if (feature == TFFeature.trollCave) {
			TwilightForestMod.LOGGER.info("Troll Cave @ {} {} {}", x, y, z);
			return new ComponentTFTrollCaveMain(world, rand, 0, x, y, z);
		}
		if (feature == TFFeature.iceTower) {
			TwilightForestMod.LOGGER.info("Ice Tower @ {} {} {}", x, y, z);
			return new ComponentTFIceTowerMain(world, rand, 0, x, y, z);
		}
		if (feature == TFFeature.darkTower) {
			return new ComponentTFDarkTowerMain(world, rand, 0, x, y - 1, z);
		}
		if (feature == TFFeature.labyrinth) {
			return new ComponentTFMazeRuins(world, rand, 0, x, y, z);
		}
		if (feature == TFFeature.mushroomTower) {
			return new ComponentTFMushroomTowerMain(world, rand, 0, x, y, z);
		}
		if (feature == TFFeature.tfStronghold) {
			return new ComponentTFStrongholdEntrance(world, rand, 0, x, y, z);
		}
		if (feature == TFFeature.finalCastle) {
			return new ComponentTFFinalCastleMain(world, rand, 0, x, y, z);
		}

		return null;
	}

	@Override
	public boolean isSizeableStructure() {
		return feature.isStructureEnabled;
	}


	/**
	 * Move the whole structure up or down
	 */
	protected void moveToAvgGroundLevel(World world, int x, int z) {
		if (world.getBiomeProvider() instanceof TFBiomeProvider) {
			// determine the biome at the origin
			Biome biomeAt = world.getBiome(new BlockPos(x, 0, z));

			int offY = (int) ((biomeAt.getBaseHeight() + biomeAt.getHeightVariation()) * 8);

			// dark forest doesn't seem to get the right value.  Why is my calculation so bad?
			if (biomeAt == TFBiomes.darkForest) {
				offY += 4;
			}

			if (offY > 0) {
				boundingBox.offset(0, offY, 0);

				for (StructureComponent com : getComponents()) {
					com.getBoundingBox().offset(0, offY, 0);
				}
			}
		}
	}

//    /**
//     * Keeps iterating Structure Pieces and spawning them until the checks tell it to stop
//     */
//    public void generateStructure(World par1World, Random par2Random, StructureBoundingBox par3StructureBoundingBox)
//    {
//        if (this.getComponents().getFirst() instanceof ComponentTFStrongholdEntrance)
//        {
//
//			for (StructureComponent component : (LinkedList<StructureComponent>) getComponents())
//			{
//				
//				// TODO: we need to test the shield bounding box here, otherwise we lose shield facings across chunk boundires
//				
//	            if (isShieldable(component) ? isIntersectingLarger(par3StructureBoundingBox, component) : isIntersectingLarger(par3StructureBoundingBox, component))
//	            {
//	            	if (isShieldable(component))
//	            	{
//	            		addShieldFor(par1World, component, (LinkedList<StructureComponent>) getComponents(), par3StructureBoundingBox);
//	            	}
//	            	component.addComponentParts(par1World, par2Random, par3StructureBoundingBox);
//	            }
//			}
//        }
//        else
//        {
//        	super.generateStructure(par1World, par2Random, par3StructureBoundingBox);
//        }
//    }

	/**
	 * Check if the component is within the chunk bounding box, but check as if it was one larger
	 */
	private boolean isIntersectingLarger(StructureBoundingBox chunkBB, StructureComponent component) {
		StructureBoundingBox compBB = component.getBoundingBox();

		// don't bother checking Y
		return (compBB.maxX + 1) >= chunkBB.minX && (compBB.minX - 1) <= chunkBB.maxX && (compBB.maxZ + 1) >= chunkBB.minZ && (compBB.minZ - 1) <= chunkBB.maxZ;

	}

	private boolean isShieldable(StructureComponent component) {
		return component.getBoundingBox().maxY <= 32;
	}

	/**
	 * Make the stronghold shield around a component's bounding box
	 */
	private void addShieldFor(World world, StructureComponent component, List<StructureComponent> otherComponents, StructureBoundingBox chunkBox) {
		StructureBoundingBox shieldBox = new StructureBoundingBox(component.getBoundingBox());

		shieldBox.minX--;
		shieldBox.minY--;
		shieldBox.minZ--;

		shieldBox.maxX++;
		shieldBox.maxY++;
		shieldBox.maxZ++;

		ArrayList<StructureComponent> intersecting = new ArrayList<StructureComponent>();

		for (StructureComponent other : otherComponents) {
			if (other != component && shieldBox.intersectsWith(other.getBoundingBox())) {
				intersecting.add(other);
			}
		}

		// trace outline
		for (int x = shieldBox.minX; x <= shieldBox.maxX; x++) {
			for (int y = shieldBox.minY; y <= shieldBox.maxY; y++) {
				for (int z = shieldBox.minZ; z <= shieldBox.maxZ; z++) {
					if (x == shieldBox.minX || x == shieldBox.maxX || y == shieldBox.minY || y == shieldBox.maxY || z == shieldBox.minZ || z == shieldBox.maxZ) {
						BlockPos pos = new BlockPos(x, y, z);
						if (chunkBox.isVecInside(pos)) {
							// test other boxes
							boolean notIntersecting = true;

							for (StructureComponent other : intersecting) {
								if (other.getBoundingBox().isVecInside(pos)) {
									notIntersecting = false;
								}
							}


							if (notIntersecting) {
								world.setBlockState(pos, TFBlocks.shield.getDefaultState().withProperty(TFBlockProperties.FACING, calculateShieldFacing(shieldBox, x, y, z)), 2);
							}

						}
					}
				}
			}
		}
	}

	private EnumFacing calculateShieldFacing(StructureBoundingBox shieldBox, int x, int y, int z) {
		EnumFacing facing = EnumFacing.DOWN;
		if (x == shieldBox.minX) {
			facing = EnumFacing.EAST;
		}
		if (x == shieldBox.maxX) {
			facing = EnumFacing.WEST;
		}
		if (z == shieldBox.minZ) {
			facing = EnumFacing.SOUTH;
		}
		if (z == shieldBox.maxZ) {
			facing = EnumFacing.NORTH;
		}
		if (y == shieldBox.minY) {
			facing = EnumFacing.UP;
		}
		if (y == shieldBox.maxY) {
			facing = EnumFacing.DOWN;
		}
		return facing;
	}

	@Override
	public void writeToNBT(NBTTagCompound par1NBTTagCompound) {
		super.writeToNBT(par1NBTTagCompound);
		par1NBTTagCompound.setBoolean("Conquered", this.isConquered);
		par1NBTTagCompound.setInteger("FeatureID", this.feature.featureID);
		par1NBTTagCompound.setByteArray("Locks", this.lockBytes);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbttagcompound) {
		super.readFromNBT(nbttagcompound);
		this.isConquered = nbttagcompound.getBoolean("Conquered");
		this.feature = TFFeature.featureList[nbttagcompound.getInteger("FeatureID")];
		this.lockBytes = nbttagcompound.getByteArray("Locks");
	}

	public boolean isLocked(int lockIndex) {
		if (lockIndex < this.lockBytes.length) {

			TwilightForestMod.LOGGER.info("Checking locks for lockIndex " + lockIndex);

			for (int i = 0; i < this.lockBytes.length; i++) {
				TwilightForestMod.LOGGER.info("Lock " + i + " = " + this.lockBytes[i]);
			}

			return this.lockBytes[lockIndex] != 0;
		} else {

			TwilightForestMod.LOGGER.info("Current lock index, " + lockIndex + " is beyond array bounds " + this.lockBytes.length);


			return false;
		}
	}

}
