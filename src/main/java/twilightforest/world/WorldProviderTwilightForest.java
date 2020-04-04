package twilightforest.world;

import net.minecraft.client.audio.MusicTicker;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import twilightforest.TFConfig;
import twilightforest.TwilightForestMod;
import twilightforest.biomes.TFBiomes;
import twilightforest.client.TFClientProxy;
import twilightforest.client.renderer.TFSkyRenderer;
import twilightforest.client.renderer.TFWeatherRenderer;

import javax.annotation.Nullable;

/**
 * @author Ben
 */
public class WorldProviderTwilightForest extends OverworldDimension { //TODO: Should we be extending OverworldDimension?

	private static final String SEED_KEY = "CustomSeed";
	private static final String SKYLIGHT_KEY = "HasSkylight";

	private static volatile boolean skylightEnabled = true;

	private long seed;

	public static void syncFromConfig() {
		skylightEnabled = TFConfig.COMMON_CONFIG.PERFORMANCE.enableSkylight.get();
	}

	public static void setSkylightEnabled(boolean enabled) {
		skylightEnabled = enabled;
	}

	public static boolean isSkylightEnabled(CompoundNBT data) {
		return data.contains(SKYLIGHT_KEY, Constants.NBT.TAG_BYTE) ? data.getBoolean(SKYLIGHT_KEY) : skylightEnabled;
	}

	public WorldProviderTwilightForest(World world, DimensionType type) {
		super(world, type);
	}

	/* TODO Breaking change. Uncomment for 1.13.
	// TODO: Actually, we don't control this anymore
	Reason for adding ID is if we want multiple TF worlds for servers in future.

	@Override
	public String getSaveFolder() {
		return "twilightforest" + getDimension();
	}*/

	@Nullable
	@Override
	@OnlyIn(Dist.CLIENT)
	public MusicTicker.MusicType getMusicType() {
		return TFClientProxy.TFMUSICTYPE;
	}

	@Override
	public float[] calcSunriseSunsetColors(float celestialAngle, float f1) {
		return null;
	}

	@Override
	public Vec3d getFogColor(float f, float f1) {
		float bright = MathHelper.cos(0.25f * 3.141593F * 2.0F) * 2.0F + 0.5F;
		if (bright < 0.0F) {
			bright = 0.0F;
		}
		if (bright > 1.0F) {
			bright = 1.0F;
		}
		float red = 0.7529412F; // 192
		float green = 1.0F; // 255
		float blue = 0.8470588F; // 216
		red *= bright * 0.94F + 0.06F;
		green *= bright * 0.94F + 0.06F;
		blue *= bright * 0.91F + 0.09F;
		return new Vec3d(red, green, blue);
	}

	// Pin the celestial angle at night/evening so things that use it see night
	@Override
	public float calculateCelestialAngle(long worldTime, float partialTicks) {
		return 0.225f;
	}

	@Override
	public void init() {
		CompoundNBT data = TFWorld.getDimensionData(world);
		seed = data.contains(SEED_KEY, Constants.NBT.TAG_LONG) ? data.getLong(SEED_KEY) : loadSeed();
		hasSkyLight = isSkylightEnabled(data);
		biomeProvider = new TFBiomeProvider(world);
	}

	@Override
	protected void generateLightBrightnessTable() {
		float f = this.hasSkyLight ? 0.0F : 0.1F;
		for (int i = 0; i <= 15; ++i) {
			float f1 = 1.0F - (float)i / 15.0F;
			this.lightBrightnessTable[i] = (1.0F - f1) / (f1 * 3.0F + 1.0F) * (1.0F - f) + f;
		}
	}

	@Override
	public ChunkGenerator<? extends GenerationSettings> createChunkGenerator() {
		return TFConfig.COMMON_CONFIG.DIMENSION.skylightForest.get()
				? new ChunkGeneratorTwilightVoid(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled())
				: new ChunkGeneratorTwilightForest(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled());
	}

	/**
	 * This seems to be a function checking whether we have an ocean.
	 */
	@OnlyIn(Dist.CLIENT)
	@Override
	public boolean isSkyColored() {
		return false;
	}

	@Override
	public int getAverageGroundLevel() {
		return 30;
	}

	@Override
	public double getVoidFogYFactor() {
		// allow for terrain squashing
		return super.getVoidFogYFactor() * 2.0;
	}

	@Override
	public boolean canRespawnHere() {
		// lie about this until the world is initialized
		// otherwise the server will try to generate enough terrain for a spawn point and that's annoying
		return world.getWorldInfo().isInitialized();
	}

	@Override
	public DimensionType getDimensionType() {
		return TwilightForestMod.dimType;
	}

	@Override
	public boolean isDaytime() {
		return false;
	}

	@Override
	public boolean shouldMapSpin(String entityName, double x, double z, double rotation) {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
		// TODO Maybe in the future we can get the return of sky color by biome?
		return new Vec3d(32 / 256.0, 34 / 256.0, 74 / 256.0);
	}

	@Override
	public void getLightmapColors(float partialTicks, float sunBrightness, float skyLight, float blockLight, float[] colors) {
		final float r = 64f / 255f, g = 85f / 255f, b = 72f / 255f;
		if (!hasSkyLight) {
			colors[0] = r + blockLight * (1.0f - r);
			colors[1] = g + blockLight * (1.0f - g);
			colors[2] = b + blockLight * (1.0f - b);
		}
	}

	//TODO: Move to Sky Renderer
//	@Override
//	@OnlyIn(Dist.CLIENT)
//	public float getStarBrightness(float partialTicks) {
//		return 1.0F;
//	}

	@Override
	public double getHorizon() {
		return TFWorld.SEALEVEL;
	}

	@Override
	public Biome getBiomeForCoords(BlockPos pos) {
		Biome biome = super.getBiomeForCoords(pos);
		if (biome == null) {
			biome = TFBiomes.twilightForest;
		}
		return biome;
	}

	/**
	 * If there is a specific twilight forest seed set, use that.  Otherwise use the world seed.
	 */
	@Override
	public long getSeed() {
		return seed == 0L ? super.getSeed() : seed;
	}

	private long loadSeed() {
		String seed = TFConfig.COMMON_CONFIG.DIMENSION.twilightForestSeed.get();
		if (seed != null && !seed.isEmpty()) {
			try {
				return Long.parseLong(seed);
			} catch (NumberFormatException e) {
				return seed.hashCode();
			}
		}
		return 0L;
	}

	@Override
	public void onWorldSave() {
		CompoundNBT data = new CompoundNBT();
		data.setLong(SEED_KEY, seed);
		// TODO: decide on persisting this
		//data.putBoolean(SKYLIGHT_KEY, hasSkyLight);
		TFWorld.setDimensionData(world, data);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public IRenderHandler getSkyRenderer() {
		if (super.getSkyRenderer() == null) {
			this.setSkyRenderer(new TFSkyRenderer());
		}
		return super.getSkyRenderer();
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public IRenderHandler getWeatherRenderer() {
		if (super.getWeatherRenderer() == null) {
			this.setWeatherRenderer(new TFWeatherRenderer());
		}
		return super.getWeatherRenderer();
	}

	// no OnlyIn
	@Override
	public float getCloudHeight() {
		return TFConfig.COMMON_CONFIG.DIMENSION.skylightForest.get() ? -1F : 161F;
	}
}
