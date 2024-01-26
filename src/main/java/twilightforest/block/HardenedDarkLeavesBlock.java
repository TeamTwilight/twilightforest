package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
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
	public int getFlammability(BlockState state, BlockGetter getter, BlockPos pos, Direction face) {
		return 1;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter getter, BlockPos pos, Direction face) {
		return 0;
	}

	@Override
	public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
		return false;
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult result, LevelReader reader, BlockPos pos, Player player) {
		return new ItemStack(TFBlocks.DARK_LEAVES.get());
	}
}
