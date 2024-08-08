package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.FastColor;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.level.Level;
import twilightforest.components.item.PotionFlaskComponent;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;
import twilightforest.init.TFSounds;

import java.util.List;

public class BrittleFlaskItem extends Item {

	public static final int DOSES = 3;

	public BrittleFlaskItem(Properties properties) {
		super(properties);
	}

	@Override
	public ItemStack getDefaultInstance() {
		ItemStack itemstack = super.getDefaultInstance();
		itemstack.set(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY);
		return itemstack;
	}

	@Override
	public boolean isBarVisible(ItemStack stack) {
		return stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY).potion() != PotionContents.EMPTY;
	}

	@Override
	public int getBarColor(ItemStack stack) {
		return FastColor.ARGB32.opaque(stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY).potion().getColor());
	}

	@Override
	public boolean overrideOtherStackedOnMe(ItemStack stack, ItemStack other, Slot slot, ClickAction action, Player player, SlotAccess access) {
		PotionFlaskComponent flaskContents = stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY);
		PotionContents potionContents = other.get(DataComponents.POTION_CONTENTS);

		if (potionContents != null) {
			if (action == ClickAction.SECONDARY && other.is(Items.POTION)) {
				if (potionContents != PotionContents.EMPTY && flaskContents.breakage() <= 0) {
					if (flaskContents.potion() == PotionContents.EMPTY || flaskContents.doses() < DOSES) {
						if (!player.getAbilities().instabuild) {
							other.shrink(1);
							player.getInventory().add(new ItemStack(Items.GLASS_BOTTLE));
						}
						stack.update(TFDataComponents.POTION_FLASK_CONTENTS, flaskContents, component -> component.tryAddDose(potionContents));
						player.playSound(TFSounds.FLASK_FILL.get(), (flaskContents.doses() + 1) * 0.25F, player.level().getRandom().nextFloat() * 0.1F + 0.9F);
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
		PotionFlaskComponent flaskContents = stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY);

		if (flaskContents.potion() == PotionContents.EMPTY) {
			return InteractionResultHolder.fail(player.getItemInHand(hand));
		}

		if (flaskContents.doses() > 0) {
			return ItemUtils.startUsingInstantly(level, player, hand);
		}

		return InteractionResultHolder.fail(player.getItemInHand(hand));
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 32;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity entity) {
		PotionFlaskComponent flaskContents = stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY);
		if (flaskContents.potion() != PotionContents.EMPTY) {
			if (entity instanceof Player player) {
				if (!level.isClientSide()) {
					if (!player.isCreative() && !player.isSpectator() && player instanceof ServerPlayer serverPlayer) {
						flaskContents.potion().potion().ifPresent(potion -> player.getData(TFDataAttachments.FLASK_DOSES).trackDrink(potion, serverPlayer));
					}
					for (MobEffectInstance mobeffectinstance : flaskContents.potion().getAllEffects()) {
						if (mobeffectinstance.getEffect().value().isInstantenous()) {
							mobeffectinstance.getEffect().value().applyInstantenousEffect(player, player, player, mobeffectinstance.getAmplifier(), 1.0D);
						} else {
							player.addEffect(new MobEffectInstance(mobeffectinstance));
						}
					}
				}
				player.awardStat(Stats.ITEM_USED.get(this));
				if (!player.getAbilities().instabuild) {
					stack.update(TFDataComponents.POTION_FLASK_CONTENTS, flaskContents, component -> {
						if (component.breakable() && !player.getAbilities().instabuild) {
							if (component.doses() <= 0) {
								stack.shrink(1);
								level.playSound(null, player, TFSounds.BRITTLE_FLASK_BREAK.get(), player.getSoundSource(), 1.5F, 0.7F);
							} else {
								level.playSound(null, player, TFSounds.BRITTLE_FLASK_CRACK.get(), player.getSoundSource(), 1.5F, 2.0F);
							}
						}
						return component.removeDose();
					});
				}
			}
		}
		return super.finishUsingItem(stack, level, entity);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		PotionFlaskComponent flaskContents = stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY);
		if (flaskContents.potion() != PotionContents.EMPTY) {
			flaskContents.potion().addPotionTooltip(tooltip::add, 1.0F, context.tickRate());
		}
		tooltip.add(Component.translatable("item.twilightforest.flask.doses", flaskContents.doses(), DOSES).withStyle(ChatFormatting.GRAY));
		if (flaskContents.breakage() > 0)
			tooltip.add(Component.translatable("item.twilightforest.flask.no_refill").withStyle(ChatFormatting.RED));
	}

	//copied from Item.getBarWidth, but reversed the "durability" check so it increments up, not down
	@Override
	public int getBarWidth(ItemStack stack) {
		return Math.round(13.0F - Math.abs(stack.getOrDefault(TFDataComponents.POTION_FLASK_CONTENTS, PotionFlaskComponent.EMPTY).doses() - DOSES) * 13.0F / DOSES);
	}
}