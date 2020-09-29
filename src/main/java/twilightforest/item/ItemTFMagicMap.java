package twilightforest.item;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SMapDataPacket;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.network.NetworkDirection;
import twilightforest.TFFeature;
import twilightforest.TFMagicMapData;
import twilightforest.biomes.TFBiomes;
import twilightforest.network.PacketMagicMap;
import twilightforest.network.TFPacketHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

// [VanillaCopy] super everything, but with appropriate redirections to our own datastructures. finer details noted
public class ItemTFMagicMap extends FilledMapItem {
	public static final String STR_ID = "magicmap";
	private static final Map<ResourceLocation, MapColorBrightness> BIOME_COLORS = new HashMap<>();

	protected ItemTFMagicMap(Properties props) {
		super(props);
	}

	private static class MapColorBrightness {
		public MaterialColor color;
		public int brightness;

		public MapColorBrightness(MaterialColor color, int brightness) {
			this.color = color;
			this.brightness = brightness;
		}

		public MapColorBrightness(MaterialColor color) {
			this.color = color;
			this.brightness = 1;
		}
	}

	public static ItemStack setupNewMap(World world, int worldX, int worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking) {
		ItemStack itemstack = new ItemStack(TFItems.magic_map.get());
		createMapData(itemstack, world, worldX, worldZ, scale, trackingPosition, unlimitedTracking, world.getDimensionKey());
		return itemstack;
	}

	@Nullable
	public static TFMagicMapData getData(ItemStack stack, World world) {
		return TFMagicMapData.getMagicMapData(world, getMapName(getMapId(stack)));
	}

	@Nullable
	@Override
	protected TFMagicMapData getCustomMapData(ItemStack stack, World world) {
		TFMagicMapData mapdata = getData(stack, world);
		if (mapdata == null && !world.isRemote) {
			mapdata = ItemTFMagicMap.createMapData(stack, world, world.getWorldInfo().getSpawnX(), world.getWorldInfo().getSpawnZ(), 3, false, false, world.getDimensionKey());
		}

		return mapdata;
	}

	private static TFMagicMapData createMapData(ItemStack stack, World world, int x, int z, int scale, boolean trackingPosition, boolean unlimitedTracking, RegistryKey<World> dimension) {
		int i = world.getNextMapId();
		TFMagicMapData mapdata = new TFMagicMapData(getMapName(i));
		mapdata.initData(x, z, scale, trackingPosition, unlimitedTracking, dimension);
		TFMagicMapData.registerMagicMapData(world, mapdata); // call our own register method
		stack.getOrCreateTag().putInt("map", i);
		return mapdata;
	}

	public static String getMapName(int id) {
		return STR_ID + "_" + id;
	}

