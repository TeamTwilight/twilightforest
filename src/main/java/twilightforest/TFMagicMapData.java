package twilightforest;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.MapDecoration;
import twilightforest.world.TFBiomeProvider;

import java.util.ArrayList;
import java.util.List;

public class TFMagicMapData extends MapData {
	public final List<MapDecoration> featuresVisibleOnMap = new ArrayList<>();

	public TFMagicMapData(String name) {
		super(name);
	}

	@Override
	public void readFromNBT(NBTTagCompound cmp) {
		super.readFromNBT(cmp);

		byte[] featureStorage = cmp.getByteArray("features");
		if (featureStorage.length > 0) {
			this.deserializeFeatures(featureStorage);
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound cmp) {
		cmp = super.writeToNBT(cmp);

		if (this.featuresVisibleOnMap.size() > 0) {
			cmp.setByteArray("features", serializeFeatures());
		}

		return cmp;
	}

	/**
	 * Adds a twilight forest feature to the map.
	 */
	public void addFeatureToMap(TFFeature feature, int x, int z) {
		byte relativeX = (byte) ((x - this.xCenter) >> this.scale);
		byte relativeZ = (byte) ((z - this.zCenter) >> this.scale);
		byte rangeX = 64;
		byte rangeY = 64;

		if (relativeX >= (-rangeX) && relativeZ >= (-rangeY) && relativeX <= rangeX && relativeZ <= rangeY) {
			byte markerIcon = (byte) feature.featureID;
			byte mapX = (byte) (relativeX << 1);
			byte mapZ = (byte) (relativeZ << 1);
			byte mapRotation = 8;

			// look for a feature already at those coordinates
			for (MapDecoration existingCoord : featuresVisibleOnMap) {
				if (existingCoord.getX() == mapX && existingCoord.getY() == mapZ) {
					return;
				}
			}

			// if we didn't find it, add it
			// FIXME 1.11 decoration type
			this.featuresVisibleOnMap.add(new MapDecoration(MapDecoration.Type.RED_MARKER, mapX, mapZ, mapRotation));
		}
	}

	/**
	 * Checks existing features against the feature cache changes wrong ones
	 */
	public void checkExistingFeatures(World world) {
		List<MapDecoration> toRemove = new ArrayList<>();
		List<MapDecoration> toAdd = new ArrayList<>();

		for (MapDecoration coord : featuresVisibleOnMap) {
			int worldX = (coord.getX() << this.scale - 1) + this.xCenter;
			int worldZ = (coord.getY() << this.scale - 1) + this.zCenter;

			if (world != null && world.getBiomeProvider() instanceof TFBiomeProvider) {

				// FIXME 1.11 decoration type
				byte trueId = (byte) TFFeature.getFeatureID(worldX, worldZ, world);
				if (coord.getType() != MapDecoration.Type.RED_MARKER) {
					toRemove.add(coord);
					toAdd.add(new MapDecoration(MapDecoration.Type.RED_MARKER, coord.getX(), coord.getY(), coord.getRotation()));
				}
			}
		}

		featuresVisibleOnMap.removeAll(toRemove);
		featuresVisibleOnMap.addAll(toAdd);
	}

	public void deserializeFeatures(byte[] arr) {
		this.featuresVisibleOnMap.clear();

		for (int i = 0; i < arr.length / 3; ++i) {
			byte markerIcon = arr[i * 3];
			byte mapX = arr[i * 3 + 1];
			byte mapZ = arr[i * 3 + 2];
			byte mapRotation = 8;
			// FIXME 1.11 decoration type
			this.featuresVisibleOnMap.add(new MapDecoration(MapDecoration.Type.RED_MARKER, mapX, mapZ, mapRotation));
		}
	}

	public byte[] serializeFeatures() {
		byte[] storage = new byte[this.featuresVisibleOnMap.size() * 3];

		for (int i = 0; i < featuresVisibleOnMap.size(); ++i) {
			MapDecoration featureCoord = this.featuresVisibleOnMap.get(i);
			// FIXME 1.11 decoration type
			storage[i * 3] = (byte) featureCoord.getType().ordinal();
			storage[i * 3 + 1] = featureCoord.getX();
			storage[i * 3 + 2] = featureCoord.getY();
		}

		return storage;
	}

	// VanillaCopy of super, but adjust origin
	@Override
	public void calculateMapCenter(double x, double z, int mapScale) {
		// magic maps are offset by 1024 from normal maps so that 0,0 is in the middle of the map containing those coords
		int mapSize = 128 * (1 << mapScale);
		int roundX = (int) Math.round(x / mapSize);
		int roundZ = (int) Math.round(z / mapSize);
		this.xCenter = roundX * mapSize;
		this.zCenter = roundZ * mapSize;
	}
}
