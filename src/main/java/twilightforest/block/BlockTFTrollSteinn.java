package twilightforest.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

import java.util.Map;
import java.util.Random;

public class BlockTFTrollSteinn extends Block implements ModelRegisterCallback {

	public static final PropertyBool DOWN_LIT = PropertyBool.create("down");
	public static final PropertyBool UP_LIT = PropertyBool.create("up");
	public static final PropertyBool NORTH_LIT = PropertyBool.create("north");
	public static final PropertyBool SOUTH_LIT = PropertyBool.create("south");
	public static final PropertyBool WEST_LIT = PropertyBool.create("west");
	public static final PropertyBool EAST_LIT = PropertyBool.create("east");

	private static final Map<EnumFacing, PropertyBool> PROPS = ImmutableMap.<EnumFacing, PropertyBool>builder()
			.put(EnumFacing.DOWN, DOWN_LIT).put(EnumFacing.UP, UP_LIT)
			.put(EnumFacing.NORTH, NORTH_LIT).put(EnumFacing.SOUTH, SOUTH_LIT)
			.put(EnumFacing.WEST, WEST_LIT).put(EnumFacing.EAST, EAST_LIT).build();
	private static final int LIGHT_THRESHHOLD = 7;

	protected BlockTFTrollSteinn() {
		super(Material.ROCK);

		this.setHardness(2F);
		this.setResistance(15F);
		this.setSoundType(SoundType.STONE);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(blockState.getBaseState()
				.withProperty(DOWN_LIT, false).withProperty(UP_LIT, false)
				.withProperty(NORTH_LIT, false).withProperty(SOUTH_LIT, false)
				.withProperty(WEST_LIT, false).withProperty(EAST_LIT, false));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, DOWN_LIT, UP_LIT, NORTH_LIT, SOUTH_LIT, WEST_LIT, EAST_LIT);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	@Deprecated
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		for (EnumFacing e : EnumFacing.VALUES) {
			state = state.withProperty(PROPS.get(e), isBlockLit(world, pos.offset(e)));
		}

		return state;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextInt(2) == 0) {
			this.sparkle(world, pos, rand);
		}
	}

	private void sparkle(World world, BlockPos pos, Random rand) {
		Random random = rand;
		double pixel = 0.0625D;
		int threshhold = LIGHT_THRESHHOLD;

		for (EnumFacing side : EnumFacing.VALUES) {
			double rx = (double) ((float) pos.getX() + random.nextFloat());
			double ry = (double) ((float) pos.getY() + random.nextFloat());
			double rz = (double) ((float) pos.getZ() + random.nextFloat());

			if (side == EnumFacing.DOWN && !world.getBlockState(pos.down()).isOpaqueCube() && world.getLight(pos.down()) <= threshhold) {
				ry = (double) (pos.getY() + 0) - pixel;
			}

			if (side == EnumFacing.UP && !world.getBlockState(pos.up()).isOpaqueCube() && world.getLight(pos.up()) <= threshhold) {
				ry = (double) (pos.getY() + 1) + pixel;
			}

			if (side == EnumFacing.NORTH && !world.getBlockState(pos.north()).isOpaqueCube() && world.getLight(pos.north()) <= threshhold) {
				rz = (double) (pos.getZ() + 1) + pixel;
			}
			// todo 1.9 discrepancy in original code in these two cases, recheck
			if (side == EnumFacing.SOUTH && !world.getBlockState(pos.south()).isOpaqueCube() && world.getLight(pos.south()) <= threshhold) {
				rz = (double) (pos.getZ() + 0) - pixel;
			}

			if (side == EnumFacing.WEST && !world.getBlockState(pos.west()).isOpaqueCube() && world.getLight(pos.west()) <= threshhold) {
				rx = (double) (pos.getX() + 1) + pixel;
			}
			// todo 1.9 discrepancy in original code in these two cases, recheck
			if (side == EnumFacing.EAST && !world.getBlockState(pos.east()).isOpaqueCube() && world.getLight(pos.east()) <= threshhold) {
				rx = (double) (pos.getX() + 0) - pixel;
			}

			if (rx < (double) pos.getX() || rx > (double) (pos.getX() + 1) || ry < 0.0D || ry > (double) (pos.getY() + 1) || rz < (double) pos.getZ() || rz > (double) (pos.getZ() + 1)) {
				world.spawnParticle(EnumParticleTypes.REDSTONE, rx, ry, rz, 0.25D, -1.0D, 0.5D);
			}
		}
	}

	private boolean isBlockLit(IBlockAccess world, BlockPos pos) {
		int threshhold = LIGHT_THRESHHOLD << 4;

		if (world.getBlockState(pos).isOpaqueCube()) {
			return false;
		} else {
			int light = world.getCombinedLight(pos, 0);
			int sky = light % 65536;
			int block = light / 65536;

			return sky > threshhold || block > threshhold;
		}
	}
}
