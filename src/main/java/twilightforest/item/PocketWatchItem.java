package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jspecify.annotations.Nullable;

import java.util.function.Consumer;

public class PocketWatchItem extends Item {
	private static final MutableComponent TOOLTIP = Component.translatable("item.twilightforest.pocket_watch.desc").withStyle(ChatFormatting.GRAY);

	public PocketWatchItem(Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if (!level.isClientSide() && owner instanceof Player player) {
			if (slot == null) {
				for (int i = 0; i <= 8; i++) {
					if (player.getInventory().getItem(i).is(this)) {
						player.addEffect(new MobEffectInstance(MobEffects.SPEED, 5, 0, false, false, false));
						player.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 5, 0, false, false, false));
						break;
					}
				}
			}
			if (player.isHolding(this)) {
				player.addEffect(new MobEffectInstance(MobEffects.SPEED, 5, 0, false, false, false));
				player.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 5, 0, false, false, false));
				player.addEffect(new MobEffectInstance(MobEffects.HASTE, 5, 0, false, false, false));
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
		builder.accept(TOOLTIP);
	}
}
