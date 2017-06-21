package twilightforest.block;

import com.google.common.collect.ImmutableList;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.block.enums.LeavesVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;


@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockTFLeaves extends BlockLeaves implements ModelRegisterCallback {
	public static final PropertyEnum<LeavesVariant> VARIANT = PropertyEnum.create("variant", LeavesVariant.class);

	protected BlockTFLeaves() {
		this.setHardness(0.2F);
		this.setLightOpacity(2);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(blockState.getBaseState().withProperty(CHECK_DECAY, true).withProperty(DECAYABLE, true).withProperty(VARIANT, LeavesVariant.OAK));
	}

	@Override
	public BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CHECK_DECAY, DECAYABLE, VARIANT);
	}

	@Override
	@Deprecated
	public IBlockState getStateFromMeta(int meta) {
		LeavesVariant variant = LeavesVariant.values()[meta & 0b11];
		return this.getDefaultState().withProperty(VARIANT, variant).withProperty(DECAYABLE, (meta & 4) == 0).withProperty(CHECK_DECAY, (meta & 8) > 0);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(VARIANT).ordinal();

		if (!state.getValue(DECAYABLE)) {
			i |= 4;
		}

		if (state.getValue(CHECK_DECAY)) {
			i |= 8;
		}

		return i;
	}

	@Override
	public BlockPlanks.EnumType getWoodType(int meta) {
		return BlockPlanks.EnumType.OAK;
	}

	@Override
	public void getSubBlocks(Item par1, CreativeTabs par2CreativeTabs, NonNullList<ItemStack> par3List) {
		par3List.add(new ItemStack(par1, 1, 0));
		par3List.add(new ItemStack(par1, 1, 1));
		par3List.add(new ItemStack(this, 1, 2));
		par3List.add(new ItemStack(this, 1, 3));

	}

	@Override
	public int quantityDropped(Random par1Random) {
		return par1Random.nextInt(40) == 0 ? 1 : 0;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random par2Random, int par3) {
		return Item.getItemFromBlock(TFBlocks.sapling);
	}

	@Override
	public int damageDropped(IBlockState state) {
		LeavesVariant leafType = state.getValue(VARIANT);
		return leafType == LeavesVariant.RAINBOAK ? 9 : leafType.ordinal();
	}

	@Override
	public void dropBlockAsItemWithChance(World par1World, BlockPos pos, IBlockState state, float par6, int par7) {
		if (!par1World.isRemote) {
			byte chance = 40;

			if (state.getValue(BlockTFLeaves.VARIANT) == LeavesVariant.MANGROVE) {
				chance = 20;
			}

			if (par1World.rand.nextInt(chance) == 0) {
				Item item = this.getItemDropped(state, par1World.rand, par7);
				spawnAsEntity(par1World, pos, new ItemStack(item, 1, damageDropped(state)));
			}
		}
	}

	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
		return ImmutableList.of(); // todo 1.9
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(CHECK_DECAY).ignore(DECAYABLE).build());
		ModelUtils.registerToStateSingleVariant(this, VARIANT);
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 60;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return 30;
	}
}
