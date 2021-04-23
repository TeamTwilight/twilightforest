package twilightforest.world.feature.tree;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.Material;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.math.shapes.BitSetVoxelShapePart;
import net.minecraft.util.math.shapes.VoxelShapePart;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldWriter;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.IWorldGenerationReader;
import net.minecraft.world.gen.feature.BaseTreeFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;

import java.util.*;

//[VanillaCopy] of TreeFeature but allows trees on snow
public class SnowTreePlacer extends Feature<BaseTreeFeatureConfig> {

    public SnowTreePlacer(Codec<BaseTreeFeatureConfig> p_i231999_1_) {
        super(p_i231999_1_);
    }

    public static boolean func_236410_c_(IWorldGenerationBaseReader p_236410_0_, BlockPos p_236410_1_) {
        return isReplaceableAt(p_236410_0_, p_236410_1_) || p_236410_0_.hasBlockState(p_236410_1_, (p_236417_0_) -> {
            return p_236417_0_.isIn(BlockTags.LOGS);
        });
    }

    private static boolean func_236414_e_(IWorldGenerationBaseReader p_236414_0_, BlockPos p_236414_1_) {
        return p_236414_0_.hasBlockState(p_236414_1_, (p_236415_0_) -> {
            return p_236415_0_.isIn(Blocks.VINE);
        });
    }

    private static boolean isWaterAt(IWorldGenerationBaseReader p_236416_0_, BlockPos p_236416_1_) {
        return p_236416_0_.hasBlockState(p_236416_1_, (p_236413_0_) -> {
            return p_236413_0_.isIn(Blocks.WATER);
        });
    }

    public static boolean isAirOrLeavesAt(IWorldGenerationBaseReader p_236412_0_, BlockPos p_236412_1_) {
        return p_236412_0_.hasBlockState(p_236412_1_, (p_236411_0_) -> {
            return p_236411_0_.isAir() || p_236411_0_.isIn(BlockTags.LEAVES);
        });
    }

    private static boolean isDirtOrSnowAt(IWorldGenerationBaseReader p_236418_0_, BlockPos p_236418_1_) {
        return p_236418_0_.hasBlockState(p_236418_1_, (p_236409_0_) -> {
            Block block = p_236409_0_.getBlock();
            return isDirt(block) || block == Blocks.SNOW_BLOCK;
        });
    }

    private static boolean isTallPlantAt(IWorldGenerationBaseReader p_236419_0_, BlockPos p_236419_1_) {
        return p_236419_0_.hasBlockState(p_236419_1_, (p_236406_0_) -> {
            Material material = p_236406_0_.getMaterial();
            return material == Material.TALL_PLANTS;
        });
    }

    public static void func_236408_b_(IWorldWriter p_236408_0_, BlockPos p_236408_1_, BlockState p_236408_2_) {
        p_236408_0_.setBlockState(p_236408_1_, p_236408_2_, 19);
    }

    public static boolean isReplaceableAt(IWorldGenerationBaseReader p_236404_0_, BlockPos p_236404_1_) {
        return isAirOrLeavesAt(p_236404_0_, p_236404_1_) || isTallPlantAt(p_236404_0_, p_236404_1_) || isWaterAt(p_236404_0_, p_236404_1_);
    }

