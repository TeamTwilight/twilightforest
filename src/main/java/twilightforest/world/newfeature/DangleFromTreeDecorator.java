package twilightforest.world.newfeature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import twilightforest.util.FeatureUtil;
import twilightforest.world.feature.TFTreeGenerator;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

public class DangleFromTreeDecorator extends TreeDecorator {
    public static final Codec<DangleFromTreeDecorator> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.intRange(0, 32).fieldOf("attempts_minimum").forGetter(o -> o.count),
                    Codec.intRange(0, 32).fieldOf("random_add_attempts").orElse(0).forGetter(o -> o.randomAddCount),
                    Codec.intRange(1, 24).fieldOf("minimum_length").forGetter(o -> o.minimumLength),
                    Codec.intRange(1, 24).fieldOf("base_length").forGetter(o -> o.length),
                    Codec.intRange(0, 16).fieldOf("random_add_length").orElse(0).forGetter(o -> o.randomAddLength),
                    BlockStateProvider.CODEC.fieldOf("rope_provider").forGetter(o -> o.rope),
                    BlockStateProvider.CODEC.fieldOf("baggage_provider").forGetter(o -> o.baggage)
            ).apply(instance, DangleFromTreeDecorator::new)
    );
    private final int count;
    private final int randomAddCount;
    private final int minimumLength;
    private final int length;
    private final int randomAddLength;
    private final BlockStateProvider rope;
    private final BlockStateProvider baggage;

    public DangleFromTreeDecorator(int count, int randomAddCount, int minimumLength, int length, int randomAddLength, BlockStateProvider rope, BlockStateProvider baggage) {
        this.count = count;
        this.randomAddCount = randomAddCount;
        this.minimumLength = minimumLength;
        this.length = length;
        this.randomAddLength = randomAddLength;
        this.rope = rope;
        this.baggage = baggage;
    }

    @Override
    protected TreeDecoratorType<DangleFromTreeDecorator> func_230380_a_() {
        return TwilightFeatures.DANGLING_DECORATOR;
    }

    @Override
    public void func_225576_a_(ISeedReader world, Random random, List<BlockPos> trunkBlocks, List<BlockPos> leafBlocks, Set<BlockPos> decorations, MutableBoundingBox mutableBoundingBox) {
        if (leafBlocks.isEmpty())
            return;

        int totalTries = count + random.nextInt(randomAddCount + 1);
        int leafTotal = leafBlocks.size();
        boolean clearedOfPossibleLeaves;
        boolean isAir;
        int cordLength;
        BlockPos pos;

        totalTries = Math.min(totalTries, leafTotal);

        for (int attempt = 0; attempt < totalTries; attempt++) {
            clearedOfPossibleLeaves = false;
            pos = leafBlocks.get(random.nextInt(leafTotal));

            cordLength = length + random.nextInt(randomAddLength + 1);

            // Scan to make sure we have
            for (int ropeUnrolling = 1; ropeUnrolling <= cordLength; ropeUnrolling++) {
                isAir = world.isAirBlock(pos.down(ropeUnrolling));

                if (!clearedOfPossibleLeaves && isAir)
                    clearedOfPossibleLeaves = true;

                if (clearedOfPossibleLeaves && !isAir) {
                    cordLength = ropeUnrolling;

                    break;
                }
            }

            if (cordLength > minimumLength) { // We don't want no pathetic unroped baggage
                for (int ropeUnrolling = 1; ropeUnrolling < cordLength; ropeUnrolling++) {
                    pos = pos.down(1);

                    func_227423_a_(world, pos, rope.getBlockState(random, pos), decorations, mutableBoundingBox);
                }

                pos = pos.down(1);

                func_227423_a_(world, pos, baggage.getBlockState(random, pos), decorations, mutableBoundingBox);
            }
        }
    }
}
