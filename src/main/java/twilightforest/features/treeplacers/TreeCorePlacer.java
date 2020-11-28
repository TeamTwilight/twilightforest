package twilightforest.features.treeplacers;

import java.util.List;
import java.util.Random;
import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import twilightforest.features.TwilightFeatures;

public class TreeCorePlacer extends TreeDecorator {

	public static final Codec<TreeCorePlacer> CODEC = RecordCodecBuilder.create(
	        instance -> instance.group(
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
	protected TreeDecoratorType<TreeCorePlacer> func_230380_a_() {
		return TwilightFeatures.CORE_PLACER;
	}

	@Override
	public void func_225576_a_(ISeedReader world, Random random, List<BlockPos> trunkBlocks, List<BlockPos> leafBlocks,
			Set<BlockPos> decorations, MutableBoundingBox mutableBoundingBox) {
		BlockPos pos = trunkBlocks.get(0);
		BlockPos position = pos.add(0, this.corePos, 0);
		placeCore(world, random, position, decorations, 0, corePos, 0, mutableBoundingBox, core);
	}
	
	public void placeCore(ISeedReader world, Random random, BlockPos pos, Set<BlockPos> decorations, double offset, int iteration, int length, MutableBoundingBox mutableBoundingBox, BlockStateProvider coreType) {
		BlockPos position = pos.add(0, this.corePos, 0);
		func_227423_a_(world, pos, coreType.getBlockState(random, position), decorations, mutableBoundingBox);
	}

}
