package twilightforest.world.feature;

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
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import twilightforest.util.FeatureUtil;
import twilightforest.world.TFGenerationSettings;
import twilightforest.world.feature.config.TFTreeFeatureConfig;

import java.util.*;

/**
 * Makes large trees with flat leaf ovals that provide a canopy for the forest
 *
 * @author Ben
 */

//Lots of things from TreeFeature, but we're checking for dirt to place on
public class TFGenDarkCanopyTree extends Feature<BaseTreeFeatureConfig> {

	int difference = 0;
	BlockPos validPos = new BlockPos(0, 0, 0);

	public TFGenDarkCanopyTree(Codec<BaseTreeFeatureConfig> config) {
		super(config);
	}

	@Override
	public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BaseTreeFeatureConfig config) {
		// if we are given leaves as a starting position, seek dirt or grass underneath
		boolean foundDirt = false;
		Material materialUnder;
		for (int dy = pos.getY(); dy >= TFGenerationSettings.SEALEVEL; dy--) {
			materialUnder = reader.getBlockState(new BlockPos(pos.getX(), dy - 1, pos.getZ())).getMaterial();
			if (materialUnder == Material.ORGANIC || materialUnder == Material.EARTH) {
				// yes!
				foundDirt = true;
				pos = new BlockPos(pos.getX(), dy, pos.getZ());
				validPos = pos;
				break;
			} else if (materialUnder == Material.ROCK || materialUnder == Material.SAND) {
				// nope
				break;
			}
		}

		if (!foundDirt) {
			return false;
		}

		// do not grow next to another tree
		for (Direction e : Direction.Plane.HORIZONTAL) {
			if (reader.getBlockState(pos.offset(e)).getMaterial() == Material.WOOD)
				return false;
		}

		//Taken from TreeFeature.generate, adjusting our BoundingBox to fit where the dirt is
		Set<BlockPos> set = Sets.newHashSet();
		Set<BlockPos> set1 = Sets.newHashSet();
		Set<BlockPos> set2 = Sets.newHashSet();
		MutableBoundingBox mutableboundingbox = MutableBoundingBox.getNewBoundingBox();
		boolean flag = place(reader, rand, pos, set, set1, mutableboundingbox, config);
		difference = mutableboundingbox.minY - pos.getY();
		mutableboundingbox.minY = pos.getY();
		mutableboundingbox.maxY = mutableboundingbox.maxY - difference;
		if(flag && !set.isEmpty()) {
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

	//Mostly [VanillaCopy] of TreeFeature.place, edits noted
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
			//set our blockpos to the valid dirt pos, not highest ground
			blockpos = new BlockPos(positionIn.getX(), validPos.getY(), positionIn.getZ());
		} else {
			blockpos = positionIn;
		}

		if (blockpos.getY() >= 1 && blockpos.getY() + i + 1 <= 256) {
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
		} else {
			return false;
		}
	}

	//everything beyond this point is a [VanillaCopy] of TreeFeature
	private int func_241521_a_(IWorldGenerationBaseReader p_241521_1_, int p_241521_2_, BlockPos p_241521_3_, BaseTreeFeatureConfig p_241521_4_) {
		BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

		for(int i = 0; i <= p_241521_2_ + 1; ++i) {
			int j = p_241521_4_.minimumSize.func_230369_a_(p_241521_2_, i);

			for(int k = -j; k <= j; ++k) {
				for(int l = -j; l <= j; ++l) {
					blockpos$mutable.setAndOffset(p_241521_3_, k, i, l);
					if (!p_241521_4_.ignoreVines) {
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

	public static void func_236408_b_(IWorldWriter p_236408_0_, BlockPos p_236408_1_, BlockState p_236408_2_) {
		p_236408_0_.setBlockState(p_236408_1_, p_236408_2_, 19);
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
