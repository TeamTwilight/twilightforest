package twilightforest.block;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockTFDirectionalRotatedPillar extends BlockRotatedPillar {

	public static final IProperty<Boolean> REVERSED = PropertyBool.create("reversed");

	public BlockTFDirectionalRotatedPillar(Material materialIn) {
		super(materialIn);
	}

	public BlockTFDirectionalRotatedPillar(Material materialIn, MapColor color) {
		super(materialIn, color);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = super.getStateFromMeta(meta);
		return state.withProperty(REVERSED, (meta & 1) != 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta = super.getMetaFromState(state);
		return meta | (state.getValue(REVERSED) ? 1 : 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, AXIS, REVERSED);
	}

	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return super.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
		            .withProperty(REVERSED, facing.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE);
	}

	@Override
	public IBlockState withMirror(IBlockState state, Mirror mirror) {
		if (mirror != Mirror.NONE) {
			EnumFacing.Axis axis = state.getValue(AXIS);
			if (axis == EnumFacing.Axis.Y
					|| mirror == Mirror.LEFT_RIGHT && axis == EnumFacing.Axis.Z
					|| mirror == Mirror.FRONT_BACK && axis == EnumFacing.Axis.X) {

				return state.cycleProperty(REVERSED);
			}
		}
		return super.withMirror(state, mirror);
	}
}
