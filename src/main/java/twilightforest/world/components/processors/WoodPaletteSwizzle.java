package twilightforest.world.components.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;
import twilightforest.init.custom.WoodPalettes;
import twilightforest.util.WoodPalette;

public final class WoodPaletteSwizzle extends StructureProcessor {
    private final Holder<WoodPalette> targetPalette;
    private final Holder<WoodPalette> replacementPalette;

    public static final Codec<WoodPaletteSwizzle> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            WoodPalettes.CODEC.fieldOf("target_palette").forGetter(s -> s.targetPalette),
            WoodPalettes.CODEC.fieldOf("replacement_palette").forGetter(s -> s.replacementPalette)
    ).apply(instance, WoodPaletteSwizzle::new));

    public WoodPaletteSwizzle(Holder<WoodPalette> targetPalette, Holder<WoodPalette> replacementPalette) {
        this.targetPalette = targetPalette;
        this.replacementPalette = replacementPalette;
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader worldIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo p_215194_3_, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        return this.replacementPalette.value().modifyBlockWithType(this.targetPalette.value(), blockInfo);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TFStructureProcessors.PLANK_SWIZZLE.get();
    }
}
