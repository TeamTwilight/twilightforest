package twilightforest.block;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.redstone.Orientation;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import twilightforest.config.TFConfig;
import twilightforest.init.TFSounds;
import twilightforest.init.TFStats;
import twilightforest.inventory.UncraftingMenu;

import java.util.List;

public class UncraftingTableBlock extends Block {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	public UncraftingTableBlock(BlockBehaviour.Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(POWERED, false));
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult result) {
		if (TFConfig.disableEntireTable) {
			player.displayClientMessage(Component.translatable("block.twilightforest.uncrafting_table.disabled"), true);
			return InteractionResult.PASS;
		}
		if (level.isClientSide()) {
			return InteractionResult.SUCCESS;
		} else {
			player.openMenu(state.getMenuProvider(level, pos));
			player.awardStat(TFStats.UNCRAFTING_TABLE_INTERACTIONS.get());
			return InteractionResult.CONSUME;
		}
	}

	@Override
	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, @Nullable Orientation orientation, boolean isMoving) {
		if (!level.isClientSide()) {
			boolean flag = level.hasNeighborSignal(pos);
			if (flag != state.getValue(POWERED)) {
				if (flag && level.getBlockState(pos.below()).is(Blocks.AMETHYST_BLOCK)) {
					level.playSound(null, pos, TFSounds.UNCRAFTING_TABLE_ACTIVATE.get(), SoundSource.BLOCKS, 0.5F, 1.0F);
				}
				level.setBlockAndUpdate(pos, state.setValue(POWERED, flag));
			}
		}
	}

	@Override
	public void onPlace(BlockState state, Level level, BlockPos pos, BlockState newState, boolean moving) {
		if (!newState.is(state.getBlock())) {
			this.neighborChanged(state, level, pos, this, null, moving);
		}
	}

	@Override
	public MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
		return new SimpleMenuProvider((id, inv, player) -> new UncraftingMenu(id, inv, player.level(), ContainerLevelAccess.create(level, pos)),
			Component.translatable("container.twilightforest.uncrafting_table"));
	}

	@Override
	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		if (TFConfig.disableEntireTable) {
			tooltip.add(Component.translatable("block.twilightforest.uncrafting_table.disabled").withStyle(ChatFormatting.RED));
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}
}
