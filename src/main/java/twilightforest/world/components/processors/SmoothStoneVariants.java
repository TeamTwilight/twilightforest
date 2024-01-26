package twilightforest.world.components.processors;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;
import twilightforest.util.FeaturePlacers;

public class SmoothStoneVariants extends StructureProcessor {
    public static final SmoothStoneVariants INSTANCE = new SmoothStoneVariants();
    public static final Codec<SmoothStoneVariants> CODEC = Codec.unit(() -> INSTANCE);

    private SmoothStoneVariants() {
    }

    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos origin, BlockPos centerBottom, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        RandomSource random = settings.getRandom(modifiedBlockInfo.pos());

        // We use nextBoolean in other processors so this lets us re-seed deterministically
        random.setSeed(random.nextLong() * 4);

        if (modifiedBlockInfo.state().is(Blocks.SMOOTH_STONE_SLAB) && random.nextBoolean())
            return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(modifiedBlockInfo.state(), Blocks.COBBLESTONE_SLAB), null);

        if (modifiedBlockInfo.state().is(Blocks.SMOOTH_STONE) && random.nextBoolean())
            return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), Blocks.COBBLESTONE.defaultBlockState(), null);

        return modifiedBlockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TFStructureProcessors.SMOOTH_STONE_VARIANTS.get();
    }
}
