package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFSounds;

import java.util.List;

public class BrittleFlaskItem extends Item {

	public static final int DOSES = 3;

	public BrittleFlaskItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack stack = new ItemStack(this);
		stack.getOrCreateTag().putInt("Uses", 0);
		stack.getOrCreateTag().putInt("Breakage", 0);
		stack.getOrCreateTag().putBoolean("Refillable", true);
		PotionUtils.setPotion(stack, Potions.EMPTY);
		return stack;
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return PotionUtils.getPotion(stack) != Potions.EMPTY;
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return PotionUtils.getColor(stack);
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
		CompoundTag flaskTag = stack.getTag();
		CompoundTag potionTag = other.getTag();

		if (flaskTag != null && potionTag != null) {
			if (action == ClickAction.SECONDARY && other.is(Items.POTION)) {
				if (potionTag.contains("Potion") && this.canBeRefilled(stack)) {
					if (flaskTag.contains("Potion") && flaskTag.getString("Potion").equals(potionTag.getString("Potion")) && flaskTag.getInt("Uses") < DOSES) {
						if (!player.getAbilities().instabuild) {
							other.shrink(1);
							player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
						}
						flaskTag.putInt("Uses", flaskTag.getInt("Uses") + 1);
						player.playSound(TFSounds.FLASK_FILL.get(), flaskTag.getInt("Uses") * 0.25F, player.level().getRandom().nextFloat() * 0.1F + 0.9F);
						return true;
					} else if (!flaskTag.contains("Potion")) {
						if (!player.getAbilities().instabuild) {
							other.shrink(1);
							player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
						}
						flaskTag.putString("Potion", potionTag.getString("Potion"));
						flaskTag.putInt("Uses", flaskTag.getInt("Uses") + 1);
						player.playSound(TFSounds.FLASK_FILL.get(), flaskTag.getInt("Uses") * 0.25F, player.level().getRandom().nextFloat() * 0.1F + 0.9F);
						return true;
					}
				}
			}
		}
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		CompoundTag tag = stack.getTag();

		if (tag != null && tag.contains("Potion") && tag.getString("Potion").equals(Potions.EMPTY.toString())) {
			return InteractionResultHolder.fail(player.getItemInHand(hand));
		}

		if (tag != null && tag.contains("Uses") && tag.getInt("Uses") > 0) {
			return ItemUtils.startUsingInstantly(level, player, hand);
		}

		return InteractionResultHolder.fail(player.getItemInHand(hand));
	}

	public int getUseDuration(ItemStack stack) {
		return 32;
	}

	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		CompoundTag tag = stack.getTag();
		if (tag != null) {
			if (entity instanceof Player player) {
				if (!level.isClientSide()) {
					if (!player.isCreative() && !player.isSpectator()) {
						player.getData(TFDataAttachments.FLASK_DOSES).incrementDoses(PotionUtils.getPotion(stack), (ServerPlayer) player);
					}
					for (MobEffectInstance mobeffectinstance : PotionUtils.getMobEffects(stack)) {
						if (mobeffectinstance.getEffect().isInstantenous()) {
							mobeffectinstance.getEffect().applyInstantenousEffect(player, player, player, mobeffectinstance.getAmplifier(), 1.0D);
						} else {
							player.addEffect(new MobEffectInstance(mobeffectinstance));
						}
					}
				}
				player.awardStat(Stats.ITEM_USED.get(this));
				if (!player.getAbilities().instabuild) {
					tag.putInt("Uses", tag.getInt("Uses") - 1);
				}

				if (tag.getInt("Uses") <= 0) {
					tag.remove("Potion");
				}

				if (this.canBreak() && !player.getAbilities().instabuild) {
					if (tag.getInt("Uses") <= 0) {
						stack.shrink(1);
						level.playSound(null, player, TFSounds.BRITTLE_FLASK_BREAK.get(), player.getSoundSource(), 1.5F, 0.7F);
					} else {
						tag.putInt("Breakage", tag.getInt("Breakage") + 1);
						tag.putBoolean("Refillable", false);
						level.playSound(null, player, TFSounds.BRITTLE_FLASK_CRACK.get(), player.getSoundSource(), 1.5F, 2.0F);
					}
				}
			}
		}
		return super.finishUsingItem(stack, level, entity);
	}

	public boolean canBreak() {
		return true;
	}

	public boolean canBeRefilled(ItemStack stack) {
		if (stack.getTag() != null && stack.getTag().contains("Refillable")) {
			return stack.getTag() != null && stack.getTag().getBoolean("Refillable");
		}
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		PotionUtils.addPotionTooltip(stack, tooltip, 1.0F, level == null ? 20.0F : level.tickRateManager().tickrate());
		if (stack.getTag() != null) {
			tooltip.add(Component.translatable("item.twilightforest.flask.doses", stack.getTag().getInt("Uses"), DOSES).withStyle(ChatFormatting.GRAY));
			if (stack.getTag().contains("Refillable") && !stack.getTag().getBoolean("Refillable"))
				tooltip.add(Component.translatable("item.twilightforest.flask.no_refill").withStyle(ChatFormatting.RED));
		}
	}

	//copied from Item.getBarWidth, but reversed the "durability" check so it increments up, not down
	@Override
	public int getBarWidth(ItemStack stack) {
		if (stack.getTag() != null) {
			return Math.round(13.0F - Math.abs(stack.getTag().getInt("Uses") - DOSES) * 13.0F / DOSES);
		}
		return 0;
	}
}