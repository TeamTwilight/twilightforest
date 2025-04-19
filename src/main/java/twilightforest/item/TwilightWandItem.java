package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import twilightforest.enchantment.RechargeScepterEffect;
import twilightforest.entity.projectile.MoonwormShot;
import twilightforest.entity.projectile.TwilightWandBolt;
import twilightforest.init.TFEnchantments;
import twilightforest.init.TFItems;
import twilightforest.init.TFSounds;
import twilightforest.util.TFItemStackUtils;

import java.util.List;

public class TwilightWandItem extends Item {

	public TwilightWandItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (TFItemStackUtils.isAtZeroDurability(stack) && !player.getAbilities().instabuild) {
			return InteractionResult.FAIL;
		} else {
			if (level instanceof ServerLevel serverLevel) {
				level.playSound(null, player.blockPosition(), TFSounds.TWILIGHT_SCEPTER_USE.get(), SoundSource.PLAYERS, 1.0F, (level.getRandom().nextFloat() - level.getRandom().nextFloat()) * 0.2F + 1.0F);
				Projectile.spawnProjectileFromRotation((lev, owner, stacc) -> new TwilightWandBolt(lev, owner), serverLevel, stack, player, 0.0F, 1.5F, 1.0F);

				if (!player.getAbilities().instabuild && (!player.getItemBySlot(EquipmentSlot.HEAD).is(TFItems.MYSTIC_CROWN) || level.getRandom().nextFloat() > 0.05f)) {
					TFItemStackUtils.hurtWithoutBreaking(stack, 1, player);
				}
			}

			return InteractionResult.SUCCESS;
		}
	}

	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
		if (entity.tickCount % 20 == 0 && level instanceof ServerLevel serverLevel && stack.has(DataComponents.ENCHANTMENTS) && !isSelected) {
			int renewal = stack.get(DataComponents.ENCHANTMENTS).getLevel(level.holderOrThrow(TFEnchantments.RENEWAL));
			if (renewal > 0) {
				RechargeScepterEffect.applyRecharge(serverLevel, stack, entity);
			}
		}
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		tooltip.add(Component.translatable("item.twilightforest.scepter.desc", stack.getMaxDamage() - stack.getDamageValue()).withStyle(ChatFormatting.GRAY));
	}
}