package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ColumnPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.StructureTagGenerator;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFItems;
import twilightforest.item.mapdata.TFMagicMapData;
import twilightforest.util.LandmarkUtil;
import twilightforest.util.LegacyLandmarkPlacements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// [VanillaCopy] super everything, but with appropriate redirections to our own datastructures. finer details noted

public class MagicMapItem extends MapItem {

	public static final String STR_ID = "magicmap";
	private static final Map<ResourceLocation, MapColorBrightness> BIOME_COLORS = new HashMap<>();

	public MagicMapItem(Properties properties) {
		super(properties);
	}

	private static class MapColorBrightness {
		public final MapColor color;
		public final int brightness;

		public MapColorBrightness(MapColor color, int brightness) {
			this.color = color;
			this.brightness = brightness;
		}

		public MapColorBrightness(MapColor color) {
			this.color = color;
			this.brightness = 1;
		}
	}

	public static ItemStack setupNewMap(Level level, int worldX, int worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking) {
		ItemStack itemstack = new ItemStack(TFItems.FILLED_MAGIC_MAP.get());
		createMapData(itemstack, level, worldX, worldZ, scale, trackingPosition, unlimitedTracking, level.dimension());
		return itemstack;
	}

	@Nullable
	public static TFMagicMapData getData(ItemStack stack, Level level) {
		Integer id = getMapId(stack);
		return id == null ? null : TFMagicMapData.getMagicMapData(level, getMapName(id));
	}

	@Nullable
	@Override
	protected TFMagicMapData getCustomMapData(ItemStack stack, Level level) {
		TFMagicMapData mapdata = getData(stack, level);
		if (mapdata == null && !level.isClientSide()) {
			mapdata = MagicMapItem.createMapData(stack, level, level.getLevelData().getXSpawn(), level.getLevelData().getZSpawn(), 3, false, false, level.dimension());
		}

		return mapdata;
	}

	public static ColumnPos getMagicMapCenter(int x, int z) {
		// magic maps are aligned to the key biome grid so that 0,0 -> 2048,2048 is the covered area
		int mapSize = 2048;
		int roundX = (int) Math.round((double) (x - 1024) / mapSize);
		int roundZ = (int) Math.round((double) (z - 1024) / mapSize);
		int scaledX = roundX * mapSize + 1024;
		int scaledZ = roundZ * mapSize + 1024;
		return new ColumnPos(scaledX, scaledZ);
	}

	private static TFMagicMapData createMapData(ItemStack stack, Level level, int x, int z, int scale, boolean trackingPosition, boolean unlimitedTracking, ResourceKey<Level> dimension) {
		int i = level.getFreeMapId();
		ColumnPos pos = getMagicMapCenter(x, z);

		TFMagicMapData mapdata = new TFMagicMapData(pos.x(), pos.z(), (byte) scale, trackingPosition, unlimitedTracking, false, dimension);
		TFMagicMapData.registerMagicMapData(level, mapdata, getMapName(i)); // call our own register method
		stack.getOrCreateTag().putInt("map", i);
		return mapdata;
	}

	public static String getMapName(int id) {
		return STR_ID + "_" + id;
	}

	private static final Map<ChunkPos, ResourceLocation[]> CACHE = new HashMap<>();
	private static final ResourceLocation NULL_BIOME = new ResourceLocation("null");

