package twilightforest.asmhooks;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import twilightforest.item.CustomDamageProvider;

@SuppressWarnings("unused")
public class DamageSourceHooks {

	/**
	 * {@link twilightforest.asm.transformers.damagesources.DamageSourcesTransformer} <p/>
	 * <p>
	 * Injection Points:<br/>
	 * {@link net.minecraft.world.damagesource.DamageSources#mobAttack(LivingEntity)}<br/>
	 * {@link net.minecraft.world.damagesource.DamageSources#playerAttack(Player)}
	 */
	public static DamageSource getCustomDamageSource(DamageSource o, LivingEntity entity) {
		return entity.getWeaponItem().getItem() instanceof CustomDamageProvider customDamageType ? customDamageType.getDamageSource(entity) : o;
	}

}
