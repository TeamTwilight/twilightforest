package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import twilightforest.util.EffectUtil;

import java.util.List;

public class PocketWatchItem extends Item {
	private static final MutableComponent TOOLTIP = Component.translatable("item.twilightforest.pocket_watch.desc").withStyle(ChatFormatting.GRAY);

	public PocketWatchItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean held) {
		if (!level.isClientSide && entity instanceof LivingEntity living) {
			if ((slot >= 0 && slot <= 8) || slot == Inventory.SLOT_OFFHAND) {
				// Not addEffect every tick: that forces an effect + attribute resync every tick (see EffectUtil).
				EffectUtil.maintainHiddenEffect(living, MobEffects.MOVEMENT_SPEED, 0, 40, 20);
				EffectUtil.maintainHiddenEffect(living, MobEffects.JUMP, 0, 40, 20);
			}

			if (living.isHolding(this)) {
				EffectUtil.maintainHiddenEffect(living, MobEffects.DIG_SPEED, 0, 40, 20);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		tooltip.add(TOOLTIP);
	}
}
