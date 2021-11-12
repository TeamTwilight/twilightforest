package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.TFSounds;
import twilightforest.enums.PlantVariant;
import twilightforest.item.TFItems;
import twilightforest.util.TFStats;

import java.util.Random;

public class TorchberryPlantBlock extends TFPlantBlock implements BonemealableBlock {

	public static final BooleanProperty HAS_BERRIES = BooleanProperty.create("has_torchberries");

	public TorchberryPlantBlock(PlantVariant plant, Properties props) {
		super(plant, props);
		registerDefaultState(getStateDefinition().any().setValue(HAS_BERRIES, false));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if(state.getValue(HAS_BERRIES)) {
			level.setBlockAndUpdate(pos, state.setValue(HAS_BERRIES, false));
			level.playSound(null, pos, TFSounds.PICKED_TORCHBERRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
			ItemEntity torchberries = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(TFItems.TORCHBERRIES.get()));
			level.addFreshEntity(torchberries);
			if(player instanceof ServerPlayer) player.awardStat(TFStats.TORCHBERRIES_HARVESTED);
			return InteractionResult.sidedSuccess(level.isClientSide);
		}
		return super.use(state, level, pos, player, hand, result);
	}

	@Override
	public boolean isValidBonemealTarget(BlockGetter level, BlockPos pos, BlockState state, boolean isClient) {
		return !state.getValue(HAS_BERRIES);
	}

	@Override
	public boolean isBonemealSuccess(Level level, Random rand, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void performBonemeal(ServerLevel level, Random rand, BlockPos pos, BlockState state) {
		level.setBlock(pos, state.setValue(HAS_BERRIES, true), 2);
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HAS_BERRIES);
	}

}
