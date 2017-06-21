package twilightforest.world;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenStructure;
import net.minecraft.world.gen.structure.StructureStart;
import twilightforest.TFConfig;
import twilightforest.TFFeature;
import twilightforest.biomes.TFBiomes;
import twilightforest.structures.hollowtree.StructureTFHollowTreeStart;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;


public class MapGenTFHollowTree extends MapGenStructure {
	//public static final int SPAWN_CHANCE = 48;

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

	@Override
	public String getStructureName() {
		return "TFHollowTree";
	}

	@Nullable
	@Override
	public BlockPos getClosestStrongholdPos(World worldIn, BlockPos pos, boolean findUnexplored) {
		// todo 1.11
		return null;
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		return rand.nextInt(TFConfig.performance.twilightOakChance) == 0
				&& TFFeature.getNearestFeature(chunkX, chunkZ, world).areChunkDecorationsEnabled
				&& this.world.getBiomeProvider().areBiomesViable(chunkX * 16 + 8, chunkZ * 16 + 8, 0, oakSpawnBiomes);
	}

	@Nonnull
	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new StructureTFHollowTreeStart(world, rand, chunkX, chunkZ);
	}
}
