package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.CicadaBlockEntity;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFSounds;
import twilightforest.loot.TFLootTables;

public class CicadaBlock extends CritterBlock {

	public static final MapCodec<CicadaBlock> CODEC = simpleCodec(CicadaBlock::new);

	public CicadaBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new CicadaBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.CICADA.get(), CicadaBlockEntity::tick);
	}

	@Override
	public @Nullable ResourceLocation getSquishLootTable() {
		return TFLootTables.CICADA_SQUISH_DROPS;
	}

	@Override
	public void destroy(LevelAccessor accessor, BlockPos pos, BlockState state) {
		super.destroy(accessor, pos, state);
		if (accessor.isClientSide())
			Minecraft.getInstance().getSoundManager().stop(TFSounds.CICADA.get().getLocation(), SoundSource.NEUTRAL);
	}
}
