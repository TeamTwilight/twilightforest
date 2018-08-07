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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class MapGenTFHollowTree extends MapGenStructure {

	//public static final int SPAWN_CHANCE = 48;

	private static final List<Supplier<Biome>> oakSpawnBiomes = Arrays.asList(
			() -> TFBiomes.twilightForest,
			() -> TFBiomes.denseTwilightForest,
			() -> TFBiomes.mushrooms,
			() -> TFBiomes.tfSwamp,
			() -> TFBiomes.clearing,
			() -> TFBiomes.oakSavanna,
			() -> TFBiomes.fireflyForest,
			() -> TFBiomes.deepMushrooms,
			() -> TFBiomes.enchantedForest,
			() -> TFBiomes.fireSwamp
	);

	@Override
	public String getStructureName() {
		return "TFHollowTree";
	}

	@Nullable
	@Override
	public BlockPos getNearestStructurePos(World worldIn, BlockPos pos, boolean findUnexplored) {
		this.world = worldIn;
		return findNearestStructurePosBySpacing(worldIn, this, pos, 20, 11, 10387313, true, 100, findUnexplored);
	}

	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		return rand.nextInt(TFConfig.performance.twilightOakChance) == 0
				&& TFFeature.getNearestFeature(chunkX, chunkZ, world).areChunkDecorationsEnabled
				&& this.world.getBiomeProvider().areBiomesViable(chunkX * 16 + 8, chunkZ * 16 + 8, 0, getCurrentSpawnBiomes());
	}

	@Nonnull
	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new StructureTFHollowTreeStart(world, rand, chunkX, chunkZ);
	}

	private static List<Biome> getCurrentSpawnBiomes() {
		List<Biome> biomes = new ArrayList<>(oakSpawnBiomes.size());
		for (Supplier<Biome> biomeSupplier : oakSpawnBiomes) {
			biomes.add(biomeSupplier.get());
		}
		return biomes;
	}
}
