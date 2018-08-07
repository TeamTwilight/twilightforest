/**
 *
 */
package twilightforest.world;

import net.minecraft.client.audio.MusicTicker;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProviderSurface;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.client.IRenderHandler;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
public class WorldProviderTwilightForest extends WorldProviderSurface {

	private static final String SEED_KEY = "CustomSeed";

	private long seed;

	public WorldProviderTwilightForest() {
		setDimension(TFConfig.dimension.dimensionID);
	}

	@Nullable
	@Override
	@SideOnly(Side.CLIENT)
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
		float red = 0.7529412F;
		float green = 1.0F;
		float blue = 0.8470588F;
		red *= bright * 0.94F + 0.06F;
		green *= bright * 0.94F + 0.06F;
		blue *= bright * 0.91F + 0.09F;
		return new Vec3d(red, green, blue);
	}

	// Pin the celestial angle at night/evening so things that use it see night
	@Override
	public float calculateCelestialAngle(long par1, float par3) {
		return 0.225f;
	}

	@Override
	public void init() {
		super.init();
		this.biomeProvider = new TFBiomeProvider(world);
		NBTTagCompound data = world.getWorldInfo().getDimensionData(TFConfig.dimension.dimensionID);
		seed = data.hasKey(SEED_KEY, Constants.NBT.TAG_LONG) ? data.getLong(SEED_KEY) : loadSeed();
	}

	@Override
	public IChunkGenerator createChunkGenerator() {
		return TFConfig.dimension.skylightForest
				? new ChunkGeneratorTwilightVoid(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled())
				: new ChunkGeneratorTwilightForest(world, world.getSeed(), world.getWorldInfo().isMapFeaturesEnabled());
	}

	/**
	 * This seems to be a function checking whether we have an ocean.
	 */
	@SideOnly(Side.CLIENT)
	@Override
	public boolean isSkyColored() {
		return false;
	}

	@Override
	public int getAverageGroundLevel() {
		return 30;
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
	@SideOnly(Side.CLIENT)
	public Vec3d getSkyColor(Entity cameraEntity, float partialTicks) {
		// TODO Maybe in the future we can get the return of sky color by biome?
		return new Vec3d(32 / 256.0, 34 / 256.0, 74 / 256.0);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public float getStarBrightness(float par1) {
		return 1.0F;
	}

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
		String seed = TFConfig.dimension.twilightForestSeed;
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
		NBTTagCompound data = new NBTTagCompound();
		data.setLong(SEED_KEY, seed);
		world.getWorldInfo().setDimensionData(TFConfig.dimension.dimensionID, data);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getSkyRenderer() {
		if (super.getSkyRenderer() == null) {
			this.setSkyRenderer(new TFSkyRenderer());
		}

		return super.getSkyRenderer();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IRenderHandler getWeatherRenderer() {
		if (super.getWeatherRenderer() == null) {
			this.setWeatherRenderer(new TFWeatherRenderer());
		}

		return super.getWeatherRenderer();
	}

	// no sideonly
	@Override
	public float getCloudHeight() {
		return TFConfig.dimension.skylightForest ? -1F : 161F;
	}
}
