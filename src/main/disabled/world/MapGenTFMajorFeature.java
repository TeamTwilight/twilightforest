package twilightforest.world;

import com.google.common.base.Predicates;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructurePiece;
import net.minecraft.world.gen.feature.structure.StructureStart;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.advancements.TFAdvancements;
import twilightforest.structures.StructureTFComponent;
import twilightforest.structures.start.StructureStartTFFeatureAbstract;
import twilightforest.util.StructureBoundingBoxUtils;

import javax.annotation.Nullable;

import java.util.Collections;
import java.util.List;

import static twilightforest.TFFeature.NOTHING;

public class MapGenTFMajorFeature<T extends NoFeatureConfig> extends Structure<T> {

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
    public BlockPos findNearest(World worldIn, ChunkGenerator generator, BlockPos pos, int p_211405_4_, boolean findUnexplored) {
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

        TwilightForestMod.LOGGER.debug("{} @ chunk [{}, {}]", this.FEATURE, chunkX, chunkZ);

        return this.getFeature().provideStructureStart(world, rand, chunkX, chunkZ);
    }

	/**
     * Returns true if the structure generator has generated a structure located at the given position tuple.
     */
    public int getSpawnListIndexAt(BlockPos pos) {
        int highestFoundIndex = -1;

        for (StructureStart start : this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ())) {

                for (StructurePiece component : start.getComponents()) {
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

    // TODO: look at combining duplicate logic here
    @Nullable
    public List<Biome.SpawnListEntry> getPossibleCreatures(EntityClassification creatureType, BlockPos pos) {

        // if the feature is already conquered, no hostile spawns
        if (creatureType == EntityClassification.MONSTER && isStructureConquered(pos)) {
            return Collections.emptyList();
        }

        // check the precise coords.
        int spawnListIndex = getSpawnListIndexAt(pos);
        if (spawnListIndex >= 0) {
            return getFeature().getSpawnableList(creatureType, spawnListIndex);
        }

        return null;
    }

    /**
     * Get the structure bounding box, if any, at the specified position
     */
    @Nullable
    public MutableBoundingBox getSBBAt(BlockPos pos) {
        MutableBoundingBox boxFound = null;

        for (StructureStart start : this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ())) {

                for (StructurePiece component : start.getComponents()) {
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
                for (StructurePiece component : start.getComponents())
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

                for (StructurePiece component : start.getComponents()) {
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

    public void setStructureConquered(BlockPos pos, boolean flag) {
        for (StructureStart start : this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ())) {
                if (start instanceof StructureStartTFFeatureAbstract) {
                    StructureStartTFFeatureAbstract featureStart = (StructureStartTFFeatureAbstract) start;
                    featureStart.isConquered = flag;
                    this.structureData.writeInstance(featureStart.writeStructureComponentsToNBT(start.getChunkPosX(), start.getChunkPosZ()), start.getChunkPosX(), start.getChunkPosZ());
                    this.structureData.setDirty(true);
                    if (flag) {
                        for (ServerPlayerEntity player : getPlayersInsideStructure(start)) {
                            TFAdvancements.STRUCTURE_CLEARED.trigger(player, FEATURE.name);
                        }
                    }
                }
            }
        }
    }

    private List<ServerPlayerEntity> getPlayersInsideStructure(StructureStart start) {
        return world.getPlayers(ServerPlayerEntity.class, Predicates.and(EntitySelectors.IS_ALIVE,
                p -> p.getBoundingBox().intersects(StructureBoundingBoxUtils.toAABB(start.getBoundingBox()))));
    }

    public boolean isStructureConquered(BlockPos pos) {
        boolean conquered = false;

        for (StructureStart start : this.structureMap.values())
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ()))
                if (start instanceof StructureStartTFFeatureAbstract)
                    conquered = ((StructureStartTFFeatureAbstract) start).isConquered;

        return conquered;
    }

    /**
     * Check the lock at the specified lockIndex for the structure at the specified coords
     */
    public boolean isStructureLocked(BlockPos pos, int lockIndex) {
        boolean locked = false;

        for (StructureStart start : this.structureMap.values())
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(pos.getX(), pos.getZ(), pos.getX(), pos.getZ()))
                if (start instanceof StructureStartTFFeatureAbstract)
                    locked = ((StructureStartTFFeatureAbstract) start).isLocked(lockIndex);

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
        MutableBoundingBox rangeBB = new MutableBoundingBox(mapX - range, mapZ - range, mapX + range, mapZ + range);
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
    public MutableBoundingBox getFullSBBAt(int mapX, int mapZ) {
        for (StructureStart start : this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(mapX, mapZ, mapX, mapZ)) {
                return start.getBoundingBox();
            }
        }
        return null;
    }

    @Nullable
    public MutableBoundingBox getFullSBBNear(int mapX, int mapZ, int range) {
        MutableBoundingBox rangeBB = new MutableBoundingBox(mapX - range, mapZ - range, mapX + range, mapZ + range);
        for (StructureStart start : this.structureMap.values()) {
            if (start.isSizeableStructure() && start.getBoundingBox().intersectsWith(rangeBB)) {
                return start.getBoundingBox();
            }
        }
        return null;
    }
}
