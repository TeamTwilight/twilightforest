package twilightforest.compat.tcon.traits;

import net.minecraft.core.Direction;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;
import twilightforest.world.registration.TFGenerationSettings;

public class TwilitModifier extends NoLevelsModifier {
	@Override
	public float getEntityDamage(IToolStackView tool, int level, ToolAttackContext context, float baseDamage, float damage) {
		return !context.getAttacker().level.dimension().equals(TFGenerationSettings.DIMENSION_KEY) ? damage + 2.0F : damage;
	}

	@Override
	public void onBreakSpeed(IToolStackView tool, int level, PlayerEvent.BreakSpeed event, Direction sideHit, boolean isEffective, float miningSpeedModifier) {
		if(event.getEntityLiving().level.dimension().equals(TFGenerationSettings.DIMENSION_KEY)) {
			event.setNewSpeed(event.getNewSpeed() + 2.0F);
		}
	}
}
