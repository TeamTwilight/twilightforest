package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
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
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import twilightforest.TFSounds;
import twilightforest.advancements.TFAdvancements;

import javax.annotation.Nullable;
import java.util.List;

public class BrittleFlaskItem extends Item {

	private static Potion lastUsedPotion;
	private static int timesUsed;
	private static boolean advancementWindow;
	public static int seconds;

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
	public boolean showDurabilityBar(ItemStack stack) {
		return stack.getOrCreateTag().contains("Potion");
	}

	@Override
	public int getRGBDurabilityForDisplay(ItemStack stack) {
		return PotionUtils.getColor(stack);
	}

	@Override
	public boolean isFoil(ItemStack stack) {
		return super.isFoil(stack) || !PotionUtils.getMobEffects(stack).isEmpty();
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
		CompoundTag flaskTag = stack.getOrCreateTag();
		CompoundTag potionTag = other.getOrCreateTag();

		if(action == ClickAction.SECONDARY && other.is(Items.POTION)) {
			if(potionTag.contains("Potion") && canBeRefilled(stack)) {
				if(flaskTag.contains("Potion") && flaskTag.getString("Potion").equals(potionTag.getString("Potion")) && flaskTag.getInt("Uses") < 4) {
					if(!player.getAbilities().instabuild) {
						other.shrink(1);
						player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
					}
					flaskTag.putInt("Uses", flaskTag.getInt("Uses") + 1);
					player.level.playSound(null, player, TFSounds.FLASK_FILL, player.getSoundSource(), flaskTag.getInt("Uses") * 0.33F, player.level.random.nextFloat() * 0.1F + 0.9F);
					return true;
				} else if(!flaskTag.contains("Potion")) {
					if(!player.getAbilities().instabuild) {
						other.shrink(1);
						player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
					}
					flaskTag.putString("Potion", potionTag.getString("Potion"));
					flaskTag.putInt("Uses", flaskTag.getInt("Uses") + 1);
					player.level.playSound(null, player, TFSounds.FLASK_FILL, player.getSoundSource(), flaskTag.getInt("Uses") * 0.33F, player.level.random.nextFloat() * 0.1F + 0.9F);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		CompoundTag tag = stack.getTag();

		if(tag != null && tag.contains("Potion") && tag.getString("Potion").equals(Potions.EMPTY.toString())) {
			return InteractionResultHolder.fail(player.getItemInHand(hand));
		}

		if(tag != null && tag.contains("Uses") && tag.getInt("Uses") > 0) {
			return ItemUtils.startUsingInstantly(level, player, hand);
		}

		return InteractionResultHolder.fail(player.getItemInHand(hand));
	}

	public int getUseDuration(ItemStack pStack) {
		return 32;
	}

	public UseAnim getUseAnimation(ItemStack pStack) {
		return UseAnim.DRINK;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		CompoundTag tag = stack.getOrCreateTag();
		if (entity instanceof Player player) {
			if (!level.isClientSide) {
				if(!player.isCreative()) addTowardsAdvancement(Potion.byName(tag.getString("Potion")), player);
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

			if(tag.getInt("Uses") <= 0) {
				tag.remove("Potion");
			}

			if (canBreak() && !player.getAbilities().instabuild) {
				if (tag.getInt("Uses") <= 0) {
					stack.shrink(1);
					level.playSound(null, player, TFSounds.BRITTLE_FLASK_BREAK, player.getSoundSource(), 1.5F, 0.7F);
				} else {
					tag.putInt("Breakage", tag.getInt("Breakage") + 1);
					tag.putBoolean("Refillable", false);
					level.playSound(null, player, TFSounds.BRITTLE_FLASK_CRACK, player.getSoundSource(), 1.5F, 2.0F);
				}
			}
		}
		return super.finishUsingItem(stack, level, entity);
	}

	private void addTowardsAdvancement(Potion potionDrank, Player drinker) {
		if(lastUsedPotion == null) {
			lastUsedPotion = Potions.EMPTY;
		}

		if (!lastUsedPotion.equals(potionDrank)) {
			timesUsed = 1;
			lastUsedPotion = potionDrank;
			advancementWindow = true;
		} else {
			timesUsed++;
		}

		if(drinker instanceof ServerPlayer player && drinker.isAlive() && advancementWindow) {
			TFAdvancements.DRINK_FROM_FLASK.trigger(player, timesUsed, lastUsedPotion);
		}
	}

	public static void ticker() {
		if(advancementWindow) seconds++;

		if(seconds == 8) {
			advancementWindow = false;
			timesUsed = 0;
			lastUsedPotion = null;
			seconds = 0;
		}
	}

	public boolean canBreak() {
		return true;
	}

	public boolean canBeRefilled(ItemStack stack) {
		return stack.getOrCreateTag().getBoolean("Refillable");
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
		tooltip.add(new TranslatableComponent("item.twilightforest.flask_doses", stack.getOrCreateTag().getInt("Uses"), 4).withStyle(ChatFormatting.GRAY));
		if(!stack.getOrCreateTag().getBoolean("Refillable")) tooltip.add(new TranslatableComponent("item.twilightforest.flask_no_refill").withStyle(ChatFormatting.RED));
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return Math.abs((double)stack.getOrCreateTag().getInt("Uses") - 4) / 4;
	}

	@Override
	public void fillItemCategory(CreativeModeTab tab, NonNullList<ItemStack> items) {
		if(allowdedIn(tab)) {
			ItemStack stack = new ItemStack(this);
			stack.getOrCreateTag().putInt("Uses", 0);
			stack.getOrCreateTag().putInt("Breakage", 0);
			stack.getOrCreateTag().putBoolean("Refillable", true);
			items.add(stack);
		}
	}
}
