package twilightforest.block;

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

import java.util.Random;

public class BlockTFTrollSteinn extends Block implements ModelRegisterCallback {

	static final PropertyBool DOWN_LIT = PropertyBool.create("down");
	static final PropertyBool UP_LIT = PropertyBool.create("up");
	static final PropertyBool NORTH_LIT = PropertyBool.create("north");
	static final PropertyBool SOUTH_LIT = PropertyBool.create("south");
	static final PropertyBool WEST_LIT = PropertyBool.create("west");
	static final PropertyBool EAST_LIT = PropertyBool.create("east");

	private static final int LIGHT_THRESHHOLD = 7;

	BlockTFTrollSteinn() {
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
		if (!(world instanceof World)) return this.getDefaultState();

		for (SideProps side : SideProps.values())
			state = state.withProperty(side.prop, ((World) world).getLight(pos.offset(side.facing)) > LIGHT_THRESHHOLD);

		return state;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (rand.nextInt(2) == 0) this.sparkle(world, pos);
	}

	// [VanillaCopy] Based on BlockRedstoneOre.spawnParticles
	private void sparkle(World world, BlockPos pos) {
		Random random = world.rand;
		int threshhold = LIGHT_THRESHHOLD;

		for (EnumFacing side : EnumFacing.VALUES) {
			double rx = (double) ((float) pos.getX() + random.nextFloat());
			double ry = (double) ((float) pos.getY() + random.nextFloat());
			double rz = (double) ((float) pos.getZ() + random.nextFloat());

			if (side == EnumFacing.DOWN && !world.getBlockState(pos.down()).isOpaqueCube() && world.getLight(pos.down()) <= threshhold) {
				ry = (double)pos.getY() - 0.0625D;
			}

			if (side == EnumFacing.UP && !world.getBlockState(pos.up()).isOpaqueCube() && world.getLight(pos.up()) <= threshhold) {
				ry = (double)pos.getY() + 0.0625D + 1.0D;
			}

			if (side == EnumFacing.NORTH && !world.getBlockState(pos.north()).isOpaqueCube() && world.getLight(pos.north()) <= threshhold) {
				rz = (double)pos.getZ() - 0.0625D;
			}

			if (side == EnumFacing.SOUTH && !world.getBlockState(pos.south()).isOpaqueCube() && world.getLight(pos.south()) <= threshhold) {
				rz = (double)pos.getZ() + 0.0625D + 1.0D;
			}

			if (side == EnumFacing.WEST && !world.getBlockState(pos.west()).isOpaqueCube() && world.getLight(pos.west()) <= threshhold) {
				rx = (double)pos.getX() - 0.0625D;
			}

			if (side == EnumFacing.EAST && !world.getBlockState(pos.east()).isOpaqueCube() && world.getLight(pos.east()) <= threshhold) {
				rx = (double)pos.getX() + 0.0625D + 1.0D;
			}

			if (rx < (double) pos.getX() || rx > (double) (pos.getX() + 1) || ry < 0.0D || ry > (double) (pos.getY() + 1) || rz < (double) pos.getZ() || rz > (double) (pos.getZ() + 1)) {
				world.spawnParticle(EnumParticleTypes.REDSTONE, rx, ry, rz, 0.25D, -1.0D, 0.5D);
			}
		}
	}

	private enum SideProps {
		UP(UP_LIT, EnumFacing.UP),
		DOWN(DOWN_LIT, EnumFacing.DOWN),
		NORTH(NORTH_LIT, EnumFacing.NORTH),
		SOUTH(SOUTH_LIT, EnumFacing.SOUTH),
		WEST(WEST_LIT, EnumFacing.WEST),
		EAST(EAST_LIT, EnumFacing.EAST);

		private final PropertyBool prop;
		private final EnumFacing facing;

		SideProps(PropertyBool prop, EnumFacing faceing) {
			this.prop = prop;
			this.facing = faceing;
		}
	}
}
