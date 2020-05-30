package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.DirectionalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import twilightforest.tileentity.critters.TileEntityTFMoonworm;

import javax.annotation.Nullable;

public class BlockTFMoonworm extends BlockTFCritter {

	protected BlockTFMoonworm(Block.Properties props) {
		super(props);
	}

	@Override
	public float getWidth() {
		return 0.25F;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		//TODO: Get per side, if possible
		return new TileEntityTFMoonworm();
	}

	@Override
	protected boolean checkAndDrop(World world, BlockPos pos, BlockState state) {
		Direction facing = state.get(DirectionalBlock.FACING);
		if (!canPlaceAt(world, pos.offset(facing.getOpposite()), facing)) {
			world.destroyBlock(pos, false);
			return false;
		}
		return true;
	}

//	@Override
//	public int quantityDropped(BlockState state, int fortune, Random random) {
//		return 0;
//	}

	@Override
	public ItemStack getSquishResult() {
		return new ItemStack(Items.LIME_DYE, 1);
	}

	//Atomic: Forge would like to get rid of registerTESRItemStack, but there's no alternative yet (as at 1.11)
	//TODO 1.14: Something may have changed, look into this when we compile
//	@SuppressWarnings("deprecation")
//	@OnlyIn(Dist.CLIENT)
//	@Override
//	public void registerModel() {
//		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(BlockDirectional.FACING).build());
//		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
//		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(this), 0, TileEntityTFMoonwormTicking.class);
//	}
}
