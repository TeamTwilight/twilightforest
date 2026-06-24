package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import org.jspecify.annotations.Nullable;
import twilightforest.block.entity.KeepsakeCasketBlockEntity;
import twilightforest.block.entity.SkullChestBlockEntity;
import twilightforest.enums.BlockLoggingEnum;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;

public class KeepsakeCasketBlock extends SkullChestBlock {
	public static final IntegerProperty BREAKAGE = IntegerProperty.create("damage", 0, 2);
	public static final MapCodec<KeepsakeCasketBlock> CODEC = simpleCodec(KeepsakeCasketBlock::new);

	public KeepsakeCasketBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(FACING, Direction.NORTH).setValue(BREAKAGE, 0));
	}

	@Override
	protected MapCodec<? extends KeepsakeCasketBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new KeepsakeCasketBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.KEEPSAKE_CASKET.get(), SkullChestBlockEntity::tick);
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		boolean flag = false;
		if (state.getValue(BlockLoggingEnum.MULTILOGGED).getBlock() == Blocks.AIR || state.getValue(BlockLoggingEnum.MULTILOGGED).getFluid() != Fluids.EMPTY) {
			if (stack.is(TFItems.CHARM_OF_KEEPING_3.get()) && state.getValue(BREAKAGE) > 0) {
				stack.consume(1, player);
				level.setBlockAndUpdate(pos, state.setValue(BREAKAGE, state.getValue(BREAKAGE) - 1));
				level.playSound(null, pos, TFSounds.CASKET_REPAIR.get(), SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
				flag = true;
			} else {
				if (level.isClientSide()) {
					return InteractionResult.SUCCESS;
				} else {
					MenuProvider provider = this.getMenuProvider(state, level, pos);

					if (provider != null) {
						player.openMenu(provider);
					}
					flag = true;
				}
			}
		}
		return flag ? InteractionResult.SUCCESS : InteractionResult.PASS;
	}

	@Override
	protected void modifyDrop(BlockState state, ItemStack stack) {
		if (state.getValue(BREAKAGE) > 0)
			stack.set(TFDataComponents.CASKET_DAMAGE, state.getValue(BREAKAGE));
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		level.setBlock(pos, state.setValue(BREAKAGE, stack.getOrDefault(TFDataComponents.CASKET_DAMAGE, 0)), Block.UPDATE_CLIENTS);

		super.setPlacedBy(level, pos, state, placer, stack);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(BREAKAGE);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
		if (state.getValue(BREAKAGE) > 0) {
			ItemStack itemstack = new ItemStack(this);
			itemstack.applyComponents(DataComponentPatch.builder().set(TFDataComponents.CASKET_DAMAGE.get(), state.getValue(BREAKAGE)).build());
			return itemstack;
		}
		return super.getCloneItemStack(level, pos, state, includeData, player);
	}
}
