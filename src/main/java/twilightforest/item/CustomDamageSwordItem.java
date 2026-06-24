package twilightforest.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Tool;

//TODO data component-itize this
public class CustomDamageSwordItem extends Item implements CustomDamageProvider {

	private final ResourceKey<DamageType> damageType;

	public CustomDamageSwordItem(ResourceKey<DamageType> damageType, Properties properties) {
		super(properties);
		this.damageType = damageType;
	}

	public CustomDamageSwordItem(ResourceKey<DamageType> damageType, Tier tier, Properties properties, Tool toolComponentData) {
		super(tier, properties, toolComponentData);
		this.damageType = damageType;
	}

	@Override
	public DamageSource getDamageSource(LivingEntity attacker) {
		return attacker.damageSources().source(this.damageType, attacker);
	}
}