    private boolean place(IWorldGenerationReader generationReader, Random rand, BlockPos positionIn, Set<BlockPos> p_225557_4_, Set<BlockPos> p_225557_5_, MutableBoundingBox boundingBoxIn, BaseTreeFeatureConfig configIn) {
        int i = configIn.trunkPlacer.func_236917_a_(rand);
        int j = configIn.foliagePlacer.func_230374_a_(rand, i, configIn);
        int k = i - j;
        int l = configIn.foliagePlacer.func_230376_a_(rand, k);
        BlockPos blockpos;
        if (!configIn.forcePlacement) {
            int i1 = generationReader.getHeight(Heightmap.Type.OCEAN_FLOOR, positionIn).getY();
            int j1 = generationReader.getHeight(Heightmap.Type.WORLD_SURFACE, positionIn).getY();
            if (j1 - i1 > configIn.maxWaterDepth) {
                return false;
            }

            int k1;
            if (configIn.field_236682_l_ == Heightmap.Type.OCEAN_FLOOR) {
                k1 = i1;
            } else if (configIn.field_236682_l_ == Heightmap.Type.WORLD_SURFACE) {
                k1 = j1;
            } else {
                k1 = generationReader.getHeight(configIn.field_236682_l_, positionIn).getY();
            }

            blockpos = new BlockPos(positionIn.getX(), k1, positionIn.getZ());
        } else {
            blockpos = positionIn;
        }

        if (blockpos.getY() >= 1 && blockpos.getY() + i + 1 <= 256) {
            if (!isDirtOrSnowAt(generationReader, blockpos.down())) {
                return false;
            } else {
                OptionalInt optionalint = configIn.minimumSize.func_236710_c_();
                int l1 = this.func_241521_a_(generationReader, i, blockpos, configIn);
                if (l1 >= i || optionalint.isPresent() && l1 >= optionalint.getAsInt()) {
                    List<FoliagePlacer.Foliage> list = configIn.trunkPlacer.func_230382_a_(generationReader, rand, l1, blockpos, p_225557_4_, boundingBoxIn, configIn);
                    list.forEach((p_236407_8_) -> {
                        configIn.foliagePlacer.func_236752_a_(generationReader, rand, configIn, l1, p_236407_8_, j, l, p_225557_5_, boundingBoxIn);
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

    private int func_241521_a_(IWorldGenerationBaseReader p_241521_1_, int p_241521_2_, BlockPos p_241521_3_, BaseTreeFeatureConfig p_241521_4_) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(int i = 0; i <= p_241521_2_ + 1; ++i) {
            int j = p_241521_4_.minimumSize.func_230369_a_(p_241521_2_, i);

            for(int k = -j; k <= j; ++k) {
                for(int l = -j; l <= j; ++l) {
                    blockpos$mutable.setAndOffset(p_241521_3_, k, i, l);
                    if (!func_236410_c_(p_241521_1_, blockpos$mutable) || !p_241521_4_.ignoreVines && func_236414_e_(p_241521_1_, blockpos$mutable)) {
                        return i - 2;
                    }
                }
            }
        }

        return p_241521_2_;
    }

    protected void setBlockState(IWorldWriter world, BlockPos pos, BlockState state) {
        func_236408_b_(world, pos, state);
    }

    @Override
    public final boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BaseTreeFeatureConfig config) {
        Set<BlockPos> set = Sets.newHashSet();
        Set<BlockPos> set1 = Sets.newHashSet();
        Set<BlockPos> set2 = Sets.newHashSet();
        MutableBoundingBox mutableboundingbox = MutableBoundingBox.getNewBoundingBox();
        boolean flag = this.place(reader, rand, pos, set, set1, mutableboundingbox, config);
        if (mutableboundingbox.minX <= mutableboundingbox.maxX && flag && !set.isEmpty()) {
            if (!config.decorators.isEmpty()) {
                List<BlockPos> list = Lists.newArrayList(set);
                List<BlockPos> list1 = Lists.newArrayList(set1);
                list.sort(Comparator.comparingInt(Vector3i::getY));
                list1.sort(Comparator.comparingInt(Vector3i::getY));
                config.decorators.forEach((p_236405_6_) -> {
                    p_236405_6_.func_225576_a_(reader, rand, list, list1, set2, mutableboundingbox);
                });
            }

            VoxelShapePart voxelshapepart = this.func_236403_a_(reader, mutableboundingbox, set, set2);
            Template.func_222857_a(reader, 3, voxelshapepart, mutableboundingbox.minX, mutableboundingbox.minY, mutableboundingbox.minZ);
            return true;
        } else {
            return false;
        }
    }

    private VoxelShapePart func_236403_a_(IWorld p_236403_1_, MutableBoundingBox p_236403_2_, Set<BlockPos> p_236403_3_, Set<BlockPos> p_236403_4_) {
        List<Set<BlockPos>> list = Lists.newArrayList();
        VoxelShapePart voxelshapepart = new BitSetVoxelShapePart(p_236403_2_.getXSize(), p_236403_2_.getYSize(), p_236403_2_.getZSize());
        int i = 6;

        for(int j = 0; j < 6; ++j) {
            list.add(Sets.newHashSet());
        }

        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for(BlockPos blockpos : Lists.newArrayList(p_236403_4_)) {
            if (p_236403_2_.isVecInside(blockpos)) {
                voxelshapepart.setFilled(blockpos.getX() - p_236403_2_.minX, blockpos.getY() - p_236403_2_.minY, blockpos.getZ() - p_236403_2_.minZ, true, true);
            }
        }

        for(BlockPos blockpos1 : Lists.newArrayList(p_236403_3_)) {
            if (p_236403_2_.isVecInside(blockpos1)) {
                voxelshapepart.setFilled(blockpos1.getX() - p_236403_2_.minX, blockpos1.getY() - p_236403_2_.minY, blockpos1.getZ() - p_236403_2_.minZ, true, true);
            }

            for(Direction direction : Direction.values()) {
                blockpos$mutable.setAndMove(blockpos1, direction);
                if (!p_236403_3_.contains(blockpos$mutable)) {
                    BlockState blockstate = p_236403_1_.getBlockState(blockpos$mutable);
                    if (blockstate.hasProperty(BlockStateProperties.DISTANCE_1_7)) {
                        list.get(0).add(blockpos$mutable.toImmutable());
                        func_236408_b_(p_236403_1_, blockpos$mutable, blockstate.with(BlockStateProperties.DISTANCE_1_7, Integer.valueOf(1)));
                        if (p_236403_2_.isVecInside(blockpos$mutable)) {
                            voxelshapepart.setFilled(blockpos$mutable.getX() - p_236403_2_.minX, blockpos$mutable.getY() - p_236403_2_.minY, blockpos$mutable.getZ() - p_236403_2_.minZ, true, true);
                        }
                    }
                }
            }
        }

        for(int l = 1; l < 6; ++l) {
            Set<BlockPos> set = list.get(l - 1);
            Set<BlockPos> set1 = list.get(l);

            for(BlockPos blockpos2 : set) {
                if (p_236403_2_.isVecInside(blockpos2)) {
                    voxelshapepart.setFilled(blockpos2.getX() - p_236403_2_.minX, blockpos2.getY() - p_236403_2_.minY, blockpos2.getZ() - p_236403_2_.minZ, true, true);
                }

                for(Direction direction1 : Direction.values()) {
                    blockpos$mutable.setAndMove(blockpos2, direction1);
                    if (!set.contains(blockpos$mutable) && !set1.contains(blockpos$mutable)) {
                        BlockState blockstate1 = p_236403_1_.getBlockState(blockpos$mutable);
                        if (blockstate1.hasProperty(BlockStateProperties.DISTANCE_1_7)) {
                            int k = blockstate1.get(BlockStateProperties.DISTANCE_1_7);
                            if (k > l + 1) {
                                BlockState blockstate2 = blockstate1.with(BlockStateProperties.DISTANCE_1_7, Integer.valueOf(l + 1));
                                func_236408_b_(p_236403_1_, blockpos$mutable, blockstate2);
                                if (p_236403_2_.isVecInside(blockpos$mutable)) {
                                    voxelshapepart.setFilled(blockpos$mutable.getX() - p_236403_2_.minX, blockpos$mutable.getY() - p_236403_2_.minY, blockpos$mutable.getZ() - p_236403_2_.minZ, true, true);
                                }

                                set1.add(blockpos$mutable.toImmutable());
                            }
                        }
                    }
                }
            }
        }

        return voxelshapepart;
    }
}