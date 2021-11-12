package twilightforest.block;

import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.lighting.BlockLightEngine;
import net.minecraft.world.level.lighting.LayerLightEventListener;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.StringUtils;
import twilightforest.block.entity.SkullCandleBlockEntity;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.ToIntFunction;

public abstract class AbstractSkullCandleBlock extends AbstractLightableBlock {

	private final SkullBlock.Type type;
	private int candleCount;
	private int color;
	@Nullable
	private GameProfile owner;

	public AbstractSkullCandleBlock(SkullBlock.Type type, Properties properties) {
		super(properties);
		this.type = type;
	}

	public int getColor() {
		return color;
	}

	public int getCandleCount() {
		return candleCount;
	}

	public SkullBlock.Type getType() {
		return type;
	}

	protected abstract Iterable<Vec3> getParticleOffsets(BlockState state, Level level, BlockPos pos);

	@Override
	public int getLightEmission(BlockState state, BlockGetter world, BlockPos pos) {
		if(world.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) {
			switch (state.getValue(LIGHTING)) {
				case NONE -> {
					return 0;
				}
				case NORMAL -> {
					return 3 * sc.candleAmount;
				}
				case OMINOUS -> {
					return 2 * sc.candleAmount;
				}
			}
		}
		return 0;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
		return new SkullCandleBlockEntity(blockPos, blockState, getColor(), getCandleCount());
	}

	//input one of the enum names to convert it into a candle block
	public static Block candleColorToCandle(String candleName) {
		if(!candleName.equals(CandleColors.PLAIN.getSerializedName())) {
			return Objects.requireNonNull(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(candleName + "_candle")));
		}
		return Blocks.CANDLE;
	}

	//inverse of above
	public static CandleColors candleToCandleColor(Item candle) {
		if(!(candle == Blocks.CANDLE.asItem())) {
			return CandleColors.valueOf(candle.getRegistryName().getPath().replace("_candle", "").replace("\"", "").toUpperCase(Locale.ROOT));
		}
		return CandleColors.PLAIN;
	}

	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.INVISIBLE;
	}

	@Override
	public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
		super.setPlacedBy(level, pos, state, placer, stack);
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof SkullCandleBlockEntity sc) {
			if(stack.hasTag() && stack.getTag() != null) {
					CompoundTag tag = stack.getTagElement("BlockEntityTag");
					if(tag != null) {
						if (tag.contains("CandleAmount")) sc.candleAmount = tag.getInt("CandleAmount");
						if (tag.contains("CandleColor")) sc.candleColor = tag.getInt("CandleColor");
					}
				if(type == SkullBlock.Types.PLAYER) {
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
	public void playerWillDestroy(Level world, BlockPos pos, BlockState state, Player player) {
		if(!world.isClientSide && !player.isCreative() && world.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) {
			color = sc.candleColor;
			candleCount = sc.candleAmount;
			owner = sc.getOwnerProfile();
		}
		super.playerWillDestroy(world, pos, state, player);
	}

	@Override
	public void playerDestroy(Level world, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity entity, ItemStack stack) {
		if (!world.isClientSide && !player.isCreative() && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && candleCount > 0) {
			//if we have silk touch, assign the candle values to the item and drop it
			if(EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0) {
				ItemStack newStack = new ItemStack(this);
				ItemEntity itementity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), newStack);
				CompoundTag tag = new CompoundTag();
				tag.putInt("CandleColor", color);
				tag.putInt("CandleAmount", candleCount);
				newStack.addTagElement("BlockEntityTag", tag);
				if(owner != null) newStack.getOrCreateTag().put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), owner));
				itementity.setDefaultPickUpDelay();
				world.addFreshEntity(itementity);
				//otherwise lets drop the skull and candles
			} else {
				//skull is handled via loot table
				ItemStack newStack = new ItemStack(candleColorToCandle(CandleColors.colorFromInt(color).getSerializedName()), candleCount);
				ItemEntity itementity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), newStack);
				world.addFreshEntity(itementity);
			}
		}
		super.playerDestroy(world, player, pos, state, entity, stack);
	}

	@Override
	public ItemStack getPickBlock(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
		ItemStack newStack = new ItemStack(this);
		CompoundTag tag = new CompoundTag();
		if(world.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) {
			if(sc.getOwnerProfile() != null) newStack.getOrCreateTag().put("SkullOwner", NbtUtils.writeGameProfile(new CompoundTag(), sc.getOwnerProfile()));
			tag.putInt("CandleColor", sc.candleColor);
			tag.putInt("CandleAmount", sc.candleAmount);
			newStack.addTagElement("BlockEntityTag", tag);
		}
		return newStack;
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
		if(level.getBlockEntity(pos) instanceof SkullCandleBlockEntity sc) {
			if (player.getItemInHand(hand).is(ItemTags.CANDLES)
					&& player.getItemInHand(hand).is(candleColorToCandle(CandleColors.colorFromInt(sc.candleColor).getSerializedName()).asItem())
					&& !player.isShiftKeyDown()) {
				if (sc.candleAmount < 4) {
					sc.candleAmount++;
					level.playSound(null, pos, SoundEvents.CANDLE_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
					if (!player.getAbilities().instabuild) player.getItemInHand(hand).shrink(1);
					level.getLightEngine().checkBlock(pos);
					return InteractionResult.sidedSuccess(level.isClientSide);
				}

			}
		}
		return super.use(state, level, pos, player, hand, result);
	}

	@Override
	public void animateTick(BlockState state, Level level, BlockPos pos, Random rand) {
		if (state.getValue(LIGHTING) != Lighting.NONE) {
			this.getParticleOffsets(state, level, pos).forEach((offset) ->
					addParticlesAndSound(level, offset.add(pos.getX(), pos.getY(), pos.getZ()), rand, state.getValue(LIGHTING) == Lighting.OMINOUS));
		}
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
			return value;
		}

		@Override
		public String getSerializedName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}
}
