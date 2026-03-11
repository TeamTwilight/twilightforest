package twilightforest.world.components.feature.trees.treeplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import twilightforest.init.TFFeatureModifiers;

public class TreeCorePlacer extends TreeDecorator {
	public static final MapCodec<TreeCorePlacer> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
		Codec.intRange(0, 20).fieldOf("core_position").forGetter(o -> o.corePos),
		BlockStateProvider.CODEC.fieldOf("deco_provider").forGetter(o -> o.core)
	).apply(instance, TreeCorePlacer::new));

	private final int corePos;
	private final BlockStateProvider core;

	public TreeCorePlacer(int corePos, BlockStateProvider core) {
		this.corePos = corePos;
		this.core = core;
	}

	@Override
	protected TreeDecoratorType<TreeCorePlacer> type() {
		return TFFeatureModifiers.CORE_PLACER.get();
	}

	@Override
	public void place(Context context) {
		BlockPos pos = context.logs().get(0).offset(0, this.corePos, 0);
		context.setBlock(pos, this.core.getState(context.random(), pos));
	}
}
