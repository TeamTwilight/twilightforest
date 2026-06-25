package twilightforest.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jspecify.annotations.Nullable;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.inventory.InventoryUtil;
import twilightforest.init.TFBlockEntities;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class AbstractSkullCandleBlock extends BaseEntityBlock implements LightableBlock {
	public static final IntegerProperty CANDLES = BlockStateProperties.CANDLES;
	public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
	private final SkullBlock.Type type;

	public AbstractSkullCandleBlock(SkullBlock.Type type, Properties properties) {
		super(properties);
		this.type = type;
		this.registerDefaultState(this.getStateDefinition().any().setValue(LIGHTING, Lighting.NONE).setValue(CANDLES, 1).setValue(POWERED, false));
	}

	public SkullBlock.Type getType() {
		return this.type;
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter getter, BlockPos pos) {
		return switch (state.getValue(LIGHTING)) {
			case NORMAL -> 3 * state.getValue(CANDLES);
			case OMINOUS -> 2 * state.getValue(CANDLES);
			case DIM -> state.getValue(CANDLES);
			default -> 0;
		};
	}

	@Override
	public BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
		if (ItemAbilities.FIRESTARTER_LIGHT == itemAbility) {
			if (this.canBeLit(state)) {
				return state.setValue(LIGHTING, Lighting.NORMAL);
			}
		}
		return super.getToolModifiedState(state, context, itemAbility, simulate);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SkullCandleBlockEntity(pos, state);
	}

	//input one of the enum names to convert it into a candle block
	public static Block candleColorToCandle(CandleColors color) {
		if (color != CandleColors.PLAIN) {
			return BuiltInRegistries.BLOCK.getValue(Identifier.withDefaultNamespace(color.getSerializedName() + "_candle"));
		}
		return Blocks.CANDLE;
	}

	//inverse of above
	public static CandleColors candleToCandleColor(Item candle) {
		if (!(candle == Blocks.CANDLE.asItem())) {
			return CandleColors.valueOf(BuiltInRegistries.ITEM.getKey(candle).getPath().replace("_candle", "").replace("\"", "").toUpperCase(Locale.ROOT));
		}
		return CandleColors.PLAIN;
	}

	//TODO look into better loot table stuff for this
