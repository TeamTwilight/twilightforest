package twilightforest.command;

/**
 * Thank you @SuperCoder79 (from Twitter) for sharing the original code! Code sourced from a LGPL project
 */

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.fml.loading.FMLEnvironment;
import twilightforest.item.ItemTFMagicMap;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class MapBiomesCommand {
    private static final DecimalFormat numberFormat = new DecimalFormat("#.00");

    private static final HashMap<Biome, Color> BIOME2COLOR = new HashMap<>();

    private static void init() {
        if (!BIOME2COLOR.isEmpty())
            return;

        /* FIXME
        BIOME2COLOR.put(TFBiomes.stream             .get(), new Color(0, 0x66, 255));
        BIOME2COLOR.put(TFBiomes.tfLake             .get(), new Color(0, 0x44, 255));
        BIOME2COLOR.put(TFBiomes.clearing           .get(), new Color(180, 220, 100));
        BIOME2COLOR.put(TFBiomes.oakSavanna         .get(), new Color(250, 240, 150));
        BIOME2COLOR.put(TFBiomes.twilightForest     .get(), new Color(100, 128, 100));
        BIOME2COLOR.put(TFBiomes.denseTwilightForest.get(), new Color(50, 100, 50));
        BIOME2COLOR.put(TFBiomes.fireflyForest      .get(), new Color(150, 255, 0));
        BIOME2COLOR.put(TFBiomes.enchantedForest    .get(), new Color(255, 100, 255));
        BIOME2COLOR.put(TFBiomes.mushrooms          .get(), new Color(255, 100, 80));
        BIOME2COLOR.put(TFBiomes.deepMushrooms      .get(), new Color(200, 80, 80));

        BIOME2COLOR.put(TFBiomes.tfSwamp            .get(), new Color(128, 0, 0));
        BIOME2COLOR.put(TFBiomes.fireSwamp          .get(), new Color(128, 0x22, 0));

        BIOME2COLOR.put(TFBiomes.darkForest         .get(), new Color(0, 60, 0));
        BIOME2COLOR.put(TFBiomes.darkForestCenter   .get(), new Color(0, 80, 0));

        BIOME2COLOR.put(TFBiomes.snowy_forest       .get(), new Color(220, 240, 240));
        BIOME2COLOR.put(TFBiomes.glacier            .get(), new Color(180, 255, 255));

        BIOME2COLOR.put(TFBiomes.highlands          .get(), new Color(100, 65, 0));
        BIOME2COLOR.put(TFBiomes.thornlands         .get(), new Color(128, 100, 90));
        BIOME2COLOR.put(TFBiomes.highlandsCenter    .get(), new Color(128, 128, 128));*/
    }

    public static LiteralArgumentBuilder<CommandSource> register() {
        // TODO elevate command perm
        return Commands.literal("biomepng").requires(cs -> cs.hasPermissionLevel(2)).executes(MapBiomesCommand::execute);
    }

    private static int execute(CommandContext<CommandSource> source) {
        if (FMLEnvironment.dist.isDedicatedServer())
            return -1;

        init();

        //setup image
        Map<Biome, Integer> biomeCount = new HashMap<>();
        BufferedImage img = new BufferedImage(4096, 4096, BufferedImage.TYPE_INT_RGB);

        int progressUpdate = img.getHeight() / 8;

        for (int x = 0; x < img.getHeight(); x++) {
            for (int z = 0; z < img.getWidth(); z++) {
                Biome b = source.getSource().getWorld().getNoiseBiome(x - 2048, 0, z - 2048);
                Color color = BIOME2COLOR.get(b);

                if (color == null) {
                    int colorInt = ItemTFMagicMap.getBiomeColor(b);

                    if (colorInt == 0)
                        colorInt = b.getGrassColor(0, 0);

                    BIOME2COLOR.put(b, color = new Color(colorInt | 0xFF000000));
                }

                if (!biomeCount.containsKey(b)) {
                    biomeCount.put(b, 0);
                } else {
                    biomeCount.put(b, biomeCount.get(b) + 1);
                }

                //set the color
                img.setRGB(x, z, color.getRGB());
            }

            //send a progress update to let people know the server isn't dying
            if (x % progressUpdate == 0) {
                source.getSource().sendFeedback(new TranslationTextComponent(((double) x / img.getHeight()) * 100 + "% Done mapping"), true);
            }
        }

        source.getSource().sendFeedback(new StringTextComponent("Approximate biome-block counts within an 2048x2048 region"), true);
        int totalCount = biomeCount.values().stream().mapToInt(i -> i).sum();
        biomeCount.forEach((biome, integer) -> source.getSource().sendFeedback(new StringTextComponent(biome.toString()).appendString(": " + (integer) + TextFormatting.GRAY + " (" + numberFormat.format(((double) integer / totalCount) * 100) + "%)"), true));

        //save the biome map
        Path p = Paths.get("biomemap.png");
        try {
            ImageIO.write(img, "png", p.toAbsolutePath().toFile());
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }

        source.getSource().sendFeedback(new StringTextComponent("Image saved!"), true);

        return Command.SINGLE_SUCCESS;
    }
}