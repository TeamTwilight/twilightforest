package twilightforest.item;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

public interface CustomDamageProvider {
	DamageSource getDamageSource(LivingEntity attacker);
}
