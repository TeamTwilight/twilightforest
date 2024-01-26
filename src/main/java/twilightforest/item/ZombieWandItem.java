package twilightforest.item;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import twilightforest.entity.monster.LoyalZombie;
import twilightforest.init.TFEntities;
import twilightforest.init.TFSounds;

import java.util.List;

public class ZombieWandItem extends Item {

	public ZombieWandItem(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {

		ItemStack stack = player.getItemInHand(hand);

		if (stack.getDamageValue() == stack.getMaxDamage()) {
			return InteractionResultHolder.fail(stack);
		}

		if (!level.isClientSide()) {
			// what block is the player pointing at?
			BlockHitResult result = getPlayerPOVHitResult(level, player, ClipContext.Fluid.NONE);

			if (result.getType() != HitResult.Type.MISS) {
				LoyalZombie zombie = TFEntities.LOYAL_ZOMBIE.get().create(level);
				zombie.moveTo(result.getLocation());
				if (!level.noCollision(zombie, zombie.getBoundingBox())) {
					return InteractionResultHolder.pass(stack);
				}
				zombie.spawnAnim();
				zombie.setTame(true);
				zombie.setOwnerUUID(player.getUUID());
				zombie.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 1200, 1));
				level.addFreshEntity(zombie);
				level.gameEvent(player, GameEvent.ENTITY_PLACE, result.getBlockPos());

				stack.hurt(1, level.getRandom(), null);
				zombie.playSound(TFSounds.LOYAL_ZOMBIE_SUMMON.get(), 1.0F, zombie.getVoicePitch());
			}
		}

		return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}

	@Override
	public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
		return false;
	}

	@Override
	public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		tooltip.add(Component.translatable("item.twilightforest.scepter.desc", stack.getMaxDamage() - stack.getDamageValue()).withStyle(ChatFormatting.GRAY));
	}
}