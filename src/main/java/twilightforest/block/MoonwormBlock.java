package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;
import twilightforest.block.entity.MoonwormBlockEntity;
import twilightforest.init.TFBlockEntities;
import twilightforest.loot.TFLootTables;

public class MoonwormBlock extends CritterBlock {

	public static final MapCodec<MoonwormBlock> CODEC = simpleCodec(MoonwormBlock::new);

	public MoonwormBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new MoonwormBlockEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.MOONWORM.get(), MoonwormBlockEntity::tick);
	}

	@Override
	public ResourceKey<LootTable> getSquishLootTable() {
		return TFLootTables.MOONWORM_SQUISH_DROPS;
	}
}
