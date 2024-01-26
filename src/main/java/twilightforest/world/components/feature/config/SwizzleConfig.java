package twilightforest.world.components.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.jetbrains.annotations.NotNull;
import twilightforest.TFRegistries;
import twilightforest.data.tags.CustomTagGenerator;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.util.WoodPalette;
import twilightforest.world.components.processors.StateTransfiguringProcessor;
import twilightforest.world.components.processors.WoodPaletteSwizzle;

import java.util.Collections;
import java.util.List;

public record SwizzleConfig(HolderSet<WoodPalette> targets, WeightedRandomList<WeightedEntry.Wrapper<HolderSet<WoodPalette>>> paletteChoices, List<ProcessorRule> preprocessingRules) implements FeatureConfiguration {
    public static final Codec<SwizzleConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(TFRegistries.Keys.WOOD_PALETTES).fieldOf("target_palettes").forGetter(SwizzleConfig::targets),
            WeightedRandomList.codec(WeightedEntry.Wrapper.codec(RegistryCodecs.homogeneousList(TFRegistries.Keys.WOOD_PALETTES))).fieldOf("palette_choices").forGetter(SwizzleConfig::paletteChoices),
            ProcessorRule.CODEC.listOf().fieldOf("preprocessing_rules").orElseGet(Collections::emptyList).forGetter(SwizzleConfig::preprocessingRules)
    ).apply(instance, SwizzleConfig::new));

    @NotNull
    public static WeightedRandomList<WeightedEntry.Wrapper<HolderSet<WoodPalette>>> buildRarityPalette(HolderGetter<WoodPalette> paletteHolders) {
        // Old code with chances:
        //  getRandomWeighted(RandomSource random) {
        //	  int randomVal = random.nextInt();
        //	  if ((randomVal & 0b1) == 0) return ArrayUtil.wrapped(COMMON, randomVal >> 1); // 50% chance
        //	  if ((randomVal & 0b10) == 0) return ArrayUtil.wrapped(UNCOMMON, randomVal >> 2); // 25% chance
        //	  if ((randomVal & 0b1100) != 0) return ArrayUtil.wrapped(RARE, randomVal >> 4); // 18.75% chance
        //	  return ArrayUtil.wrapped(TREASURE, randomVal >> 4); // 6.25% chance
        //  }

        WeightedEntry.Wrapper<HolderSet<WoodPalette>> common = // 50% chance
                WeightedEntry.wrap(paletteHolders.get(CustomTagGenerator.WoodPaletteTagGenerator.COMMON_PALETTES).get(), 8);
        WeightedEntry.Wrapper<HolderSet<WoodPalette>> uncommon = // 25% chance
                WeightedEntry.wrap(paletteHolders.get(CustomTagGenerator.WoodPaletteTagGenerator.UNCOMMON_PALETTES).get(), 4);
        WeightedEntry.Wrapper<HolderSet<WoodPalette>> rare = // 18.75% chance
                WeightedEntry.wrap(paletteHolders.get(CustomTagGenerator.WoodPaletteTagGenerator.RARE_PALETTES).get(), 3);
        WeightedEntry.Wrapper<HolderSet<WoodPalette>> treasure = // 6.25% chance
                WeightedEntry.wrap(paletteHolders.get(CustomTagGenerator.WoodPaletteTagGenerator.TREASURE_PALETTES).get(), 1);

        return WeightedRandomList.create(common, uncommon, rare, treasure);
    }

    public static SwizzleConfig generate(HolderGetter<WoodPalette> paletteHolders, TagKey<WoodPalette> swizzleMask, WeightedRandomList<WeightedEntry.Wrapper<HolderSet<WoodPalette>>> paletteChoices, ProcessorRule... postProcessingRules) {
        return new SwizzleConfig(paletteHolders.getOrThrow(swizzleMask), paletteChoices, List.of(postProcessingRules));
    }

    public void buildAddProcessors(StructurePlaceSettings settings, RandomSource random) {
        // If there's no rules then don't even bother adding the processor for them
        if (!this.preprocessingRules().isEmpty())
            settings.addProcessor(new StateTransfiguringProcessor(this.preprocessingRules()));

        for (Holder<WoodPalette> targetPalette : this.targets) {
            settings.addProcessor(new WoodPaletteSwizzle(targetPalette, this.paletteChoices().getRandom(random).get().getData().getRandomElement(random).get()));
        }
    }
}
