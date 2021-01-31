package twilightforest.item;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.block.Blocks;
import net.minecraft.item.*;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SMapDataPacket;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.common.Tags;
import net.minecraftforge.fml.network.NetworkDirection;
import twilightforest.TFMazeMapData;
import twilightforest.network.PacketMazeMap;
import twilightforest.network.TFPacketHandler;

import javax.annotation.Nullable;

// [VanillaCopy] super everything, but with appropriate redirections to our own datastructures. finer details noted
// FIXME: Map does not display data. Investigate
public class ItemTFMazeMap extends FilledMapItem {

	public static final String STR_ID = "mazemap";
	private static final int YSEARCH = 3;

	protected final boolean mapOres;

	protected ItemTFMazeMap(boolean mapOres, Properties props) {
		super(props);
		this.mapOres = mapOres;
	}

	// [VanillaCopy] super with own item and methods, plus y and mapOres
	public static ItemStack setupNewMap(World world, int worldX, int worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking, int worldY, boolean mapOres) {
		ItemStack itemstack = new ItemStack(mapOres ? TFItems.ore_map.get() : TFItems.maze_map.get());
		createMapData(itemstack, world, worldX, worldZ, scale, trackingPosition, unlimitedTracking, world.getDimensionKey(), worldY);
		return itemstack;
	}

	@Nullable
	public static TFMazeMapData getData(ItemStack stack, World world) {
		return TFMazeMapData.getMazeMapData(world, getMapName(getMapId(stack)));
	}

	@Nullable
	@Override
	protected TFMazeMapData getCustomMapData(ItemStack stack, World world) {
		TFMazeMapData mapdata = getData(stack, world);
		if (mapdata == null && !world.isRemote) {
			mapdata = ItemTFMazeMap.createMapData(stack, world, world.getWorldInfo().getSpawnX(), world.getWorldInfo().getSpawnZ(), 0, false, false, world.getDimensionKey(), world.getWorldInfo().getSpawnY());
		}

		return mapdata;
	}

	// [VanillaCopy] super with own item and id, and y parameter
	private static TFMazeMapData createMapData(ItemStack stack, World world, int x, int z, int scale, boolean trackingPosition, boolean unlimitedTracking, RegistryKey<World> dimension, int y) {
		int i = world.getNextMapId();
		TFMazeMapData mapdata = new TFMazeMapData(getMapName(i));
		mapdata.initData(x, z, scale, trackingPosition, unlimitedTracking, dimension);
		mapdata.calculateMapCenter(world, x, y, z, scale); // call our own map center calculation
		TFMazeMapData.registerMazeMapData(world, mapdata); // call our own register method
		stack.getOrCreateTag().putInt("map", i);
		return mapdata;
	}

	public static String getMapName(int id) {
		return STR_ID + "_" + id;
	}

