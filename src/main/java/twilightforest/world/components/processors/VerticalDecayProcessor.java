package twilightforest.world.components.processors;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;

import java.util.Collections;
import java.util.List;

public class VerticalDecayProcessor extends StructureProcessor {
	public static final MapCodec<VerticalDecayProcessor> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
		Block.CODEC.codec().listOf().fieldOf("decay_blocks").forGetter(VerticalDecayProcessor::getDecayBlocks),
		Codec.FLOAT.fieldOf("decay_chance").forGetter(p -> p.decayChance)
	).apply(inst, VerticalDecayProcessor::new));

	private final List<Block> decayBlocks;
	private final float decayChance;

	public VerticalDecayProcessor(List<Block> decayBlocks, float decayChance) {
		this.decayBlocks = Collections.unmodifiableList(decayBlocks);
		this.decayChance = decayChance;
	}

	@Nullable
	@Override
	public StructureTemplate.StructureBlockInfo process(LevelReader level, BlockPos offset, BlockPos piecePos, StructureTemplate.StructureBlockInfo originalInfo, StructureTemplate.StructureBlockInfo modifiedInfo, StructurePlaceSettings placeSettings, @Nullable StructureTemplate template) {
		if (this.decayBlocks.contains(modifiedInfo.state().getBlock())
			&& placeSettings.getRandom(modifiedInfo.pos().atY(offset.getY())).nextFloat() < this.decayChance) {
			return null;
		}

		return modifiedInfo;
	}

	public List<Block> getDecayBlocks() {
		return this.decayBlocks;
	}

	@Override
	protected StructureProcessorType<VerticalDecayProcessor> getType() {
		return TFStructureProcessors.VERTICAL_DECAY.value();
	}
}
