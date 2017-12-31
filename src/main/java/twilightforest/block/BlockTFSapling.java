package twilightforest.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenerator;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.enums.SaplingVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;
import twilightforest.world.TFGenCanopyTree;
import twilightforest.world.TFGenDarkCanopyTree;
import twilightforest.world.TFGenHollowTree;
import twilightforest.world.TFGenLargeRainboak;
import twilightforest.world.TFGenMangroveTree;
import twilightforest.world.TFGenMinersTree;
import twilightforest.world.TFGenSmallRainboak;
import twilightforest.world.TFGenSmallTwilightOak;
import twilightforest.world.TFGenSortingTree;
import twilightforest.world.TFGenTreeOfTime;
import twilightforest.world.TFGenTreeOfTransformation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockTFSapling extends BlockBush implements IGrowable, ModelRegisterCallback {
	public static final PropertyEnum<SaplingVariant> TF_TYPE = PropertyEnum.create("tf_type", SaplingVariant.class);
	protected static final AxisAlignedBB SAPLING_AABB = new AxisAlignedBB(0.09999999403953552D, 0.0D, 0.09999999403953552D, 0.8999999761581421D, 0.800000011920929D, 0.8999999761581421D);

	protected BlockTFSapling() {
		this.setCreativeTab(TFItems.creativeTab);
		this.setSoundType(SoundType.PLANT);
		this.setDefaultState(blockState.getBaseState()
				.withProperty(TF_TYPE, SaplingVariant.OAK));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TF_TYPE);
	}

	@Override
	public void grow(World world, Random rand, BlockPos pos, IBlockState state) {
		WorldGenerator treeGenerator;

		switch (state.getValue(TF_TYPE)) {
			case CANOPY:
				treeGenerator = new TFGenCanopyTree(true);
				break;
			case MANGROVE:
				treeGenerator = new TFGenMangroveTree(true);
				break;
			case DARKWOOD:
				treeGenerator = new TFGenDarkCanopyTree(true);
				break;
			case HOLLOW_OAK:
				treeGenerator = new TFGenHollowTree(true);
				break;
			case TIME:
				treeGenerator = new TFGenTreeOfTime(true);
				break;
			case TRANSFORMATION:
				treeGenerator = new TFGenTreeOfTransformation(true);
				break;
			case MINING:
				treeGenerator = new TFGenMinersTree(true);
				break;
			case SORTING:
				treeGenerator = new TFGenSortingTree(true);
				break;
			case RAINBOW:
				treeGenerator = rand.nextInt(7) == 0 ? new TFGenLargeRainboak(true) : new TFGenSmallRainboak(true);
				break;
			case OAK:
			default:
				treeGenerator = new TFGenSmallTwilightOak(true);
				break;
		}

		world.setBlockState(pos, Blocks.AIR.getDefaultState(), 4);

		if (!treeGenerator.generate(world, rand, pos)) {
			world.setBlockState(pos, state, 4);
		}
	}

	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}

	@Override
	public void getSubBlocks(CreativeTabs par2CreativeTabs, NonNullList<ItemStack> par3List) {
		par3List.add(new ItemStack(this, 1, 0));
		par3List.add(new ItemStack(this, 1, 1));
		par3List.add(new ItemStack(this, 1, 2));
		par3List.add(new ItemStack(this, 1, 3));
		par3List.add(new ItemStack(this, 1, 4));
		par3List.add(new ItemStack(this, 1, 5));
		par3List.add(new ItemStack(this, 1, 6));
		par3List.add(new ItemStack(this, 1, 7));
		par3List.add(new ItemStack(this, 1, 8));
		par3List.add(new ItemStack(this, 1, 9));
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return SAPLING_AABB;
	}

	@Override
	public String getLocalizedName() {
		return I18n.format(this.getUnlocalizedName() + "." + BlockPlanks.EnumType.OAK.getUnlocalizedName() + ".name");
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		if (!worldIn.isRemote) {
			super.updateTick(worldIn, pos, state, rand);

			if (worldIn.getLightFromNeighbors(pos.up()) >= 9 && rand.nextInt(7) == 0) {
				this.grow(worldIn, rand, pos, state);
			}
		}
	}

	@Override
	public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state) {
		return (double) worldIn.rand.nextFloat() < 0.45D;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TF_TYPE).ordinal();
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TF_TYPE, SaplingVariant.values()[meta % SaplingVariant.values().length]);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		for (int i = 0; i < SaplingVariant.values().length; i++) {
			String variant = "inventory_" + SaplingVariant.values()[i].getName();
			ModelResourceLocation mrl = new ModelResourceLocation(getRegistryName(), variant);
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), i, mrl);
		}
	}
}
