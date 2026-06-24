package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.common.ItemAbilities;
import twilightforest.tags.TFBlockTags;

import java.util.stream.IntStream;

public class BerryBushBlock extends TFBushBlock implements BonemealableBlock {

	public BerryBushBlock(ResourceKey<LootTable> berryTable, Properties properties) {
		super(berryTable, properties);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		super.randomTick(state, level, pos, random);

		int height = (int) IntStream.iterate(1, n -> level.getBlockState(pos.below(n)).getBlock() == this, n -> n + 1).count();
		if (random.nextInt(20) == 0 && height < 2 && canGrowAt(state, level, pos))  // bone meal growth doesn't care about canGrowAt
			this.tryGrowUpwards(state, level, pos, random);
	}

	@Override
	public float getDestroyProgress(BlockState state, Player player, BlockGetter getter, BlockPos pos) {
		return player.getMainHandItem().canPerformAction(ItemAbilities.SHEARS_DIG) ? 0.2F : super.getDestroyProgress(state, player, getter, pos);
	}

	@Override
	public boolean canBePlacedAt(BlockState state) {
		return state.is(TFBlockTags.TF_BERRY_BUSHES_SURVIVE);
	}

	protected void tryGrowUpwards(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (random.nextInt(3) == 0 && state.getValue(AGE) >= 2) {
			BlockState aboveState = level.getBlockState(pos.above());
			if (aboveState.isAir()) {
				level.setBlock(pos.above(), state.getBlock().defaultBlockState(), Block.UPDATE_CLIENTS);
			} else if (aboveState.is(Blocks.SNOW)) {
				level.setBlock(pos.above(), state.getBlock().defaultBlockState().setValue(SNOW_LAYERS, aboveState.getValue(SnowLayerBlock.LAYERS)), Block.UPDATE_CLIENTS);
			}
		}
	}

	@Override
	protected boolean canGrowAt(BlockState state, LevelReader level, BlockPos pos) {
		return level.getRawBrightness(pos, 0) >= 8 && super.canGrowAt(state, level, pos);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return state.getValue(AGE) < MAX_AGE - 1 || level.getBlockState(pos.above()).isAir() || level.getBlockState(pos.above()).is(Blocks.SNOW);
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		int age = state.getValue(AGE);
		if (age < 2)
			this.grow(state, level, pos, Math.min(state.getValue(AGE) + 1 + random.nextInt(2), MAX_AGE - 1));
		this.tryGrowUpwards(state, level, pos, random);
	}
}
