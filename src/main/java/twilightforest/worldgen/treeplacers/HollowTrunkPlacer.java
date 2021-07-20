package twilightforest.worldgen.treeplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.blockstateprovider.BlockStateProvider;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;
import twilightforest.util.FeatureUtil;
import twilightforest.worldgen.TwilightFeatures;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class HollowTrunkPlacer extends AbstractTrunkPlacer {
    public static final Codec<HollowTrunkPlacer> CODEC = RecordCodecBuilder.create(instance ->
            getAbstractTrunkCodec(instance).and(instance.group(
                    Codec.floatRange(1, 16).fieldOf("outside_radius").forGetter(o -> o.outerRadius),
                    Codec.intRange(0, 8).fieldOf("random_add_radius").forGetter(o -> o.randomAddRadius),
                    BranchesConfig.CODEC.fieldOf("branch_config").forGetter(o -> o.branchesConfig),
                    BlockStateProvider.CODEC.fieldOf("eastside_ladder").forGetter(o -> o.ladder)
            )).apply(instance, HollowTrunkPlacer::new)
    );

    private final float outerRadius;
    private final int randomAddRadius;
    private final BranchesConfig branchesConfig;
    private final BlockStateProvider ladder;

    public HollowTrunkPlacer(int baseHeight, int randomHeightA, int randomHeightB, float outerRadius, int randomAddRadius, BranchesConfig branchesConfig, BlockStateProvider ladder) {
        super(baseHeight, randomHeightA, randomHeightB);
        this.outerRadius = outerRadius;
        this.randomAddRadius = randomAddRadius;
        this.branchesConfig = branchesConfig;
        this.ladder = ladder;
    }

    @Override
    protected TrunkPlacerType<HollowTrunkPlacer> getPlacerType() {
        return TwilightFeatures.HOLLOW_TRUNK;
    }

    @Override
    public List<FoliagePlacer.Foliage> getFoliages(IWorldGenerationReader world, Random random, int height, BlockPos pos, Set<BlockPos> trunkBlocks, MutableBoundingBox mutableBoundingBox, BaseTreeFeatureConfig baseTreeFeatureConfig) {
        final float additionalRadius = random.nextInt(randomAddRadius + 1);
        final float outerRadius = this.outerRadius + additionalRadius;
        final float hollowRadius = outerRadius / 2f;

        final float outerRadiusSquared = outerRadius * outerRadius;
        final float hollowRadiusSquared = hollowRadius * hollowRadius;

        ArrayList<FoliagePlacer.Foliage> leaves = new ArrayList<>();

        buildTrunk(world, random, height, pos, trunkBlocks, mutableBoundingBox, baseTreeFeatureConfig, outerRadius, outerRadiusSquared, hollowRadiusSquared);

        buildFullCrown(world, random, pos, leaves, trunkBlocks, outerRadius, height, mutableBoundingBox, baseTreeFeatureConfig);

        return leaves;
    }

    private void buildTrunk(IWorldGenerationReader world, Random random, int height, BlockPos startPos, Set<BlockPos> trunkBlocks, MutableBoundingBox mutableBoundingBox, BaseTreeFeatureConfig baseTreeFeatureConfig, float outerRadius, float outerRadiusSquared, float hollowRadiusSquared) {
        int dist;
        boolean trunkDelta;
        boolean notHollow = false;

        // build the trunk upwards
        for (int z = 0; z <= outerRadius; z++) {
            for (int x = (int) outerRadius; x >= 0; x--) { // We go in reverse just to simplify logic for vines
                dist = x * x + z * z;

                if (dist < outerRadiusSquared) { // Can confirm inside the circle
                    trunkDelta = notHollow;
                    notHollow = dist >= hollowRadiusSquared;

                    for (int y = 0; y <= height; y++) {
                        BlockPos dPos = startPos.add(x, y, z);

                        if (trunkDelta && !notHollow) {
                            world.setBlockState(dPos, ladder.getBlockState(random, dPos), 3);

                            BlockPos opposite = startPos.add(x, y, -z);

                            if (!dPos.equals(opposite)) {
                                world.setBlockState(opposite, ladder.getBlockState(random, opposite), 3);
                            }
                        }

                        if (notHollow) {
                            func_236911_a_(world, random, dPos, trunkBlocks, mutableBoundingBox, baseTreeFeatureConfig);
                            func_236911_a_(world, random, startPos.add( -x, y, -z), trunkBlocks, mutableBoundingBox, baseTreeFeatureConfig);
                            func_236911_a_(world, random, startPos.add( -z, y,  x), trunkBlocks, mutableBoundingBox, baseTreeFeatureConfig);
                            func_236911_a_(world, random, startPos.add(  z, y, -x), trunkBlocks, mutableBoundingBox, baseTreeFeatureConfig);
                        }
                    }
                }
            }
        }
    }

    protected void buildFullCrown(IWorldGenerationReader world, Random random, BlockPos pos, List<FoliagePlacer.Foliage> leaves, Set<BlockPos> branch, float diameter, int height, MutableBoundingBox mbb, BaseTreeFeatureConfig config) {
        int crownRadius = (int) (diameter * 4f + 4f);
        int bvar = (int) (diameter + 2f);

        // okay, let's do 3-5 main branches starting at the bottom of the crown
        buildBranchRing(world, random, pos, leaves, branch, diameter, height - crownRadius, 0, crownRadius, 0.35D, bvar, bvar + 2, 2, true, mbb, config);

        // then, let's do 3-5 medium branches at the crown middle
        buildBranchRing(world, random, pos, leaves, branch, diameter, height - (crownRadius >> 1), 0, crownRadius, 0.28D, bvar, bvar + 2, 1, true, mbb, config);

        // finally, let's do 2-4 main branches at the crown top
        buildBranchRing(world, random, pos, leaves, branch, diameter, height, 0, crownRadius, 0.15D, 2, 4, 2, true, mbb, config);

        // and extra finally, let's do 3-6 medium branches going straight up
        buildBranchRing(world, random, pos, leaves, branch, diameter, height, 0, crownRadius >> 1, 0.05D, bvar, bvar + 2, 1, true, mbb, config);

        // this glass sphere approximates where we want our crown
        //drawBlob(x, y + height, z, (byte)crownRadius, (byte)Blocks.GLASS, false);
    }

    /**
     * Build a ring of branches around the tree
     * size 0 = small, 1 = med, 2 = large, 3 = root
     */
    protected void buildBranchRing(IWorldGenerationReader world, Random random, BlockPos pos, List<FoliagePlacer.Foliage> leaves, Set<BlockPos> branch, float diameter, int branchHeight, int heightVar, float length, double tilt, int minBranches, int maxBranches, int size, boolean leafy, MutableBoundingBox mbb, BaseTreeFeatureConfig config) {
        //let's do this!
        int numBranches = random.nextInt(maxBranches - minBranches) + minBranches;

        double branchRotation = 1.0 / (numBranches + 1);
        double branchOffset = random.nextDouble();

        for (int i = 0; i <= numBranches; i++) {
            int dHeight;
            if (heightVar > 0) {
                dHeight = branchHeight - heightVar + random.nextInt(2 * heightVar);
            } else {
                dHeight = branchHeight;
            }

            if (size == 2) {
                makeLargeBranch(world, random, pos, leaves, branch, diameter, dHeight, length, i * branchRotation + branchOffset, tilt, leafy, mbb, config);
            } else if (size == 1) {
                makeMedBranch(world, random, pos, leaves, branch, diameter, dHeight, length, i * branchRotation + branchOffset, tilt, leafy, mbb, config);
            } else if (size == 3) {
                makeRoot(world, random, pos, diameter, dHeight, length, i * branchRotation + branchOffset, tilt, config);
            } else {
                makeSmallBranch(world, random, pos, leaves, branch, diameter, dHeight, length, i * branchRotation + branchOffset, tilt, leafy, mbb, config);
            }
        }
    }

    /**
     * Make a large, branching "base" branch off of the tree
     */
    protected void makeLargeBranch(IWorldGenerationReader world, Random random, BlockPos pos, List<FoliagePlacer.Foliage> leaves, Set<BlockPos> branch, float diameter, int branchHeight, float length, double angle, double tilt, boolean leafy, MutableBoundingBox mbb, BaseTreeFeatureConfig config) {
        BlockPos src = FeatureUtil.translate(pos.up(branchHeight), diameter, angle, 0.5);
        makeLargeBranch(world, random, src, leaves, branch, length, angle, tilt, leafy, mbb, config);
    }

    /**
     * Make a large, branching "base" branch in a specific location.
     * <p>
     * The large branch will have 1-4 medium branches and several small branches too
     */
    protected void makeLargeBranch(IWorldGenerationReader world, Random random, BlockPos src, List<FoliagePlacer.Foliage> leaves, Set<BlockPos> branch, float length, double angle, double tilt, boolean leafy, MutableBoundingBox mbb, BaseTreeFeatureConfig config) {
        BlockPos dest = FeatureUtil.translate(src, length, angle, tilt);

        // draw the main branch
        FeatureUtil.drawBresenhamBranch(world, random, src, dest, branch, mbb, config);

        // reinforce it
        //drawBresehnam(src[0], src[1] + 1, src[2], dest[0], dest[1], dest[2], treeBlock, true);
        int reinforcements = random.nextInt(3);
        for (int i = 0; i <= reinforcements; i++) {
            int vx = (i & 2) == 0 ? 1 : 0;
            int vy = (i & 1) == 0 ? 1 : -1;
            int vz = (i & 2) == 0 ? 0 : 1;
            FeatureUtil.drawBresenhamBranch(world, random, src.add(vx, vy, vz), dest, branch, mbb, config);
        }

        if (leafy) {
            // add a leaf blob at the end
            //FeatureUtil.drawLeafBlob(world, dest.up(), 3, config.leavesProvider.getBlockState(random, dest.up()), leaves);
            leaves.add(new FoliagePlacer.Foliage(dest.up(), 3, false));
        }

        // go about halfway out and make a few medium branches.
        // the number of medium branches we can support depends on the length of the big branch
        // every other branch switches sides
        int numMedBranches = random.nextInt((int) (length / 6)) + random.nextInt(2) + 1;

        for (int i = 0; i <= numMedBranches; i++) {

            double outVar = (random.nextDouble() * 0.3) + 0.3;
            double angleVar = random.nextDouble() * 0.225 * ((i & 1) == 0 ? 1.0 : -1.0);
            BlockPos bsrc = FeatureUtil.translate(src, length * outVar, angle, tilt);

            makeMedBranch(world, random, bsrc, leaves, branch, length * 0.6, angle + angleVar, tilt, leafy, mbb, config);
        }

        // make 1-2 small ones near the base
        int numSmallBranches = random.nextInt(2) + 1;
        for (int i = 0; i <= numSmallBranches; i++) {

            double outVar = (random.nextDouble() * 0.25) + 0.25;
            double angleVar = random.nextDouble() * 0.25 * ((i & 1) == 0 ? 1.0 : -1.0);
            BlockPos bsrc = FeatureUtil.translate(src, length * outVar, angle, tilt);

            makeSmallBranch(world, random, bsrc, leaves, branch, Math.max(length * 0.3, 2), angle + angleVar, tilt, leafy, mbb, config);
        }

        //if (random.nextInt(LEAF_DUNGEON_CHANCE) == 0) {
        //    makeLeafDungeon(world, random, dest.up(), leaves, config);
        //}
    }

    /**
     * Make a branch!
     */
    protected void makeMedBranch(IWorldGenerationReader world, Random random, BlockPos pos, List<FoliagePlacer.Foliage> leaves, Set<BlockPos> branch, float diameter, int branchHeight, double length, double angle, double tilt, boolean leafy, MutableBoundingBox mbb, BaseTreeFeatureConfig config) {
        BlockPos src = FeatureUtil.translate(pos.up(branchHeight), diameter, angle, 0.5);
        makeMedBranch(world, random, src, leaves, branch, length, angle, tilt, leafy, mbb, config);
    }

    /**
     * Make a branch!
     */
    protected void makeMedBranch(IWorldGenerationReader world, Random random, BlockPos src, List<FoliagePlacer.Foliage> leaves, Set<BlockPos> branch, double length, double angle, double tilt, boolean leafy, MutableBoundingBox mbb, BaseTreeFeatureConfig config) {
        BlockPos dest = FeatureUtil.translate(src, length, angle, tilt);

        FeatureUtil.drawBresenhamBranch(world, random, src, dest, branch, mbb, config);

        // with leaves!

        if (leafy) {
			/*
			int numLeafBalls = random.nextInt(2) + 1;
			for(int i = 0; i <= numLeafBalls; i++) {

				double slength = random.nextDouble() * 0.6 + 0.2;
				int[] bdst = translate(src[0], src[1], src[2], slength, angle, tilt);

				drawBlob(bdst[0], bdst[1], bdst[2], 2, leafBlock, false);
			}
			*/

            // and a blob at the end
            // FeatureUtil.drawLeafBlob(world, dest, 2, config.leavesProvider.getBlockState(random, dest), leaves);
            leaves.add(new FoliagePlacer.Foliage(dest, 2, false));
        }

        // and several small branches

        int numShoots = random.nextInt(2) + 1;
        double angleInc, angleVar, outVar, tiltVar;

        angleInc = 0.8 / numShoots;

        for (int i = 0; i <= numShoots; i++) {

            angleVar = (angleInc * i) - 0.4;
            outVar = (random.nextDouble() * 0.8) + 0.2;
            tiltVar = (random.nextDouble() * 0.75) + 0.15;

            BlockPos bsrc = FeatureUtil.translate(src, length * outVar, angle, tilt);
            double slength = length * 0.4;

            makeSmallBranch(world, random, bsrc, leaves, branch, slength, angle + angleVar, tilt * tiltVar, leafy, mbb, config);
        }
    }

    /**
     * Make a small branch with a leaf blob at the end
     */
    protected void makeSmallBranch(IWorldGenerationReader world, Random random, BlockPos src, List<FoliagePlacer.Foliage> leaves, Set<BlockPos> branch, double length, double angle, double tilt, boolean leafy, MutableBoundingBox mbb, BaseTreeFeatureConfig config) {
        BlockPos dest = FeatureUtil.translate(src, length, angle, tilt);

        FeatureUtil.drawBresenhamBranch(world, random, src, dest, branch, mbb, config);

        if (leafy) {
            //FeatureUtil.makeLeafSpheroid(world, dest, (byte) (random.nextInt(2) + 1), config.leavesProvider.getBlockState(random, dest), leaves);
            leaves.add(new FoliagePlacer.Foliage(dest, random.nextInt(2) + 1, false));
        }
    }

    /**
     * Make a small branch at a certain height
     */
    protected void makeSmallBranch(IWorldGenerationReader world, Random random, BlockPos pos, List<FoliagePlacer.Foliage> leaves, Set<BlockPos> branch, float diameter, int branchHeight, double length, double angle, double tilt, boolean leafy, MutableBoundingBox mbb, BaseTreeFeatureConfig config) {
        BlockPos src = FeatureUtil.translate(pos.up(branchHeight), diameter, angle, 0.5);
        makeSmallBranch(world, random, src, leaves, branch, length, angle, tilt, leafy, mbb, config);
    }

    /**
     * Make a root
     */
    protected void makeRoot(IWorldGenerationReader world, Random random, BlockPos pos, float diameter, int branchHeight, double length, double angle, double tilt, BaseTreeFeatureConfig config) {
        BlockPos src = FeatureUtil.translate(pos.up(branchHeight), diameter, angle, 0.5);
        BlockPos dest = FeatureUtil.translate(src, length, angle, tilt);

        BlockPos[] lineArray = FeatureUtil.getBresenhamArrays(src, dest);
        boolean stillAboveGround = true;
        for (BlockPos coord : lineArray) {
            if (stillAboveGround && FeatureUtil.hasAirAround(world, coord)) {
                world.setBlockState(coord, config.trunkProvider.getBlockState(random, coord), 3);
                world.setBlockState(coord.down(), config.trunkProvider.getBlockState(random, coord.down()), 3);
            } else {
                // TODO Hook in Root Decor
                world.setBlockState(coord, config.trunkProvider.getBlockState(random, coord), 3);
                world.setBlockState(coord.down(), config.trunkProvider.getBlockState(random, coord.down()), 3);
                stillAboveGround = false;
            }
        }
    }
}
