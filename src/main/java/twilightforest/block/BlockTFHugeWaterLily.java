package twilightforest.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.item.TFItems;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class BlockTFHugeWaterLily extends BlockLilyPad implements ModelRegisterCallback {

	private static final AxisAlignedBB AABB = new AxisAlignedBB(0.1, 0.1, 0.1, 0.9, 0.9, 0.9);

	protected BlockTFHugeWaterLily() {
		this.setSoundType(SoundType.PLANT);
		this.setCreativeTab(TFItems.creativeTab);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
		return AABB;
	}

	/*@Override
	public boolean canBlockStay(World world, BlockPos pos, IBlockState state) {
		IBlockState down = world.getBlockState(pos.down());
		Block b = down.getBlock();
		IProperty<Integer> levelProp = b instanceof BlockLiquid || b instanceof BlockFluidBase
				? BlockLiquid.LEVEL
				: null;

		return down.getMaterial() == Material.WATER
				&& (levelProp == null || down.getValue(levelProp) == 0);
	}*/

	@Override
	protected boolean canSustainBush(IBlockState state) {
		return state.getBlock() == Blocks.WATER;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
	}
}
