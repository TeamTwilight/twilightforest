package twilightforest.block;

import com.mojang.authlib.GameProfile;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import twilightforest.block.entity.SkullCandleBlockEntity;
import twilightforest.init.TFBlockEntities;

import java.util.*;

public abstract class AbstractSkullCandleBlock extends BaseEntityBlock implements LightableBlock, Equipable {

	private final SkullBlock.Type type;

	public AbstractSkullCandleBlock(SkullBlock.Type type, Properties properties) {
		super(properties);
		this.type = type;
		this.registerDefaultState(this.getStateDefinition().any().setValue(LIGHTING, Lighting.NONE));
	}

	public SkullBlock.Type getType() {
		return this.type;
	}

	@Override
	public int getLightEmission(BlockState state, BlockGetter getter, BlockPos pos) {
		if (getter.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) {
			switch (state.getValue(LIGHTING)) {
				case NONE -> {
					return 0;
				}
				case NORMAL -> {
					return 3 * sc.getCandleAmount();
				}
				case OMINOUS -> {
					return 2 * sc.getCandleAmount();
				}
			}
		}
		return 0;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SkullCandleBlockEntity(pos, state, 0, 0);
	}

	//input one of the enum names to convert it into a candle block
	public static Block candleColorToCandle(CandleColors color) {
		if (color != CandleColors.PLAIN) {
			return Objects.requireNonNull(BuiltInRegistries.BLOCK.get(new ResourceLocation(color.getSerializedName() + "_candle")));
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

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof SkullCandleBlockEntity sc) {
			if (stack.hasTag() && stack.getTag() != null) {
				CompoundTag tag = stack.getTagElement("BlockEntityTag");
				if (tag != null) {
					if (tag.contains("CandleAmount")) sc.setCandleAmount(tag.getInt("CandleAmount"));
					if (tag.contains("CandleColor")) sc.setCandleColor(tag.getInt("CandleColor"));
				}
				if (this.type == SkullBlock.Types.PLAYER) {
					GameProfile gameprofile = null;
					CompoundTag compoundtag = stack.getTag();
					if (compoundtag.contains("SkullOwner", 10)) {
						gameprofile = NbtUtils.readGameProfile(compoundtag.getCompound("SkullOwner"));
					} else if (compoundtag.contains("SkullOwner", 8) && !StringUtils.isBlank(compoundtag.getString("SkullOwner"))) {
						gameprofile = new GameProfile(null, compoundtag.getString("SkullOwner"));
					}
					sc.setOwner(gameprofile);
				}
			}
		}
	}

	@Override
	@SuppressWarnings("deprecation")
	public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
		List<ItemStack> drops = super.getDrops(state, builder);
		Optional<ItemStack> skullStack = drops.stream().filter(item -> item.is(Tags.Items.HEADS) && !item.is(this.asItem())).findFirst();
		if (skullStack.isPresent()) {
			BlockEntity blockEntity = builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
			if (blockEntity instanceof SkullCandleBlockEntity sc) {
				if (!builder.getParameter(LootContextParams.TOOL).isEmpty() && builder.getParameter(LootContextParams.TOOL).getEnchantmentLevel(Enchantments.SILK_TOUCH) > 0) {
					ItemStack newStack = new ItemStack(this);
					CompoundTag tag = new CompoundTag();
					tag.putInt("CandleColor", sc.getCandleColor());
					tag.putInt("CandleAmount", sc.getCandleAmount());
					newStack.addTagElement("BlockEntityTag", tag);
					if (sc.getOwnerProfile() != null) newStack.getOrCreateTag().put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), sc.getOwnerProfile()));
					drops.remove(skullStack.get());
					drops.add(newStack);
				} else {
					drops.add(new ItemStack(candleColorToCandle(CandleColors.colorFromInt(sc.getCandleColor())), sc.getCandleAmount()));
				}
			}
		}

		return drops;
	}

	@Override
	public ItemStack getCloneItemStack(BlockState state, HitResult target, LevelReader level, BlockPos pos, Player player) {
		ItemStack newStack = new ItemStack(this);
		CompoundTag tag = new CompoundTag();
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) {
			if (sc.getOwnerProfile() != null)
				newStack.getOrCreateTag().put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), sc.getOwnerProfile()));
			tag.putInt("CandleColor", sc.getCandleColor());
			tag.putInt("CandleAmount", sc.getCandleAmount());
			newStack.addTagElement("BlockEntityTag", tag);
		}
		return newStack;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if (level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) {
			if (player.getItemInHand(hand).is(ItemTags.CANDLES)
					&& player.getItemInHand(hand).is(candleColorToCandle(CandleColors.colorFromInt(sc.getCandleColor())).asItem())
					&& !player.isShiftKeyDown()) {
				if (sc.getCandleAmount() < 4) {
					sc.incrementCandleAmount();
					level.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
					if (!player.getAbilities().instabuild) player.getItemInHand(hand).shrink(1);
					level.getLightEngine().checkBlock(pos);
					return InteractionResult.sidedSuccess(level.isClientSide());
				}

			}
		}
		return this.lightCandles(state, level, pos, player, hand);
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
		builder.add(LIGHTING);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, TFBlockEntities.SKULL_CANDLE.get(), SkullCandleBlockEntity::tick);
	}

	@Override
	public EquipmentSlot getEquipmentSlot() {
		return EquipmentSlot.HEAD;
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
