package twilightforest.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nullable;
import java.util.List;

public class TripleBowItem extends BowItem {

	public TripleBowItem(Properties properties) {
		super(properties);
	}

	// Half [VanillaCopy]: copy of modified super to fire three arrows
	@Override
	public boolean releaseUsing(ItemStack stack, Level level, LivingEntity living, int timeLeft) {
		if (living instanceof Player player) {
			ItemStack arrowStack = player.getProjectile(stack);

			int i = this.getUseDuration(stack, player) - timeLeft;
			i = EventHooks.onArrowLoose(stack, level, player, i, !arrowStack.isEmpty());
			if (i < 0) return false;

			if (!arrowStack.isEmpty()) {
				float f = getPowerForTime(i);
				if (f >= 0.1D) {
					List<ItemStack> list = draw(stack, arrowStack, player);
					if (!level.isClientSide() && !list.isEmpty()) {
						this.shoot((ServerLevel) level, player, player.getUsedItemHand(), stack, list, f * 2.5F, 1.0F, f == 1.0F, null);
					}

					level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F / (level.getRandom().nextFloat() * 0.4F + 1.2F) + f * 0.5F);
					player.awardStat(Stats.ITEM_USED.get(this));
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected void shoot(ServerLevel level, LivingEntity living, InteractionHand hand, ItemStack stack, List<ItemStack> arrows, float speed, float accuracy, boolean crit, @Nullable LivingEntity target) {
		float f1 = arrows.size() == 1 ? 0.0F : 20.0F / (float)(arrows.size() - 1);
		float f2 = (float)((arrows.size() - 1) % 2) * f1 / 2.0F;
		float f3 = 1.0F;

		for (int i = 0; i < arrows.size(); i++) {
			ItemStack itemstack = arrows.get(i);
			if (!itemstack.isEmpty()) {
				float f4 = f2 + f3 * (float)((i + 1) / 2) * f1;
				f3 = -f3;
				stack.hurtAndBreak(this.getDurabilityUse(itemstack), living, LivingEntity.getSlotForHand(hand));

				for (int j = -1; j < 2; j++) {
					ItemStack copy = itemstack.copy();
					if (j != 0) copy.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
					Projectile projectile = this.createProjectile(level, living, stack, copy, crit);
					this.shootProjectile(living, projectile, i, speed, accuracy, f4, target);
					projectile.setDeltaMovement(projectile.getDeltaMovement().add(0.0D, 0.0075D * 20D * j, 0.0D));
					level.addFreshEntity(projectile);
				}
			}
		}
	}
}