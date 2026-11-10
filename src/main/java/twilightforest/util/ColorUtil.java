package twilightforest.util;

import com.google.common.collect.ImmutableMap;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.AbstractMap;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;

public record ColorUtil(Function<DyeColor, Block> function) {
	public static final ColorUtil WOOL = new ColorUtil(color -> switch (color) {
		default -> Blocks.WOOL.white();
		case ORANGE -> Blocks.WOOL.orange();
		case MAGENTA -> Blocks.WOOL.magenta();
		case LIGHT_BLUE -> Blocks.WOOL.lightBlue();
		case YELLOW -> Blocks.WOOL.yellow();
		case LIME -> Blocks.WOOL.lime();
		case PINK -> Blocks.WOOL.pink();
		case GRAY -> Blocks.WOOL.gray();
		case LIGHT_GRAY -> Blocks.WOOL.lightGray();
		case CYAN -> Blocks.WOOL.cyan();
		case PURPLE -> Blocks.WOOL.purple();
		case BLUE -> Blocks.WOOL.blue();
		case BROWN -> Blocks.WOOL.brown();
		case GREEN -> Blocks.WOOL.green();
		case RED -> Blocks.WOOL.red();
		case BLACK -> Blocks.WOOL.black();
	});

	public static final ColorUtil TERRACOTTA = new ColorUtil(color -> switch (color) {
		default -> Blocks.DYED_TERRACOTTA.white();
		case ORANGE -> Blocks.DYED_TERRACOTTA.orange();
		case MAGENTA -> Blocks.DYED_TERRACOTTA.magenta();
		case LIGHT_BLUE -> Blocks.DYED_TERRACOTTA.lightBlue();
		case YELLOW -> Blocks.DYED_TERRACOTTA.yellow();
		case LIME -> Blocks.DYED_TERRACOTTA.lime();
		case PINK -> Blocks.DYED_TERRACOTTA.pink();
		case GRAY -> Blocks.DYED_TERRACOTTA.gray();
		case LIGHT_GRAY -> Blocks.DYED_TERRACOTTA.lightGray();
		case CYAN -> Blocks.DYED_TERRACOTTA.cyan();
		case PURPLE -> Blocks.DYED_TERRACOTTA.purple();
		case BLUE -> Blocks.DYED_TERRACOTTA.blue();
		case BROWN -> Blocks.DYED_TERRACOTTA.brown();
		case GREEN -> Blocks.DYED_TERRACOTTA.green();
		case RED -> Blocks.DYED_TERRACOTTA.red();
		case BLACK -> Blocks.DYED_TERRACOTTA.black();
	});

	public static final ColorUtil STAINED_GLASS = new ColorUtil(color -> switch (color) {
		default -> Blocks.STAINED_GLASS.white();
		case ORANGE -> Blocks.STAINED_GLASS.orange();
		case MAGENTA -> Blocks.STAINED_GLASS.magenta();
		case LIGHT_BLUE -> Blocks.STAINED_GLASS.lightBlue();
		case YELLOW -> Blocks.STAINED_GLASS.yellow();
		case LIME -> Blocks.STAINED_GLASS.lime();
		case PINK -> Blocks.STAINED_GLASS.pink();
		case GRAY -> Blocks.STAINED_GLASS.gray();
		case LIGHT_GRAY -> Blocks.STAINED_GLASS.lightGray();
		case CYAN -> Blocks.STAINED_GLASS.cyan();
		case PURPLE -> Blocks.STAINED_GLASS.purple();
		case BLUE -> Blocks.STAINED_GLASS.blue();
		case BROWN -> Blocks.STAINED_GLASS.brown();
		case GREEN -> Blocks.STAINED_GLASS.green();
		case RED -> Blocks.STAINED_GLASS.red();
		case BLACK -> Blocks.STAINED_GLASS.black();
	});


	public BlockState getColor(DyeColor color) {
		return this.function.apply(color).defaultBlockState();
	}

	public Block getRandomColor(Random rand) {
		DyeColor color = DyeColor.byId(rand.nextInt(16));
		return this.getColor(color).getBlock();
	}

