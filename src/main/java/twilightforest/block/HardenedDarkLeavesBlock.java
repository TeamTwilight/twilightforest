package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.HitResult;
import twilightforest.init.TFBlocks;

public class HardenedDarkLeavesBlock extends Block {

	public HardenedDarkLeavesBlock(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
		return new ItemStack(TFBlocks.DARK_LEAVES.get());
	}
}
