package twilightforest.item;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Iterables;
import com.google.common.collect.Multiset;
import com.google.common.collect.Multisets;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFMazeMapData;
import twilightforest.TFPacketHandler;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.network.PacketMazeMap;

import javax.annotation.Nullable;

public class ItemTFMazeMap extends ItemMap implements ModelRegisterCallback {
	private static final String STR_ID = "mazemap";
	private static final int YSEARCH = 3;
	protected boolean mapOres;

	protected ItemTFMazeMap(boolean par2MapOres) {
		this.mapOres = par2MapOres;
	}

	// [VanillaCopy] super with own item and id, and y parameter, also whether we have an ore map or not
	public static ItemStack setupNewMap(World world, double worldX, double worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking, double worldY, boolean mapOres) {
		ItemStack itemstack = new ItemStack(mapOres ? TFItems.ore_map : TFItems.maze_map, 1, world.getUniqueDataId(STR_ID));
		String s = STR_ID + "_" + itemstack.getMetadata();
		TFMazeMapData mapdata = new TFMazeMapData(s);
		world.setData(s, mapdata);
		mapdata.scale = scale;
		mapdata.calculateMapCenter(world, worldX, worldY, worldZ, scale); // TF custom method here
		mapdata.dimension = world.provider.getDimension();
		mapdata.trackingPosition = trackingPosition;
		mapdata.unlimitedTracking = unlimitedTracking;
		mapdata.markDirty();
		return itemstack;
	}

	// [VanillaCopy] super, with own string ID and class, narrowed types
	@SideOnly(Side.CLIENT)
	public static TFMazeMapData loadMapData(int mapId, World worldIn) {
		String s = STR_ID + "_" + mapId;
		TFMazeMapData mapdata = (TFMazeMapData) worldIn.loadData(TFMazeMapData.class, s);

		if (mapdata == null) {
			mapdata = new TFMazeMapData(s);
			worldIn.setData(s, mapdata);
		}

		return mapdata;
	}

	// [VanillaCopy] super, with own string ID and class
	@Override
	public TFMazeMapData getMapData(ItemStack stack, World worldIn) {
		String s = STR_ID + "_" + stack.getMetadata();
		TFMazeMapData mapdata = (TFMazeMapData) worldIn.loadData(TFMazeMapData.class, s);

		if (mapdata == null && !worldIn.isRemote) {
			stack.setItemDamage(worldIn.getUniqueDataId(STR_ID));
			s = STR_ID + "_" + stack.getMetadata();
			mapdata = new TFMazeMapData(s);
			mapdata.scale = 0; // TF - fix scale at 0
			mapdata.calculateMapCenter((double) worldIn.getWorldInfo().getSpawnX(), (double) worldIn.getWorldInfo().getSpawnZ(), mapdata.scale);
			mapdata.dimension = worldIn.provider.getDimension();
			mapdata.markDirty();
			worldIn.setData(s, mapdata);
		}

		return mapdata;
	}

