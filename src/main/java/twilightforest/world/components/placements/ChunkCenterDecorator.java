package twilightforest.world.components.placements;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.feature.configurations.NoneDecoratorConfiguration;
import net.minecraft.world.level.levelgen.placement.DecorationContext;
import net.minecraft.world.level.levelgen.placement.FeatureDecorator;

import java.util.Random;
import java.util.stream.Stream;

public class ChunkCenterDecorator extends FeatureDecorator<NoneDecoratorConfiguration> {

	public ChunkCenterDecorator(Codec<NoneDecoratorConfiguration> codec) {
		super(codec);
	}

	@Override
	public Stream<BlockPos> getPositions(DecorationContext pContext, Random pRandom, NoneDecoratorConfiguration pConfig, BlockPos pos) {
		return Stream.of(new BlockPos((pos.getX() & 0xfffffff0) + 8, pos.getY(), (pos.getZ() & 0xfffffff0) + 8));
	}
}
