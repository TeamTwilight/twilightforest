package twilightforest.world;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ServerWorld;
import twilightforest.TFConfig;
import twilightforest.TFFeature;
import twilightforest.TwilightForestMod;

import javax.annotation.Nullable;

public class TFGenerationSettings /*extends GenerationSettings*/ {

	public static final int SEALEVEL = 31;
	public static final int CHUNKHEIGHT = 256; // more like world generation height
	public static final int MAXHEIGHT = 256; // actual max height

	@Nullable
	public static ChunkGeneratorTFBase getChunkGenerator(World world) {
		if (world instanceof ServerWorld) {
			ChunkGenerator chunkGenerator = ((ServerWorld) world).getChunkProvider().generator;
			return chunkGenerator instanceof ChunkGeneratorTFBase ? (ChunkGeneratorTFBase) chunkGenerator : null;
		}
		return null;
	}

	public static boolean isTwilightForest(World world) {
		return world.getDimensionKey().getLocation().toString().equals(TFConfig.COMMON_CONFIG.DIMENSION.twilightForestID.get());
	}

	public static boolean isProgressionEnforced(World world) {
		return world.getGameRules().getBoolean(TwilightForestMod.ENFORCED_PROGRESSION_RULE);
	}

	public static boolean isBiomeSafeFor(Biome biome, Entity entity) {
		/* FIXME
		if (biome instanceof TFBiomeBase && entity instanceof PlayerEntity) {
			return ((TFBiomeBase) biome).doesPlayerHaveRequiredAdvancements((PlayerEntity) entity);
		}
		return true;*/

		return true;
	}

	public static void markStructureConquered(World world, BlockPos pos, TFFeature feature) {
		ChunkGeneratorTFBase generator = getChunkGenerator(world);
		if (generator != null && TFFeature.getFeatureAt(pos.getX(), pos.getZ(), (ServerWorld) world) == feature) {
			//generator.setStructureConquered(pos, true);
		}
	}
}
