package twilightforest.item;

import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketMaps;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TFFeature;
import twilightforest.TFMagicMapData;
import twilightforest.TFPacketHandler;
import twilightforest.biomes.TFBiomes;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.network.PacketMagicMap;

import javax.annotation.Nullable;
import java.util.HashMap;

public class ItemTFMagicMap extends ItemMap implements ModelRegisterCallback {
	public static final String STR_ID = "magicmap";
	private static final HashMap<Biome, MapColorBrightness> BIOME_COLORS = new HashMap<>();

	private static class MapColorBrightness {
		public MapColor color;
		public int brightness;

		public MapColorBrightness(MapColor color, int brightness) {
			this.color = color;
			this.brightness = brightness;
		}

		public MapColorBrightness(MapColor color) {
			this.color = color;
			this.brightness = 1;
		}
	}

	// [VanillaCopy] super with own id
	public static ItemStack setupNewMap(World world, double worldX, double worldZ, byte scale, boolean trackingPosition, boolean unlimitedTracking) {
		// TF - own item and string id
		ItemStack itemstack = new ItemStack(TFItems.magic_map, 1, world.getUniqueDataId(ItemTFMagicMap.STR_ID));
		String s = ItemTFMagicMap.STR_ID + "_" + itemstack.getMetadata();
		MapData mapdata = new TFMagicMapData(s);
		world.setData(s, mapdata);
		mapdata.scale = scale;
		mapdata.calculateMapCenter(worldX, worldZ, mapdata.scale);
		mapdata.dimension = world.provider.getDimension();
		mapdata.trackingPosition = trackingPosition;
		mapdata.unlimitedTracking = unlimitedTracking;
		mapdata.markDirty();
		return itemstack;
	}

	// [VanillaCopy] super, with own string ID and class, narrowed types
	@Nullable
	@SideOnly(Side.CLIENT)
	public static TFMagicMapData loadMapData(int mapId, World worldIn) {
		String s = STR_ID + "_" + mapId;
		return (TFMagicMapData) worldIn.loadData(TFMagicMapData.class, s);
	}

	// [VanillaCopy] super, with own string ID and class
	@Override
	public TFMagicMapData getMapData(ItemStack stack, World worldIn) {
		String s = STR_ID + "_" + stack.getMetadata();
		TFMagicMapData mapdata = (TFMagicMapData) worldIn.loadData(TFMagicMapData.class, s);

		if (mapdata == null && !worldIn.isRemote) {
			stack.setItemDamage(worldIn.getUniqueDataId(STR_ID));
			s = STR_ID + "_" + stack.getMetadata();
			mapdata = new TFMagicMapData(s);
			mapdata.scale = 3;
			mapdata.calculateMapCenter((double) worldIn.getWorldInfo().getSpawnX(), (double) worldIn.getWorldInfo().getSpawnZ(), mapdata.scale);
			mapdata.dimension = worldIn.provider.getDimension();
			mapdata.markDirty();
			worldIn.setData(s, mapdata);
		}

		return mapdata;
	}

