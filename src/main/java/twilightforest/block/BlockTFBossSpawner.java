package twilightforest.block;

import java.util.Random;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import twilightforest.block.enums.BossVariant;
import twilightforest.item.TFItems;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockTFBossSpawner extends Block {
	public static final PropertyEnum<BossVariant> VARIANT = PropertyEnum.create("boss", BossVariant.class, input -> input != BossVariant.NONE);

	protected BlockTFBossSpawner() {
		super(Material.ROCK);
		this.setHardness(20F);
		//this.setResistance(10F);
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
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(VARIANT, BossVariant.values()[meta]);
	}

	@Override
	public boolean hasTileEntity(IBlockState state)
	{
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
	public Item getItemDropped(IBlockState state, Random par2Random, int fortune)
	{
		return null;
	}

	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}
}
