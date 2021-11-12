package twilightforest.world.components.feature.trees.growers;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.core.Direction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.shapes.BitSetDiscreteVoxelShape;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelWriter;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

import java.util.*;
import java.util.function.BiConsumer;

//[VanillaCopy] of TreeFeature but allows trees on snow
public class SnowTreePlacer extends Feature<TreeConfiguration> {
    public SnowTreePlacer(Codec<TreeConfiguration> p_67201_) {
        super(p_67201_);
    }

    public static boolean isFree(LevelSimulatedReader p_67263_, BlockPos p_67264_) {
        return validTreePos(p_67263_, p_67264_) || p_67263_.isStateAtPosition(p_67264_, (p_67281_) -> p_67281_.is(BlockTags.LOGS));
    }

    private static boolean isVine(LevelSimulatedReader p_67278_, BlockPos p_67279_) {
        return p_67278_.isStateAtPosition(p_67279_, (p_67276_) -> p_67276_.is(Blocks.VINE));
    }

    private static boolean isBlockWater(LevelSimulatedReader p_67283_, BlockPos p_67284_) {
        return p_67283_.isStateAtPosition(p_67284_, (p_67271_) -> p_67271_.is(Blocks.WATER));
    }

    public static boolean isAirOrLeaves(LevelSimulatedReader p_67268_, BlockPos p_67269_) {
        return p_67268_.isStateAtPosition(p_67269_, (p_67266_) -> p_67266_.isAir() || p_67266_.is(BlockTags.LEAVES));
    }

    private static boolean isReplaceablePlant(LevelSimulatedReader p_67289_, BlockPos p_67290_) {
        return p_67289_.isStateAtPosition(p_67290_, (p_160551_) -> {
            Material material = p_160551_.getMaterial();
            return material == Material.REPLACEABLE_PLANT;
        });
    }

    public static boolean isBlockUnderValid(LevelSimulatedReader reader, BlockPos pos) {
        return reader.isStateAtPosition(pos, state -> state.is(BlockTags.SNOW)) || reader.isStateAtPosition(pos, state -> state.is(BlockTags.DIRT));
    }

    private static void setBlockKnownShape(LevelWriter p_67257_, BlockPos p_67258_, BlockState p_67259_) {
        p_67257_.setBlock(p_67258_, p_67259_, 19);
    }

    public static boolean validTreePos(LevelSimulatedReader p_67273_, BlockPos p_67274_) {
        return isAirOrLeaves(p_67273_, p_67274_) || isReplaceablePlant(p_67273_, p_67274_) || isBlockWater(p_67273_, p_67274_);
    }

