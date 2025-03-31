package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFItems;

public class OminousFireBlock extends BaseFireBlock {
	public static final MapCodec<OminousFireBlock> CODEC = simpleCodec(OminousFireBlock::new);

	@Override
	public MapCodec<OminousFireBlock> codec() {
		return CODEC;
	}

	public OminousFireBlock(BlockBehaviour.Properties properties) {
		super(properties, 1.0F);
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader reader, ScheduledTickAccess access, BlockPos pos, Direction direction, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
		return this.canSurvive(state, reader, pos) ? this.defaultBlockState() : Blocks.AIR.defaultBlockState();
	}

	@Override
	public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		return level.getBlockState(pos.below()).isFaceSturdy(level, pos.below(), Direction.UP) && level.getFluidState(pos).isEmpty();
	}

	@Override
	protected boolean canBurn(BlockState state) {
		return true;
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
		return new ItemStack(TFItems.EXANIMATE_ESSENCE.get());
	}

	@Override
	protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
		if (!entity.getType().is(EntityTypeTags.UNDEAD) && level instanceof ServerLevel serverLevel) {
			entity.hurtServer(serverLevel, serverLevel.damageSources().source(TFDamageTypes.OMINOUS_FIRE), 1.0F);
		}
	}
}