	@Override
	public void updateMapData(World world, Entity viewer, MapData data) {
		if (world.getDimensionKey() == data.dimension && viewer instanceof PlayerEntity) {
			int biomesPerPixel = 4;
			int blocksPerPixel = 16; // don't even bother with the scale, just hardcode it
			int centerX = data.xCenter;
			int centerZ = data.zCenter;
			int viewerX = MathHelper.floor(viewer.getPosX() - (double) centerX) / blocksPerPixel + 64;
			int viewerZ = MathHelper.floor(viewer.getPosZ() - (double) centerZ) / blocksPerPixel + 64;
			int viewRadiusPixels = 512 / blocksPerPixel;

			// use the generation map, which is larger scale than the other biome map
			int startX = (centerX / blocksPerPixel - 64) * biomesPerPixel;
			int startZ = (centerZ / blocksPerPixel - 64) * biomesPerPixel;
//			Biome[] biomes = world.getBiomeAccess().getBiomesForGeneration((Biome[]) null, startX, startZ, 128 * biomesPerPixel, 128 * biomesPerPixel);
			Biome[] biomes = new Biome[128 * biomesPerPixel * 128 * biomesPerPixel];

			for (int xPixel = viewerX - viewRadiusPixels + 1; xPixel < viewerX + viewRadiusPixels; ++xPixel) {
				for (int zPixel = viewerZ - viewRadiusPixels - 1; zPixel < viewerZ + viewRadiusPixels; ++zPixel) {
					if (xPixel >= 0 && zPixel >= 0 && xPixel < 128 && zPixel < 128) {
						int xPixelDist = xPixel - viewerX;
						int zPixelDist = zPixel - viewerZ;
						boolean shouldFuzz = xPixelDist * xPixelDist + zPixelDist * zPixelDist > (viewRadiusPixels - 2) * (viewRadiusPixels - 2);

						Biome biome = biomes[xPixel * biomesPerPixel + zPixel * biomesPerPixel * 128 * biomesPerPixel];

						// make streams more visible
						Biome overBiome = biomes[xPixel * biomesPerPixel + zPixel * biomesPerPixel * 128 * biomesPerPixel + 1];
						Biome downBiome = biomes[xPixel * biomesPerPixel + (zPixel * biomesPerPixel + 1) * 128 * biomesPerPixel];
						/* FIXME
						if (overBiome == TFBiomes.stream.get() || downBiome == TFBiomes.stream.get()) {
							biome = TFBiomes.stream.get();
						}*/

						MapColorBrightness colorBrightness = this.getMapColorPerBiome(world, biome);

						MaterialColor mapcolor = colorBrightness.color;
						int brightness = colorBrightness.brightness;

						if (zPixel >= 0 && xPixelDist * xPixelDist + zPixelDist * zPixelDist < viewRadiusPixels * viewRadiusPixels && (!shouldFuzz || (xPixel + zPixel & 1) != 0)) {
							byte orgPixel = data.colors[xPixel + zPixel * 128];
							byte ourPixel = (byte) (mapcolor.colorIndex * 4 + brightness);

							if (orgPixel != ourPixel) {
								data.colors[xPixel + zPixel * 128] = ourPixel;
								data.updateMapData(xPixel, zPixel);
							}

							// look for TF features
							int worldX = (centerX / blocksPerPixel + xPixel - 64) * blocksPerPixel;
							int worldZ = (centerZ / blocksPerPixel + zPixel - 64) * blocksPerPixel;
							if (TFFeature.isInFeatureChunk(worldX, worldZ)) {
								byte mapX = (byte) ((worldX - centerX) / (float) blocksPerPixel * 2F);
								byte mapZ = (byte) ((worldZ - centerZ) / (float) blocksPerPixel * 2F);
								TFFeature feature = TFFeature.getFeatureAt(worldX, worldZ, (ServerWorld) world);
								TFMagicMapData tfData = (TFMagicMapData) data;
								tfData.tfDecorations.add(new TFMagicMapData.TFMapDecoration(feature.ordinal(), mapX, mapZ, (byte) 8));
								//TwilightForestMod.LOGGER.info("Found feature at {}, {}. Placing it on the map at {}, {}", worldX, worldZ, mapX, mapZ);
							}
						}
					}
				}
			}
		}
	}

	private MapColorBrightness getMapColorPerBiome(World world, Biome biome) {
		if (BIOME_COLORS.isEmpty()) {
			setupBiomeColors();
		}
		MapColorBrightness color = BIOME_COLORS.get(WorldGenRegistries.BIOME.getKey(biome));
		if (color != null) {
			return color;
		} else {
			//FIXME return new MapColorBrightness(biome.getSurfaceBuilderConfig().getTop().getMaterialColor(world, BlockPos.ZERO));
		}

		return color;
	}

