package twilightforest.world.components.processors;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.jspecify.annotations.Nullable;
import twilightforest.init.TFStructureProcessors;
import twilightforest.util.features.FeaturePlacers;

import java.util.Collections;
import java.util.List;

// Similar to RuleProcessor except it uses the ProcessorRule's output state as a template for transferring BlockStates onto, with FeaturePlacers.transferAllStateKeys(...)
// Despite definitions for BlockStates being supported by the schema, they merely are defaults to be overwritten from the input block's states
public class StateTransfiguringProcessor implements StructureProcessor {
	public static final MapCodec<StateTransfiguringProcessor> CODEC = ProcessorRule.CODEC.listOf().fieldOf("rules").xmap(StateTransfiguringProcessor::new, p -> p.rules);
	private final List<ProcessorRule> rules;

	public StateTransfiguringProcessor(List<? extends ProcessorRule> rules) {
		this.rules = Collections.unmodifiableList(rules);
	}

	@Override
	public StructureTemplate.@Nullable StructureBlockInfo process(LevelReader level, BlockPos origin, BlockPos centerBottom, StructureTemplate.StructureBlockInfo originalBlockInfo, StructureTemplate.StructureBlockInfo modifiedBlockInfo, StructurePlaceSettings settings, @Nullable StructureTemplate template) {
		RandomSource random = RandomSource.create(Mth.getSeed(modifiedBlockInfo.pos()));
		long i = random.nextLong();
		// Re-seed the random source for each loop iteration, the positional seed defines the initial random value
		for (ProcessorRule processorRule : this.rules) {
			// For better randomness while maintaining determinism, especially for 'binary' outcomes depending on 50% chances
			random.setSeed(i * 3);
			i += 115;

			if (processorRule.test(level, modifiedBlockInfo.state(), originalBlockInfo.pos(), modifiedBlockInfo.pos(), centerBottom, random))
				return new StructureTemplate.StructureBlockInfo(modifiedBlockInfo.pos(), FeaturePlacers.transferAllStateKeys(modifiedBlockInfo.state(), processorRule.getOutputState()), processorRule.getOutputTag(random, modifiedBlockInfo.nbt()));
		}

		return modifiedBlockInfo;
	}

	@Override
	public MapCodec<? extends StructureProcessor> codec() {
		return TFStructureProcessors.STATE_TRANSFIGURING.get();
	}
}
