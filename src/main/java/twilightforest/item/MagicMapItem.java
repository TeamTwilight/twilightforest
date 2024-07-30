package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.level.saveddata.maps.*;
import org.jetbrains.annotations.Nullable;
import twilightforest.data.tags.StructureTagGenerator;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFDataMaps;
import twilightforest.init.TFItems;
import twilightforest.item.mapdata.TFMagicMapData;
import twilightforest.util.LandmarkUtil;
import twilightforest.util.LegacyLandmarkPlacements;
import twilightforest.util.datamaps.MagicMapBiomeColor;
import twilightforest.world.components.structures.util.LandmarkStructure;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// [VanillaCopy] super everything, but with appropriate redirections to our own datastructures. finer details noted
public class MagicMapItem extends MapItem {

	public static final String STR_ID = "magicmap";

	public MagicMapItem(Properties properties) {
		super(properties);
	}

	public static ItemStack setupNewMap(Level level, int worldX, int worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking) {
		ItemStack itemstack = new ItemStack(TFItems.FILLED_MAGIC_MAP.get());
		createMapData(itemstack, level, worldX, worldZ, scale, trackingPosition, unlimitedTracking, level.dimension());
		return itemstack;
	}

	@Nullable
	public static TFMagicMapData getData(ItemStack stack, Level level) {
		MapId mapid = stack.get(DataComponents.MAP_ID);
		return mapid == null ? null : TFMagicMapData.getMagicMapData(level, getMapName(mapid.id()));
	}

	@Nullable
	public static TFMagicMapData getData(ItemStack stack, TooltipContext context) {
		MapId mapid = stack.get(DataComponents.MAP_ID);
		return mapid != null && context.mapData(mapid) instanceof TFMagicMapData mapData ? mapData : null;
	}

	@Nullable
	@Override
	protected TFMagicMapData getCustomMapData(ItemStack stack, Level level) {
		TFMagicMapData mapdata = getData(stack, level);
		if (mapdata == null && !level.isClientSide()) {
			BlockPos sharedSpawnPos = level.getSharedSpawnPos();
			mapdata = MagicMapItem.createMapData(stack, level, sharedSpawnPos.getX(), sharedSpawnPos.getZ(), 3, false, false, level.dimension());
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
		MapId freeMapId = level.getFreeMapId();
		ColumnPos pos = getMagicMapCenter(x, z);

		TFMagicMapData mapdata = new TFMagicMapData(pos.x(), pos.z(), (byte) scale, trackingPosition, unlimitedTracking, false, dimension);
		TFMagicMapData.registerMagicMapData(level, mapdata, getMapName(freeMapId.id())); // call our own register method
		stack.set(DataComponents.MAP_ID, freeMapId);
		return mapdata;
	}

	public static String getMapName(int id) {
		return STR_ID + "_" + id;
	}

	private static final Map<ChunkPos, Holder<Biome>[]> CACHE = new HashMap<>();

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
			Holder<Biome>[] biomes = CACHE.computeIfAbsent(new ChunkPos(startX, startZ), pos -> {
				@SuppressWarnings({"unchecked", "rawtypes"})
				Holder<Biome>[] array = new Holder[128 * biomesPerPixel * 128 * biomesPerPixel];
				for (int l = 0; l < 128 * biomesPerPixel; ++l) {
					for (int i1 = 0; i1 < 128 * biomesPerPixel; ++i1) {
						array[l * 128 * biomesPerPixel + i1] = level.getBiome(new BlockPos(startX * biomesPerPixel + i1 * biomesPerPixel, 0, startZ * biomesPerPixel + l * biomesPerPixel));
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

						Holder<Biome> biome = biomes[xPixel * biomesPerPixel + zPixel * biomesPerPixel * 128 * biomesPerPixel];

						// make streams more visible
						Holder<Biome> overBiome = biomes[xPixel * biomesPerPixel + zPixel * biomesPerPixel * 128 * biomesPerPixel + 1];
						Holder<Biome> downBiome = biomes[xPixel * biomesPerPixel + (zPixel * biomesPerPixel + 1) * 128 * biomesPerPixel];
						biome = overBiome != null && overBiome.is(TFBiomes.STREAM) ? overBiome : downBiome != null && downBiome.is(TFBiomes.STREAM) ? downBiome : biome;

						MagicMapBiomeColor colorBrightness = this.getMapColorPerBiome(biome);

						MapColor mapcolor = colorBrightness.color();
						int brightness = colorBrightness.brightness();

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
								ResourceKey<Structure> structureKey = LegacyLandmarkPlacements.pickLandmarkAtBlock(worldX, worldZ, level);
								// Filters by structures we want to give icons for
								if (structureRegistry.getHolder(structureKey).map(structureRef -> structureRef.is(StructureTagGenerator.LANDMARK)).orElse(false)) {
									TFMagicMapData tfData = (TFMagicMapData) data;
									if (structureRegistry.getOrThrow(structureKey) instanceof LandmarkStructure landmark) {
										landmark.getMapIcon().ifPresent(icon -> tfData.addTFDecoration(icon, level, makeName(icon, worldX, worldZ), worldX, worldZ, 180.0F, LandmarkUtil.isConquered(level, worldX, worldZ)));
										//TwilightForestMod.LOGGER.info("Found feature at {}, {}. Placing it on the map at {}, {}", worldX, worldZ, mapX, mapZ);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public static String makeName(Holder<MapDecorationType> type, int x, int z) {
		return type.value().assetId() + "_" + x + "_" + z;
	}

	private MagicMapBiomeColor getMapColorPerBiome(Holder<Biome> biome) {
		MagicMapBiomeColor color = biome.getData(TFDataMaps.MAGIC_MAP_BIOME_COLOR);
		return color != null ? color : new MagicMapBiomeColor(MapColor.COLOR_MAGENTA);
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level world, Player player) {
		// disable zooming
	}

	@Override
	@Nullable
	public Packet<?> getUpdatePacket(ItemStack stack, Level world, Player player) {
		MapId mapId = stack.get(DataComponents.MAP_ID);
		TFMagicMapData mapdata = getCustomMapData(stack, world);
		return mapId == null || mapdata == null ? null : mapdata.getUpdatePacket(mapId, player);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		MapId mapId = stack.get(DataComponents.MAP_ID);
		if (mapId != null) {
			if (flag.isAdvanced()) {
				MapItemSavedData mapitemsaveddata = TFMagicMapData.getClientMagicMapData(getMapName(mapId.id()));
				if (mapitemsaveddata != null) {
					tooltip.add((Component.translatable("filled_map.id", mapId.id())).withStyle(ChatFormatting.GRAY));
					tooltip.add((Component.translatable("filled_map.scale", 1 << mapitemsaveddata.scale)).withStyle(ChatFormatting.GRAY));
					tooltip.add((Component.translatable("filled_map.level", mapitemsaveddata.scale, 4)).withStyle(ChatFormatting.GRAY));
				} else {
					tooltip.add((Component.translatable("filled_map.unknown")).withStyle(ChatFormatting.GRAY));
				}
			} else tooltip.add(MapItem.getTooltipForId(mapId));
		}
	}
}