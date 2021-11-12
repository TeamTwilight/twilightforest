package twilightforest.world.components.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import twilightforest.enums.StructureWoodVariant;
import twilightforest.util.ArrayUtil;
import twilightforest.util.FeaturePlacers;
import twilightforest.world.registration.TFStructureProcessors;

import javax.annotation.Nullable;
import java.util.Random;

public final class CobblePlankSwizzler extends StructureProcessor {
    private final StructureWoodVariant OAK_SWIZZLE;
    private final StructureWoodVariant SPRUCE_SWIZZLE;
    private final StructureWoodVariant BIRCH_SWIZZLE;

    public static final Codec<CobblePlankSwizzler> CODEC = RecordCodecBuilder.create((instance) -> instance.group(
            Codec.intRange(0, StructureWoodVariant.values().length).fieldOf("oak_to_type").orElse(0).forGetter(s -> s.OAK_SWIZZLE.ordinal()),
            Codec.intRange(0, StructureWoodVariant.values().length).fieldOf("spruce_to_type").orElse(1).forGetter(s -> s.SPRUCE_SWIZZLE.ordinal()),
            Codec.intRange(0, StructureWoodVariant.values().length).fieldOf("birch_to_type").orElse(2).forGetter(s -> s.BIRCH_SWIZZLE.ordinal())
    ).apply(instance, CobblePlankSwizzler::new));

    private CobblePlankSwizzler(int oakSwizzle, int spruceSwizzle, int birchSwizzle) {
        this.OAK_SWIZZLE = ArrayUtil.wrapped(StructureWoodVariant.values(), oakSwizzle);
        this.SPRUCE_SWIZZLE = ArrayUtil.wrapped(StructureWoodVariant.values(), spruceSwizzle);
        this.BIRCH_SWIZZLE = ArrayUtil.wrapped(StructureWoodVariant.values(), birchSwizzle);
    }

    public CobblePlankSwizzler(Random random) {
        this.OAK_SWIZZLE = StructureWoodVariant.getRandomWeighted(random);
        this.SPRUCE_SWIZZLE = StructureWoodVariant.getRandomWeighted(random);
        this.BIRCH_SWIZZLE = StructureWoodVariant.getRandomWeighted(random);
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader worldIn, BlockPos pos, BlockPos piecepos, StructureTemplate.StructureBlockInfo p_215194_3_, StructureTemplate.StructureBlockInfo blockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        BlockState state = blockInfo.state;
        Block block = state.getBlock();

        StructureWoodVariant type = StructureWoodVariant.getVariantFromBlock(block);
        if (type != null) return switch (type) {
            case OAK -> new StructureTemplate.StructureBlockInfo(blockInfo.pos, StructureWoodVariant.modifyBlockWithType(state, this.OAK_SWIZZLE), null);
            case SPRUCE -> new StructureTemplate.StructureBlockInfo(blockInfo.pos, StructureWoodVariant.modifyBlockWithType(state, this.SPRUCE_SWIZZLE), null);
            case BIRCH -> new StructureTemplate.StructureBlockInfo(blockInfo.pos, StructureWoodVariant.modifyBlockWithType(state, this.BIRCH_SWIZZLE), null);
            default -> blockInfo;
        };

        return blockInfo;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TFStructureProcessors.COBBLE_PLANK_SWIZZLER;
    }
}
