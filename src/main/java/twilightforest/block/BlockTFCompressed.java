package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.enums.CompressedVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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
		List<CompressedVariant> variants = new ArrayList<>(VARIANT.getAllowedValues());

		for (int i = 0; i < variants.size(); i++)
			if (i != CompressedVariant.FIERY.ordinal())
				ModelUtils.registerToState(this, i, this.getDefaultState().withProperty(VARIANT, variants.get(i)));

		ModelResourceLocation mrl = new ModelResourceLocation(this.getRegistryName(), "inventory_fiery");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), CompressedVariant.FIERY.ordinal(), mrl);
    }

    @Override
	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		if ((!entityIn.isImmuneToFire())
				&& entityIn instanceof EntityLivingBase
				&& (!EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase)entityIn))
				&& worldIn.getBlockState(pos).getValue(VARIANT) == CompressedVariant.FIERY) {
			entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
		}

		super.onEntityWalk(worldIn, pos, entityIn);
	}

	@Override
	public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
		switch(worldIn.getBlockState(pos).getValue(VARIANT)) {
			case STEELLEAF:
				entityIn.fall(fallDistance, 0.75F);
				break;
			case ARCTIC_FUR:
				entityIn.fall(fallDistance, 0.1F);
				break;
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing face) {
		return CompressedVariant.FIERY == world.getBlockState(pos).getValue(VARIANT);
	}

	@Deprecated
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		if (blockState.getValue(VARIANT) == CompressedVariant.FIERY) {
			IBlockState state = blockAccess.getBlockState(pos.offset(side));
			return state.getBlock() != this || state.getValue(VARIANT) != CompressedVariant.FIERY;
		} else return super.shouldSideBeRendered(blockState, blockAccess, pos, side);
	}
}
