package twilightforest.compat.tcon.traits;

import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.player.PlayerEvent;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class PrecipitateModifier extends NoLevelsModifier {

	@Override
	public void onBreakSpeed(IToolStackView tool, int level, PlayerEvent.BreakSpeed event, Direction sideHit, boolean isEffective, float miningSpeedModifier) {
		event.setNewSpeed(event.getNewSpeed() + (this.getBonusPercentage(event.getEntityLiving()) * event.getOriginalSpeed()));
	}

	private float getBonusPercentage(LivingEntity entity) {
		float maxHealth = entity.getMaxHealth();
		return (maxHealth - entity.getHealth()) / maxHealth;
	}
}
