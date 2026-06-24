package twilightforest.command;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.LevelResource;
import net.neoforged.fml.loading.FMLEnvironment;
import twilightforest.init.TFBiomes;
import twilightforest.init.TFDataMaps;
import twilightforest.util.ColorUtil;
import twilightforest.util.datamaps.MagicMapBiomeColor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Thank you @SuperCoder79 (from Twitter) for sharing the original code! Code sourced from a LGPL project
 */
@tamaized.beanification.Component
public class MapBiomesCommand {

	private final DecimalFormat numberFormat = new DecimalFormat("#.00");

	private final HashMap<Identifier, BiomeMapColor> BIOME2COLOR = new HashMap<>();

	private void init() {
		BIOME2COLOR.put(TFBiomes.STREAM.identifier(), new BiomeMapColor(0, 0, 255));
		BIOME2COLOR.put(TFBiomes.LAKE.identifier(), new BiomeMapColor(0, 0, 255));
		BIOME2COLOR.put(TFBiomes.CLEARING.identifier(), new BiomeMapColor(132, 245, 130));
		BIOME2COLOR.put(TFBiomes.OAK_SAVANNAH.identifier(), new BiomeMapColor(239, 245, 130));
		BIOME2COLOR.put(TFBiomes.FOREST.identifier(), new BiomeMapColor(0, 255, 0));
		BIOME2COLOR.put(TFBiomes.DENSE_FOREST.identifier(), new BiomeMapColor(0, 170, 0));
		BIOME2COLOR.put(TFBiomes.FIREFLY_FOREST.identifier(), new BiomeMapColor(88, 252, 102));
		BIOME2COLOR.put(TFBiomes.ENCHANTED_FOREST.identifier(), new BiomeMapColor(0, 255, 255));
		BIOME2COLOR.put(TFBiomes.SPOOKY_FOREST.identifier(), new BiomeMapColor(119, 0, 255));
		BIOME2COLOR.put(TFBiomes.MUSHROOM_FOREST.identifier(), new BiomeMapColor(204, 0, 139));
		BIOME2COLOR.put(TFBiomes.DENSE_MUSHROOM_FOREST.identifier(), new BiomeMapColor(184, 48, 184));

		BIOME2COLOR.put(TFBiomes.SWAMP.identifier(), new BiomeMapColor(0, 204, 187));
		BIOME2COLOR.put(TFBiomes.FIRE_SWAMP.identifier(), new BiomeMapColor(140, 0, 0));

		BIOME2COLOR.put(TFBiomes.DARK_FOREST.identifier(), new BiomeMapColor(25, 61, 13));
		BIOME2COLOR.put(TFBiomes.DARK_FOREST_CENTER.identifier(), new BiomeMapColor(157, 79, 0));

		BIOME2COLOR.put(TFBiomes.SNOWY_FOREST.identifier(), new BiomeMapColor(255, 255, 255));
		BIOME2COLOR.put(TFBiomes.GLACIER.identifier(), new BiomeMapColor(130, 191, 245));

		BIOME2COLOR.put(TFBiomes.HIGHLANDS.identifier(), new BiomeMapColor(100, 65, 0));
		BIOME2COLOR.put(TFBiomes.THORNLANDS.identifier(), new BiomeMapColor(128, 100, 90));
		BIOME2COLOR.put(TFBiomes.FINAL_PLATEAU.identifier(), new BiomeMapColor(128, 128, 128));
	}

	public LiteralArgumentBuilder<CommandSourceStack> register() {
		return Commands.literal("biomepng").requires(Commands.hasPermission(Commands.LEVEL_GAMEMASTERS)).executes(context -> createMap(context.getSource(), 4096, 4096, true))
			.then(Commands.argument("width", IntegerArgumentType.integer(0))
				.executes(context -> createMap(context.getSource(), IntegerArgumentType.getInteger(context, "width"), IntegerArgumentType.getInteger(context, "width"), true))
				.then(Commands.argument("height", IntegerArgumentType.integer(0))
					.executes(context -> createMap(context.getSource(), IntegerArgumentType.getInteger(context, "width"), IntegerArgumentType.getInteger(context, "height"), true))
					.then(Commands.argument("showBiomePercents", BoolArgumentType.bool())
						.executes(context -> createMap(context.getSource(), IntegerArgumentType.getInteger(context, "width"), IntegerArgumentType.getInteger(context, "height"), BoolArgumentType.getBool(context, "showBiomePercents"))))));

	}

