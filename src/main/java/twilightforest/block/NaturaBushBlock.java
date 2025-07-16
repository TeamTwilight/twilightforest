package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredItem;
import twilightforest.data.tags.BlockTagGenerator;

import java.util.stream.IntStream;

public class NaturaBushBlock extends TFBushBlock implements BonemealableBlock {
	private static final VoxelShape SMALL_BUSH_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
	private static final VoxelShape MEDIUM_BUSH_SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 12.0, 14.0);
	private static final VoxelShape LARGE_BUSH_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0);

	public NaturaBushBlock(DeferredItem<Item> harvestItem, TagKey<Block> surviveBlockTag) {
		super(harvestItem, BlockBehaviour.Properties.of().sound(SoundType.GRASS), surviveBlockTag, SMALL_BUSH_SHAPE, MEDIUM_BUSH_SHAPE, LARGE_BUSH_SHAPE, 1, 1);
	}

	public NaturaBushBlock(DeferredItem<Item> harvestItem) {
		this(harvestItem, BlockTagGenerator.OVERWORLD_NATURA_BUSHES_SURVIVE);
	}

	@Override
	public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		super.randomTick(state, level, pos, random);

		int height = (int) IntStream.iterate(1, n -> level.getBlockState(pos.below(n)).getBlock() == this, n -> n + 1).count();
		if (random.nextInt(20) == 0 && height < 2 && canGrowAt(state, level, pos))  // bone meal growth doesn't care about canGrowAt
			tryGrowUpwards(state, level, pos, random);
	}

	protected void tryGrowUpwards(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (random.nextInt(3) == 0 && state.getValue(AGE) >= 2 && level.getBlockState(pos.above()).isAir())
			level.setBlock(pos.above(), state.setValue(AGE, 0), Block.UPDATE_CLIENTS);
	}

	@Override
	protected boolean canGrowAt(BlockState state, LevelReader level, BlockPos pos) {
		return level.getRawBrightness(pos, 0) >= 8 && super.canGrowAt(state, level, pos);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
		return state.getValue(AGE) < MAX_AGE - 1 || level.getBlockState(pos.above()).isAir();
	}

	@Override
	public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
		int age = state.getValue(AGE);
		if (age < MAX_AGE)
			level.setBlock(pos, state.setValue(AGE, Math.min(state.getValue(AGE) + 1 + random.nextInt(2), MAX_AGE - 1)), Block.UPDATE_CLIENTS);
		tryGrowUpwards(state, level, pos, random);
	}
}
