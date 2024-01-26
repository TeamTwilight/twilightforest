package twilightforest.init.custom;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import twilightforest.TFRegistries;
import twilightforest.TwilightForestMod;
import twilightforest.init.TFBlocks;
import twilightforest.util.WoodPalette;

import java.util.Locale;

public class WoodPalettes {
	public static final Codec<Holder<WoodPalette>> CODEC = RegistryFileCodec.create(TFRegistries.Keys.WOOD_PALETTES, WoodPalette.CODEC, false);

	public static final ResourceKey<WoodPalette> OAK = makeKey(new ResourceLocation("oak"));
	public static final ResourceKey<WoodPalette> SPRUCE = makeKey(new ResourceLocation("spruce"));
	public static final ResourceKey<WoodPalette> BIRCH = makeKey(new ResourceLocation("birch"));
	public static final ResourceKey<WoodPalette> JUNGLE = makeKey(new ResourceLocation("jungle"));
	public static final ResourceKey<WoodPalette> ACACIA = makeKey(new ResourceLocation("acacia"));
	public static final ResourceKey<WoodPalette> DARK_OAK = makeKey(new ResourceLocation("dark_oak"));
	public static final ResourceKey<WoodPalette> CRIMSON = makeKey(new ResourceLocation("crimson"));
	public static final ResourceKey<WoodPalette> WARPED = makeKey(new ResourceLocation("warped"));
	public static final ResourceKey<WoodPalette> VANGROVE = makeKey(new ResourceLocation("mangrove"));

	public static final ResourceKey<WoodPalette> TWILIGHT_OAK = makeKey("twilight_oak");
	public static final ResourceKey<WoodPalette> CANOPY = makeKey("canopy");
	public static final ResourceKey<WoodPalette> MANGROVE = makeKey("mangrove");
	public static final ResourceKey<WoodPalette> DARKWOOD = makeKey("darkwood");
	public static final ResourceKey<WoodPalette> TIMEWOOD = makeKey("timewood");
	public static final ResourceKey<WoodPalette> TRANSWOOD = makeKey("transwood");
	public static final ResourceKey<WoodPalette> MINEWOOD = makeKey("minewood");
	public static final ResourceKey<WoodPalette> SORTWOOD = makeKey("sortwood");

	private static ResourceKey<WoodPalette> makeKey(String name) {
		return makeKey(TwilightForestMod.prefix(name.toLowerCase(Locale.ROOT)));
	}

	private static ResourceKey<WoodPalette> makeKey(ResourceLocation name) {
		return ResourceKey.create(TFRegistries.Keys.WOOD_PALETTES, name);
	}