	private int createMap(CommandSourceStack source, int width, int height, boolean showBiomePercents) {
		if (FMLEnvironment.getDist().isDedicatedServer())
			return -1;

		if (BIOME2COLOR.isEmpty()) {
			init();
		}

		//setup image
		Map<Holder<Biome>, Integer> biomeCount = new HashMap<>();
		NativeImage img = new NativeImage(width, height, false);

		int progressUpdate = img.getHeight() / 8;

		for (int x = 0; x < img.getHeight(); x++) {
			for (int z = 0; z < img.getWidth(); z++) {
				ServerLevel level = source.getLevel();
				Holder<Biome> b = level.getNoiseBiome(x - (img.getWidth() / 2), 0, z - (img.getHeight() / 2));
				Identifier key = level.registryAccess().lookupOrThrow(Registries.BIOME).getKey(b.value());
				BiomeMapColor color = BIOME2COLOR.get(key);

				if (color == null) {
					int colorInt = getBiomeColor(b);

					if (colorInt == 0)
						colorInt = b.value().getGrassColor(0, 0);

					BIOME2COLOR.put(key, color = new BiomeMapColor(colorInt | 0xFF000000));
				}

				if (!biomeCount.containsKey(b)) {
					biomeCount.put(b, 0);
				} else {
					biomeCount.put(b, biomeCount.get(b) + 1);
				}

				//set the color
				img.setPixelABGR(x, z, ColorUtil.argbToABGR(color.getARGB()));
			}

			//send a progress update to let people know the server isn't dying
			if (x % progressUpdate == 0) {
				int finalX = x;
				double percentComplete = (double) finalX / img.getHeight() * 100;
				String percentDisplay = this.numberFormat.format(percentComplete);
				source.sendSuccess(() -> Component.translatable("commands.tffeature.biomepng.progress", percentDisplay), false);
			}
		}

		if (showBiomePercents) {
			source.sendSuccess(() -> Component.translatable("commands.tffeature.biomepng.counts_header", width, height), false);
			int totalCount = biomeCount.values().stream().mapToInt(i -> i).sum();
			biomeCount.forEach((biome, integer) -> source.sendSuccess(() -> Component.literal(
					source.getLevel().registryAccess().lookupOrThrow(Registries.BIOME).getKey(biome.value()).toString())
				.append(": " + (integer) + ChatFormatting.GRAY + " (" + numberFormat.format(((double) integer / totalCount) * 100) + "%)"), false));
		}

		int startX = Mth.floor(source.getPosition().x()) - (img.getWidth() / 2);
		int startZ = Mth.floor(source.getPosition().z()) - (img.getHeight() / 2);
		//file name is formatted as: biomemap-seed-(startX.startZ)-(endX.endZ)
		Path path = source.getLevel().getServer().getWorldPath(LevelResource.GENERATED_DIR).resolve("biomemaps").resolve(String.valueOf(source.getLevel().getSeed())).resolve("biome_map-" + source.getLevel().getSeed() + "-(" + startX + "." + startZ + ")-(" + (startX + width) + "." + (startZ + height) + ").png").normalize();
		//save the biome map
		try {
			if (!Files.exists(path)) {
				Files.createDirectories(path.getParent());
				img.writeToFile(path);
			}
		} catch (IOException e) {
			e.printStackTrace();
			source.sendFailure(Component.translatable("commands.tffeature.biomepng.save_failed"));
			return 0;
		}

		source.sendSuccess(() -> Component.translatable("commands.tffeature.biomepng.save_success"), false);

		return Command.SINGLE_SUCCESS;
	}

	public static int getBiomeColor(Holder<Biome> biome) {
		MagicMapBiomeColor c = biome.getData(TFDataMaps.MAGIC_MAP_BIOME_COLOR);
		return c != null ? getMapColor(c) : 0xFF000000;
	}

	public static int getMapColor(MagicMapBiomeColor color) {
		int j = (color.color().col >> 16 & 255);
		int k = (color.color().col >> 8 & 255);
		int l = (color.color().col & 255);
		return 0xFF000000 | l << 16 | k << 8 | j;
	}

	public static class BiomeMapColor {

		private final int value;

		public BiomeMapColor(int r, int g, int b) {
			this(r, g, b, 255);
		}

		public BiomeMapColor(int r, int g, int b, int a) {
			this.value = ((a & 0xFF) << 24) |
				((r & 0xFF) << 16) |
				((g & 0xFF) << 8) |
				((b & 0xFF));
		}

		public BiomeMapColor(int rgb) {
			this.value = 0xFF000000 | rgb;
		}

		public int getARGB() {
			return this.value;
		}
	}
}