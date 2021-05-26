package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.IBlockReader;
import twilightforest.tileentity.TFTileEntities;

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
		return TFTileEntities.MOONWORM.get().create();
	}

	@Override
	public ItemStack getSquishResult() {
		return new ItemStack(Items.LIME_DYE, 1);
	}
}