	public static void bootstrap(BootstapContext<WoodPalette> context) {
		context.register(OAK, new WoodPalette(Blocks.OAK_PLANKS, Blocks.OAK_STAIRS, Blocks.OAK_SLAB, Blocks.OAK_BUTTON, Blocks.OAK_FENCE, Blocks.OAK_FENCE_GATE, Blocks.OAK_PRESSURE_PLATE, TFBlocks.OAK_BANISTER.get()));
		context.register(SPRUCE, new WoodPalette(Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_BUTTON, Blocks.SPRUCE_FENCE, Blocks.SPRUCE_FENCE_GATE, Blocks.SPRUCE_PRESSURE_PLATE, TFBlocks.SPRUCE_BANISTER.get()));
		context.register(BIRCH, new WoodPalette(Blocks.BIRCH_PLANKS, Blocks.BIRCH_STAIRS, Blocks.BIRCH_SLAB, Blocks.BIRCH_BUTTON, Blocks.BIRCH_FENCE, Blocks.BIRCH_FENCE_GATE, Blocks.BIRCH_PRESSURE_PLATE, TFBlocks.BIRCH_BANISTER.get()));
		context.register(JUNGLE, new WoodPalette(Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_BUTTON, Blocks.JUNGLE_FENCE, Blocks.JUNGLE_FENCE_GATE, Blocks.JUNGLE_PRESSURE_PLATE, TFBlocks.JUNGLE_BANISTER.get()));
		context.register(ACACIA, new WoodPalette(Blocks.ACACIA_PLANKS, Blocks.ACACIA_STAIRS, Blocks.ACACIA_SLAB, Blocks.ACACIA_BUTTON, Blocks.ACACIA_FENCE, Blocks.ACACIA_FENCE_GATE, Blocks.ACACIA_PRESSURE_PLATE, TFBlocks.ACACIA_BANISTER.get()));
		context.register(DARK_OAK, new WoodPalette(Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_BUTTON, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_FENCE_GATE, Blocks.DARK_OAK_PRESSURE_PLATE, TFBlocks.DARK_OAK_BANISTER.get()));
		context.register(CRIMSON, new WoodPalette(Blocks.CRIMSON_PLANKS, Blocks.CRIMSON_STAIRS, Blocks.CRIMSON_SLAB, Blocks.CRIMSON_BUTTON, Blocks.CRIMSON_FENCE, Blocks.CRIMSON_FENCE_GATE, Blocks.CRIMSON_PRESSURE_PLATE, TFBlocks.CRIMSON_BANISTER.get()));
		context.register(WARPED, new WoodPalette(Blocks.WARPED_PLANKS, Blocks.WARPED_STAIRS, Blocks.WARPED_SLAB, Blocks.WARPED_BUTTON, Blocks.WARPED_FENCE, Blocks.WARPED_FENCE_GATE, Blocks.WARPED_PRESSURE_PLATE, TFBlocks.WARPED_BANISTER.get()));
		context.register(VANGROVE, new WoodPalette(Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_STAIRS, Blocks.MANGROVE_SLAB, Blocks.MANGROVE_BUTTON, Blocks.MANGROVE_FENCE, Blocks.MANGROVE_FENCE_GATE, Blocks.MANGROVE_PRESSURE_PLATE, TFBlocks.VANGROVE_BANISTER.get()));

		context.register(TWILIGHT_OAK, new WoodPalette(TFBlocks.TWILIGHT_OAK_PLANKS, TFBlocks.TWILIGHT_OAK_STAIRS, TFBlocks.TWILIGHT_OAK_SLAB, TFBlocks.TWILIGHT_OAK_BUTTON, TFBlocks.TWILIGHT_OAK_FENCE, TFBlocks.TWILIGHT_OAK_GATE, TFBlocks.TWILIGHT_OAK_PLATE, TFBlocks.TWILIGHT_OAK_BANISTER));
		context.register(CANOPY, new WoodPalette(TFBlocks.CANOPY_PLANKS, TFBlocks.CANOPY_STAIRS, TFBlocks.CANOPY_SLAB, TFBlocks.CANOPY_BUTTON, TFBlocks.CANOPY_FENCE, TFBlocks.CANOPY_GATE, TFBlocks.CANOPY_PLATE, TFBlocks.CANOPY_BANISTER));
		context.register(MANGROVE, new WoodPalette(TFBlocks.MANGROVE_PLANKS, TFBlocks.MANGROVE_STAIRS, TFBlocks.MANGROVE_SLAB, TFBlocks.MANGROVE_BUTTON, TFBlocks.MANGROVE_FENCE, TFBlocks.MANGROVE_GATE, TFBlocks.MANGROVE_PLATE, TFBlocks.MANGROVE_BANISTER));
		context.register(DARKWOOD, new WoodPalette(TFBlocks.DARK_PLANKS, TFBlocks.DARK_STAIRS, TFBlocks.DARK_SLAB, TFBlocks.DARK_BUTTON, TFBlocks.DARK_FENCE, TFBlocks.DARK_GATE, TFBlocks.DARK_PLATE, TFBlocks.DARK_BANISTER));
		context.register(TIMEWOOD, new WoodPalette(TFBlocks.TIME_PLANKS, TFBlocks.TIME_STAIRS, TFBlocks.TIME_SLAB, TFBlocks.TIME_BUTTON, TFBlocks.TIME_FENCE, TFBlocks.TIME_GATE, TFBlocks.TIME_PLATE, TFBlocks.TIME_BANISTER));
		context.register(TRANSWOOD, new WoodPalette(TFBlocks.TRANSFORMATION_PLANKS, TFBlocks.TRANSFORMATION_STAIRS, TFBlocks.TRANSFORMATION_SLAB, TFBlocks.TRANSFORMATION_BUTTON, TFBlocks.TRANSFORMATION_FENCE, TFBlocks.TRANSFORMATION_GATE, TFBlocks.TRANSFORMATION_PLATE, TFBlocks.TRANSFORMATION_BANISTER));
		context.register(MINEWOOD, new WoodPalette(TFBlocks.MINING_PLANKS, TFBlocks.MINING_STAIRS, TFBlocks.MINING_SLAB, TFBlocks.MINING_BUTTON, TFBlocks.MINING_FENCE, TFBlocks.MINING_GATE, TFBlocks.MINING_PLATE, TFBlocks.MINING_BANISTER));
		context.register(SORTWOOD, new WoodPalette(TFBlocks.SORTING_PLANKS, TFBlocks.SORTING_STAIRS, TFBlocks.SORTING_SLAB, TFBlocks.SORTING_BUTTON, TFBlocks.SORTING_FENCE, TFBlocks.SORTING_GATE, TFBlocks.SORTING_PLATE, TFBlocks.SORTING_BANISTER));
	}
}
