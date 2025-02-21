package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.stream.IntStream;

public class NaturaBushBlock extends TFBushBlock implements BonemealableBlock {
	public NaturaBushBlock(DeferredItem<Item> harvestItem) {
		super(harvestItem, BlockBehaviour.Properties.of().sound(SoundType.GRASS));
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		super.randomTick(state, level, pos, random);

		int height = (int) IntStream.iterate(1, n -> level.getBlockState(pos.below(n)).getBlock() == this, n -> n + 1).count();
		if (random.nextInt(20) == 0 && height < 2)
			tryGrowUpwards(state, level, pos, random);
	}

	protected void tryGrowUpwards(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (random.nextInt(3) == 0 && canGrowAt(level, pos) && state.getValue(AGE) >= 2 && level.getBlockState(pos.above()).isAir())
			level.setBlock(pos.above(), state.setValue(AGE, 0), Block.UPDATE_CLIENTS);
	}

	@Override
	protected int getNumberOfBerries(RandomSource random) {
		return 1;
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return true;
	}

	@Override
	protected boolean canGrowAt(ServerLevel level, BlockPos pos) {
		return level.getRawBrightness(pos, 0) >= 8;
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return state.getValue(AGE) < MAX_AGE;
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		int age = state.getValue(AGE);
		if (age < MAX_AGE)
			grow(state, level, pos, random);
		tryGrowUpwards(state, level, pos, random);
	}
}
