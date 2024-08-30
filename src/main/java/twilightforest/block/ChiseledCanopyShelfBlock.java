package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChiseledBookShelfBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.bookshelf.ChiseledCanopyShelfBlockEntity;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFSounds;
import twilightforest.network.ParticlePacket;

public class ChiseledCanopyShelfBlock extends ChiseledBookShelfBlock {
	public static final BooleanProperty SPAWNER = BooleanProperty.create("spawner");

	public ChiseledCanopyShelfBlock(Properties properties) {
		super(properties);
		BlockState blockstate = this.getStateDefinition().any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH).setValue(SPAWNER, false);

		for (BooleanProperty booleanproperty : SLOT_OCCUPIED_PROPERTIES) {
			blockstate = blockstate.setValue(booleanproperty, false);
		}

		this.registerDefaultState(blockstate);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(SPAWNER));
	}

	@Override
	public void onCaughtFire(BlockState state, Level level, BlockPos pos, @Nullable Direction face, @Nullable LivingEntity igniter) {
		if (level.getBlockState(pos).getValue(SPAWNER) && level instanceof ServerLevel serverLevel && level.getBlockEntity(pos) instanceof ChiseledCanopyShelfBlockEntity shelf) {
			for (int i = 0; i < ChiseledCanopyShelfBlock.SLOT_OCCUPIED_PROPERTIES.size(); i++) {
				BooleanProperty property = ChiseledCanopyShelfBlock.SLOT_OCCUPIED_PROPERTIES.get(i);
				if (state.hasProperty(property) && state.getValue(property)) {
					shelf.getSpawner().attemptSpawnTome(i, serverLevel, pos, true, igniter, 5);
				}
			}
			level.destroyBlock(pos, false);
		}
		super.onCaughtFire(state, level, pos, face, igniter);
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		//always allow spawn eggs to be clicked so spawns can be set
		if (stack.getItem() instanceof SpawnEggItem) return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
		if (state.getValue(SPAWNER)) return ItemInteractionResult.FAIL;
		return super.useItemOn(stack, state, level, pos, player, hand, result);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
		if (state.getValue(SPAWNER)) return InteractionResult.FAIL;
		return super.useWithoutItem(state, level, pos, player, result);
	}

	@Override
	public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
		if (level instanceof ServerLevel serverLevel && state.getValue(SPAWNER)) {
			level.playSound(null, pos, TFSounds.DEATH_TOME_DEATH.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
			ParticlePacket particlePacket = new ParticlePacket();
			for (int i = 0; i < 20; ++i) {
				particlePacket.queueParticle(ParticleTypes.POOF, false,
					(double) pos.getX() + 0.5D + level.getRandom().nextGaussian() * 0.02D * level.getRandom().nextGaussian(),
					(double) pos.getY() + level.getRandom().nextGaussian() * 0.02D * level.getRandom().nextGaussian(),
					(double) pos.getZ() + 0.5D + level.getRandom().nextGaussian() * 0.02D * level.getRandom().nextGaussian(),
					0.15F * level.getRandom().nextGaussian(), 0.15F * level.getRandom().nextGaussian(), 0.15F * level.getRandom().nextGaussian());
			}
			PacketDistributor.sendToPlayersNear(serverLevel, null, pos.getX(), pos.getY(), pos.getZ(), 32.0D, particlePacket);
		}
		super.playerDestroy(level, player, pos, state, entity, stack);
	}

	@SuppressWarnings("deprecation")
	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ChiseledCanopyShelfBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.CHISELED_CANOPY_BOOKSHELF.get(), ChiseledCanopyShelfBlockEntity::tick);
	}
}
