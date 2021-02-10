package twilightforest.block;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.PlantType;
import net.minecraftforge.common.IPlantable;

import java.util.Random;

public class BlockTFUberousSoil extends Block implements IGrowable {
	private static final VoxelShape AABB = VoxelShapes.create(new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F));

	protected BlockTFUberousSoil(Properties props) {
		super(props);
	}

	@Override
	@Deprecated
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
		return AABB;
	}

	@Override
	public boolean canSustainPlant(BlockState state, IBlockReader world, BlockPos pos, Direction direction, IPlantable plantable) {
		if (direction != Direction.UP)
			return false;
		PlantType plantType = plantable.getPlantType(world, pos.offset(direction));
		return plantType == PlantType.CROP || plantType == PlantType.PLAINS || plantType == PlantType.CAVE;
	}

	@Override
	public void neighborChanged(BlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		BlockState above = world.getBlockState(pos.up());
		Material aboveMaterial = above.getMaterial();

		if (aboveMaterial.isSolid()) {
			world.setBlockState(pos, Blocks.DIRT.getDefaultState());
		}

		// todo should probably use IGrowable and loop until it can't grow anymore
		if (above.getBlock() instanceof IPlantable) {
			IPlantable plant = (IPlantable) above.getBlock();
			// revert to farmland or grass
			if (plant.getPlantType(world, pos.up()) == PlantType.CROP) {
				world.setBlockState(pos, Blocks.FARMLAND.getDefaultState().with(FarmlandBlock.MOISTURE, 2));
			} else if (plant.getPlantType(world, pos.up()) == PlantType.PLAINS) {
				world.setBlockState(pos, Blocks.GRASS_BLOCK.getDefaultState());
			} else {
				world.setBlockState(pos, Blocks.DIRT.getDefaultState());
			}
			// apply bonemeal
			BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), world, pos.up());
			BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), world, pos.up());
			BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), world, pos.up());
			BoneMealItem.applyBonemeal(new ItemStack(Items.BONE_MEAL), world, pos.up());
			// green sparkles
			world.playEvent(2005, pos.up(), 0);
		}
	}

	@Override
	public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random rand, BlockPos pos, BlockState state) {
		pos = pos.offset(Direction.Plane.HORIZONTAL.random(rand));

		Block blockAt = world.getBlockState(pos).getBlock();
		if (world.isAirBlock(pos.up()) && (blockAt == Blocks.DIRT || blockAt == Blocks.GRASS_BLOCK || blockAt == Blocks.FARMLAND)) {
			world.setBlockState(pos, this.getDefaultState());
		}
	}
}
