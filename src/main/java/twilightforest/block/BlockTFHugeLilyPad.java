package twilightforest.block;

import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.block.enums.HugeLilypadPiece;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;
import java.util.List;

public class BlockTFHugeLilyPad extends BlockBush {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;
	public static final PropertyEnum<HugeLilypadPiece> PIECE = PropertyEnum.create("piece", HugeLilypadPiece.class);
	private static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0, 0, 1, 0.015625, 1);

	private boolean isSelfDestructing = false;

	protected BlockTFHugeLilyPad() {
		super(Material.PLANTS);
		this.setSoundType(SoundType.PLANT);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(PIECE, HugeLilypadPiece.NW));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING, PIECE);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return (state.getValue(FACING).getHorizontalIndex() | (state.getValue(PIECE).ordinal() << 2)) & 0b1111;
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		meta = meta & 0b1111;
		return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 0b0011)).withProperty(PIECE, HugeLilypadPiece.values()[(meta & 0b1100) >> 2]);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return AABB;
	}

	@Override
	public boolean canPlaceBlockAt(World world, BlockPos pos) {
		return world.getBlockState(pos.down()).getBlock() == Blocks.WATER;
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		if (!this.isSelfDestructing && !canBlockStay(world, pos, state)) {
			this.setGiantBlockToAir(world, pos);
		}
	}

	private void setGiantBlockToAir(World world, BlockPos pos) {
		// this flag is maybe not totally perfect
		this.isSelfDestructing = true;

		int bx = pos.getX() & ~0b1;
		int bz = pos.getZ() & ~0b1;

		// this is the best loop over 3 items that I've ever programmed!
		for (int dx = 0; dx < 2; dx++) {
			for (int dz = 0; dz < 2; dz++) {
				if (!(pos.getX() == bx + dx && pos.getZ() == bz + dz)) {
					BlockPos dPos = new BlockPos(bx + dx, pos.getY(), bz + dz);
					IBlockState state = world.getBlockState(dPos);
					if (state.getBlock() == this) {
						world.destroyBlock(dPos, false);
					}
				}
			}
		}

		this.isSelfDestructing = false;
	}

	@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		int bx = pos.getX() & ~0b1;
		int bz = pos.getZ() & ~0b1;

		for (int dx = 0; dx < 2; dx++) {
			for (int dz = 0; dz < 2; dz++) {
				BlockPos dPos = new BlockPos(bx + dx, pos.getY() - 1, bz + dz);
				//IBlockState dState = world.getBlockState(dPos);
				IBlockState dStateBelow = world.getBlockState(dPos);

				if (!(dStateBelow.getBlock() == Blocks.WATER || dStateBelow.getBlock() == Blocks.FLOWING_WATER)
						|| dStateBelow.getValue(BlockLiquid.LEVEL) != 0) {
					return false;
				}
			}
		}

		return true;
	}

	// [VanillaCopy] of super without dropping
	@Override
	protected void checkAndDropBlock(World worldIn, BlockPos pos, IBlockState state) {
		if (!this.canBlockStay(worldIn, pos, state)) {
			// this.dropBlockAsItem(worldIn, pos, state, 0); TF - nodrop
			worldIn.setBlockState(pos, Blocks.AIR.getDefaultState(), 3);
		}
	}

	@Override
	@Deprecated
	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.BLOCK;
	}

	@Override
	@Deprecated
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean p_185477_7_) {
		if (!(entityIn instanceof EntityBoat)) {
			addCollisionBoxToList(pos, entityBox, collidingBoxes, AABB);
		}
	}

	@Override
	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		super.onEntityCollidedWithBlock(worldIn, pos, state, entityIn);

		if (entityIn instanceof EntityBoat) {
			worldIn.destroyBlock(new BlockPos(pos), true);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}
}
