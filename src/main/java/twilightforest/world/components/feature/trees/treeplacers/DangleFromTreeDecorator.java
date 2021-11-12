package twilightforest.world.components.feature.trees.treeplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import twilightforest.world.registration.TwilightFeatures;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class DangleFromTreeDecorator extends TreeDecorator {
    public static final Codec<DangleFromTreeDecorator> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.intRange(0, 32).fieldOf("attempts_minimum").forGetter(o -> o.count),
                    Codec.intRange(0, 32).fieldOf("random_add_attempts").orElse(0).forGetter(o -> o.randomAddCount),
                    Codec.intRange(1, 24).fieldOf("minimum_required_length").forGetter(o -> o.minimumRequiredLength),
                    Codec.intRange(1, 24).fieldOf("base_length").forGetter(o -> o.baseLength),
                    Codec.intRange(0, 16).fieldOf("random_add_length").orElse(0).forGetter(o -> o.randomAddLength),
                    WeightedStateProvider.CODEC.fieldOf("rope_provider").forGetter(o -> o.rope),
                    WeightedStateProvider.CODEC.fieldOf("baggage_provider").forGetter(o -> o.baggage)
            ).apply(instance, DangleFromTreeDecorator::new)
    );
    private final int count;
    private final int randomAddCount;
    private final int minimumRequiredLength;
    private final int baseLength;
    private final int randomAddLength;
    private final WeightedStateProvider rope;
    private final WeightedStateProvider baggage;

    public DangleFromTreeDecorator(int count, int randomAddCount, int minimumRequiredLength, int baseLength, int randomAddLength, WeightedStateProvider rope, WeightedStateProvider baggage) {
        this.count = count;
        this.randomAddCount = randomAddCount;
        this.minimumRequiredLength = minimumRequiredLength;
        this.baseLength = baseLength;
        this.randomAddLength = randomAddLength;
        this.rope = rope;
        this.baggage = baggage;
    }

    @Override
    protected TreeDecoratorType<DangleFromTreeDecorator> type() {
        return TwilightFeatures.DANGLING_DECORATOR;
    }

    @Override
    public void place(LevelSimulatedReader worldReader, BiConsumer<BlockPos, BlockState> worldPlacer, Random random, List<BlockPos> trunkBlocks, List<BlockPos> leafBlocks) {
        if (leafBlocks.isEmpty())
            return;

        int totalTries = this.count + random.nextInt(this.randomAddCount + 1);
        int leafTotal = leafBlocks.size();
        boolean clearedOfPossibleLeaves;
        boolean isAir;
        int cordLength;
        BlockPos pos;

        totalTries = Math.min(totalTries, leafTotal);

        for (int attempt = 0; attempt < totalTries; attempt++) {
            clearedOfPossibleLeaves = false;
            pos = leafBlocks.get(random.nextInt(leafTotal));

            //dont place a rope where the trunk is
            if(pos.getX() == trunkBlocks.get(0).getY() && pos.getZ() == trunkBlocks.get(0).getZ()) return;

            cordLength = this.baseLength + random.nextInt(this.randomAddLength + 1);

            // Scan to make sure we have
            for (int ropeUnrolling = 1; ropeUnrolling <= cordLength; ropeUnrolling++) {
                isAir = worldReader.isStateAtPosition(pos.below(ropeUnrolling), BlockBehaviour.BlockStateBase::isAir);

                if (!clearedOfPossibleLeaves && isAir)
                    clearedOfPossibleLeaves = true;

                if (clearedOfPossibleLeaves && !isAir) {
                    cordLength = ropeUnrolling;

                    break;
                }
            }

            if (cordLength > this.minimumRequiredLength) { // We don't want no pathetic unroped baggage
                BlockState rope = this.rope.getState(random, pos);
                for (int ropeUnrolling = 1; ropeUnrolling < cordLength; ropeUnrolling++) {
                    pos = pos.below(1);

                    if(worldReader.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::isAir))
                        worldPlacer.accept(pos, rope);
                }

                pos = pos.below(1);

                if(worldReader.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::isAir))
                    worldPlacer.accept(pos, this.baggage.getState(random, pos));
            }
        }
    }
}