    private boolean doPlace(WorldGenLevel p_160511_, Random p_160512_, BlockPos p_160513_, BiConsumer<BlockPos, BlockState> p_160514_, BiConsumer<BlockPos, BlockState> p_160515_, TreeConfiguration p_160516_) {
        int i = p_160516_.trunkPlacer.getTreeHeight(p_160512_);
        int j = p_160516_.foliagePlacer.foliageHeight(p_160512_, i, p_160516_);
        int k = i - j;
        int l = p_160516_.foliagePlacer.foliageRadius(p_160512_, k);
        if (p_160513_.getY() >= p_160511_.getMinBuildHeight() + 1 && p_160513_.getY() + i + 1 <= p_160511_.getMaxBuildHeight()) {
            if (!isBlockUnderValid(p_160511_, p_160513_.below())) {
                return false;
            } else {
                OptionalInt optionalint = p_160516_.minimumSize.minClippedHeight();
                int i1 = this.getMaxFreeTreeHeight(p_160511_, i, p_160513_, p_160516_);
                if (i1 >= i || optionalint.isPresent() && i1 >= optionalint.getAsInt()) {
                    List<FoliagePlacer.FoliageAttachment> list = p_160516_.trunkPlacer.placeTrunk(p_160511_, p_160514_, p_160512_, i1, p_160513_, p_160516_);
                    list.forEach((p_160539_) -> {
                        p_160516_.foliagePlacer.createFoliage(p_160511_, p_160515_, p_160512_, p_160516_, i1, p_160539_, j, l);
                    });
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }
    }

    private int getMaxFreeTreeHeight(LevelSimulatedReader p_67216_, int p_67217_, BlockPos p_67218_, TreeConfiguration p_67219_) {
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(int i = 0; i <= p_67217_ + 1; ++i) {
            int j = p_67219_.minimumSize.getSizeAtHeight(p_67217_, i);

            for(int k = -j; k <= j; ++k) {
                for(int l = -j; l <= j; ++l) {
                    blockpos$mutableblockpos.setWithOffset(p_67218_, k, i, l);
                    if (!isFree(p_67216_, blockpos$mutableblockpos) || !p_67219_.ignoreVines && isVine(p_67216_, blockpos$mutableblockpos)) {
                        return i - 2;
                    }
                }
            }
        }

        return p_67217_;
    }

    @Override
    protected void setBlock(LevelWriter p_67221_, BlockPos p_67222_, BlockState p_67223_) {
        setBlockKnownShape(p_67221_, p_67222_, p_67223_);
    }

    @Override
    public final boolean place(FeaturePlaceContext<TreeConfiguration> p_160530_) {
        WorldGenLevel worldgenlevel = p_160530_.level();
        Random random = p_160530_.random();
        BlockPos blockpos = p_160530_.origin();
        TreeConfiguration treeconfiguration = p_160530_.config();
        Set<BlockPos> set = Sets.newHashSet();
        Set<BlockPos> set1 = Sets.newHashSet();
        Set<BlockPos> set2 = Sets.newHashSet();
        BiConsumer<BlockPos, BlockState> biconsumer = (p_160555_, p_160556_) -> {
            set.add(p_160555_.immutable());
            worldgenlevel.setBlock(p_160555_, p_160556_, 19);
        };
        BiConsumer<BlockPos, BlockState> biconsumer1 = (p_160548_, p_160549_) -> {
            set1.add(p_160548_.immutable());
            worldgenlevel.setBlock(p_160548_, p_160549_, 19);
        };
        BiConsumer<BlockPos, BlockState> biconsumer2 = (p_160543_, p_160544_) -> {
            set2.add(p_160543_.immutable());
            worldgenlevel.setBlock(p_160543_, p_160544_, 19);
        };
        boolean flag = this.doPlace(worldgenlevel, random, blockpos, biconsumer, biconsumer1, treeconfiguration);
        if (flag && (!set.isEmpty() || !set1.isEmpty())) {
            if (!treeconfiguration.decorators.isEmpty()) {
                List<BlockPos> list = Lists.newArrayList(set);
                List<BlockPos> list1 = Lists.newArrayList(set1);
                list.sort(Comparator.comparingInt(Vec3i::getY));
                list1.sort(Comparator.comparingInt(Vec3i::getY));
                treeconfiguration.decorators.forEach((p_160528_) -> {
                    p_160528_.place(worldgenlevel, biconsumer2, random, list, list1);
                });
            }

            return BoundingBox.encapsulatingPositions(Iterables.concat(set, set1, set2)).map((p_160521_) -> {
                DiscreteVoxelShape discretevoxelshape = updateLeaves(worldgenlevel, p_160521_, set, set2);
                StructureTemplate.updateShapeAtEdge(worldgenlevel, 3, discretevoxelshape, p_160521_.minX(), p_160521_.minY(), p_160521_.minZ());
                return true;
            }).orElse(false);
        } else {
            return false;
        }
    }

    private static DiscreteVoxelShape updateLeaves(LevelAccessor p_67203_, BoundingBox p_67204_, Set<BlockPos> p_67205_, Set<BlockPos> p_67206_) {
        List<Set<BlockPos>> list = Lists.newArrayList();
        DiscreteVoxelShape discretevoxelshape = new BitSetDiscreteVoxelShape(p_67204_.getXSpan(), p_67204_.getYSpan(), p_67204_.getZSpan());
        int i = 6;

        for(int j = 0; j < 6; ++j) {
            list.add(Sets.newHashSet());
        }

        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();

        for(BlockPos blockpos : Lists.newArrayList(p_67206_)) {
            if (p_67204_.isInside(blockpos)) {
                discretevoxelshape.fill(blockpos.getX() - p_67204_.minX(), blockpos.getY() - p_67204_.minY(), blockpos.getZ() - p_67204_.minZ());
            }
        }

        for(BlockPos blockpos1 : Lists.newArrayList(p_67205_)) {
            if (p_67204_.isInside(blockpos1)) {
                discretevoxelshape.fill(blockpos1.getX() - p_67204_.minX(), blockpos1.getY() - p_67204_.minY(), blockpos1.getZ() - p_67204_.minZ());
            }

            for(Direction direction : Direction.values()) {
                blockpos$mutableblockpos.setWithOffset(blockpos1, direction);
                if (!p_67205_.contains(blockpos$mutableblockpos)) {
                    BlockState blockstate = p_67203_.getBlockState(blockpos$mutableblockpos);
                    if (blockstate.hasProperty(BlockStateProperties.DISTANCE)) {
                        list.get(0).add(blockpos$mutableblockpos.immutable());
                        setBlockKnownShape(p_67203_, blockpos$mutableblockpos, blockstate.setValue(BlockStateProperties.DISTANCE, Integer.valueOf(1)));
                        if (p_67204_.isInside(blockpos$mutableblockpos)) {
                            discretevoxelshape.fill(blockpos$mutableblockpos.getX() - p_67204_.minX(), blockpos$mutableblockpos.getY() - p_67204_.minY(), blockpos$mutableblockpos.getZ() - p_67204_.minZ());
                        }
                    }
                }
            }
        }

        for(int l = 1; l < 6; ++l) {
            Set<BlockPos> set = list.get(l - 1);
            Set<BlockPos> set1 = list.get(l);

            for(BlockPos blockpos2 : set) {
                if (p_67204_.isInside(blockpos2)) {
                    discretevoxelshape.fill(blockpos2.getX() - p_67204_.minX(), blockpos2.getY() - p_67204_.minY(), blockpos2.getZ() - p_67204_.minZ());
                }

                for(Direction direction1 : Direction.values()) {
                    blockpos$mutableblockpos.setWithOffset(blockpos2, direction1);
                    if (!set.contains(blockpos$mutableblockpos) && !set1.contains(blockpos$mutableblockpos)) {
                        BlockState blockstate1 = p_67203_.getBlockState(blockpos$mutableblockpos);
                        if (blockstate1.hasProperty(BlockStateProperties.DISTANCE)) {
                            int k = blockstate1.getValue(BlockStateProperties.DISTANCE);
                            if (k > l + 1) {
                                BlockState blockstate2 = blockstate1.setValue(BlockStateProperties.DISTANCE, Integer.valueOf(l + 1));
                                setBlockKnownShape(p_67203_, blockpos$mutableblockpos, blockstate2);
                                if (p_67204_.isInside(blockpos$mutableblockpos)) {
                                    discretevoxelshape.fill(blockpos$mutableblockpos.getX() - p_67204_.minX(), blockpos$mutableblockpos.getY() - p_67204_.minY(), blockpos$mutableblockpos.getZ() - p_67204_.minZ());
                                }

                                set1.add(blockpos$mutableblockpos.immutable());
                            }
                        }
                    }
                }
            }
        }

        return discretevoxelshape;
    }
}