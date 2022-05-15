package twilightforest.compat.tcon.traits;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class StalwartModifier extends NoLevelsModifier {

	@Override
	public int afterEntityHit(IToolStackView tool, int level, ToolAttackContext context, float damageDealt) {
		if(context.isCritical() && RANDOM.nextInt(10) == 0) {
			context.getAttacker().addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 200));
		}
		return 0;
	}
}
