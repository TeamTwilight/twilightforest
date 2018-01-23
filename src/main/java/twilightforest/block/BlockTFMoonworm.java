package twilightforest.block;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import twilightforest.TwilightForestMod;
import twilightforest.client.ModelRegisterCallback;
import twilightforest.tileentity.critters.TileEntityTFMoonworm;
import twilightforest.tileentity.critters.TileEntityTFMoonwormTicking;

import java.util.Random;

public class BlockTFMoonworm extends BlockTFCritter implements ModelRegisterCallback {

	@Override
	public float getWidth() {
		return 0.25F;
	}

	@Override
	public int tickRate(World world) {
		return 50;
	}

	@Override
	@Deprecated
	public int getLightValue(IBlockState state) {
		return 14;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return TwilightForestMod.proxy.getNewMoonwormTE();
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		super.onBlockAdded(world, pos, state);
		world.scheduleUpdate(pos, this, tickRate(world));
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random random) {
		if (world.getLight(pos) < 12) {
			// do another update to check that we got it right
			world.scheduleUpdate(pos, this, tickRate(world));
		}
	}

	@Override
	protected boolean checkAndDrop(World world, BlockPos pos, IBlockState state) {
		EnumFacing facing = state.getValue(TFBlockProperties.FACING);
		if (!canPlaceAt(world, pos.offset(facing.getOpposite()))) {
			world.destroyBlock(pos, false);
			return false;
		}

		return true;
	}

	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random) {
		return 0;
	}

	//Atomic: Forge would like to get rid of registerTESRItemStack, but there's no alternative yet (as at 1.11)
	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	@Override
	public void registerModel() {
		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(TFBlockProperties.FACING).build());
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(this), 0, TileEntityTFMoonwormTicking.class);
	}
}
