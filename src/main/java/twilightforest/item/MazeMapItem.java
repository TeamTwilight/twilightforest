package twilightforest.item;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.common.Tags;
import org.jetbrains.annotations.Nullable;
import twilightforest.TFMazeMapData;
import twilightforest.init.TFItems;

// [VanillaCopy] super everything, but with appropriate redirections to our own datastructures. finer details noted

public class MazeMapItem extends MapItem {

	public static final String STR_ID = "mazemap";
	private static final int YSEARCH = 3;

	protected final boolean mapOres;

	public MazeMapItem(boolean mapOres, Properties properties) {
		super(properties);
		this.mapOres = mapOres;
	}

	public static ItemStack setupNewMap(Level level, int worldX, int worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking, int worldY, boolean mapOres) {
		ItemStack itemstack = new ItemStack(mapOres ? TFItems.FILLED_ORE_MAP.get() : TFItems.FILLED_MAZE_MAP.get());
		createMapData(itemstack, level, worldX, worldZ, scale, trackingPosition, unlimitedTracking, level.dimension(), worldY, mapOres);
		return itemstack;
	}

	@Nullable
	public static TFMazeMapData getData(ItemStack stack, Level level) {
		Integer id = getMapId(stack);
		return id == null ? null : TFMazeMapData.getMazeMapData(level, getMapName(id));
	}

	@Nullable
	@Override
	protected TFMazeMapData getCustomMapData(ItemStack stack, Level level) {
		TFMazeMapData mapdata = getData(stack, level);
		if (mapdata == null && !level.isClientSide()) {
			mapdata = MazeMapItem.createMapData(stack, level, level.getLevelData().getXSpawn(), level.getLevelData().getZSpawn(), 0, false, false, level.dimension(), level.getLevelData().getYSpawn(), mapOres);
		}

		return mapdata;
	}

	private static TFMazeMapData createMapData(ItemStack stack, Level level, int x, int z, int scale, boolean trackingPosition, boolean unlimitedTracking, ResourceKey<Level> dimension, int y, boolean ore) {
		int i = level.getFreeMapId();

		int mapSize = 128 * (1 << scale);
		int roundX = Mth.floor((x + 64.0D) / (double) mapSize);
		int roundZ = Mth.floor((z + 64.0D) / (double) mapSize);
		int scaledX = roundX * mapSize + mapSize / 2 - 64;
		int scaledZ = roundZ * mapSize + mapSize / 2 - 64;

		TFMazeMapData mapdata = new TFMazeMapData(scaledX, scaledZ, (byte) scale, trackingPosition, unlimitedTracking, false, dimension);
		mapdata.calculateMapCenter(level, x, y, z); // call our own map center calculation
		mapdata.ore = ore;
		TFMazeMapData.registerMazeMapData(level, mapdata, getMapName(i)); // call our own register method
		stack.getOrCreateTag().putInt("map", i);
		return mapdata;
	}

	public static String getMapName(int id) {
		return STR_ID + "_" + id;
	}

