package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.FireflyBlockEntity;
import twilightforest.init.TFBlockEntities;
import twilightforest.loot.TFLootTables;

public class FireflyBlock extends CritterBlock {

	public static final MapCodec<FireflyBlock> CODEC = simpleCodec(FireflyBlock::new);

	public FireflyBlock(BlockBehaviour.Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FireflyBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.FIREFLY.get(), FireflyBlockEntity::tick);
	}

	@Override
	public @Nullable ResourceLocation getSquishLootTable() {
		return TFLootTables.FIREFLY_SQUISH_DROPS;
	}
}
