package twilightforest.worldgen.biomes;

import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class TFBiomeEnchantedForest extends TFBiomeBase {

	private final Random colorRNG;

	public TFBiomeEnchantedForest(Builder props) {
		super(props);
		colorRNG = new Random();
	}

	@Override
	public void addFeatures() {
		super.addFeatures();

		TFBiomeDecorator.addWoodRoots(this);
		TFBiomeDecorator.addOres(this);
		TFBiomeDecorator.addClayDisks(this, 1);
		TFBiomeDecorator.addLakes(this);
		TFBiomeDecorator.addRuins(this);
		TFBiomeDecorator.addSprings(this);
		TFBiomeDecorator.addPlantRoots(this);
		TFBiomeDecorator.addTorchberries(this);
		TFBiomeDecorator.addCanopy(this);
		TFBiomeDecorator.addMultipleTrees(this, TFBiomeDecorator.ENCHANTED_TREES_CONFIG, 10);
		TFBiomeDecorator.addVines(this, 20);
		TFBiomeDecorator.addTwilightGrass(this, TFBiomeDecorator.ENCHANTED_GRASS_CONFIG, 12);
		TFBiomeDecorator.addFlowersMore(this);
		TFBiomeDecorator.addTallFerns(this, 20);
		TFBiomeDecorator.addMushrooms(this);
	}

	@Override
	public int getGrassColor(double x, double z) {
		return (super.getGrassColor(x, z) & 0xFFFF00) + getEnchantedColor((int) x, (int) z); //TODO
	}

	@Override
	public int getFoliageColor() {
		//return (super.getFoliageColor() & 0xFFFF00) + getEnchantedColor(pos.getX(), pos.getZ());
		return 0xFFFF00; //FIXME: Placeholder
	}

	/**
	 * Find a number between 0 and 255.  currently returns concentric circles from the biome center
	 */
	private int getEnchantedColor(int x, int z) {
		// center of the biome is at % 256 - 8
		int cx = 256 * Math.round((x - 8) / 256F) + 8;
		int cz = 256 * Math.round((z - 8) / 256F) + 8;

		int dist = (int) MathHelper.sqrt((cx - x) * (cx - x) + (cz - z) * (cz - z));
		int color = dist * 64;
		color %= 512;

		if (color > 255) {
			color = 511 - color;
		}

		color = 255 - color;

		int randomFlicker = colorRNG.nextInt(32) - 16;

		if (0 < color + randomFlicker && color + randomFlicker > 255) {
			color += randomFlicker;
		}

		return color;
	}

//	@Override
//	protected TFFeature getContainedFeature() {
//		return TFFeature.QUEST_GROVE;
//	}
}
