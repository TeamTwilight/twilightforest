package twilightforest.worldgen.treeplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.treedecorator.TreeDecorator;
import net.minecraft.world.gen.treedecorator.TreeDecoratorType;
import twilightforest.worldgen.TwilightFeatures;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class TrunkSideDecorator extends TreeDecorator {
    public static final Codec<TrunkSideDecorator> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.intRange(0, 64).fieldOf("placement_count").forGetter(o -> o.count),
                    Codec.floatRange(0f, 1f).fieldOf("probability_of_placement").forGetter(o -> o.probability),
                    BlockStateProvider.CODEC.fieldOf("deco_provider").forGetter(o -> o.decoration)
            ).apply(instance, TrunkSideDecorator::new)
    );

    private final int count;
    private final float probability;
    private final BlockStateProvider decoration;

    public TrunkSideDecorator(int count, float probability, BlockStateProvider decorator) {
        this.count = count;
        this.probability = probability;
        this.decoration = decorator;
    }

    @Override
    protected TreeDecoratorType<TrunkSideDecorator> func_230380_a_() {
        return TwilightFeatures.TRUNKSIDE_DECORATOR;
    }

    @Override
    public void func_225576_a_(ISeedReader world, Random random, List<BlockPos> trunkBlocks, List<BlockPos> leafBlocks, Set<BlockPos> decorations, MutableBoundingBox mutableBoundingBox) {
        int blockCount = trunkBlocks.size();

        for (int attempt = 0; attempt < count; attempt++) {
            if (random.nextFloat() >= probability) continue;

            Rotation rot = Rotation.randomRotation(random);
            BlockPos pos = trunkBlocks.get(random.nextInt(blockCount)).offset(rot.rotate(Direction.NORTH));

            if (Feature.isAirAt(world, pos)) // Checks if block is air
                func_227423_a_(world, pos, decoration.getBlockState(random, pos).rotate(rot), decorations, mutableBoundingBox);
        }
    }
}
