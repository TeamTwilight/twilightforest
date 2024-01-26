package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.TomeSpawnerBlockEntity;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFSounds;

public class TomeSpawnerBlock extends BaseEntityBlock {

	public static final MapCodec<TomeSpawnerBlock> CODEC = simpleCodec(TomeSpawnerBlock::new);
	public static final int MAX_STAGES = 10;
	public static final IntegerProperty BOOK_STAGES = IntegerProperty.create("book_stages", 1, MAX_STAGES);
	public static final BooleanProperty SPAWNER = BooleanProperty.create("spawner");

	public TomeSpawnerBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(BOOK_STAGES, 10).setValue(SPAWNER, true));
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(BOOK_STAGES, SPAWNER);
	}

	@Override
	public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
		if(level.getDifficulty() != Difficulty.PEACEFUL && level.getBlockState(pos).getValue(SPAWNER) && level.getBlockEntity(pos) instanceof TomeSpawnerBlockEntity ts && level instanceof ServerLevel serverLevel) {
			for(int i = 0; i < state.getValue(BOOK_STAGES); i++) {
				ts.attemptSpawnTome(serverLevel, pos, true, igniter);

			}
			level.destroyBlock(pos, false);
		}
		super.onCaughtFire(state, level, pos, face, igniter);
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
		for(Direction direction : Direction.values()) {
			if(level.getBlockState(pos.relative(direction)).is(BlockTags.FIRE)) {
				this.onCaughtFire(state, level, pos, direction, null);
				break;
			}
		}
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
		if(!level.isClientSide && state.getValue(SPAWNER)) {
			level.playSound(null, pos, TFSounds.DEATH_TOME_DEATH.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			for (int i = 0; i < 20; ++i) {
				double d3 = level.random.nextGaussian() * 0.02D;
				double d1 = level.random.nextGaussian() * 0.02D;
				double d2 = level.random.nextGaussian() * 0.02D;
				((ServerLevel) level).sendParticles(ParticleTypes.POOF, (double) pos.getX() + 0.5D, pos.getY(), (double) pos.getZ() + 0.5D, 1, d3, d1, d2, 0.15F);
			}
		}
		super.playerDestroy(level, player, pos, state, entity, stack);
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public float getEnchantPowerBonus(BlockState state, LevelReader reader, BlockPos pos) {
		return state.getValue(BOOK_STAGES) * 0.1F;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return state.getValue(SPAWNER) ? new TomeSpawnerBlockEntity(pos, state) : null;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return state.getValue(SPAWNER) ? createTickerHelper(type, TFBlockEntities.TOME_SPAWNER.get(), TomeSpawnerBlockEntity::tick) : null;
	}

	@Override
	public int getFlammability(BlockState state, BlockGetter getter, BlockPos pos, Direction face) {
		return 20;
	}

	@Override
	public int getFireSpreadSpeed(BlockState state, BlockGetter getter, BlockPos pos, Direction face) {
		return 30;
	}
}
