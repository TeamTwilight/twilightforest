package twilightforest.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.util.ConcatenatedListView;
import org.jspecify.annotations.Nullable;
import twilightforest.block.entity.spawner.SinisterSpawnerBlockEntity;
import twilightforest.block.entity.spawner.SinisterSpawnerLogic;
import twilightforest.init.TFBlockEntities;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFItems;
import twilightforest.init.TFParticleType;

import java.util.List;

import static twilightforest.init.TFBlocks.KNIGHT_PHANTOM_BOSS_SPAWNER;

public class SinisterSpawnerBlock extends BaseEntityBlock {
	public static final MapCodec<SinisterSpawnerBlock> CODEC = simpleCodec(SinisterSpawnerBlock::new);

	public SinisterSpawnerBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SinisterSpawnerBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
		return createTickerHelper(blockEntityType, TFBlockEntities.SINISTER_SPAWNER.value(), level.isClientSide() ? SinisterSpawnerBlockEntity::clientTick : SinisterSpawnerBlockEntity::serverTick);
	}

	@Override
	public int getExpDrop(BlockState state, LevelAccessor level, BlockPos pos, @Nullable BlockEntity blockEntity, @Nullable Entity breaker, ItemStack tool) {
		return 15 + level.getRandom().nextInt(15) + level.getRandom().nextInt(15);
	}

	//TODO
//	@Override
//	public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
//		super.appendHoverText(stack, context, tooltip, flag);
//		Spawner.appendHoverText(stack, tooltip, "SpawnData");
//	}

	@Override
	protected VoxelShape getVisualShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext collisionContext) {
		return Shapes.empty();
	}

	@Override
	protected boolean propagatesSkylightDown(BlockState state) {
		return true;
	}

	@Override
	protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
		return 1.0F;
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (player.isCreative()) {
			BlockEntity blockEntity = level.getBlockEntity(pos);

			if (blockEntity instanceof SinisterSpawnerBlockEntity entity && entity.getSpawner() instanceof SinisterSpawnerLogic logic) {
				List<ParticleOptions> particleOptions = this.particlesFromItem(stack);

				if (!particleOptions.isEmpty()) {
					logic.setParticles(particleOptions, true);

					return InteractionResult.SUCCESS;
				}
			}
		}

		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	private List<ParticleOptions> particlesFromItem(ItemStack stack) {
		if (stack.is(TFItems.NAGA_TROPHY) || stack.is(TFBlocks.NAGA_BOSS_SPAWNER.asItem())) {
			return List.of(ParticleTypes.SMOKE, ParticleTypes.CRIT);
		} else if (stack.is(TFItems.LICH_TROPHY) || stack.is(TFBlocks.LICH_BOSS_SPAWNER.asItem())) {
			return List.of(ParticleTypes.SMOKE, TFParticleType.OMINOUS_FLAME.get());
		} else if (stack.is(TFItems.MINOSHROOM_TROPHY) || stack.is(TFBlocks.MINOSHROOM_BOSS_SPAWNER.asItem())) {
			return List.of(ParticleTypes.SMOKE, ParticleTypes.CRIMSON_SPORE);
		} else if (stack.is(TFItems.HYDRA_TROPHY) || stack.is(TFBlocks.HYDRA_BOSS_SPAWNER.asItem())) {
			return List.of(ParticleTypes.SMOKE, ParticleTypes.FLAME);
		} else if (stack.is(TFItems.KNIGHT_PHANTOM_TROPHY) || stack.is(KNIGHT_PHANTOM_BOSS_SPAWNER.asItem())) {
			return List.of(ParticleTypes.SMOKE, TFParticleType.OMINOUS_FLAME.get());
		} else if (stack.is(TFItems.UR_GHAST_TROPHY) || stack.is(TFBlocks.UR_GHAST_BOSS_SPAWNER.asItem())) {
			return List.of(ParticleTypes.SMOKE, DustParticleOptions.REDSTONE);
		} else if (stack.is(TFItems.ALPHA_YETI_TROPHY) || stack.is(TFBlocks.ALPHA_YETI_BOSS_SPAWNER.asItem())) {
			return List.of(TFParticleType.SNOW.get(), ParticleTypes.FALLING_WATER);
		} else if (stack.is(TFItems.SNOW_QUEEN_TROPHY) || stack.is(TFBlocks.SNOW_QUEEN_BOSS_SPAWNER.asItem())) {
			return List.of(TFParticleType.SNOW.get(), TFParticleType.SNOW_WARNING.get());
		} else if (stack.is(TFBlocks.FINAL_BOSS_BOSS_SPAWNER.asItem())) {
			return List.of(TFParticleType.ANNIHILATE.get());
		} else if (stack.is(TFItems.QUEST_RAM_TROPHY)) {
			return List.of(TFParticleType.TRANSFORMATION_PARTICLE.get());
		}

		return List.of();
	}

	@Override
	protected List<ItemStack> getDrops(BlockState state, LootParams.Builder paramBuilder) {
		List<ItemStack> drops = super.getDrops(state, paramBuilder);

		if (paramBuilder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof SinisterSpawnerBlockEntity entity) {
			LootTable lootTable = entity.getLootTable();
			if (lootTable != null) {
				LootParams params = paramBuilder.withParameter(LootContextParams.BLOCK_STATE, state).create(LootContextParamSets.BLOCK);
				return ConcatenatedListView.of(drops, lootTable.getRandomItems(params));
			}
		}

		return drops;
	}
}