	// [VanillaCopy] of superclass, with sane variable names and noted changes
	@Override
	public void update(Level level, Entity viewer, MapItemSavedData data) {
		if (level.dimension() == data.dimension && viewer instanceof Player) {
			int blocksPerPixel = 1 << data.scale;
			int centerX = data.centerX;
			int centerZ = data.centerZ;
			int viewerX = Mth.floor(viewer.getX() - centerX) / blocksPerPixel + 64;
			int viewerZ = Mth.floor(viewer.getZ() - centerZ) / blocksPerPixel + 64;
			int viewRadiusPixels = 16; // TF this is smaller on the maze map

			if (level.dimensionType().hasCeiling()) {
				viewRadiusPixels /= 2;
			}

			MapItemSavedData.HoldingPlayer mapdata$mapinfo = data.getHoldingPlayer((Player) viewer);
			++mapdata$mapinfo.step;
			boolean flag = false;

			for (int xPixel = viewerX - viewRadiusPixels + 1; xPixel < viewerX + viewRadiusPixels; ++xPixel) {
				if ((xPixel & 15) == (mapdata$mapinfo.step & 15) || flag) {
					flag = false;

					for (int zPixel = viewerZ - viewRadiusPixels - 1; zPixel < viewerZ + viewRadiusPixels; ++zPixel) {
						if (xPixel >= 0 && zPixel >= -1 && xPixel < 128 && zPixel < 128) {
							int xPixelDist = xPixel - viewerX;
							int zPixelDist = zPixel - viewerZ;
							boolean shouldFuzz = xPixelDist * xPixelDist + zPixelDist * zPixelDist > (viewRadiusPixels - 2) * (viewRadiusPixels - 2);
							int worldX = (centerX / blocksPerPixel + xPixel - 64) * blocksPerPixel;
							int worldZ = (centerZ / blocksPerPixel + zPixel - 64) * blocksPerPixel;
							Multiset<MapColor> multiset = HashMultiset.create();
							LevelChunk chunk = level.getChunkAt(new BlockPos(worldX, 0, worldZ));

							int brightness = 1;
							if (!chunk.isEmpty()) {
								int worldXRounded = worldX & 15;
								int worldZRounded = worldZ & 15;

								if (level.dimensionType().hasCeiling()) {
									int l3 = worldX + worldZ * 231871;
									l3 = l3 * l3 * 31287121 + l3 * 11;

									if ((l3 >> 20 & 1) == 0) {
										multiset.add(Blocks.DIRT.defaultBlockState().getMapColor(level, BlockPos.ZERO), 10);
									} else {
										multiset.add(Blocks.STONE.defaultBlockState().getMapColor(level, BlockPos.ZERO), 100);
									}

								} else {
									// TF - remove extra 2 levels of loops
									// maze maps are always 0 scale, which is 1 pixel = 1 block, so the loops are unneeded
									int yCenter = ((TFMazeMapData) data).yCenter;
									BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(worldXRounded, yCenter, worldZRounded);
									BlockState state = chunk.getBlockState(blockpos$mutableblockpos);

									multiset.add(state.getMapColor(level, blockpos$mutableblockpos));

									if (state.is(Blocks.STONE) || state.isAir()) {
										for (int i = -YSEARCH; i <= YSEARCH; i++) {
											blockpos$mutableblockpos.setY(yCenter + i);
											BlockState searchID = chunk.getBlockState(blockpos$mutableblockpos);
											if (searchID.is(Blocks.STONE) && !searchID.isAir()) {
												state = searchID;
												if (i > 0) {
													brightness = 2;
												}
												if (i < 0) {
													brightness = 0;
												}

												break;
											}
										}
									}

									if (mapOres) {
										// recolor ores
										if (state.is(BlockTags.COAL_ORES)) {
											multiset.add(MapColor.COLOR_BLACK, 1000);
										} else if (state.is(BlockTags.GOLD_ORES)) {
											multiset.add(MapColor.GOLD, 1000);
										} else if (state.is(BlockTags.IRON_ORES)) {
											multiset.add(MapColor.METAL, 1000);
										} else if (state.is(BlockTags.LAPIS_ORES)) {
											multiset.add(MapColor.LAPIS, 1000);
										} else if (state.is(BlockTags.REDSTONE_ORES)) {
											multiset.add(MapColor.COLOR_RED, 1000);
										} else if (state.is(BlockTags.DIAMOND_ORES)) {
											multiset.add(MapColor.DIAMOND, 1000);
										} else if (state.is(BlockTags.EMERALD_ORES)) {
											multiset.add(MapColor.EMERALD, 1000);
										} else if (state.is(BlockTags.COPPER_ORES)) {
											multiset.add(MapColor.COLOR_ORANGE, 1000);
										} else if (state.getBlock() != Blocks.AIR && state.is(Tags.Blocks.ORES)) {
											multiset.add(MapColor.COLOR_PINK, 1000);
										}
									}
								}

								MapColor mapcolor = Iterables.getFirst(Multisets.copyHighestCountFirst(multiset), MapColor.NONE);

								if (zPixel >= 0 && xPixelDist * xPixelDist + zPixelDist * zPixelDist < viewRadiusPixels * viewRadiusPixels && (!shouldFuzz || (xPixel + zPixel & 1) != 0)) {
									byte b0 = data.colors[xPixel + zPixel * 128];
									byte b1 = (byte) (mapcolor.id * 4 + brightness);

									if (b0 != b1) {
										data.setColor(xPixel, zPixel, b1);
										data.setDirty();
										flag = true;
									}
								}
							}
						}
					}
				}
			}
		}
	}

	// [VanillaCopy] super but shows a dot if player is too far in the vertical direction as well
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean isSelected) {
		if (!level.isClientSide()) {
			TFMazeMapData mapdata = this.getCustomMapData(stack, level);

			if (mapdata != null) {
				if (entity instanceof Player entityplayer) {
					mapdata.tickCarriedBy(entityplayer, stack);

					// TF - if player is far away vertically, show a dot
					int yProximity = Mth.floor(entityplayer.getY() - mapdata.yCenter);
					if (yProximity < -YSEARCH || yProximity > YSEARCH) {
						MapDecoration decoration = mapdata.decorations.get(entityplayer.getName().getString());
						if (decoration != null) {
							mapdata.decorations.put(entityplayer.getName().getString(), new MapDecoration(MapDecoration.Type.PLAYER_OFF_MAP, decoration.getX(), decoration.getY(), decoration.getRot(), null));
						}
					}
				}

				if (!mapdata.locked && (isSelected || entity instanceof Player player && player.getOffhandItem() == stack)) {
					this.update(level, entity, mapdata);
				}
			}
		}
	}

	@Override
	public void onCraftedBy(ItemStack stack, Level level, Player player) {
		// disable zooming
	}

	@Override
	@Nullable
	public Packet<?> getUpdatePacket(ItemStack stack, Level level, Player player) {
		Integer id = getMapId(stack);
		TFMazeMapData mapdata = getCustomMapData(stack, level);
		return id == null || mapdata == null ? null : mapdata.getUpdatePacket(id, player);
	}
}
