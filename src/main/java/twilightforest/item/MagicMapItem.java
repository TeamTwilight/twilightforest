package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundMapItemDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.fmllegacy.network.NetworkDirection;
import net.minecraftforge.registries.ForgeRegistries;
import twilightforest.world.registration.TFFeature;
import twilightforest.TFMagicMapData;
import twilightforest.network.MagicMapPacket;
import twilightforest.network.TFPacketHandler;
import twilightforest.world.registration.TFGenerationSettings;
import twilightforest.world.registration.biomes.BiomeKeys;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// [VanillaCopy] super everything, but with appropriate redirections to our own datastructures. finer details noted

public class MagicMapItem extends MapItem {

	public static final String STR_ID = "magicmap";
	private static final Map<ResourceLocation, MapColorBrightness> BIOME_COLORS = new HashMap<>();

	protected MagicMapItem(Properties props) {
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

	public static ItemStack setupNewMap(Level world, int worldX, int worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking) {
		ItemStack itemstack = new ItemStack(TFItems.FILLED_MAGIC_MAP.get());
		createMapData(itemstack, world, worldX, worldZ, scale, trackingPosition, unlimitedTracking, world.dimension());
		return itemstack;
	}

	@Nullable
	public static TFMagicMapData getData(ItemStack stack, Level world) {
		Integer id = getMapId(stack);
		return id == null ? null : TFMagicMapData.getMagicMapData(world, getMapName(id));
	}

	@Nullable
	@Override
	protected TFMagicMapData getCustomMapData(ItemStack stack, Level world) {
		TFMagicMapData mapdata = getData(stack, world);
		if (mapdata == null && !world.isClientSide) {
			mapdata = MagicMapItem.createMapData(stack, world, world.getLevelData().getXSpawn(), world.getLevelData().getZSpawn(), 3, false, false, world.dimension());
		}

		return mapdata;
	}

	private static TFMagicMapData createMapData(ItemStack stack, Level world, int x, int z, int scale, boolean trackingPosition, boolean unlimitedTracking, ResourceKey<Level> dimension) {
		int i = world.getFreeMapId();

		// magic maps are offset by 1024 from normal maps so that 0,0 is in the middle of the map containing those coords
		int mapSize = 128 * (1 << scale);
		int roundX = (int) Math.round((double) x / mapSize);
		int roundZ = (int) Math.round((double) z / mapSize);
		int scaledX = roundX * mapSize;
		int scaledZ = roundZ * mapSize;

		TFMagicMapData mapdata = new TFMagicMapData(scaledX, scaledZ, (byte)scale, trackingPosition, unlimitedTracking, false, dimension);
		TFMagicMapData.registerMagicMapData(world, mapdata, getMapName(i)); // call our own register method
		stack.getOrCreateTag().putInt("map", i);
		return mapdata;
	}

	public static String getMapName(int id) {
		return STR_ID + "_" + id;
	}

	private static final Map<ChunkPos, Biome[]> CACHE = new HashMap<>();

	@Override
	public void update(Level world, Entity viewer, MapItemSavedData data) {
		if (world.dimension() == data.dimension && viewer instanceof Player && world instanceof ServerLevel serverLevel && TFGenerationSettings.usesTwilightChunkGenerator(serverLevel)) {
			int biomesPerPixel = 4;
			int blocksPerPixel = 16; // don't even bother with the scale, just hardcode it
			int centerX = data.x;
			int centerZ = data.z;
			int viewerX = Mth.floor(viewer.getX() - centerX) / blocksPerPixel + 64;
			int viewerZ = Mth.floor(viewer.getZ() - centerZ) / blocksPerPixel + 64;
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
						biome = overBiome != null && BiomeKeys.STREAM.location().equals(overBiome.getRegistryName()) ? overBiome : downBiome != null && BiomeKeys.STREAM.location().equals(downBiome.getRegistryName()) ? downBiome : biome;

						MapColorBrightness colorBrightness = this.getMapColorPerBiome(world, biome);

						MaterialColor mapcolor = colorBrightness.color;
						int brightness = colorBrightness.brightness;

						if (zPixel >= 0 && xPixelDist * xPixelDist + zPixelDist * zPixelDist < viewRadiusPixels * viewRadiusPixels && (!shouldFuzz || (xPixel + zPixel & 1) != 0)) {
							byte orgPixel = data.colors[xPixel + zPixel * 128];
							byte ourPixel = (byte) (mapcolor.id * 4 + brightness);

							if (orgPixel != ourPixel) {
								data.setColor(xPixel, zPixel, ourPixel);
								data.setDirty();
							}

							// look for TF features
							int worldX = (centerX / blocksPerPixel + xPixel - 64) * blocksPerPixel;
							int worldZ = (centerZ / blocksPerPixel + zPixel - 64) * blocksPerPixel;
							if (TFFeature.isInFeatureChunk(worldX, worldZ)) {
								byte mapX = (byte) ((worldX - centerX) / (float) blocksPerPixel * 2F);
								byte mapZ = (byte) ((worldZ - centerZ) / (float) blocksPerPixel * 2F);
								TFFeature feature = TFFeature.getFeatureAt(worldX, worldZ, (ServerLevel) world);
								TFMagicMapData tfData = (TFMagicMapData) data;
								tfData.tfDecorations.add(new TFMagicMapData.TFMapDecoration(feature, mapX, mapZ, (byte) 8));
								//TwilightForestMod.LOGGER.info("Found feature at {}, {}. Placing it on the map at {}, {}", worldX, worldZ, mapX, mapZ);
							}
						}
					}
				}
			}
		}
	}

	private MapColorBrightness getMapColorPerBiome(Level world, Biome biome) {
		if (BIOME_COLORS.isEmpty()) {
			setupBiomeColors();
		}
		if(biome == null)
			return new MapColorBrightness(MaterialColor.COLOR_BLACK);
		ResourceLocation key = biome.getRegistryName();
			MapColorBrightness color = BIOME_COLORS.get(key);
			if (color != null) {
				return color;
			}
		return new MapColorBrightness(biome.getGenerationSettings().getSurfaceBuilderConfig().getTopMaterial().getMapColor(world, BlockPos.ZERO));
	}

	private static void setupBiomeColors() {
		putBiomeColor(BiomeKeys.FOREST, new MapColorBrightness(MaterialColor.PLANT, 1));
		putBiomeColor(BiomeKeys.DENSE_FOREST, new MapColorBrightness(MaterialColor.PLANT, 0));
		putBiomeColor(BiomeKeys.LAKE, new MapColorBrightness(MaterialColor.WATER, 3));
		putBiomeColor(BiomeKeys.STREAM, new MapColorBrightness(MaterialColor.WATER, 1));
		putBiomeColor(BiomeKeys.SWAMP, new MapColorBrightness(MaterialColor.DIAMOND, 3));
		putBiomeColor(BiomeKeys.FIRE_SWAMP, new MapColorBrightness(MaterialColor.NETHER, 1));
		putBiomeColor(BiomeKeys.CLEARING, new MapColorBrightness(MaterialColor.GRASS, 2));
		putBiomeColor(BiomeKeys.OAK_SAVANNAH, new MapColorBrightness(MaterialColor.GRASS, 0));
		putBiomeColor(BiomeKeys.HIGHLANDS, new MapColorBrightness(MaterialColor.DIRT, 0));
		putBiomeColor(BiomeKeys.THORNLANDS, new MapColorBrightness(MaterialColor.WOOD, 3));
		putBiomeColor(BiomeKeys.FINAL_PLATEAU, new MapColorBrightness(MaterialColor.COLOR_LIGHT_GRAY, 2));
		putBiomeColor(BiomeKeys.FIREFLY_FOREST, new MapColorBrightness(MaterialColor.EMERALD, 1));
		putBiomeColor(BiomeKeys.DARK_FOREST, new MapColorBrightness(MaterialColor.COLOR_GREEN, 3));
		putBiomeColor(BiomeKeys.DARK_FOREST_CENTER, new MapColorBrightness(MaterialColor.COLOR_ORANGE, 3));
		putBiomeColor(BiomeKeys.SNOWY_FOREST, new MapColorBrightness(MaterialColor.SNOW, 1));
		putBiomeColor(BiomeKeys.GLACIER, new MapColorBrightness(MaterialColor.ICE, 1));
		putBiomeColor(BiomeKeys.MUSHROOM_FOREST, new MapColorBrightness(MaterialColor.COLOR_ORANGE, 0));
		putBiomeColor(BiomeKeys.DENSE_MUSHROOM_FOREST, new MapColorBrightness(MaterialColor.COLOR_PINK, 0));
		putBiomeColor(BiomeKeys.ENCHANTED_FOREST, new MapColorBrightness(MaterialColor.COLOR_CYAN, 2));
		putBiomeColor(BiomeKeys.SPOOKY_FOREST, new MapColorBrightness(MaterialColor.COLOR_PURPLE, 0));
	}

	private static void putBiomeColor(ResourceKey<Biome> biome, MapColorBrightness color) {
		BIOME_COLORS.put(biome.location(), color);
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

		switch (mcb.color.id) {
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

		int j = (mcb.color.col >> 16 & 255) * i / 255;
		int k = (mcb.color.col >> 8 & 255) * i / 255;
		int l = (mcb.color.col & 255) * i / 255;
		return 0xFF000000 | l << 16 | k << 8 | j;
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level world, Player player) {
		// disable zooming
	}

	@Override
	@Nullable
	public Packet<?> getUpdatePacket(ItemStack stack, Level world, Player player) {
		Integer id = getMapId(stack);
		TFMagicMapData mapdata = getCustomMapData(stack, world);
		Packet<?> p = id == null || mapdata == null ? null : mapdata.getUpdatePacket(id, player);
		return p instanceof ClientboundMapItemDataPacket ? TFPacketHandler.CHANNEL.toVanillaPacket(new MagicMapPacket(mapdata, (ClientboundMapItemDataPacket) p), NetworkDirection.PLAY_TO_CLIENT) : p;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
		Integer integer = getMapId(stack);
		TFMagicMapData mapitemsaveddata = pLevel == null ? null : getData(stack, pLevel);
		if (pFlag.isAdvanced()) {
			if (mapitemsaveddata != null) {
				pTooltip.add((new TranslatableComponent("filled_map.id", integer)).withStyle(ChatFormatting.GRAY));
				pTooltip.add((new TranslatableComponent("filled_map.scale", 1 << mapitemsaveddata.scale)).withStyle(ChatFormatting.GRAY));
				pTooltip.add((new TranslatableComponent("filled_map.level", mapitemsaveddata.scale, 4)).withStyle(ChatFormatting.GRAY));
			} else {
				pTooltip.add((new TranslatableComponent("filled_map.unknown")).withStyle(ChatFormatting.GRAY));
			}
		} else {
			if (integer != null) {
				pTooltip.add((new TextComponent("#" + integer)).withStyle(ChatFormatting.GRAY));
			}
		}

	}
}