	// [VanillaCopy] of superclass, with sane variable names and noted changes
	@SuppressWarnings("unused")
	@Override
	public void updateMapData(World world, Entity viewer, MapData data) {
		if (world.provider.getDimension() == data.dimension && viewer instanceof EntityPlayer) {
			int blocksPerPixel = 1 << data.scale;
			int centerX = data.xCenter;
			int centerZ = data.zCenter;
			int viewerX = MathHelper.floor(viewer.posX - (double) centerX) / blocksPerPixel + 64;
			int viewerZ = MathHelper.floor(viewer.posZ - (double) centerZ) / blocksPerPixel + 64;
			int viewRadiusPixels = 16; // TF this is smaller on the maze map

			if (world.provider.isNether()) {
				viewRadiusPixels /= 2;
			}

			MapData.MapInfo mapdata$mapinfo = data.getMapInfo((EntityPlayer) viewer);
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
							Multiset<MapColor> multiset = HashMultiset.<MapColor>create();
							Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(worldX, 0, worldZ));

							int brightness = 1;
							if (!chunk.isEmpty()) {
								int worldXRounded = worldX & 15;
								int worldZRounded = worldZ & 15;
								int numLiquid = 0;
								double d1 = 0.0D;

								if (world.provider.isNether()) {
									int l3 = worldX + worldZ * 231871;
									l3 = l3 * l3 * 31287121 + l3 * 11;

									if ((l3 >> 20 & 1) == 0) {
										multiset.add(Blocks.DIRT.getDefaultState().withProperty(BlockDirt.VARIANT, BlockDirt.DirtType.DIRT).getMapColor(world, BlockPos.ORIGIN), 10);
									} else {
										multiset.add(Blocks.STONE.getDefaultState().withProperty(BlockStone.VARIANT, BlockStone.EnumType.STONE).getMapColor(world, BlockPos.ORIGIN), 100);
									}

									d1 = 100.0D;
								} else {
									// TF - remove extra 2 levels of loops
									// maze maps are always 0 scale, which is 1 pixel = 1 block, so the loops are unneeded
									int yCenter = ((TFMazeMapData) data).yCenter;
									BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(worldXRounded, yCenter, worldZRounded);
									IBlockState state = chunk.getBlockState(blockpos$mutableblockpos);

									multiset.add(state.getMapColor(world, blockpos$mutableblockpos));

									if (state.getBlock() == Blocks.STONE || state.getBlock() == Blocks.AIR) {
										for (int i = -YSEARCH; i <= YSEARCH; i++) {
											blockpos$mutableblockpos.setY(yCenter + i);
											IBlockState searchID = chunk.getBlockState(blockpos$mutableblockpos);
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
											multiset.add(MapColor.BLACK, 1000);
										} else if (state.getBlock() == Blocks.GOLD_ORE) {
											multiset.add(MapColor.GOLD, 1000);
										} else if (state.getBlock() == Blocks.IRON_ORE) {
											multiset.add(MapColor.IRON, 1000);
										} else if (state.getBlock() == Blocks.LAPIS_ORE) {
											multiset.add(MapColor.LAPIS, 1000);
										} else if (state.getBlock() == Blocks.REDSTONE_ORE || state.getBlock() == Blocks.LIT_REDSTONE_ORE) {
											multiset.add(MapColor.RED, 1000);
										} else if (state.getBlock() == Blocks.DIAMOND_ORE) {
											multiset.add(MapColor.DIAMOND, 1000);
										} else if (state.getBlock() == Blocks.EMERALD_ORE) {
											multiset.add(MapColor.EMERALD, 1000);
										} else if (state.getBlock() != Blocks.AIR && state.getBlock().getUnlocalizedName().toLowerCase().contains("ore")) // TODO 1.10: improve this 0.o
										{
											// any other ore, catchall
											multiset.add(MapColor.PINK, 1000);
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

								MapColor mapcolor = (MapColor) Iterables.getFirst(Multisets.<MapColor>copyHighestCountFirst(multiset), MapColor.AIR);

                                /*if (mapcolor == MapColor.WATER)
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
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int slot, boolean isSelected) {
		if (!worldIn.isRemote) {
			TFMazeMapData mapdata = this.getMapData(stack, worldIn);

			if (entityIn instanceof EntityPlayer) {
				EntityPlayer entityplayer = (EntityPlayer) entityIn;
				mapdata.updateVisiblePlayers(entityplayer, stack);

				// TF - if player is far away vertically, show a dot
				int yProximity = MathHelper.floor(entityplayer.posY - mapdata.yCenter);
				if (yProximity < -YSEARCH || yProximity > YSEARCH) {
					MapDecoration decoration = mapdata.mapDecorations.get(entityplayer.getName());
					if (decoration != null) {
						mapdata.mapDecorations.put(entityplayer.getName(), new MapDecoration(MapDecoration.Type.PLAYER_OFF_MAP, decoration.getX(), decoration.getY(), decoration.getRotation()));
					}
				}
			}

			if (isSelected || entityIn instanceof EntityPlayer && ((EntityPlayer) entityIn).getHeldItemOffhand() == stack) {
				this.updateMapData(worldIn, entityIn, mapdata);
			}
		}
	}

	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		// disable zooming
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return mapOres ? EnumRarity.EPIC : EnumRarity.UNCOMMON;
	}

	@Nullable
	public Packet<?> createMapDataPacket(ItemStack stack, World worldIn, EntityPlayer player) {
		Packet<?> p = super.createMapDataPacket(stack, worldIn, player);
		if (p instanceof SPacketMaps) {
			return TFPacketHandler.CHANNEL.getPacketFrom(new PacketMazeMap((SPacketMaps) p));
		} else {
			return p;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelLoader.setCustomMeshDefinition(this, stack -> new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
