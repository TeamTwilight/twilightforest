package twilightforest.block;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.registries.DeferredBlock;
import twilightforest.block.entity.OminousCandleBlockEntity;
import twilightforest.init.TFBlocks;
import twilightforest.init.TFParticleType;

import java.util.HashMap;
import java.util.List;

public class OminousCandleBlock extends BaseEntityBlock {
	public static final MapCodec<OminousCandleBlock> CODEC = RecordCodecBuilder.mapCodec(
		app -> app.group(BuiltInRegistries.BLOCK.byNameCodec().fieldOf("candle").forGetter(block -> block.candle), propertiesCodec())
			.apply(app, OminousCandleBlock::new)
	);

	private static final VoxelShape ONE_AABB = Block.box(7.0, 0.0, 7.0, 9.0, 6.0, 9.0);
	private static final VoxelShape TWO_AABB = Block.box(5.0, 0.0, 6.0, 11.0, 6.0, 9.0);
	private static final VoxelShape THREE_AABB = Block.box(5.0, 0.0, 6.0, 10.0, 6.0, 11.0);
	private static final VoxelShape FOUR_AABB = Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 10.0);
	public static final IntegerProperty CANDLES = BlockStateProperties.CANDLES;

	public static final HashMap<Block, DeferredBlock<OminousCandleBlock>> CANDLE_MAP = Util.make(new HashMap<>(), map -> {
		map.put(Blocks.CANDLE, TFBlocks.OMINOUS_CANDLE);
		map.put(Blocks.WHITE_CANDLE, TFBlocks.OMINOUS_WHITE_CANDLE);
		map.put(Blocks.ORANGE_CANDLE, TFBlocks.OMINOUS_ORANGE_CANDLE);
		map.put(Blocks.MAGENTA_CANDLE, TFBlocks.OMINOUS_MAGENTA_CANDLE);
		map.put(Blocks.LIGHT_BLUE_CANDLE, TFBlocks.OMINOUS_LIGHT_BLUE_CANDLE);
		map.put(Blocks.YELLOW_CANDLE, TFBlocks.OMINOUS_YELLOW_CANDLE);
		map.put(Blocks.LIME_CANDLE, TFBlocks.OMINOUS_LIME_CANDLE);
		map.put(Blocks.PINK_CANDLE, TFBlocks.OMINOUS_PINK_CANDLE);
		map.put(Blocks.GRAY_CANDLE, TFBlocks.OMINOUS_GRAY_CANDLE);
		map.put(Blocks.LIGHT_GRAY_CANDLE, TFBlocks.OMINOUS_LIGHT_GRAY_CANDLE);
		map.put(Blocks.CYAN_CANDLE, TFBlocks.OMINOUS_CYAN_CANDLE);
		map.put(Blocks.PURPLE_CANDLE, TFBlocks.OMINOUS_PURPLE_CANDLE);
		map.put(Blocks.BLUE_CANDLE, TFBlocks.OMINOUS_BLUE_CANDLE);
		map.put(Blocks.BROWN_CANDLE, TFBlocks.OMINOUS_BROWN_CANDLE);
		map.put(Blocks.GREEN_CANDLE, TFBlocks.OMINOUS_GREEN_CANDLE);
		map.put(Blocks.RED_CANDLE, TFBlocks.OMINOUS_RED_CANDLE);
		map.put(Blocks.BLACK_CANDLE, TFBlocks.OMINOUS_BLACK_CANDLE);
	});

	public static final Int2ObjectMap<List<Vec2>> CANDLE_OFFSETS = Util.make(
		() -> {
			Int2ObjectMap<List<Vec2>> int2objectmap = new Int2ObjectOpenHashMap<>();
			int2objectmap.defaultReturnValue(ImmutableList.of());
			int2objectmap.put(1, ImmutableList.of(new Vec2(0.5F, 0.5F)));
			int2objectmap.put(2, ImmutableList.of(new Vec2(0.375F, 0.5F), new Vec2(0.625F, 0.44F)));
			int2objectmap.put(3, ImmutableList.of(new Vec2(0.5F, 0.625F), new Vec2(0.375F, 0.5F), new Vec2(0.56F, 0.44F)));
			int2objectmap.put(4, ImmutableList.of(new Vec2(0.44F, 0.56F), new Vec2(0.625F, 0.56F), new Vec2(0.375F, 0.375F), new Vec2(0.56F, 0.375F)));
			return Int2ObjectMaps.unmodifiable(int2objectmap);
		}
	);

	public final Block candle;

	public OminousCandleBlock(Block candle, Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any().setValue(CANDLES, 1));
		this.candle = candle;
	}

	@Override
	protected MapCodec<? extends BaseEntityBlock> codec() {
		return CODEC;
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
		int candles = state.getValue(CANDLES);
		int i = 1;
		for (Vec2 vec2 : CANDLE_OFFSETS.get(candles)) {
			addParticlesAndSound(level, Vec3.atLowerCornerWithOffset(pos, vec2.x, getCurrentY(level.getGameTime(), 0.0F, pos, i++) + 0.5D, vec2.y), random);
		}
	}

	public static double getCurrentY(long ticks, float partialTick, BlockPos pos, int candle) {
		double time = (ticks + partialTick) * 0.014D;

		double x = Math.sin(time * 7.0D + (pos.getX() * candle));
		double y = Math.sin(time * 4.2D + (pos.getY() * candle));
		double z = Math.sin(time * 3.0D + (pos.getZ() * candle));

		return (((x + y + z) / 4.0D) * 0.5D + 0.5D) * 0.25D;
	}

	private static void addParticlesAndSound(Level level, Vec3 offset, RandomSource random) {
		float f = random.nextFloat();
		if (f < 0.3F) {
			level.addParticle(ParticleTypes.SMOKE, offset.x, offset.y, offset.z, 0.0, 0.0, 0.0);
			if (f < 0.17F) {
				level.playLocalSound(
					offset.x + 0.5,
					offset.y + 0.5,
					offset.z + 0.5,
					SoundEvents.CANDLE_AMBIENT,
					SoundSource.BLOCKS,
					1.0F + random.nextFloat(),
					random.nextFloat() * 0.7F + 0.3F,
					false
				);
			}
		}

		level.addParticle(TFParticleType.OMINOUS_FLAME.get(), offset.x, offset.y, offset.z, 0.0, 0.0, 0.0);
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return switch (state.getValue(CANDLES)) {
			case 2 -> TWO_AABB;
			case 3 -> THREE_AABB;
			case 4 -> FOUR_AABB;
			default -> ONE_AABB;
		};
	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (player.getAbilities().mayBuild) {
			if (stack.isEmpty()) {
				BlockState newState = this.candle.defaultBlockState().setValue(CANDLES, state.getValue(CANDLES));
				level.setBlock(pos, newState, Block.UPDATE_ALL);
				AbstractCandleBlock.extinguish(player, newState, level, pos);
				return InteractionResult.SUCCESS;
			} else if (stack.getItem() instanceof BlockItem blockItem && blockItem.getBlock() instanceof CandleBlock candleBlock && state.getValue(CANDLES) < 4 && this.candle == candleBlock) {
				level.setBlock(pos, state.setValue(CANDLES, state.getValue(CANDLES) + 1), Block.UPDATE_ALL);

				candleBlock.setPlacedBy(level, pos, state, player, stack);
				if (player instanceof ServerPlayer) CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayer) player, pos, stack);
				player.awardStat(Stats.ITEM_USED.get(blockItem));

				BlockState newState = level.getBlockState(pos);

				SoundType soundtype = newState.getSoundType(level, pos, player);
				level.playSound(
					player,
					pos,
					soundtype.getPlaceSound(),
					SoundSource.BLOCKS,
					(soundtype.getVolume() + 1.0F) / 2.0F,
					soundtype.getPitch() * 0.8F
				);
				level.gameEvent(GameEvent.BLOCK_PLACE, pos, GameEvent.Context.of(player, newState));
				stack.consume(1, player);

				return InteractionResult.SUCCESS;
			}
		}
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	public static void eruptFlameParticles(Level level, BlockPos pos, BlockState state) {
		Vec3 start = Vec3.atLowerCornerOf(pos);
		for (Vec2 vec2 : CANDLE_OFFSETS.get(state.getValue(CANDLES).intValue())) {
			for (int j = 0; j < 5; j++) {
				level.addParticle(TFParticleType.OMINOUS_FLAME.get(), start.x + vec2.x, start.y + 0.5D, start.z + vec2.y, (level.getRandom().nextDouble() - 0.5D) * 0.05D, 0.015F, (level.getRandom().nextDouble() - 0.5D) * 0.05D);
			}
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(CANDLES);
	}

	@Override
	public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, Player player) {
		return new ItemStack(this.candle);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new OminousCandleBlockEntity(pos, state);
	}
}
