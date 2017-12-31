package twilightforest.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.enums.BossVariant;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.client.ModelUtils;
import twilightforest.item.TFItems;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockTFBossSpawner extends Block implements ModelRegisterCallback {
	public static final PropertyEnum<BossVariant> VARIANT = PropertyEnum.create("boss", BossVariant.class);

	protected BlockTFBossSpawner() {
		super(Material.ROCK);
		this.setHardness(20F);
		this.setCreativeTab(TFItems.creativeTab);
		this.setDefaultState(blockState.getBaseState().withProperty(VARIANT, BossVariant.NAGA));
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
		return getDefaultState().withProperty(VARIANT, BossVariant.values()[meta]);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	@Nonnull
	public TileEntity createTileEntity(World world, IBlockState state) {
		try {
			return state.getValue(VARIANT).getSpawnerClass().newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random par2Random, int fortune) {
		return null;
	}

	@Override
	public int quantityDropped(Random random) {
		return 0;
	}

	@Override
	@Deprecated
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(VARIANT).build());
		ModelUtils.registerToState(this, 0, getDefaultState());
	}
}
