package twilightforest.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.block.BlockTFPlant;
import twilightforest.enums.PlantVariant;

public class ItemBlockTFPlant extends ItemBlockTFMeta {

	public ItemBlockTFPlant(Block block) {
		super(block);
	}

	@Override
	public boolean canPlaceBlockOnSide(World world, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack) {
		int meta = stack.getItemDamage();
		if (meta == PlantVariant.ROOT_STRAND.ordinal() || meta == PlantVariant.TORCHBERRY.ordinal()) {
			// [VanillaCopy] super's side offsetting logic
			Block block = world.getBlockState(pos).getBlock();

			if (block == Blocks.SNOW_LAYER && block.isReplaceable(world, pos)) {
				side = EnumFacing.UP;
			} else if (!block.isReplaceable(world, pos)) {
				pos = pos.offset(side);
			}

			return BlockTFPlant.canPlaceRootAt(world, pos);
		} else {
			return super.canPlaceBlockOnSide(world, pos, side, player, stack);
		}
	}
}
