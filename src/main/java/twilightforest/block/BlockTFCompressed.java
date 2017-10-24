package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.block.enums.CompressedVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;

public class BlockTFCompressed extends Block implements ModelRegisterCallback {
    public static final PropertyEnum<CompressedVariant> VARIANT = PropertyEnum.create("variant", CompressedVariant.class);

    public BlockTFCompressed() {
        super(Material.IRON, MapColor.IRON);
        this.setHardness(5.0F);
        this.setResistance(10.0F);
        this.setSoundType(SoundType.METAL);
        this.setDefaultState(this.blockState.getBaseState().withProperty(VARIANT, CompressedVariant.IRONWOOD));
		this.setCreativeTab(TFItems.creativeTab);
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, VARIANT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, CompressedVariant.values()[meta]);
    }

    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
        for (CompressedVariant variation : CompressedVariant.values() ) {
            list.add(new ItemStack(this, 1, variation.ordinal()));
        }
    }

	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
		switch (state.getValue(VARIANT)) {
			default:
			case FIERY:
				return SoundType.METAL;
			case IRONWOOD:
				return SoundType.WOOD;
			case STEELLEAF:
				return SoundType.PLANT;
			case ARCTIC_FUR:
				return SoundType.CLOTH;
			case CARMINITE:
				return SoundType.SLIME;
		}
	}

	@Override
	public Material getMaterial(IBlockState state) {
		switch (state.getValue(VARIANT)) {
			default:
			case FIERY:
				return super.getMaterial(state);
			case IRONWOOD:
				return Material.WOOD;
			case STEELLEAF:
				return Material.LEAVES;
			case ARCTIC_FUR:
				return Material.CLOTH;
			case CARMINITE:
				return Material.CLAY;
		}
	}

	@Override
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
		switch (blockState.getValue(VARIANT)) {
			default:
			case FIERY:
			case IRONWOOD:
			case STEELLEAF:
				super.getBlockHardness(blockState, worldIn, pos);
			case ARCTIC_FUR:
				return 0.8F;
			case CARMINITE:
				return 0.0F;
		}
	}

	@Override
	public boolean isBeaconBase(IBlockAccess worldObj, BlockPos pos, BlockPos beacon) {
		return true;
	}

	@Override
    public int damageDropped(IBlockState state) {
        return getMetaFromState(state);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModel() {
        ModelUtils.registerToStateSingleVariant(this, VARIANT);
    }
}
