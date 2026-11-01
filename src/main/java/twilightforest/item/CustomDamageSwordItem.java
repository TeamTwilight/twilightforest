package twilightforest.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.component.Tool;

public class CustomDamageSwordItem extends SwordItem implements CustomDamageProvider {

	private final ResourceKey<DamageType> damageType;

	public CustomDamageSwordItem(ResourceKey<DamageType> damageType, Tier tier, Properties properties) {
		super(tier, properties);
		this.damageType = damageType;
	}

	public CustomDamageSwordItem(ResourceKey<DamageType> damageType, Tier tier, Properties properties, Tool toolComponentData) {
		// PORTAGE : NeoForge accepte le composant d'outil en quatrieme parametre. Chez Forge il se
		// pose sur les proprietes, ce qui revient au meme puisque c'est la qu'il finit.
		super(tier, properties.component(net.minecraft.core.component.DataComponents.TOOL, toolComponentData));
		this.damageType = damageType;
	}

	@Override
	public DamageSource getDamageSource(LivingEntity attacker) {
		// PORTAGE : « DamageSources.source » est privee chez Mojang, NeoForge l'ouvre. Le
		// constructeur public de DamageSource fait la meme chose, a partir du meme registre.
		return new DamageSource(attacker.registryAccess()
			.registryOrThrow(net.minecraft.core.registries.Registries.DAMAGE_TYPE)
			.getHolderOrThrow(this.damageType), attacker);
	}
}
