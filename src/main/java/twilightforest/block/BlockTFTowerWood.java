package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.enums.TowerWoodVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.entity.EntityTFTowerTermite;
import twilightforest.item.TFItems;

import java.util.Random;

/**
 * Tower wood is a type of plank block that forms the walls of Dark Towers
 *
 * @author Ben
 */
public class BlockTFTowerWood extends Block implements ModelRegisterCallback {

	public static final IProperty<TowerWoodVariant> VARIANT = PropertyEnum.create("variant", TowerWoodVariant.class);

	public BlockTFTowerWood() {
		super(Material.WOOD, MapColor.ADOBE);
		this.setHardness(40F);
		this.setResistance(10F);
		this.setSoundType(SoundType.WOOD);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(blockState.getBaseState().withProperty(VARIANT, TowerWoodVariant.PLAIN));
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
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, TowerWoodVariant.values()[meta]);
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(VARIANT) == TowerWoodVariant.ENCASED ? MapColor.SAND : super.getMapColor(state, world, pos);
	}

	@Override
	public void getSubBlocks(CreativeTabs creativeTab, NonNullList<ItemStack> list) {
		for (TowerWoodVariant variant : TowerWoodVariant.values()) {
			list.add(new ItemStack(this, 1, variant.ordinal()));
		}
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		if (state.getValue(VARIANT) == TowerWoodVariant.INFESTED) {
			return 0;
		} else {
			return super.quantityDropped(state, fortune, random);
		}
	}

	@Override
	@Deprecated
	public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
		if (state.getValue(VARIANT) == TowerWoodVariant.INFESTED) {
			return 0.75F;
		} else {
			return super.getBlockHardness(state, world, pos);
		}
	}

	@Override
	public void dropBlockAsItemWithChance(World world, BlockPos pos, IBlockState state, float chance, int fortune) {
		if (!world.isRemote && state.getValue(VARIANT) == TowerWoodVariant.INFESTED) {
			EntityTFTowerTermite termite = new EntityTFTowerTermite(world);
			termite.setLocationAndAngles(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5D, 0.0F, 0.0F);
			world.spawnEntity(termite);
			termite.spawnExplosionParticle();
		}

		super.dropBlockAsItemWithChance(world, pos, state, chance, fortune);
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return 1;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing side) {
		return 0;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelUtils.registerToStateSingleVariant(this, VARIANT);
	}

}
