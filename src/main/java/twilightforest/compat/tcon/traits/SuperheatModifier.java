package twilightforest.compat.tcon.traits;

import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

//return of an old tinkers modifier
public class SuperheatModifier extends NoLevelsModifier {

	@Override
	public float getEntityDamage(IToolStackView tool, int level, ToolAttackContext context, float baseDamage, float damage) {
		return context.getLivingTarget() != null && context.getLivingTarget().isOnFire() ? damage + (damage * 0.35F) : damage;
	}
}