	public static float[] rgbToHSV(int r, int g, int b) {
		float h = 0;
		float s;
		float rabs = r / 255.0F;
		float gabs = g / 255.0F;
		float babs = b / 255.0F;
		float v = Math.max(rabs, Math.max(gabs, babs));
		float diff = v - Math.min(rabs, Math.min(gabs, babs));
		Function<Float, Float> diffc = c -> (v - c) / 6 / diff + 1 / 2;
		if (diff == 0) {
			h = s = 0;
		} else {
			s = diff / v;
			float rr = diffc.apply(rabs);
			float gg = diffc.apply(gabs);
			float bb = diffc.apply(babs);

			if (rabs == v) {
				h = bb - gg;
			} else if (gabs == v) {
				h = (1.0F / 3.0F) + rr - bb;
			} else if (babs == v) {
				h = (2.0F / 3.0F) + gg - rr;
			}
			if (h < 0) {
				h += 1;
			} else if (h > 1) {
				h -= 1;
			}
		}
		return new float[]{h, s, v};
	}

	public static int hsvToRGB(float hue, float saturation, float value) {
		final float normaliedHue = (hue - (float) Math.floor(hue));
		final int h = (int) (normaliedHue * 6);
		final float f = normaliedHue * 6 - h;
		final float p = value * (1 - saturation);
		final float q = value * (1 - f * saturation);
		final float t = value * (1 - (1 - f) * saturation);

		return switch (h) {
			case 0 -> rgb(value, t, p);
			case 1 -> rgb(q, value, p);
			case 2 -> rgb(p, value, t);
			case 3 -> rgb(p, q, value);
			case 4 -> rgb(t, p, value);
			case 5 -> rgb(value, p, q);
			default -> throw new RuntimeException("Something went wrong when converting from HSV to RGB. Input was " + hue + ", " + saturation + ", " + value);
		};
	}

	private static int rgb(float r, float g, float b) {
		return (((int) ((r * 255F) + 0.5F) & 0xFF) << 16) | (((int) ((g * 255F) + 0.5F) & 0xFF) << 8) | ((int) ((b * 255F) + 0.5F) & 0xFF);
	}

	public static int argbToABGR(int argbColor) {
		int r = (argbColor >> 16) & 0xFF;
		int b = argbColor & 0xFF;
		return (argbColor & 0xFF00FF00) | (b << 16) | r;
	}

	//We COULD use the WOOL method at the very top of this class, but then we have to use the order of the dyecolor enum, which doesnt show the wools in the order the ram displays them.
	//I personally like this order better so suck it
	public static final Map<DyeColor, Block> WOOL_TO_DYE_IN_RAM_ORDER = ImmutableMap.ofEntries(
		entryOf(DyeColor.WHITE, Blocks.WOOL.white()), entryOf(DyeColor.LIGHT_GRAY, Blocks.WOOL.lightGray()),
		entryOf(DyeColor.GRAY, Blocks.WOOL.gray()), entryOf(DyeColor.BLACK, Blocks.WOOL.black()),
		entryOf(DyeColor.RED, Blocks.WOOL.red()), entryOf(DyeColor.ORANGE, Blocks.WOOL.orange()),
		entryOf(DyeColor.YELLOW, Blocks.WOOL.yellow()), entryOf(DyeColor.LIME, Blocks.WOOL.lime()),
		entryOf(DyeColor.GREEN, Blocks.WOOL.green()), entryOf(DyeColor.LIGHT_BLUE, Blocks.WOOL.lightBlue()),
		entryOf(DyeColor.CYAN, Blocks.WOOL.cyan()), entryOf(DyeColor.BLUE, Blocks.WOOL.blue()),
		entryOf(DyeColor.PURPLE, Blocks.WOOL.purple()), entryOf(DyeColor.MAGENTA, Blocks.WOOL.magenta()),
		entryOf(DyeColor.PINK, Blocks.WOOL.pink()), entryOf(DyeColor.BROWN, Blocks.WOOL.brown()));

	static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
		return new AbstractMap.SimpleImmutableEntry<>(key, value);
	}

	public static int blendColors(int a, int b, double ratio) {
		long mask1 = 0x00FF00FFL;
		long mask2 = 0xFF00FF00L;

		long f2 = (long) (256 * ratio);
		long f1 = 256 - f2;

		long al = a & 0xFFFFFFFFL;
		long bl = b & 0xFFFFFFFFL;

		return (int) (((((al & mask1) * f1 + (bl & mask1) * f2) >> 8) & mask1)
			| (((((al & mask2) * f1 + (bl & mask2) * f2) >> 8) & mask2)));
	}
}
