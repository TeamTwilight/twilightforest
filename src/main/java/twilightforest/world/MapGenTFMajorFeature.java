package twilightforest.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.structures.StructureTFComponent;
import twilightforest.structures.start.StructureStartTFAbstract;

import javax.annotation.Nullable;

import static twilightforest.TFFeature.NOTHING;

public class MapGenTFMajorFeature extends MapGenStructure {
    private final TFFeature FEATURE;

    public MapGenTFMajorFeature() {
        this.FEATURE = NOTHING;
    }

    public MapGenTFMajorFeature(TFFeature feature) {
        this.FEATURE = feature;
    }

    @SuppressWarnings("ConstantConditions")
    public TFFeature getFeature() {
        return FEATURE != null ? FEATURE : NOTHING;
    }

    @Override
    public String getStructureName() {
        return this.getFeature().name.toLowerCase();
    }

    @Nullable
    @Override
    public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored) {
        this.world = worldIn;
        return findNearestStructurePosBySpacing(worldIn, this, pos, 20, 11, 10387313, true, 100, findUnexplored);
    }

    @Override
    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
        return FEATURE.isStructureEnabled && TFFeature.getFeatureDirectlyAt(chunkX, chunkZ, world) == FEATURE;
    }

    @Override
    protected StructureStart getStructureStart(int chunkX, int chunkZ) {
        // fix rand
        this.rand.setSeed(world.getSeed());
        long rand1 = this.rand.nextLong();
        long rand2 = this.rand.nextLong();
        long chunkXr1 = (long) (chunkX) * rand1;
        long chunkZr2 = (long) (chunkZ) * rand2;
        this.rand.setSeed(chunkXr1 ^ chunkZr2 ^ world.getSeed());
        this.rand.nextInt();

        //TFFeature feature = TFFeature.getFeatureDirectlyAt(chunkX, chunkZ, world);

        TwilightForestMod.LOGGER.info(this.FEATURE + " @ chunk {} {}", chunkX, chunkZ);

        return this.getFeature().provideStructureStart(world, rand, chunkX, chunkZ);
    }

    /**
     * Returns true if the structure generator has generated a structure located at the given position tuple.
     */
    public int getSpawnListIndexAt(BlockPos pos) {
        int highestFoundIndex = -1;

        for (StructureStart start : this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ())) {

                for (StructureComponent component : start.getComponents()) {
                    if (component.getBoundingBox().isVecInside(pos)) {
                        if (component instanceof StructureTFComponent) {
                            StructureTFComponent tfComponent = (StructureTFComponent) component;

                            if (tfComponent.spawnListIndex > highestFoundIndex) {
                                highestFoundIndex = tfComponent.spawnListIndex;
                            }
                        } else {
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
    @Nullable
    public StructureBoundingBox getSBBAt(BlockPos pos) {
        StructureBoundingBox boxFound = null;

        for (StructureStart start : this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ())) {

                for (StructureComponent component : start.getComponents()) {
                    if (component.getBoundingBox().isVecInside(pos)) {
                        boxFound = component.getBoundingBox();
                    }
                }
            }
        }

        return boxFound;
    }

    public TFFeature getFeatureAt(BlockPos pos) {
        for (StructureStart start : this.structureMap.values())
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ()))
                for (StructureComponent component : start.getComponents())
                    if (component.getBoundingBox().isVecInside(pos))
                        if (component instanceof StructureTFComponent)
                            return ((StructureTFComponent) component).getFeatureType();
        return NOTHING;
    }

    /**
     * Is the block at the coordinates given a protected one?
     */
    public boolean isBlockProtectedAt(BlockPos pos) {
        boolean blockProtected = false;

        for (StructureStart start : this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ())) {

                for (StructureComponent component : start.getComponents()) {
                    if (component.getBoundingBox().isVecInside(pos)) {

                        if (component instanceof StructureTFComponent) {
                            StructureTFComponent tfComp = (StructureTFComponent) component;

                            blockProtected = tfComp.isComponentProtected();

                        } else {
                            blockProtected = true;
                        }

                        // check if it's a twilight forest component, then check if it's protected
                    }
                }
            }
        }

        return blockProtected;

    }

    public void setStructureConquered(int mapX, int mapY, int mapZ, boolean flag) {
        for (StructureStart start : this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(mapX, mapZ, mapX, mapZ)) {
                if (start instanceof StructureStartTFAbstract) {
                    StructureStartTFAbstract featureStart = (StructureStartTFAbstract) start;
                    featureStart.isConquered = flag;
                    this.structureData.writeInstance(featureStart.writeStructureComponentsToNBT(start.getChunkPosX(), start.getChunkPosZ()), start.getChunkPosX(), start.getChunkPosZ());
                    this.structureData.setDirty(true);
                }
            }
        }
    }

    public boolean isStructureConquered(BlockPos pos) {
        boolean conquered = false;

        for (StructureStart start : this.structureMap.values())
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ()))
                if (start instanceof StructureStartTFAbstract)
                    conquered = ((StructureStartTFAbstract) start).isConquered;

        return conquered;
    }

    /**
     * Check the lock at the specified lockIndex for the structure at the specified coords
     */
    public boolean isStructureLocked(BlockPos pos, int lockIndex) {
        boolean locked = false;

        for (StructureStart start : this.structureMap.values())
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ()))
                if (start instanceof StructureStartTFAbstract)
                    locked = ((StructureStartTFAbstract) start).isLocked(lockIndex);

        return locked;
    }

    /**
     * Do the specified x & z coordinates intersect the full structure?
     */
    public boolean isBlockInFullStructure(int mapX, int mapZ) {
        for (StructureStart start : this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(mapX, mapZ, mapX, mapZ)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Are the specified x & z coordinates close to a full structure?
     */
    public boolean isBlockNearFullStructure(int mapX, int mapZ, int range) {
        StructureBoundingBox rangeBB = new StructureBoundingBox(mapX - range, mapZ - range, mapX + range, mapZ + range);
        for (StructureStart start : this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(rangeBB)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Get full structure bounding box at the specified x, z coordinates.
     */
    @Nullable
    public StructureBoundingBox getFullSBBAt(int mapX, int mapZ) {
        for (StructureStart start : this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(mapX, mapZ, mapX, mapZ)) {
                return start.getBoundingBox();
            }
        }
        return null;
    }

    @Nullable
    public StructureBoundingBox getFullSBBNear(int mapX, int mapZ, int range) {
        StructureBoundingBox rangeBB = new StructureBoundingBox(mapX - range, mapZ - range, mapX + range, mapZ + range);
        for (StructureStart start : this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(rangeBB)) {
                return start.getBoundingBox();
            }
        }
        return null;
    }
}