//	@Override
//	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
//		List<ItemStack> drops = super.getDrops(state, builder);
//		Optional<ItemStack> skullStack = drops.stream().filter(item -> item.is(ItemTags.SKULLS) && !item.is(this.asItem())).findFirst();
//		if (skullStack.isPresent()) {
//			BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
//			if (blockEntity instanceof SkullCandleBlockEntity sc) {
//				if (!builder.getParameter(LootContextParams.TOOL).isEmpty() && builder.getParameter(LootContextParams.TOOL).getEnchantmentLevel(sc.getLevel().registryAccess().registryOrThrow(Registries.ENCHANTMENT).getHolderOrThrow(Enchantments.SILK_TOUCH)) > 0) {
//					ItemStack newStack = new ItemStack(this);
//
//					newStack.set(TFDataComponents.SKULL_CANDLES, new SkullCandles(sc.getCandleColor(), state.getValue(CANDLES)));
//
//					if (this.type == SkullBlock.Types.PLAYER && sc.getOwnerProfile() != null)
//						newStack.set(DataComponents.PROFILE, sc.getOwnerProfile());
//
//					drops.remove(skullStack.get());
//					drops.add(newStack);
//				} else {
//					drops.add(new ItemStack(candleColorToCandle(CandleColors.colorFromInt(sc.getCandleColor())), state.getValue(CANDLES)));
//				}
//			}
//		}
//
//		return drops;
//	}

	@Override
	protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) {
			if (stack.is(ItemTags.CANDLES)
				&& stack.is(candleColorToCandle(CandleColors.colorFromInt(sc.candleInfo.color())).asItem())
				&& !player.isShiftKeyDown()) {
				int candles = state.getValue(CANDLES);
				if (candles < 4) {
					level.setBlockAndUpdate(pos, state.setValue(CANDLES, candles + 1));

					level.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
					stack.consume(1, player);
					level.getLightEngine().checkBlock(pos);
					return InteractionResult.SUCCESS;
				}
			}
		}
		return this.tryLightCandles(stack, state, level, pos, player);
	}

	@Override
	protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc && player.isSecondaryUseActive() && state.getValue(CANDLES) > 0) {
			int newCandleAmount = state.getValue(CANDLES) - 1;
			if (newCandleAmount > 0) {
				level.setBlockAndUpdate(pos, state.setValue(CANDLES, newCandleAmount));
			} else {
				boolean wall = state.getBlock() instanceof WallSkullCandleBlock;
				Block newBlock = getNoCandleSkull(wall);
				if (newBlock != null) {
					DataComponentMap components = sc.components();
                    BlockState newState;
                    if (wall) {
                        newState = newBlock.defaultBlockState().setValue(WallSkullBlock.FACING, state.getValue(WallSkullCandleBlock.FACING));
                    } else {
                        newState = newBlock.defaultBlockState().setValue(SkullBlock.ROTATION, state.getValue(SkullCandleBlock.ROTATION));
                    }
                    level.setBlockAndUpdate(pos, newState);
                    level.setBlockEntity(new SkullBlockEntity(pos, newState));
                    if (level.getBlockEntity(pos) instanceof SkullBlockEntity sc1) sc1.setComponents(components);
				}
			}
			level.playSound(null, pos, SoundEvents.CANDLE_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
			level.getLightEngine().checkBlock(pos);
			ItemStack candle = new ItemStack(candleColorToCandle(CandleColors.colorFromInt(sc.candleInfo.color())));
			if (player.hasInfiniteMaterials()) {
				if (!player.getInventory().contains(candle)) {
					player.getInventory().add(candle);
				}
			} else {
				InventoryUtil.giveItemToPlayer(player, candle);
			}
			return InteractionResult.SUCCESS;
		}
		return super.useWithoutItem(state, level, pos, player, hitResult);
	}

	@Nullable
	private Block getNoCandleSkull(boolean wall) {
		Block newBlock;
		switch ((SkullBlock.Types) this.getType()) {
			case SKELETON -> {
				if (wall) newBlock = Blocks.SKELETON_WALL_SKULL;
				else newBlock = Blocks.SKELETON_SKULL;
			}
			case WITHER_SKELETON -> {
				if (wall) newBlock = Blocks.WITHER_SKELETON_WALL_SKULL;
				else newBlock = Blocks.WITHER_SKELETON_SKULL;
			}
			case PLAYER -> {
				if (wall) newBlock = Blocks.PLAYER_WALL_HEAD;
				else newBlock = Blocks.PLAYER_HEAD;
			}
			case ZOMBIE -> {
				if (wall) newBlock = Blocks.ZOMBIE_WALL_HEAD;
				else newBlock = Blocks.ZOMBIE_HEAD;
			}
			case CREEPER -> {
				if (wall) newBlock = Blocks.CREEPER_WALL_HEAD;
				else newBlock = Blocks.CREEPER_HEAD;
			}
			case PIGLIN -> {
				if (wall) newBlock = Blocks.PIGLIN_WALL_HEAD;
				else newBlock = Blocks.PIGLIN_HEAD;
			}
			default -> newBlock = null;
		}
		return newBlock;
	}

	@Override
	public void onProjectileHit(Level level, BlockState state, BlockHitResult result, Projectile projectile) {
		this.lightCandlesWithProjectile(level, state, result, projectile);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource rand) {
		if (state.getValue(LIGHTING) != Lighting.NONE) {
			this.getParticleOffsets(state, level, pos).forEach(offset -> {
				Vec3 trueOffset = offset.add(pos.getX(), pos.getY(), pos.getZ());
				this.addParticlesAndSound(level, trueOffset.x(), trueOffset.y(), trueOffset.z(), rand, state.getValue(LIGHTING));
			});
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LIGHTING, CANDLES, POWERED);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.SKULL_CANDLE.get(), SkullCandleBlockEntity::animation);
	}

	public enum CandleColors implements StringRepresentable {

		PLAIN(0),
		WHITE(1), LIGHT_GRAY(2), GRAY(3), BLACK(4),
		RED(5), ORANGE(6), YELLOW(7), GREEN(8),
		LIME(9), BLUE(10), CYAN(11), LIGHT_BLUE(12),
		PURPLE(13), MAGENTA(14), PINK(15), BROWN(16);

		private final int value;
		private static final Map<Integer, CandleColors> map = new HashMap<>();

		CandleColors(int value) {
			this.value = value;
		}

		static {
			for (CandleColors color : CandleColors.values()) {
				map.put(color.value, color);
			}
		}

		public static CandleColors colorFromInt(int value) {
			return map.get(value);
		}

		public int getValue() {
			return this.value;
		}

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}
}