	@Override
	public void update(Level level, Entity viewer, MapItemSavedData data) {
        if (level.dimension() == data.dimension && viewer instanceof Player && !level.isClientSide) {
            int biomesPerPixel = 4;
            int blocksPerPixel = 16; // don't even bother with the scale, just hardcode it
            int centerX = data.centerX;
            int centerZ = data.centerZ;
            int viewerX = Mth.floor(viewer.getX() - centerX) / blocksPerPixel + 64;
            int viewerZ = Mth.floor(viewer.getZ() - centerZ) / blocksPerPixel + 64;
            int viewRadiusPixels = 512 / blocksPerPixel;

            int startX = (centerX / blocksPerPixel - 64) * biomesPerPixel;
            int startZ = (centerZ / blocksPerPixel - 64) * biomesPerPixel;
            ResourceLocation[] biomes = CACHE.computeIfAbsent(new ChunkPos(startX, startZ), pos -> {
                ResourceLocation[] array = new ResourceLocation[128 * biomesPerPixel * 128 * biomesPerPixel];
                for (int l = 0; l < 128 * biomesPerPixel; ++l) {
                    for (int i1 = 0; i1 < 128 * biomesPerPixel; ++i1) {
                        array[l * 128 * biomesPerPixel + i1] = level
                                .getBiome(new BlockPos(startX * biomesPerPixel + i1 * biomesPerPixel, 0, startZ * biomesPerPixel + l * biomesPerPixel))
                                .unwrapKey()
                                .map(ResourceKey::location)
                                .orElse(NULL_BIOME);
                    }
                }
                return array;
            });

			Registry<Structure> structureRegistry = level.registryAccess().registryOrThrow(Registries.STRUCTURE);

            for (int xPixel = viewerX - viewRadiusPixels + 1; xPixel < viewerX + viewRadiusPixels; ++xPixel) {
                for (int zPixel = viewerZ - viewRadiusPixels - 1; zPixel < viewerZ + viewRadiusPixels; ++zPixel) {
                    if (xPixel >= 0 && zPixel >= 0 && xPixel < 128 && zPixel < 128) {
                        int xPixelDist = xPixel - viewerX;
                        int zPixelDist = zPixel - viewerZ;
                        boolean shouldFuzz = xPixelDist * xPixelDist + zPixelDist * zPixelDist > (viewRadiusPixels - 2) * (viewRadiusPixels - 2);

                        ResourceLocation biome = biomes[xPixel * biomesPerPixel + zPixel * biomesPerPixel * 128 * biomesPerPixel];

                        // make streams more visible
                        ResourceLocation overBiome = biomes[xPixel * biomesPerPixel + zPixel * biomesPerPixel * 128 * biomesPerPixel + 1];
                        ResourceLocation downBiome = biomes[xPixel * biomesPerPixel + (zPixel * biomesPerPixel + 1) * 128 * biomesPerPixel];
                        biome = overBiome != null && TFBiomes.STREAM.location().equals(overBiome) ? overBiome : downBiome != null && TFBiomes.STREAM.location().equals(downBiome) ? downBiome : biome;

                        MapColorBrightness colorBrightness = this.getMapColorPerBiome(biome);

                        MapColor mapcolor = colorBrightness.color;
                        int brightness = colorBrightness.brightness;

                        if (xPixelDist * xPixelDist + zPixelDist * zPixelDist < viewRadiusPixels * viewRadiusPixels && (!shouldFuzz || (xPixel + zPixel & 1) != 0)) {
                            byte orgPixel = data.colors[xPixel + zPixel * 128];
                            byte ourPixel = (byte) (mapcolor.id * 4 + brightness);

                            if (orgPixel != ourPixel) {
                                data.setColor(xPixel, zPixel, ourPixel);
                                data.setDirty();
                            }

                            // look for TF features
                            int worldX = (centerX / blocksPerPixel + xPixel - 64) * blocksPerPixel;
                            int worldZ = (centerZ / blocksPerPixel + zPixel - 64) * blocksPerPixel;
                            if (LegacyLandmarkPlacements.blockIsInLandmarkCenter(worldX, worldZ)) {
                                byte mapX = (byte) ((worldX - centerX) / (float) blocksPerPixel * 2F);
                                byte mapZ = (byte) ((worldZ - centerZ) / (float) blocksPerPixel * 2F);

								ResourceKey<Structure> structureKey = LegacyLandmarkPlacements.pickLandmarkAtBlock(worldX, worldZ, level);
								// Filters by structures we want to give icons for
								if (structureRegistry.getHolder(structureKey).map(structureRef -> structureRef.is(StructureTagGenerator.LANDMARK)).orElse(false)) {
									boolean isConquered = LandmarkUtil.isConquered(level, worldX, worldZ);

									TFMagicMapData tfData = (TFMagicMapData) data;
									tfData.putMapData(new TFMagicMapData.TFMapDecoration(structureKey, mapX, mapZ, isConquered));
									//TwilightForestMod.LOGGER.info("Found feature at {}, {}. Placing it on the map at {}, {}", worldX, worldZ, mapX, mapZ);
								}
                            }
                        }
                    }
                }
            }
        }
	}

	private MapColorBrightness getMapColorPerBiome(ResourceLocation biome) {
		if (BIOME_COLORS.isEmpty()) {
			setupBiomeColors();
		}
		if (biome == NULL_BIOME)
			return new MapColorBrightness(MapColor.COLOR_BLACK);
		MapColorBrightness color = BIOME_COLORS.get(biome);
		if (color != null) {
			return color;
		}
		return new MapColorBrightness(MapColor.COLOR_MAGENTA);
	}

