package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFPlant;
import twilightforest.block.enums.PlantVariant;

public class ItemBlockTFPlant extends ItemBlock {

	public ItemBlockTFPlant(Block block) {
		super(block);
		setHasSubtypes(true);
		setMaxDamage(0);
	}

	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		return String.format("%s.%d", super.getUnlocalizedName(itemstack), itemstack.getItemDamage());
	}

	@Override
	public int getMetadata(int i) {
		return i;
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
		int meta = stack.getItemDamage();

		if ((meta == PlantVariant.ROOT_STRAND.ordinal() || meta == PlantVariant.TORCHBERRY.ordinal())
				&& side == EnumFacing.DOWN && BlockTFPlant.canPlaceRootBelow(world, pos)) {
			return true;
		} else {
			return super.canPlaceBlockOnSide(world, pos, side, player, stack);
		}
	}
}
