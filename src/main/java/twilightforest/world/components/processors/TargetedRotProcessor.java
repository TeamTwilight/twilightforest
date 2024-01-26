package twilightforest.world.components.processors;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;

import java.util.ArrayList;

public final class TargetedRotProcessor extends BlockRotProcessor {
    public static final Codec<TargetedRotProcessor> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.listOf().xmap(ImmutableSet::copyOf, ArrayList::new).fieldOf("blocks_to_rot").forGetter(p -> p.blocksToRot),
            Codec.FLOAT.fieldOf("integrity").orElse(1.0f).forGetter(p -> p.integrity)
    ).apply(instance, TargetedRotProcessor::new));

    private final ImmutableSet<BlockState> blocksToRot;

    public TargetedRotProcessor(ImmutableSet<BlockState> blocksToRot, float integrity) {
        super(integrity);
        this.blocksToRot = blocksToRot;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos origin, BlockPos centerBottom, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
        if (!this.blocksToRot.contains(modifiedBlockInfo.state())) return modifiedBlockInfo;
        return super.processBlock(level, origin, centerBottom, originalBlockInfo, modifiedBlockInfo, settings);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return TFStructureProcessors.TARGETED_ROT.get();
    }
}