	private static void setupBiomeColors() {
		/* FIXME arrrrgh!
		putBiomeColor(TFBiomes.twilightForest, new MapColorBrightness(MaterialColor.FOLIAGE, 1));
		putBiomeColor(TFBiomes.denseTwilightForest, new MapColorBrightness(MaterialColor.FOLIAGE, 0));
		putBiomeColor(TFBiomes.tfLake, new MapColorBrightness(MaterialColor.WATER, 3));
		putBiomeColor(TFBiomes.stream, new MapColorBrightness(MaterialColor.WATER, 1));
		putBiomeColor(TFBiomes.tfSwamp, new MapColorBrightness(MaterialColor.DIAMOND, 3));
		putBiomeColor(TFBiomes.fireSwamp, new MapColorBrightness(MaterialColor.NETHERRACK, 1));
		putBiomeColor(TFBiomes.clearing, new MapColorBrightness(MaterialColor.GRASS, 2));
		putBiomeColor(TFBiomes.oakSavanna, new MapColorBrightness(MaterialColor.GRASS, 0));
		putBiomeColor(TFBiomes.highlands, new MapColorBrightness(MaterialColor.DIRT, 0));
		putBiomeColor(TFBiomes.thornlands, new MapColorBrightness(MaterialColor.WOOD, 3));
		putBiomeColor(TFBiomes.highlandsCenter, new MapColorBrightness(MaterialColor.LIGHT_GRAY, 2));
		putBiomeColor(TFBiomes.fireflyForest, new MapColorBrightness(MaterialColor.EMERALD, 1));
		putBiomeColor(TFBiomes.darkForest, new MapColorBrightness(MaterialColor.GREEN, 3));
		putBiomeColor(TFBiomes.darkForestCenter, new MapColorBrightness(MaterialColor.ADOBE, 3));
		putBiomeColor(TFBiomes.snowy_forest, new MapColorBrightness(MaterialColor.SNOW, 1));
		putBiomeColor(TFBiomes.glacier, new MapColorBrightness(MaterialColor.ICE, 1));
		putBiomeColor(TFBiomes.mushrooms, new MapColorBrightness(MaterialColor.ADOBE, 0));
		putBiomeColor(TFBiomes.deepMushrooms, new MapColorBrightness(MaterialColor.PINK, 0));
		putBiomeColor(TFBiomes.enchantedForest, new MapColorBrightness(MaterialColor.LIME, 2));
		putBiomeColor(TFBiomes.spookyForest, new MapColorBrightness(MaterialColor.PURPLE, 0));*/
	}

	public static int getBiomeColor(Biome biome) {
		if (BIOME_COLORS.isEmpty()) {
			setupBiomeColors();
		}

		MapColorBrightness c = BIOME_COLORS.get(WorldGenRegistries.BIOME.getKey(biome));

		return c != null ? getMapColor(c) : 0xFF000000;
	}

	public static int getMapColor(MapColorBrightness mcb) {
		int i = 220;
		if (mcb.color.colorIndex == 3) {
			i = 135;
		}

		if (mcb.color.colorIndex == 2) {
			i = 255;
		}

		if (mcb.color.colorIndex == 1) {
			i = 220;
		}

		if (mcb.color.colorIndex == 0) {
			i = 180;
		}

		int j = (mcb.color.colorValue >> 16 & 255) * i / 255;
		int k = (mcb.color.colorValue >> 8 & 255) * i / 255;
		int l = (mcb.color.colorValue & 255) * i / 255;
		return -16777216 | l << 16 | k << 8 | j;
	}

	@Override
	public void onCreated(ItemStack stack, World world, PlayerEntity player) {
		// disable zooming
	}

	@Override
	@Nullable
	public IPacket<?> getUpdatePacket(ItemStack stack, World world, PlayerEntity player) {
		IPacket<?> p = super.getUpdatePacket(stack, world, player);
		if (p instanceof SMapDataPacket) {
			TFMagicMapData mapdata = getCustomMapData(stack, world);
			return TFPacketHandler.CHANNEL.toVanillaPacket(new PacketMagicMap(mapdata, (SMapDataPacket) p), NetworkDirection.PLAY_TO_CLIENT);
		} else {
			return p;
		}
	}
}
