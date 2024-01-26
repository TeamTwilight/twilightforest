package twilightforest.world.components.feature.trees.treeplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import twilightforest.init.TFFeatureModifiers;
import twilightforest.util.FeaturePlacers;

public class LeafSpheroidFoliagePlacer extends FoliagePlacer {
    public static final Codec<LeafSpheroidFoliagePlacer> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.floatRange(0, 16f).fieldOf("horizontal_radius").forGetter(o -> o.horizontalRadius),
                    Codec.floatRange(0, 16f).fieldOf("vertical_radius").forGetter(o -> o.verticalRadius),
                    IntProvider.codec(0, 8).fieldOf("offset").forGetter(obj -> obj.offset),
                    Codec.intRange(0, 16).fieldOf("random_add_horizontal").orElse(0).forGetter(o -> o.randomHorizontal),
                    Codec.intRange(0, 16).fieldOf("random_add_vertical").orElse(0).forGetter(o -> o.randomVertical),
                    Codec.floatRange(-0.5f, 0.5f).fieldOf("vertical_filler_bias").orElse(0f).forGetter(o -> o.verticalBias),
                    Codec.intRange(0, 256).fieldOf("shag_factor").orElse(0).forGetter(o -> o.shag_factor) // Shhh
            ).apply(instance, LeafSpheroidFoliagePlacer::new)
    );

    // These two variables are floats to help round out the pixel-snapping of the sphere-filling algorithm
    // n+0.5 numbers seem to work best but messing with it is encouraged to find best results
    private final float horizontalRadius;
    private final float verticalRadius;
    private final float verticalBias;

    private final int randomHorizontal;
    private final int randomVertical;
    private final int shag_factor;

    public static final LeafSpheroidFoliagePlacer NO_OP = new LeafSpheroidFoliagePlacer(0, 0, ConstantInt.of(0), 0, 0, 0.0F, 0);

    public LeafSpheroidFoliagePlacer(float horizontalRadius, float verticalRadius, IntProvider yOffset, int randomHorizontal, int randomVertical, float verticalBias, int shag_factor) {
        super(ConstantInt.of((int) horizontalRadius), yOffset);

        this.horizontalRadius = horizontalRadius;
        this.verticalRadius = verticalRadius;
        this.randomHorizontal = randomHorizontal;
        this.randomVertical = randomVertical;
        this.verticalBias = verticalBias;
        this.shag_factor = shag_factor;
    }

    @Override
    protected FoliagePlacerType<LeafSpheroidFoliagePlacer> type() {
        return TFFeatureModifiers.FOLIAGE_SPHEROID.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader worldReader, FoliageSetter setter, RandomSource random, TreeConfiguration baseTreeFeatureConfig, int trunkHeight, FoliageAttachment foliage, int foliageHeight, int radius, int offset) {
        BlockPos center = foliage.pos().above(offset); // foliage.getCenter

        FeaturePlacers.placeSpheroid(worldReader, setter, FeaturePlacers.VALID_TREE_POS, random, center, foliage.radiusOffset() + this.horizontalRadius + random.nextInt(this.randomHorizontal + 1), foliage.radiusOffset() + this.verticalRadius + random.nextInt(this.randomVertical + 1), this.verticalBias, baseTreeFeatureConfig.foliageProvider);

        if (this.shag_factor > 0) {
            for (int i = 0; i < this.shag_factor; i++) {
                float randomYaw = random.nextFloat() * Mth.TWO_PI;
                float randomPitch = random.nextFloat() * 2f - 1f; //random.nextFloat() * 0.75f + 0.25f;
                float yUnit = Mth.sqrt(1 - randomPitch * randomPitch); // Inverse Trigonometry identity (sin(arcos(t)) == sqrt(1 - t*t))
                float xCircleOffset = yUnit * Mth.cos(randomYaw) * (this.horizontalRadius - 1f); // We do radius minus 1 here so the leaf 2x2 generates overlapping the existing foilage
                float zCircleOffset = yUnit * Mth.sin(randomYaw) * (this.horizontalRadius - 1f); // instead of making awkward 2x2 pieces of foilage jutting out

                BlockPos placement = center.offset((int) (xCircleOffset + ((int) xCircleOffset >> 31)), (int) (randomPitch * (this.verticalRadius + 0.25f) + this.verticalBias), (int) (zCircleOffset + ((int) zCircleOffset >> 31)));

                placeLeafCluster(worldReader, setter, random, placement.immutable(), baseTreeFeatureConfig.foliageProvider);
            }
        }
    }

    private static void placeLeafCluster(LevelSimulatedReader worldReader, FoliageSetter setter, RandomSource random, BlockPos pos, BlockStateProvider state) {
        FeaturePlacers.placeLeaf(worldReader, setter, FeaturePlacers.VALID_TREE_POS, pos, state, random);
        FeaturePlacers.placeLeaf(worldReader, setter, FeaturePlacers.VALID_TREE_POS, pos.east(), state, random);
        FeaturePlacers.placeLeaf(worldReader, setter, FeaturePlacers.VALID_TREE_POS, pos.south(), state, random);
        FeaturePlacers.placeLeaf(worldReader, setter, FeaturePlacers.VALID_TREE_POS, pos.offset(1, 0, 1), state, random);
    }

    @Override // foliage Height
    public int foliageHeight(RandomSource random, int i, TreeConfiguration baseTreeFeatureConfig) {
        return 0;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int i, int i1, int i2, int i3, boolean b) {
        return false;
    }
}