	private static void setupBiomeColors() {
		putBiomeColor(TFBiomes.FOREST, new MapColorBrightness(MapColor.PLANT, 1));
		putBiomeColor(TFBiomes.DENSE_FOREST, new MapColorBrightness(MapColor.PLANT, 0));
		putBiomeColor(TFBiomes.LAKE, new MapColorBrightness(MapColor.WATER, 3));
		putBiomeColor(TFBiomes.STREAM, new MapColorBrightness(MapColor.WATER, 1));
		putBiomeColor(TFBiomes.SWAMP, new MapColorBrightness(MapColor.DIAMOND, 3));
		putBiomeColor(TFBiomes.FIRE_SWAMP, new MapColorBrightness(MapColor.NETHER, 1));
		putBiomeColor(TFBiomes.CLEARING, new MapColorBrightness(MapColor.GRASS, 2));
		putBiomeColor(TFBiomes.OAK_SAVANNAH, new MapColorBrightness(MapColor.GRASS, 0));
		putBiomeColor(TFBiomes.HIGHLANDS, new MapColorBrightness(MapColor.DIRT, 0));
		putBiomeColor(TFBiomes.THORNLANDS, new MapColorBrightness(MapColor.WOOD, 3));
		putBiomeColor(TFBiomes.FINAL_PLATEAU, new MapColorBrightness(MapColor.COLOR_LIGHT_GRAY, 2));
		putBiomeColor(TFBiomes.FIREFLY_FOREST, new MapColorBrightness(MapColor.EMERALD, 1));
		putBiomeColor(TFBiomes.DARK_FOREST, new MapColorBrightness(MapColor.COLOR_GREEN, 3));
		putBiomeColor(TFBiomes.DARK_FOREST_CENTER, new MapColorBrightness(MapColor.COLOR_ORANGE, 3));
		putBiomeColor(TFBiomes.SNOWY_FOREST, new MapColorBrightness(MapColor.SNOW, 1));
		putBiomeColor(TFBiomes.GLACIER, new MapColorBrightness(MapColor.ICE, 1));
		putBiomeColor(TFBiomes.MUSHROOM_FOREST, new MapColorBrightness(MapColor.COLOR_ORANGE, 0));
		putBiomeColor(TFBiomes.DENSE_MUSHROOM_FOREST, new MapColorBrightness(MapColor.COLOR_PINK, 0));
		putBiomeColor(TFBiomes.ENCHANTED_FOREST, new MapColorBrightness(MapColor.COLOR_CYAN, 2));
		putBiomeColor(TFBiomes.SPOOKY_FOREST, new MapColorBrightness(MapColor.COLOR_PURPLE, 0));
	}

	private static void putBiomeColor(ResourceKey<Biome> biome, MapColorBrightness color) {
		BIOME_COLORS.put(biome.location(), color);
	}

	public static int getBiomeColor(Level level, Biome biome) {
		if (BIOME_COLORS.isEmpty()) {
			setupBiomeColors();
		}

		MapColorBrightness c = BIOME_COLORS.get(level.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome));

		return c != null ? getMapColor(c) : 0xFF000000;
	}

	public static int getMapColor(MapColorBrightness mcb) {
		int i = switch (mcb.color.id) {
			case 3 -> 135;
			case 2 -> 255;
			case 0 -> 180;
			default -> 220;
		};

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
		return id == null || mapdata == null ? null : mapdata.getUpdatePacket(id, player);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		Integer integer = getMapId(stack);
		TFMagicMapData mapitemsaveddata = level == null ? null : getData(stack, level);
		if (flag.isAdvanced()) {
			if (mapitemsaveddata != null) {
				tooltip.add((Component.translatable("filled_map.id", integer)).withStyle(ChatFormatting.GRAY));
				tooltip.add((Component.translatable("filled_map.scale", 1 << mapitemsaveddata.scale)).withStyle(ChatFormatting.GRAY));
				tooltip.add((Component.translatable("filled_map.level", mapitemsaveddata.scale, 4)).withStyle(ChatFormatting.GRAY));
			} else {
				tooltip.add((Component.translatable("filled_map.unknown")).withStyle(ChatFormatting.GRAY));
			}
		} else {
			if (integer != null) {
				tooltip.add(Component.literal("#" + integer).withStyle(ChatFormatting.GRAY));
			}
		}

	}
}