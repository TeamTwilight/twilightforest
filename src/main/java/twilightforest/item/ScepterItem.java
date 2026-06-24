package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;
import twilightforest.enchantment.RechargeScepterEffect;
import twilightforest.init.TFEnchantments;

import java.util.function.Consumer;

public abstract class ScepterItem extends Item {

	public ScepterItem(Properties properties) {
		super(properties);
	}

	public abstract InteractionResult performScepterAction(Level level, ItemStack stack, Player player, InteractionHand hand);

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (stack.nextDamageWillBreak() && !player.isCreative()) {
			return InteractionResult.FAIL;
		}

		return this.performScepterAction(level, stack, player, hand);
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
		if (owner.tickCount % 20 == 0 && level instanceof ServerLevel serverLevel && stack.has(DataComponents.ENCHANTMENTS) && slot == null) {
			int renewal = stack.get(DataComponents.ENCHANTMENTS).getLevel(level.holderOrThrow(TFEnchantments.RENEWAL));
			if (renewal > 0) {
				RechargeScepterEffect.applyRecharge(serverLevel, stack, owner);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag flag) {
		builder.accept(Component.translatable("item.twilightforest.scepter.desc", stack.getMaxDamage() - stack.getDamageValue()).withStyle(ChatFormatting.GRAY));
	}
}
