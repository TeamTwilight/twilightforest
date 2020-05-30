package twilightforest.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import twilightforest.item.ItemTFMazebreakerPick;

/**
 * Mazestone mimics other types of stone in appearance, but is much harder to mine
 *
 * @author Ben
 */
public class BlockTFMazestone extends Block {

	public BlockTFMazestone(Block.Properties props) {
		super(props);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		super.onBlockHarvested(world, pos, state, player);
		ItemStack stack = player.getHeldItem(Hand.MAIN_HAND);

		// damage the player's pickaxe
		if (!world.isRemote && !stack.isEmpty() && stack.getItem().isDamageable() && !(stack.getItem() instanceof ItemTFMazebreakerPick)) {
			stack.damageItem(16, player, (user) -> user.sendBreakAnimation(Hand.MAIN_HAND));
		}
	}
}
