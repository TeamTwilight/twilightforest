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
		default -> Blocks.WHITE_WOOL;
		case ORANGE -> Blocks.ORANGE_WOOL;
		case MAGENTA -> Blocks.MAGENTA_WOOL;
		case LIGHT_BLUE -> Blocks.LIGHT_BLUE_WOOL;
		case YELLOW -> Blocks.YELLOW_WOOL;
		case LIME -> Blocks.LIME_WOOL;
		case PINK -> Blocks.PINK_WOOL;
		case GRAY -> Blocks.GRAY_WOOL;
		case LIGHT_GRAY -> Blocks.LIGHT_GRAY_WOOL;
		case CYAN -> Blocks.CYAN_WOOL;
		case PURPLE -> Blocks.PURPLE_WOOL;
		case BLUE -> Blocks.BLUE_WOOL;
		case BROWN -> Blocks.BROWN_WOOL;
		case GREEN -> Blocks.GREEN_WOOL;
		case RED -> Blocks.RED_WOOL;
		case BLACK -> Blocks.BLACK_WOOL;
	});

	public static final ColorUtil TERRACOTTA = new ColorUtil(color -> switch (color) {
		default -> Blocks.WHITE_TERRACOTTA;
		case ORANGE -> Blocks.ORANGE_TERRACOTTA;
		case MAGENTA -> Blocks.MAGENTA_TERRACOTTA;
		case LIGHT_BLUE -> Blocks.LIGHT_BLUE_TERRACOTTA;
		case YELLOW -> Blocks.YELLOW_TERRACOTTA;
		case LIME -> Blocks.LIME_TERRACOTTA;
		case PINK -> Blocks.PINK_TERRACOTTA;
		case GRAY -> Blocks.GRAY_TERRACOTTA;
		case LIGHT_GRAY -> Blocks.LIGHT_GRAY_TERRACOTTA;
		case CYAN -> Blocks.CYAN_TERRACOTTA;
		case PURPLE -> Blocks.PURPLE_TERRACOTTA;
		case BLUE -> Blocks.BLUE_TERRACOTTA;
		case BROWN -> Blocks.BROWN_TERRACOTTA;
		case GREEN -> Blocks.GREEN_TERRACOTTA;
		case RED -> Blocks.RED_TERRACOTTA;
		case BLACK -> Blocks.BLACK_TERRACOTTA;
	});

	public static final ColorUtil STAINED_GLASS = new ColorUtil(color -> switch (color) {
		default -> Blocks.WHITE_STAINED_GLASS;
		case ORANGE -> Blocks.ORANGE_STAINED_GLASS;
		case MAGENTA -> Blocks.MAGENTA_STAINED_GLASS;
		case LIGHT_BLUE -> Blocks.LIGHT_BLUE_STAINED_GLASS;
		case YELLOW -> Blocks.YELLOW_STAINED_GLASS;
		case LIME -> Blocks.LIME_STAINED_GLASS;
		case PINK -> Blocks.PINK_STAINED_GLASS;
		case GRAY -> Blocks.GRAY_STAINED_GLASS;
		case LIGHT_GRAY -> Blocks.LIGHT_GRAY_STAINED_GLASS;
		case CYAN -> Blocks.CYAN_STAINED_GLASS;
		case PURPLE -> Blocks.PURPLE_STAINED_GLASS;
		case BLUE -> Blocks.BLUE_STAINED_GLASS;
		case BROWN -> Blocks.BROWN_STAINED_GLASS;
		case GREEN -> Blocks.GREEN_STAINED_GLASS;
		case RED -> Blocks.RED_STAINED_GLASS;
		case BLACK -> Blocks.BLACK_STAINED_GLASS;
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
			}else if (h > 1) {
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
		return (((int)((r * 255F) + 0.5F) & 0xFF) << 16) | (((int)((g * 255F) + 0.5F) & 0xFF) << 8) | ((int)((b * 255F) + 0.5F) & 0xFF);
	}

	public static int argbToABGR(int argbColor) {
		int r = (argbColor >> 16) & 0xFF;
		int b = argbColor & 0xFF;
		return (argbColor & 0xFF00FF00) | (b << 16) | r;
	}

	//We COULD use the WOOL method at the very top of this class, but then we have to use the order of the dyecolor enum, which doesnt show the wools in the order the ram displays them.
	//I personally like this order better so suck it
	public static final Map<DyeColor, Block> WOOL_TO_DYE_IN_RAM_ORDER = ImmutableMap.ofEntries(
			entryOf(DyeColor.WHITE, Blocks.WHITE_WOOL), entryOf(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_WOOL),
			entryOf(DyeColor.GRAY, Blocks.GRAY_WOOL), entryOf(DyeColor.BLACK, Blocks.BLACK_WOOL),
			entryOf(DyeColor.RED, Blocks.RED_WOOL), entryOf(DyeColor.ORANGE, Blocks.ORANGE_WOOL),
			entryOf(DyeColor.YELLOW, Blocks.YELLOW_WOOL), entryOf(DyeColor.LIME, Blocks.LIME_WOOL),
			entryOf(DyeColor.GREEN, Blocks.GREEN_WOOL), entryOf(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_WOOL),
			entryOf(DyeColor.CYAN, Blocks.CYAN_WOOL), entryOf(DyeColor.BLUE, Blocks.BLUE_WOOL),
			entryOf(DyeColor.PURPLE, Blocks.PURPLE_WOOL), entryOf(DyeColor.MAGENTA, Blocks.MAGENTA_WOOL),
			entryOf(DyeColor.PINK, Blocks.PINK_WOOL), entryOf(DyeColor.BROWN, Blocks.BROWN_WOOL));

	static <K, V> Map.Entry<K, V> entryOf(K key, V value) {
		return new AbstractMap.SimpleImmutableEntry<>(key, value);
	}
}
