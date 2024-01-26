package twilightforest.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.spawner.BossSpawnerBlockEntity;
import twilightforest.enums.BossVariant;

public class BossSpawnerBlock extends BaseEntityBlock {

	public static final MapCodec<BossSpawnerBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
					BossVariant.CODEC.fieldOf("variant").forGetter(o -> o.boss),
					propertiesCodec())
			.apply(instance, BossSpawnerBlock::new)
	);
	private final BossVariant boss;

	public BossSpawnerBlock(BossVariant variant, BlockBehaviour.Properties properties) {
		super(properties);
		this.boss = variant;
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return boss.getType().create(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, boss.getType(), BossSpawnerBlockEntity::tick);
	}

	@Override
	public boolean canEntityDestroy(BlockState state, BlockGetter getter, BlockPos pos, Entity entity) {
		return state.getDestroySpeed(getter, pos) >= 0f;
	}
}
