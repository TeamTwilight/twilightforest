package twilightforest;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.MapData;
import twilightforest.world.TFGenerationSettings;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

public class TFMazeMapData extends MapData {
	private static final Map<World, Map<String, TFMazeMapData>> CLIENT_DATA = new WeakHashMap<>();

	public int yCenter;

	public TFMazeMapData(String name) {
		super(name);
	}

	@Override
	public void read(CompoundNBT nbt) {
		super.read(nbt);
		this.yCenter = nbt.getInt("yCenter");
	}

	@Override
	public CompoundNBT write(CompoundNBT nbt) {
		CompoundNBT ret = super.write(nbt);
		ret.putInt("yCenter", this.yCenter);
		return ret;
	}

	public void calculateMapCenter(World world, int x, int y, int z, int mapScale) {
		super.calculateMapCenter(x, z, mapScale);
		this.yCenter = y;

		// when we are in a labyrinth, snap to the LABYRINTH
		if (world instanceof ServerWorld && TFGenerationSettings.isTwilightChunk((ServerWorld) world)) {
			if (TFFeature.getFeatureForRegion(x >> 4, z >> 4, (ServerWorld) world) == TFFeature.LABYRINTH) {
				BlockPos mc = TFFeature.getNearestCenterXYZ(x >> 4, z >> 4);
				this.xCenter = mc.getX();
				this.zCenter = mc.getZ();
			}
		}
	}

	// [VanillaCopy] Adapted from World.getMapData
	@Nullable
	public static TFMazeMapData getMazeMapData(World world, String name) {
		if (world.isRemote) {
			return CLIENT_DATA.getOrDefault(world, Collections.emptyMap()).get(name);
		} else {
			return world.getServer().getWorld(World.OVERWORLD).getSavedData().get(() -> new TFMazeMapData(name), name);
		}
	}

	// [VanillaCopy] Adapted from World.registerMapData
	public static void registerMazeMapData(World world, TFMazeMapData data) {
		if (world.isRemote) {
			CLIENT_DATA.computeIfAbsent(world, k -> new HashMap<>()).put(data.getName(), data);
		} else {
			world.getServer().getWorld(World.OVERWORLD).getSavedData().set(data);
		}
	}
}