	@Override
	public void updateMapData(World world, Entity viewer, MapData data) {
		if (world.provider.getDimension() == data.dimension && viewer instanceof EntityPlayer) {
			int biomesPerPixel = 4;
			int blocksPerPixel = 16; // don't even bother with the scale, just hardcode it
			int centerX = data.xCenter;
			int centerZ = data.zCenter;
			int viewerX = MathHelper.floor(viewer.posX - (double) centerX) / blocksPerPixel + 64;
			int viewerZ = MathHelper.floor(viewer.posZ - (double) centerZ) / blocksPerPixel + 64;
			int viewRadiusPixels = 512 / blocksPerPixel;

			// use the generation map, which is larger scale than the other biome map
			int startX = (centerX / blocksPerPixel - 64) * biomesPerPixel;
			int startZ = (centerZ / blocksPerPixel - 64) * biomesPerPixel;
			Biome[] biomes = world.getBiomeProvider().getBiomesForGeneration((Biome[]) null, startX, startZ, 128 * biomesPerPixel, 128 * biomesPerPixel);

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
						if (overBiome == TFBiomes.stream || downBiome == TFBiomes.stream) {
							biome = TFBiomes.stream;
						}

						MapColorBrightness colorBrightness = this.getMapColorPerBiome(world, biome);

						MapColor mapcolor = colorBrightness.color;
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
							if (TFFeature.isInFeatureChunk(world, worldX, worldZ)) {
								byte mapX = (byte) ((worldX - centerX) / (float) blocksPerPixel * 2F);
								byte mapZ = (byte) ((worldZ - centerZ) / (float) blocksPerPixel * 2F);
								TFFeature feature = TFFeature.getFeatureAt(worldX, worldZ, world);
								TFMagicMapData tfData = (TFMagicMapData) data;
								tfData.tfDecorations.add(new TFMagicMapData.TFMapDecoration(feature.featureID, mapX, mapZ, (byte) 8));
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
			ItemTFMagicMap.setupBiomeColors();
		}

		if (BIOME_COLORS.containsKey(biome)) {
			return BIOME_COLORS.get(biome);
		} else {
			return new MapColorBrightness(biome.topBlock.getMapColor(world, BlockPos.ORIGIN));
		}
	}

	private static void setupBiomeColors() {
		BIOME_COLORS.put(TFBiomes.twilightForest, new MapColorBrightness(MapColor.FOLIAGE, 1));
		BIOME_COLORS.put(TFBiomes.denseTwilightForest, new MapColorBrightness(MapColor.FOLIAGE, 0));
		BIOME_COLORS.put(TFBiomes.tfLake, new MapColorBrightness(MapColor.WATER, 3));
		BIOME_COLORS.put(TFBiomes.stream, new MapColorBrightness(MapColor.WATER, 1));
		BIOME_COLORS.put(TFBiomes.tfSwamp, new MapColorBrightness(MapColor.DIAMOND, 3));
		BIOME_COLORS.put(TFBiomes.fireSwamp, new MapColorBrightness(MapColor.NETHERRACK, 1));
		BIOME_COLORS.put(TFBiomes.clearing, new MapColorBrightness(MapColor.GRASS, 2));
		BIOME_COLORS.put(TFBiomes.oakSavanna, new MapColorBrightness(MapColor.GRASS, 0));
		BIOME_COLORS.put(TFBiomes.highlands, new MapColorBrightness(MapColor.DIRT, 0));
		BIOME_COLORS.put(TFBiomes.thornlands, new MapColorBrightness(MapColor.WOOD, 3));
		BIOME_COLORS.put(TFBiomes.highlandsCenter, new MapColorBrightness(MapColor.SILVER, 2));
		BIOME_COLORS.put(TFBiomes.fireflyForest, new MapColorBrightness(MapColor.EMERALD, 1));
		BIOME_COLORS.put(TFBiomes.darkForest, new MapColorBrightness(MapColor.GREEN, 3));
		BIOME_COLORS.put(TFBiomes.darkForestCenter, new MapColorBrightness(MapColor.ADOBE, 3));
		BIOME_COLORS.put(TFBiomes.snowy_forest, new MapColorBrightness(MapColor.SNOW, 1));
		BIOME_COLORS.put(TFBiomes.glacier, new MapColorBrightness(MapColor.ICE, 1));
		BIOME_COLORS.put(TFBiomes.mushrooms, new MapColorBrightness(MapColor.ADOBE, 0));
		BIOME_COLORS.put(TFBiomes.deepMushrooms, new MapColorBrightness(MapColor.PINK, 0));
		BIOME_COLORS.put(TFBiomes.enchantedForest, new MapColorBrightness(MapColor.LIME, 2));
	}

	@Override
	public void onCreated(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
		// disable zooming
	}

	@Override
	public EnumRarity getRarity(ItemStack par1ItemStack) {
		return EnumRarity.UNCOMMON;
	}

	@Nullable
	public Packet<?> createMapDataPacket(ItemStack stack, World world, EntityPlayer player) {
		Packet<?> p = super.createMapDataPacket(stack, world, player);
		if (p instanceof SPacketMaps) {
			TFMagicMapData mapdata = getMapData(stack, world);
			return TFPacketHandler.CHANNEL.getPacketFrom(new PacketMagicMap(stack.getItemDamage(), mapdata, (SPacketMaps) p));
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
