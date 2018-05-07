package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

import java.util.Random;

public class BlockTFUberousSoil extends Block implements IGrowable, ModelRegisterCallback {
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.0F, 0.0F, 0.0F, 1.0F, 0.9375F, 1.0F);

	protected BlockTFUberousSoil() {
		super(Material.GROUND);
		this.setLightOpacity(255);
		this.setHardness(0.6F);
		this.setSoundType(SoundType.GROUND);
		this.setTickRandomly(true);

		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	@Deprecated
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	@Deprecated
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	@Deprecated
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return face == EnumFacing.DOWN ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(Blocks.DIRT);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		Material aboveMaterial = world.getBlockState(pos.up()).getMaterial();
		if (aboveMaterial.isSolid()) {
			world.setBlockState(pos, Blocks.DIRT.getDefaultState());
		}
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		if (direction != EnumFacing.UP)
			return false;
		EnumPlantType plantType = plantable.getPlantType(world, pos.offset(direction));
		return plantType == EnumPlantType.Crop || plantType == EnumPlantType.Plains || plantType == EnumPlantType.Cave;
	}

	@Override
	@Deprecated
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block neighbor, BlockPos fromPos) {
		IBlockState above = world.getBlockState(pos.up());
		Material aboveMaterial = above.getMaterial();

		if (aboveMaterial.isSolid()) {
			world.setBlockState(pos, Blocks.DIRT.getDefaultState());
		}

		// todo should probably use IGrowable and loop until it can't grow anymore
		if (above.getBlock() instanceof IPlantable) {
			IPlantable plant = (IPlantable) above.getBlock();
			// revert to farmland or grass
			if (plant.getPlantType(world, pos.up()) == EnumPlantType.Crop) {
				world.setBlockState(pos, Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 2));
			} else if (plant.getPlantType(world, pos.up()) == EnumPlantType.Plains) {
				world.setBlockState(pos, Blocks.GRASS.getDefaultState());
			} else {
				world.setBlockState(pos, Blocks.DIRT.getDefaultState());
			}
			// apply bonemeal
			ItemDye.applyBonemeal(new ItemStack(Items.DYE), world, pos.up());
			ItemDye.applyBonemeal(new ItemStack(Items.DYE), world, pos.up());
			ItemDye.applyBonemeal(new ItemStack(Items.DYE), world, pos.up());
			ItemDye.applyBonemeal(new ItemStack(Items.DYE), world, pos.up());
			// green sparkles
			world.playEvent(2005, pos.up(), 0);
		}
	}

	@Override
	public boolean canGrow(World world, BlockPos pos, IBlockState state, boolean var5) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World world, Random rand, BlockPos pos, IBlockState state) {
		return true;
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		pos = pos.offset(EnumFacing.HORIZONTALS[rand.nextInt(EnumFacing.HORIZONTALS.length)]);

		Block blockAt = world.getBlockState(pos).getBlock();
		if (world.isAirBlock(pos.up()) && (blockAt == Blocks.DIRT || blockAt == Blocks.GRASS || blockAt == Blocks.FARMLAND)) {
			world.setBlockState(pos, this.getDefaultState());
		}
	}
}
