package twilightforest.world.components.processors;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockRotProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;

import java.util.ArrayList;

public final class TargetedRotProcessor implements StructureProcessor {
	public static final MapCodec<TargetedRotProcessor> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		BlockState.CODEC.listOf().xmap(ImmutableSet::copyOf, ArrayList::new).fieldOf("blocks_to_rot").forGetter(p -> p.blocksToRot),
		Codec.FLOAT.fieldOf("integrity").orElse(1.0f).forGetter(p -> p.integrity)
	).apply(instance, TargetedRotProcessor::new));

	private final ImmutableSet<BlockState> blocksToRot;

	private final float integrity;

	private final BlockRotProcessor rotProcessor;

	public TargetedRotProcessor(ImmutableSet<BlockState> blocksToRot, float integrity) {
		this.blocksToRot = blocksToRot;
		this.integrity = integrity;
		rotProcessor = new BlockRotProcessor(integrity);
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos origin, BlockPos centerBottom, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
		if (!this.blocksToRot.contains(modifiedBlockInfo.state())) return modifiedBlockInfo;
		return rotProcessor.process(level, origin, centerBottom, originalBlockInfo, modifiedBlockInfo, settings, template);
	}

	@Override
	public MapCodec<? extends StructureProcessor> codec() {
		return TFStructureProcessors.TARGETED_ROT.get();
	}
}
