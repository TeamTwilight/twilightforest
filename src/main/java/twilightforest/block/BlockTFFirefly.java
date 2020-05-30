package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import twilightforest.tileentity.critters.TileEntityTFFirefly;

import javax.annotation.Nullable;

public class BlockTFFirefly extends BlockTFCritter {

	protected BlockTFFirefly(Block.Properties props) {
		super(props);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		//TODO: Return differently per side?
		return new TileEntityTFFirefly();
	}

	@Override
	public ItemStack getSquishResult() {
		return new ItemStack(Items.GLOWSTONE_DUST);
	}

	//Atomic: Forge would like to get rid of registerTESRItemStack, but there's no alternative yet (as at 1.11)
	//TODO 1.14: Something may have changed, look into this when we compile
//	@SuppressWarnings("deprecation")
//	@OnlyIn(Dist.CLIENT)
//	@Override
//	public void registerModel() {
//		ModelLoader.setCustomStateMapper(this, new StateMap.Builder().ignore(BlockDirectional.FACING).build());
//		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
//		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(this), 0, TileEntityTFFireflyTicking.class);
//	}
}
