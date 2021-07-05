package twilightforest.worldgen.treeplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;
import twilightforest.worldgen.TwilightFeatures;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class TrunkRiser extends AbstractTrunkPlacer {
    public static final Codec<TrunkRiser> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.intRange(0, 16).fieldOf("offset_up").forGetter(o -> o.offset),
                    AbstractTrunkPlacer.CODEC.fieldOf("trunk_placer").forGetter(o -> o.placer)
            ).apply(instance, TrunkRiser::new)
    );

    private final int offset;
    private final AbstractTrunkPlacer placer;

    public TrunkRiser(int baseHeight, AbstractTrunkPlacer placer) {
        super(placer.baseHeight, placer.heightRandA, placer.heightRandB);

        this.offset = baseHeight;
        this.placer = placer;
    }

    @Override
    protected TrunkPlacerType<TrunkRiser> getPlacerType() {
        return TwilightFeatures.TRUNK_RISER;
    }

    @Override
    public List<FoliagePlacer.Foliage> getFoliages(IWorldGenerationReader iWorldGenerationReader, Random random, int i, BlockPos blockPos, Set<BlockPos> set, MutableBoundingBox mutableBoundingBox, BaseTreeFeatureConfig baseTreeFeatureConfig) {
        return placer.getFoliages(iWorldGenerationReader, random, i, blockPos.up(offset), set, mutableBoundingBox, baseTreeFeatureConfig);
    }
}
