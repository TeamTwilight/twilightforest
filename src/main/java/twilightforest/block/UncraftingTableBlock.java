package twilightforest.block;

import net.minecraft.client.Minecraft;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;
import twilightforest.inventory.UncraftingContainer;

import javax.annotation.Nullable;

import net.minecraft.world.level.block.state.BlockBehaviour.Properties;
import twilightforest.util.TFStats;

public class UncraftingTableBlock extends Block {

	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

	protected UncraftingTableBlock() {
		super(Properties.of(Material.WOOD).strength(2.5F).sound(SoundType.WOOD));
		registerDefaultState(stateDefinition.any().setValue(POWERED, false));
	}

	@Override
	@Deprecated
	public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
		if (!world.isClientSide) {
			player.openMenu(state.getMenuProvider(world, pos));
			player.awardStat(TFStats.UNCRAFTING_TABLE_INTERACTIONS);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
		if(!worldIn.isClientSide) {
			boolean flag = worldIn.hasNeighborSignal(pos);
			if (flag != state.getValue(POWERED)) {
				if (flag && worldIn.getBlockState(pos.below()).is(Blocks.AMETHYST_BLOCK)) {
					worldIn.playSound(null, pos, TFSounds.UNCRAFTING_TABLE_ACTIVATE, SoundSource.BLOCKS, 0.5F, 1.0F);
				}
				worldIn.setBlockAndUpdate(pos, state.setValue(POWERED, flag));
			}
		}
	}

	@Nullable
	@Override
	public MenuProvider getMenuProvider(BlockState state, Level world, BlockPos pos) {
		return new SimpleMenuProvider((id, inv, player) -> new UncraftingContainer(id, inv, player.level, ContainerLevelAccess.create(world, pos)),
						new TranslatableComponent(getDescriptionId()));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

}
