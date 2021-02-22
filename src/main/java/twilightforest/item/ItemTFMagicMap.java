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
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.TFFeature;
import twilightforest.TFMagicMapData;
import twilightforest.worldgen.biomes.BiomeKeys;
import twilightforest.network.PacketMagicMap;
import twilightforest.network.TFPacketHandler;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

// [VanillaCopy] super everything, but with appropriate redirections to our own datastructures. finer details noted
// FIXME: Maps are empty. Anything could be the cause, so the comment sits here
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

	private static final Map<ChunkPos, Biome[]> CACHE = new HashMap<>();

	@Override
	public void updateMapData(World world, Entity viewer, MapData data) {
		if (world.getDimensionKey() == data.dimension && viewer instanceof PlayerEntity) {
			int biomesPerPixel = 4;
			int blocksPerPixel = 16; // don't even bother with the scale, just hardcode it
			int centerX = data.xCenter;
			int centerZ = data.zCenter;
			int viewerX = MathHelper.floor(viewer.getPosX() - centerX) / blocksPerPixel + 64;
			int viewerZ = MathHelper.floor(viewer.getPosZ() - centerZ) / blocksPerPixel + 64;
			int viewRadiusPixels = 512 / blocksPerPixel;

			int startX = (centerX / blocksPerPixel - 64) * biomesPerPixel;
			int startZ = (centerZ / blocksPerPixel - 64) * biomesPerPixel;
			Biome[] biomes = CACHE.computeIfAbsent(new ChunkPos(startX, startZ), pos -> {
				Biome[] array = new Biome[128 * biomesPerPixel * 128 * biomesPerPixel];
				for(int l = 0; l < 128 * biomesPerPixel; ++l) {
					for(int i1 = 0; i1 < 128 * biomesPerPixel; ++i1) {
						array[l * 128 * biomesPerPixel + i1] = world.getBiome(new BlockPos(startX * biomesPerPixel + i1 * biomesPerPixel, 0, startZ * biomesPerPixel + l * biomesPerPixel));
					}
				}
				return array;
			});

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
						biome = overBiome != null && BiomeKeys.STREAM.getLocation().equals(overBiome.getRegistryName()) ? overBiome : downBiome != null && BiomeKeys.STREAM.getLocation().equals(downBiome.getRegistryName()) ? downBiome : biome;

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
		if(biome == null)
			return new MapColorBrightness(MaterialColor.BLACK);
		ResourceLocation key = biome.getRegistryName();
			MapColorBrightness color = BIOME_COLORS.get(key);
			if (color != null) {
				return color;
			}
		return new MapColorBrightness(biome.getGenerationSettings().getSurfaceBuilderConfig().getTop().getMaterialColor(world, BlockPos.ZERO));
	}

	private static void setupBiomeColors() {
		putBiomeColor(BiomeKeys.FOREST, new MapColorBrightness(MaterialColor.FOLIAGE, 1));
		putBiomeColor(BiomeKeys.DENSE_FOREST, new MapColorBrightness(MaterialColor.FOLIAGE, 0));
		putBiomeColor(BiomeKeys.LAKE, new MapColorBrightness(MaterialColor.WATER, 3));
		putBiomeColor(BiomeKeys.STREAM, new MapColorBrightness(MaterialColor.WATER, 1));
		putBiomeColor(BiomeKeys.SWAMP, new MapColorBrightness(MaterialColor.DIAMOND, 3));
		putBiomeColor(BiomeKeys.FIRE_SWAMP, new MapColorBrightness(MaterialColor.NETHERRACK, 1));
		putBiomeColor(BiomeKeys.CLEARING, new MapColorBrightness(MaterialColor.GRASS, 2));
		putBiomeColor(BiomeKeys.OAK_SAVANNAH, new MapColorBrightness(MaterialColor.GRASS, 0));
		putBiomeColor(BiomeKeys.HIGHLANDS, new MapColorBrightness(MaterialColor.DIRT, 0));
		putBiomeColor(BiomeKeys.THORNLANDS, new MapColorBrightness(MaterialColor.WOOD, 3));
		putBiomeColor(BiomeKeys.FINAL_PLATEAU, new MapColorBrightness(MaterialColor.LIGHT_GRAY, 2));
		putBiomeColor(BiomeKeys.FIREFLY_FOREST, new MapColorBrightness(MaterialColor.EMERALD, 1));
		putBiomeColor(BiomeKeys.DARK_FOREST, new MapColorBrightness(MaterialColor.GREEN, 3));
		putBiomeColor(BiomeKeys.DARK_FOREST_CENTER, new MapColorBrightness(MaterialColor.ADOBE, 3));
		putBiomeColor(BiomeKeys.SNOWY_FOREST, new MapColorBrightness(MaterialColor.SNOW, 1));
		putBiomeColor(BiomeKeys.GLACIER, new MapColorBrightness(MaterialColor.ICE, 1));
		putBiomeColor(BiomeKeys.MUSHROOM_FOREST, new MapColorBrightness(MaterialColor.ADOBE, 0));
		putBiomeColor(BiomeKeys.DENSE_MUSHROOM_FOREST, new MapColorBrightness(MaterialColor.PINK, 0));
		putBiomeColor(BiomeKeys.ENCHANTED_FOREST, new MapColorBrightness(MaterialColor.CYAN, 2));
		putBiomeColor(BiomeKeys.SPOOKY_FOREST, new MapColorBrightness(MaterialColor.PURPLE, 0));
	}

	private static void putBiomeColor(RegistryKey<Biome> biome, MapColorBrightness color) {
		BIOME_COLORS.put(biome.getLocation(), color);
	}

	public static int getBiomeColor(Biome biome) {
		if (BIOME_COLORS.isEmpty()) {
			setupBiomeColors();
		}

		MapColorBrightness c = BIOME_COLORS.get(ForgeRegistries.BIOMES.getKey(biome));

		return c != null ? getMapColor(c) : 0xFF000000;
	}

	public static int getMapColor(MapColorBrightness mcb) {
		int i = 220;

		switch (mcb.color.colorIndex) {
			case 3:
				i = 135;
				break;
			case 2:
				i = 255;
				break;
			case 0:
				i = 180;
				break;
		}

		int j = (mcb.color.colorValue >> 16 & 255) * i / 255;
		int k = (mcb.color.colorValue >> 8 & 255) * i / 255;
		int l = (mcb.color.colorValue & 255) * i / 255;
		return 0xFF000000 | l << 16 | k << 8 | j;
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
