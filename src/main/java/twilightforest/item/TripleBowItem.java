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
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.EventHooks;

import javax.annotation.Nullable;
import java.util.List;

public class TripleBowItem extends BowItem {

	public TripleBowItem(Properties properties) {
		super(properties);
	}

	//[VanillaCopy] of super: edits noted
	@Override
	protected void shoot(ServerLevel level, LivingEntity shooter, InteractionHand hand, ItemStack weapon, List<ItemStack> projectiles, float velocity, float inaccuracy, boolean crit, @Nullable LivingEntity target) {
		float f = EnchantmentHelper.processProjectileSpread(level, weapon, shooter, 0.0F);
		float f1 = projectiles.size() == 1 ? 0.0F : 2.0F * f / (float)(projectiles.size() - 1);
		float f2 = (float)((projectiles.size() - 1) % 2) * f1 / 2.0F;
		float f3 = 1.0F;

		for (int i = 0; i < projectiles.size(); i++) {
			ItemStack itemstack = projectiles.get(i);
			if (!itemstack.isEmpty()) {
				float f4 = f2 + f3 * (float)((i + 1) / 2) * f1;
				f3 = -f3;

				//TF: modify to always shoot a row of 3 arrows
				for (int j = -1; j < 2; j++) {
					ItemStack copy = itemstack.copy();
					//TF: set all projectiles except the middle to intangible so people cant dupe arrows
					if (i != 0 || j != 0) copy.set(DataComponents.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
					Projectile projectile = this.createProjectile(level, shooter, weapon, copy, crit);
					this.shootProjectile(shooter, projectile, i, velocity, inaccuracy, f4, target);
					projectile.setDeltaMovement(projectile.getDeltaMovement().add(0.0D, 0.0075D * 20D * j, 0.0D));
					level.addFreshEntity(projectile);
					projectile.applyOnProjectileSpawned(level, itemstack);
				}

				weapon.hurtAndBreak(this.getDurabilityUse(itemstack), shooter, LivingEntity.getSlotForHand(hand));
				if (weapon.isEmpty()) {
					break;
				}
			}
		}
	}
}