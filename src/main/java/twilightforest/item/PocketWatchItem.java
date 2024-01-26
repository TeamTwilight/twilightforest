package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class PocketWatchItem extends Item {
    private static final MutableComponent TOOLTIP = Component.translatable("item.twilightforest.pocket_watch.desc").withStyle(ChatFormatting.GRAY);

    public PocketWatchItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean held) {
        if (entity instanceof LivingEntity living) {
            if (slot >= 0 && slot <= 8) {
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 0, 0, false, false, false));
            }

            if (living.getOffhandItem().is(this) || living.getMainHandItem().is(this)) {
                living.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 0, 0, false, false, false));

                if (living.hasEffect(MobEffects.DIG_SLOWDOWN)) {
                    living.removeEffect(MobEffects.DIG_SLOWDOWN);
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level getter, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(TOOLTIP);
    }
}
