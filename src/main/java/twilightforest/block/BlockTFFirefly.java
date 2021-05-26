package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import twilightforest.tileentity.TFTileEntities;

import javax.annotation.Nullable;

public class BlockTFFirefly extends BlockTFCritter {

	protected BlockTFFirefly(Block.Properties props) {
		super(props);
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return TFTileEntities.FIREFLY.get().create();
	}

	@Override
	public ItemStack getSquishResult() {
		return new ItemStack(Items.GLOWSTONE_DUST);
	}
}
