package twilightforest.world;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Callable;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.MapGenBase;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureStart;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;
import twilightforest.biomes.TFBiomeBase;
import twilightforest.biomes.TFBiomes;
import twilightforest.structures.hollowtree.StructureTFHollowTreeStart;


public class MapGenTFHollowTree extends MapGenBase {
	
    //public static final int SPAWN_CHANCE = 48;
    /**
     * Used to store a list of all structures that have been recursively generated. Used so that during recursive
     * generation, the structure generator can avoid generating structures that intersect ones that have already been
     * placed.
     */
    protected Map<Long, StructureStart> structureMap = new HashMap<Long, StructureStart>();

    @Override
    protected void recursiveGenerate(World worldIn, final int chunkX, final int chunkZ, int centerX, int centerZ, ChunkPrimer chunkPrimerIn) {
        if (!this.structureMap.containsKey(ChunkPos.asLong(chunkX, chunkZ))){
            this.rand.nextInt();

            try {
                if (this.canSpawnStructureAtCoords(chunkX, chunkZ)){
                    StructureStart structurestart = this.getStructureStart(chunkX, chunkZ);
                    this.structureMap.put(ChunkPos.asLong(chunkX, chunkZ), structurestart);
                }
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception preparing hollow tree");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Feature being prepared");
                crashreportcategory.addCrashSection("Is feature chunk", new Callable() {
                    private static final String __OBFID = "CL_00000506";
                    @Override
                    public String call() {
                        return MapGenTFHollowTree.this.canSpawnStructureAtCoords(chunkX, chunkZ) ? "True" : "False";
                    }
                });
                crashreportcategory.addCrashSection("Chunk location", String.format("%d,%d", new Object[] {Integer.valueOf(chunkX), Integer.valueOf(chunkZ)}));
                crashreportcategory.addCrashSection("Chunk pos hash", new Callable() {
                    private static final String __OBFID = "CL_00000507";
                    @Override
                    public String call() {
                        return String.valueOf(ChunkPos.asLong(chunkX, chunkZ));
                    }
                });
                crashreportcategory.addCrashSection("Structure type", new Callable() {
                    private static final String __OBFID = "CL_00000508";
                    @Override
                    public String call() {
                        return MapGenTFHollowTree.this.getClass().getCanonicalName();
                    }
                });
                throw new ReportedException(crashreport);
            }
        }
    }

    
	public boolean generateStructuresInChunk(World world, Random rand, int chunkX, int chunkZ) {
        int mapX = (chunkX << 4) + 8;
        int mapZ = (chunkZ << 4) + 8;
        boolean flag = false;

        Iterator<StructureStart> iterator = this.structureMap.values().iterator();

        while (iterator.hasNext())
        {
            StructureStart structurestart = iterator.next();

            if (structurestart.isSizeableStructure() && structurestart.getBoundingBox().intersectsWith(mapX, mapZ, mapX + 15, mapZ + 15))
            {
                structurestart.generateStructure(world, rand, new StructureBoundingBox(mapX, mapZ, mapX + 15, mapZ + 15));
                flag = true;
            }
        }

        return flag;
	}

	/** A list of all the biomes twilight oaks can spawn in. */
    private static final List<Biome> oakSpawnBiomes = Arrays.asList(
            TFBiomes.twilightForest,
            TFBiomes.denseTwilightForest,
            TFBiomes.mushrooms,
            TFBiomes.tfSwamp,
            TFBiomes.clearing,
            TFBiomes.oakSavanna,
            TFBiomes.fireflyForest,
            TFBiomes.deepMushrooms,
            TFBiomes.enchantedForest,
            TFBiomes.fireSwamp);

	private boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		return rand.nextInt(TwilightForestMod.twilightOakChance) == 0 && TFFeature.getNearestFeature(chunkX, chunkZ, world).areChunkDecorationsEnabled
				&&  this.world.getBiomeProvider().areBiomesViable(chunkX * 16 + 8, chunkZ * 16 + 8, 0, oakSpawnBiomes);
	}

	private StructureStart getStructureStart(int chunkX, int chunkZ) {
        return new StructureTFHollowTreeStart(world, rand, chunkX, chunkZ);
	}
	
}
