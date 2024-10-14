package twilightforest.world.components;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.FoliageColor;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.biome.Biome;
import twilightforest.beans.Component;
import twilightforest.util.ColorUtil;
import twilightforest.util.landmarks.LegacyLandmarkPlacements;
import twilightforest.world.components.structures.type.QuestGroveStructure;

@Component
public class BiomeColorAlgorithms {

	public int enchanted(int originalColor, int x, int z) {  // TODO
		BlockPos center = LegacyLandmarkPlacements.getNearestCenterXZ(x / 16, z / 16);  // Center is quest grove
		int cx = center.getX();
		int cz = center.getZ();

		int dist = (int) Mth.sqrt((cx - x) * (cx - x) + (cz - z) * (cz - z));
		int color = dist * 16;
		color %= 512;

		if (color > 255) {
			color = 511 - color;
		}

		color = 255 - color;

		return (originalColor & 0xFFFF00) + color;
	}

	// FIXME Flat color, resolve
	public int swamp(Type modifierType) {
		int modifiedColor = switch (modifierType) {
			case Grass -> GrassColor.get(0.8F, 0.9F);
			case Foliage -> FoliageColor.get(0.8F, 0.9F);
		};
		return ((modifiedColor & 0xFEFEFE) + 0x4E0E4E) / 2;
	}

	// FIXME Flat color, resolve
	public int darkForest(Type modifierType) {
		int modifiedColor = switch (modifierType) {
			case Grass -> GrassColor.get(0.7F, 0.8F);
			case Foliage -> FoliageColor.get(0.7F, 0.8F);
		};
		return ((modifiedColor & 0xFEFEFE) + 0x1E0E4E) / 2;
	}

	public int darkForestCenterGrass(double x, double z) {
		double noise = Biome.TEMPERATURE_NOISE.getValue(x * 0.0225D, z * 0.0225D, false); //TODO: Check
		return noise < -0.2D ? 0x667540 : 0x554114;
	}

	public int darkForestCenterFoliage(double x, double z) {
		double noise = (Biome.TEMPERATURE_NOISE.getValue(x * 0.0225D, z * 0.0225D, false) + 1D) / 2D;
		return noise < -0.1D ? 0xF9821E : 0xE94E14;
	}

	public double spookyNoise(double x, double z) {
		return (Biome.TEMPERATURE_NOISE.getValue(x * 0.0225D, z * 0.0225D, false) + 1D) / 2D;
	}

	public int spookyGrass(double x, double z) {
		double noise = spookyNoise(x, z);
		return ColorUtil.blendColors(0xc43323, 0x5BC423, noise > 0.6D ? noise * 0.1D : noise);
	}

	public int spookyFoliage(double x, double z) {
		double noise = spookyNoise(x, z);
		return ColorUtil.blendColors(0xFF0101, 0x49FF01, noise > 0.6D ? noise * 0.2D : noise);
	}

	public enum Type {
		Grass, Foliage
	}

}
