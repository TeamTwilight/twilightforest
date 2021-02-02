package twilightforest.worldgen.treeplacers;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;
import twilightforest.util.FeatureUtil;
import twilightforest.worldgen.TwilightFeatures;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class BranchingTrunkPlacer extends AbstractTrunkPlacer {
    public static final Codec<BranchingTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            func_236915_a_(instance).and(instance.group(
                    Codec.intRange(0, 24).fieldOf("branch_start_offset_down").forGetter(o -> o.branchDownwardOffset),
                    BranchesConfig.CODEC.fieldOf("branch_config").forGetter(o -> o.branchesConfig),
                    Codec.BOOL.fieldOf("perpendicular_branches").forGetter(o -> o.perpendicularBranches)
            )).apply(instance, BranchingTrunkPlacer::new)
    );

    private final int branchDownwardOffset;
    private final BranchesConfig branchesConfig;
    private final boolean perpendicularBranches;

    public BranchingTrunkPlacer(int baseHeight, int randomHeightA, int randomHeightB, int branchDownwardOffset, BranchesConfig branchesConfig, boolean perpendicularBranches) {
        super(baseHeight, randomHeightA, randomHeightB);
        this.branchDownwardOffset = branchDownwardOffset;
        this.branchesConfig = branchesConfig;
        this.perpendicularBranches = perpendicularBranches;
    }

    @Override
    protected TrunkPlacerType<BranchingTrunkPlacer> func_230381_a_() {
        return TwilightFeatures.TRUNK_BRANCHING;
    }

    @Override
    public List<FoliagePlacer.Foliage> func_230382_a_(IWorldGenerationReader world, Random random, int height, BlockPos startPos, Set<BlockPos> trunkBlocks, MutableBoundingBox mutableBoundingBox, BaseTreeFeatureConfig baseTreeFeatureConfig) {
        List<FoliagePlacer.Foliage> leafBlocks = Lists.newArrayList();

        for (int y = 0; y <= height; y++) { // Keep building upwards until we cannot, and then adjust height if we run into something
            if (!func_236911_a_(world, random, startPos.up(y), trunkBlocks, mutableBoundingBox, baseTreeFeatureConfig)) {
                height = y;
                break;
            }
        }

        leafBlocks.add(new FoliagePlacer.Foliage(startPos.up(height), 0, false));

        int numBranches = branchesConfig.branchCount + random.nextInt(branchesConfig.randomAddBranches + 1);
        float offset = random.nextFloat();
        for (int b = 0; b < numBranches; b++) {
            buildBranch(world, startPos, trunkBlocks, leafBlocks, height - branchDownwardOffset + b, branchesConfig.length, branchesConfig.spacingYaw * b + offset, branchesConfig.downwardsPitch, random, mutableBoundingBox, baseTreeFeatureConfig, perpendicularBranches);
        }

        return leafBlocks;
    }


    private static void buildBranch(IWorldGenerationReader world, BlockPos pos, Set<BlockPos> trunkBlocks, List<FoliagePlacer.Foliage> leafBlocks, int height, double length, double angle, double tilt, Random treeRNG, MutableBoundingBox mbb, BaseTreeFeatureConfig config, boolean perpendicularBranches) {
        BlockPos src = pos.up(height);
        BlockPos dest = FeatureUtil.translate(src, length, angle, tilt);

        if (perpendicularBranches) {
            drawBresenhamBranch(world, treeRNG, src, new BlockPos(dest.getX(), src.getY(), dest.getZ()), trunkBlocks, mbb, config);

            int max = Math.max(src.getY(), dest.getY());

            for (int i = Math.min(src.getY(), dest.getY()); i < max + 1; i++) {
                func_236911_a_(world, treeRNG, new BlockPos(dest.getX(), i, dest.getZ()), trunkBlocks, mbb, config);
            }
        } else {
            drawBresenhamBranch(world, treeRNG, src, dest, trunkBlocks, mbb, config);
        }

        func_236911_a_(world, treeRNG, dest.east(), trunkBlocks, mbb, config);
        func_236911_a_(world, treeRNG, dest.west(), trunkBlocks, mbb, config);
        func_236911_a_(world, treeRNG, dest.south(), trunkBlocks, mbb, config);
        func_236911_a_(world, treeRNG, dest.north(), trunkBlocks, mbb, config);

        leafBlocks.add(new FoliagePlacer.Foliage(dest, 0, false));
    }

    private static void drawBresenhamBranch(IWorldGenerationReader world, Random random, BlockPos from, BlockPos to, Set<BlockPos> state, MutableBoundingBox mbb, BaseTreeFeatureConfig config) {
        for (BlockPos pixel : FeatureUtil.getBresenhamArrays(from, to)) {
            func_236911_a_(world, random, pixel, state, mbb, config);
        }
    }
}
