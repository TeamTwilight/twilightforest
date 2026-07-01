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
		if (entitiy == null) {
			// https://github.com/FTBTeam/FTB-Modpack-Issues/issues/12518
			// SimplySwords propagates entity=null if LivingEntity is NOT a player.
			// https://github.com/Sweenus/SimplySwords/blob/0bbb7cb0b4898c24b2425d3d5b29179c83deb4bf/common/src/main/java/net/sweenus/simplyswords/effect/SoulTetherEffect.java#L40
			return o;
		}
		return entity.getWeaponItem().getItem() instanceof CustomDamageProvider customDamageType ? customDamageType.getDamageSource(entity) : o;
	}

}
