package twilightforest.world;

import java.util.Collection;
import java.util.Iterator;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.MapGenStructureData;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import twilightforest.TFFeature;
import twilightforest.structures.StructureTFComponent;
import twilightforest.structures.StructureTFMajorFeatureStart;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;


public class MapGenTFMajorFeature extends MapGenStructure {
	
    public MapGenTFMajorFeature() {}

	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		return TFFeature.getFeatureDirectlyAt(chunkX, chunkZ, world).isStructureEnabled;
	}

	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		// fix rand
        this.rand.setSeed(world.getSeed());
        long rand1 = this.rand.nextLong();
        long rand2 = this.rand.nextLong();
        long chunkXr1 = (long)(chunkX) * rand1;
        long chunkZr2 = (long)(chunkZ) * rand2;
        this.rand.setSeed(chunkXr1 ^ chunkZr2 ^ world.getSeed());
        this.rand.nextInt();
		
		return new StructureTFMajorFeatureStart(world, rand, chunkX, chunkZ);
	}

	@Override
    public String getStructureName()
    {
        return "TFFeature";
    }
	
    /**
     * Returns true if the structure generator has generated a structure located at the given position tuple.
     */
	public int getSpawnListIndexAt(BlockPos pos)
    {
    	int highestFoundIndex = -1;

	    for (StructureStart start : this.structureMap.values())
	    {
		    if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ()))
		    {

			    for (StructureComponent component : start.getComponents())
			    {
				    if (component != null && component.getBoundingBox() != null && component.getBoundingBox().isVecInside(pos))
				    {
					    if (component instanceof StructureTFComponent)
					    {
						    StructureTFComponent tfComponent = (StructureTFComponent) component;

						    //System.out.println("found a tfComponent at the specified coordinates.  It's a " + tfComponent + ", index = " + tfComponent.spawnListIndex);

						    if (tfComponent.spawnListIndex > highestFoundIndex)
						    {
							    highestFoundIndex = tfComponent.spawnListIndex;
						    }
					    } else
					    {
						    return 0;
					    }
				    }
			    }
		    }
	    }

        return highestFoundIndex;
    }

    /**
     * Get the structure bounding box, if any, at the specified position
     */
	public StructureBoundingBox getSBBAt(BlockPos pos) {
		StructureBoundingBox boxFound = null;

		for (StructureStart start : this.structureMap.values())
		{
			if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ()))
			{

				for (StructureComponent component : start.getComponents())
				{
					if (component.getBoundingBox().isVecInside(pos))
					{
						boxFound = component.getBoundingBox();
					}
				}
			}
		}

        return boxFound;
	}
	
	/**
	 * Is the block at the coordinates given a protected one?
	 */
	public boolean isBlockProtectedAt(BlockPos pos) {
		boolean blockProtected = false;

		for (StructureStart start : this.structureMap.values())
		{
			if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ()))
			{

				for (StructureComponent component : start.getComponents())
				{
					if (component.getBoundingBox().isVecInside(pos))
					{

						if (component instanceof StructureTFComponent)
						{
							StructureTFComponent tfComp = (StructureTFComponent) component;

							blockProtected = tfComp.isComponentProtected();

						} else
						{
							blockProtected = true;
						}

						// check if it's a twilight forest component, then check if it's protected
					}
				}
			}
		}

        return blockProtected;

	}

	/**
	 * Mark the structure at the specified position as defeated
	 * @return 
	 */
	@SuppressWarnings("unchecked")
	public void setStructureConquered(int mapX, int mapY, int mapZ, boolean flag) {

		for (StructureStart start : (Collection<StructureStart>)this.structureMap.values()) {
			if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(mapX, mapZ, mapX, mapZ)) {
				if (start instanceof StructureTFMajorFeatureStart) {
					
					StructureTFMajorFeatureStart featureStart =(StructureTFMajorFeatureStart)start;
					
					//System.out.println("The data says that the conquered flag is " + featureStart.isConquered);
					
					
					featureStart.isConquered = flag;
					
					MapGenStructureData data = ObfuscationReflectionHelper.getPrivateValue(MapGenStructure.class, this, "field_143029_e");
					
			        data.writeInstance(featureStart.writeStructureComponentsToNBT(start.getChunkPosX(), start.getChunkPosZ()), start.getChunkPosX(), start.getChunkPosZ());
					//System.out.println("Writing data");
					
					data.setDirty(true);
					
					//System.out.println("Set the data as dirty");
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public boolean isStructureConquered(BlockPos pos) {
		
		boolean conquered = false;
		
		for (StructureStart start : (Collection<StructureStart>)this.structureMap.values()) {
			if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ())) {
				if (start instanceof StructureTFMajorFeatureStart) {
					
					conquered = ((StructureTFMajorFeatureStart)start).isConquered;
				}
			}
		}
		
		return conquered;
	}

	/**
	 * Check the lock at the specified lockIndex for the structure at the specified coords
	 */
	@SuppressWarnings("unchecked")
	public boolean isStructureLocked(BlockPos pos, int lockIndex) {
		
		boolean locked = false;
		
		for (StructureStart start : (Collection<StructureStart>)this.structureMap.values()) {
			if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ())) {
				if (start instanceof StructureTFMajorFeatureStart) {
					
					locked = ((StructureTFMajorFeatureStart)start).isLocked(lockIndex);
				}
			}
		}
		
		return locked;
	}

	/**
	 * Do the specified x & z coordinates intersect the full structure?
	 */
	@SuppressWarnings("unchecked")
	public boolean isBlockInFullStructure(int mapX, int mapZ) {
		for (StructureStart start : (Collection<StructureStart>)this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(mapX, mapZ, mapX, mapZ)) {
            	return true;
            }
		}
		return false;
	}
	
	/**
	 * Are the specified x & z coordinates close to a full structure?
	 */
	@SuppressWarnings("unchecked")
	public boolean isBlockNearFullStructure(int mapX, int mapZ, int range) {
        StructureBoundingBox rangeBB = new StructureBoundingBox(mapX - range, mapZ - range, mapX + range, mapZ + range);
		for (StructureStart start : (Collection<StructureStart>)this.structureMap.values()) {
			if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(rangeBB)) {
            	return true;
            }
		}
		return false;
	}
	
	/**
	 * Get full structure bounding box at the specified x, z coordinates.
	 */
	@SuppressWarnings("unchecked")
	public StructureBoundingBox getFullSBBAt(int mapX, int mapZ) {
		for (StructureStart start : (Collection<StructureStart>)this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(mapX, mapZ, mapX, mapZ)) {
            	return start.getBoundingBox();
            }
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public StructureBoundingBox getFullSBBNear(int mapX, int mapZ, int range) {
        StructureBoundingBox rangeBB = new StructureBoundingBox(mapX - range, mapZ - range, mapX + range, mapZ + range);
		for (StructureStart start : (Collection<StructureStart>)this.structureMap.values()) {
			if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(rangeBB)) {
            	return start.getBoundingBox();
            }
		}
		return null;

	}
}