	// [VanillaCopy] of superclass, with sane variable names and noted changes
	@SuppressWarnings("unused")
	@Override
	public void updateMapData(World world, Entity viewer, MapData data) {
		if (world.getDimensionKey() == data.dimension && viewer instanceof PlayerEntity) {
			int blocksPerPixel = 1 << data.scale;
			int centerX = data.xCenter;
			int centerZ = data.zCenter;
			int viewerX = MathHelper.floor(viewer.getPosX() - centerX) / blocksPerPixel + 64;
			int viewerZ = MathHelper.floor(viewer.getPosZ() - centerZ) / blocksPerPixel + 64;
			int viewRadiusPixels = 16; // TF this is smaller on the maze map

			if (world.getDimensionType().getHasCeiling()) {
				viewRadiusPixels /= 2;
			}

			MapData.MapInfo mapdata$mapinfo = data.getMapInfo((PlayerEntity) viewer);
			++mapdata$mapinfo.step;
			boolean flag = false;

			for (int xPixel = viewerX - viewRadiusPixels + 1; xPixel < viewerX + viewRadiusPixels; ++xPixel) {
				if ((xPixel & 15) == (mapdata$mapinfo.step & 15) || flag) {
					flag = false;
					double d0 = 0.0D;

					for (int zPixel = viewerZ - viewRadiusPixels - 1; zPixel < viewerZ + viewRadiusPixels; ++zPixel) {
						if (xPixel >= 0 && zPixel >= -1 && xPixel < 128 && zPixel < 128) {
							int xPixelDist = xPixel - viewerX;
							int zPixelDist = zPixel - viewerZ;
							boolean shouldFuzz = xPixelDist * xPixelDist + zPixelDist * zPixelDist > (viewRadiusPixels - 2) * (viewRadiusPixels - 2);
							int worldX = (centerX / blocksPerPixel + xPixel - 64) * blocksPerPixel;
							int worldZ = (centerZ / blocksPerPixel + zPixel - 64) * blocksPerPixel;
							Multiset<MaterialColor> multiset = HashMultiset.<MaterialColor>create();
							Chunk chunk = world.getChunkAt(new BlockPos(worldX, 0, worldZ));

							int brightness = 1;
							if (!chunk.isEmpty()) {
								int worldXRounded = worldX & 15;
								int worldZRounded = worldZ & 15;
								int numLiquid = 0;
								double d1 = 0.0D;

								if (world.getDimensionType().getHasCeiling()) {
									int l3 = worldX + worldZ * 231871;
									l3 = l3 * l3 * 31287121 + l3 * 11;

									if ((l3 >> 20 & 1) == 0) {
										multiset.add(Blocks.DIRT.getDefaultState().getMaterialColor(world, BlockPos.ZERO), 10);
									} else {
										multiset.add(Blocks.STONE.getDefaultState().getMaterialColor(world, BlockPos.ZERO), 100);
									}

									d1 = 100.0D;
								} else {
									// TF - remove extra 2 levels of loops
									// maze maps are always 0 scale, which is 1 pixel = 1 block, so the loops are unneeded
									int yCenter = ((TFMazeMapData) data).yCenter;
									BlockPos.Mutable blockpos$mutableblockpos = new BlockPos.Mutable(worldXRounded, yCenter, worldZRounded);
									BlockState state = chunk.getBlockState(blockpos$mutableblockpos);

									multiset.add(state.getMaterialColor(world, blockpos$mutableblockpos));

									if (state.getBlock() == Blocks.STONE || state.getBlock() == Blocks.AIR) {
										for (int i = -YSEARCH; i <= YSEARCH; i++) {
											blockpos$mutableblockpos.setY(yCenter + i);
											BlockState searchID = chunk.getBlockState(blockpos$mutableblockpos);
											if (searchID.getBlock() != Blocks.STONE && searchID.getBlock() != Blocks.AIR) {
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
										if (state.getBlock() == Blocks.COAL_ORE) {
											multiset.add(MaterialColor.BLACK, 1000);
										} else if (state.getBlock() == Blocks.GOLD_ORE) {
											multiset.add(MaterialColor.GOLD, 1000);
										} else if (state.getBlock() == Blocks.IRON_ORE) {
											multiset.add(MaterialColor.IRON, 1000);
										} else if (state.getBlock() == Blocks.LAPIS_ORE) {
											multiset.add(MaterialColor.LAPIS, 1000);
										} else if (state.getBlock() == Blocks.REDSTONE_ORE) {
											multiset.add(MaterialColor.RED, 1000);
										} else if (state.getBlock() == Blocks.DIAMOND_ORE) {
											multiset.add(MaterialColor.DIAMOND, 1000);
										} else if (state.getBlock() == Blocks.EMERALD_ORE) {
											multiset.add(MaterialColor.EMERALD, 1000);
										} else if (state.getBlock() != Blocks.AIR && state.isIn(Tags.Blocks.ORES)) {
											multiset.add(MaterialColor.PINK, 1000);
										}
									}
								}

                                /*numLiquid = numLiquid / (blocksPerPixel * blocksPerPixel);
								double d2 = (d1 - d0) * 4.0D / (double)(blocksPerPixel + 4) + ((double)(xPixel + zPixel & 1) - 0.5D) * 0.4D;
                                int brightness = 1;

                                if (d2 > 0.6D)
                                {
                                    brightness = 2;
                                }

                                if (d2 < -0.6D)
                                {
                                    brightness = 0;
                                }*/

								MaterialColor mapcolor = Iterables.getFirst(Multisets.<MaterialColor>copyHighestCountFirst(multiset), MaterialColor.AIR);

                                /*if (mapcolor == MaterialColor.WATER)
                                {
                                    d2 = (double)numLiquid * 0.1D + (double)(xPixel + zPixel & 1) * 0.2D;
                                    brightness = 1;

                                    if (d2 < 0.5D)
                                    {
                                        brightness = 2;
                                    }

                                    if (d2 > 0.9D)
                                    {
                                        brightness = 0;
                                    }
                                }*/

								d0 = d1;

								if (zPixel >= 0 && xPixelDist * xPixelDist + zPixelDist * zPixelDist < viewRadiusPixels * viewRadiusPixels && (!shouldFuzz || (xPixel + zPixel & 1) != 0)) {
									byte b0 = data.colors[xPixel + zPixel * 128];
									byte b1 = (byte) (mapcolor.colorIndex * 4 + brightness);

									if (b0 != b1) {
										data.colors[xPixel + zPixel * 128] = b1;
										data.updateMapData(xPixel, zPixel);
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
	public void inventoryTick(ItemStack stack, World worldIn, Entity entityIn, int slot, boolean isSelected) {
		if (!worldIn.isRemote) {
			TFMazeMapData mapdata = this.getCustomMapData(stack, worldIn);

			if (mapdata != null) {
				if (entityIn instanceof PlayerEntity) {
					PlayerEntity entityplayer = (PlayerEntity) entityIn;
					mapdata.updateVisiblePlayers(entityplayer, stack);

					// TF - if player is far away vertically, show a dot
					int yProximity = MathHelper.floor(entityplayer.getPosY() - mapdata.yCenter);
					if (yProximity < -YSEARCH || yProximity > YSEARCH) {
						MapDecoration decoration = mapdata.mapDecorations.get(entityplayer.getName().getString());
						if (decoration != null) {
							mapdata.mapDecorations.put(entityplayer.getName().getString(), new MapDecoration(MapDecoration.Type.PLAYER_OFF_MAP, decoration.getX(), decoration.getY(), decoration.getRotation(), null));
						}
					}
				}

				if (!mapdata.locked && (isSelected || entityIn instanceof PlayerEntity && ((PlayerEntity)entityIn).getHeldItemOffhand() == stack)) {
					this.updateMapData(worldIn, entityIn, mapdata);
				}
			}
		}
	}

	@Override
	public void onCreated(ItemStack stack, World world, PlayerEntity player) {
		// disable zooming
	}

	@Override
	@Nullable
	public IPacket<?> getUpdatePacket(ItemStack stack, World worldIn, PlayerEntity player) {
		IPacket<?> p = super.getUpdatePacket(stack, worldIn, player);
		if (p instanceof SMapDataPacket) {
			return TFPacketHandler.CHANNEL.toVanillaPacket(new PacketMazeMap((SMapDataPacket) p), NetworkDirection.PLAY_TO_CLIENT);
		} else {
			return p;
		}
	}
}
