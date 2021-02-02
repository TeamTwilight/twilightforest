package twilightforest.worldgen.treeplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.FeatureSpread;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import twilightforest.util.FeatureUtil;
import twilightforest.worldgen.TwilightFeatures;

import java.util.Random;
import java.util.Set;

public class LeafSpheroidFoliagePlacer extends FoliagePlacer {
    public static final Codec<LeafSpheroidFoliagePlacer> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.floatRange(0, 16f).fieldOf("horizontal_radius").forGetter(o -> o.horizontalRadius),
                    Codec.floatRange(0, 16f).fieldOf("vertical_radius").forGetter(o -> o.verticalRadius),
                    FeatureSpread.func_242254_a(0, 8, 8).fieldOf("offset").forGetter(obj -> obj.field_236750_g_),
                    Codec.intRange(0, 16).fieldOf("random_add_horizontal").orElse(0).forGetter(o -> o.randomHorizontal),
                    Codec.intRange(0, 16).fieldOf("random_add_vertical").orElse(0).forGetter(o -> o.randomVertical),
                    Codec.floatRange(-0.5f, 0.5f).fieldOf("vertical_filler_bias").orElse(0f).forGetter(o -> o.verticalBias)
            ).apply(instance, LeafSpheroidFoliagePlacer::new)
    );

    // These two variables are floats to help round out the pixel-snapping of the sphere-filling algorithm
    // n+0.5 numbers seem to work best but messing with it is encouraged to find best results
    private final float horizontalRadius;
    private final float verticalRadius;
    private final float verticalBias;

    private final int randomHorizontal;
    private final int randomVertical;

    public LeafSpheroidFoliagePlacer(float horizontalRadius, float verticalRadius, FeatureSpread yOffset, int randomHorizontal, int randomVertical, float verticalBias) {
        super(FeatureSpread.func_242252_a((int) horizontalRadius), yOffset);

        this.horizontalRadius = horizontalRadius;
        this.verticalRadius = verticalRadius;
        this.randomHorizontal = randomHorizontal;
        this.randomVertical = randomVertical;
        this.verticalBias = verticalBias;
    }

    @Override
    protected FoliagePlacerType<LeafSpheroidFoliagePlacer> func_230371_a_() {
        return TwilightFeatures.FOLIAGE_SPHEROID;
    }

    @Override // place foliage
    protected void func_230372_a_(IWorldGenerationReader world, Random random, BaseTreeFeatureConfig baseTreeFeatureConfig, int trunkHeight, Foliage foliage, int foliageHeight, int radius, Set<BlockPos> set, int offset, MutableBoundingBox mutableBoundingBox) {
        BlockPos center = foliage.func_236763_a_().up(offset); // foliage.getCenter

        //FeatureUtil.makeLeafCircle(world, random, center, radius, baseTreeFeatureConfig.leavesProvider, set);
        FeatureUtil.makeLeafSpheroid(world, random, center, foliage.func_236764_b_() + horizontalRadius + random.nextInt(randomHorizontal + 1), foliage.func_236764_b_() + verticalRadius + random.nextInt(randomVertical + 1), verticalBias, baseTreeFeatureConfig.leavesProvider, set);
    }

    @Override // foliage Height
    public int func_230374_a_(Random random, int i, BaseTreeFeatureConfig baseTreeFeatureConfig) {
        return 0;
    }

    @Override
    protected boolean func_230373_a_(Random random, int i, int i1, int i2, int i3, boolean b) {
        return false;
    }
}
