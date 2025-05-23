package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.BlockHitResult;
import twilightforest.block.entity.CicadaBlockEntity;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFSounds;
import twilightforest.loot.TFLootTables;

public class CicadaBlock extends CritterBlock.WaterLoggable {
	public static final MapCodec<CicadaBlock> CODEC = simpleCodec(CicadaBlock::new);

	public CicadaBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CicadaBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.CICADA.get(), CicadaBlockEntity::tick);
	}

	@Override
	public ResourceKey<LootTable> getSquishLootTable() {
		return TFLootTables.CICADA_SQUISH_DROPS;
	}

	@Override
	protected ItemInteractionResult onJarAttempt(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		stack.consume(1, player);
		player.getInventory().add(new ItemStack(TFBlocks.CICADA_JAR.get()));
		if (level.isClientSide()) Minecraft.getInstance().getSoundManager().stop(TFSounds.CICADA.get().getLocation(), SoundSource.NEUTRAL);
		level.setBlockAndUpdate(pos, state.getFluidState().createLegacyBlock());
		return ItemInteractionResult.sidedSuccess(level.isClientSide());
	}

	@Override
	public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
		super.destroy(accessor, pos, state);
		if (accessor.isClientSide())
			Minecraft.getInstance().getSoundManager().stop(TFSounds.CICADA.get().location(), SoundSource.NEUTRAL);
	}
}
