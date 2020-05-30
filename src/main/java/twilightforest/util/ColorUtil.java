package twilightforest.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;

import java.util.Random;
import java.util.function.Function;

public class ColorUtil {
	public static final ColorUtil WOOL = new ColorUtil(color -> {
		switch (color) {
		default:
		case WHITE: return Blocks.WHITE_WOOL;
		case ORANGE: return Blocks.ORANGE_WOOL;
		case MAGENTA: return Blocks.MAGENTA_WOOL;
		case LIGHT_BLUE: return Blocks.LIGHT_BLUE_WOOL;
		case YELLOW: return Blocks.YELLOW_WOOL;
		case LIME: return Blocks.LIME_WOOL;
		case PINK: return Blocks.PINK_WOOL;
		case GRAY: return Blocks.GRAY_WOOL;
		case LIGHT_GRAY: return Blocks.LIGHT_GRAY_WOOL;
		case CYAN: return Blocks.CYAN_WOOL;
		case PURPLE: return Blocks.PURPLE_WOOL;
		case BLUE: return Blocks.BLUE_WOOL;
		case BROWN: return Blocks.BROWN_WOOL;
		case GREEN: return Blocks.GREEN_WOOL;
		case RED: return Blocks.RED_WOOL;
		case BLACK: return Blocks.BLACK_WOOL;
		}
	});
	public static final ColorUtil TERRACOTTA = new ColorUtil(color -> {
		switch (color) {
		default:
		case WHITE: return Blocks.WHITE_TERRACOTTA;
		case ORANGE: return Blocks.ORANGE_TERRACOTTA;
		case MAGENTA: return Blocks.MAGENTA_TERRACOTTA;
		case LIGHT_BLUE: return Blocks.LIGHT_BLUE_TERRACOTTA;
		case YELLOW: return Blocks.YELLOW_TERRACOTTA;
		case LIME: return Blocks.LIME_TERRACOTTA;
		case PINK: return Blocks.PINK_TERRACOTTA;
		case GRAY: return Blocks.GRAY_TERRACOTTA;
		case LIGHT_GRAY: return Blocks.LIGHT_GRAY_TERRACOTTA;
		case CYAN: return Blocks.CYAN_TERRACOTTA;
		case PURPLE: return Blocks.PURPLE_TERRACOTTA;
		case BLUE: return Blocks.BLUE_TERRACOTTA;
		case BROWN: return Blocks.BROWN_TERRACOTTA;
		case GREEN: return Blocks.GREEN_TERRACOTTA;
		case RED: return Blocks.RED_TERRACOTTA;
		case BLACK: return Blocks.BLACK_TERRACOTTA;
		}
	});
	public static final ColorUtil STAINED_GLASS = new ColorUtil(color -> {
		switch (color) {
		default:
		case WHITE: return Blocks.WHITE_STAINED_GLASS;
		case ORANGE: return Blocks.ORANGE_STAINED_GLASS;
		case MAGENTA: return Blocks.MAGENTA_STAINED_GLASS;
		case LIGHT_BLUE: return Blocks.LIGHT_BLUE_STAINED_GLASS;
		case YELLOW: return Blocks.YELLOW_STAINED_GLASS;
		case LIME: return Blocks.LIME_STAINED_GLASS;
		case PINK: return Blocks.PINK_STAINED_GLASS;
		case GRAY: return Blocks.GRAY_STAINED_GLASS;
		case LIGHT_GRAY: return Blocks.LIGHT_GRAY_STAINED_GLASS;
		case CYAN: return Blocks.CYAN_STAINED_GLASS;
		case PURPLE: return Blocks.PURPLE_STAINED_GLASS;
		case BLUE: return Blocks.BLUE_STAINED_GLASS;
		case BROWN: return Blocks.BROWN_STAINED_GLASS;
		case GREEN: return Blocks.GREEN_STAINED_GLASS;
		case RED: return Blocks.RED_STAINED_GLASS;
		case BLACK: return Blocks.BLACK_STAINED_GLASS;
		}
	});
	
	
	private final Function<DyeColor, Block> function;
	private ColorUtil(Function<DyeColor, Block> function) {
		this.function = function;
	}

	public BlockState getColor(DyeColor color) {
		return function.apply(color).getDefaultState();
	}

	public Block getRandomColor(Random rand) {
		DyeColor color = DyeColor.byId(rand.nextInt(16));
		return getColor(color).getBlock();
	}
}
