package twilightforest.util.entities;

import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.player.Player;
import twilightforest.init.TFDataAttachments;

public class OminousFireDamageSource extends DamageSource {
	public OminousFireDamageSource(DamageSource wrappedSource) {
		super(wrappedSource.typeHolder(), wrappedSource.getEntity(), wrappedSource.getDirectEntity(), wrappedSource.getSourcePosition());
	}

	@Override
	public Component getLocalizedDeathMessage(LivingEntity living) {
		if (living.getKillCredit() instanceof Zombie zombie && zombie.hasData(TFDataAttachments.ZOMBIFIED_PLAYER)) {
			if (living instanceof Player player && player.getGameProfile().name().equals(zombie.getData(TFDataAttachments.ZOMBIFIED_PLAYER).name())) {
				return Component.translatable("death.attack.twilightforest.ominousFire.zombified_player.self", living.getDisplayName());
			}
			return Component.translatable("death.attack.twilightforest.ominousFire.zombified_player", living.getDisplayName(), zombie.getData(TFDataAttachments.ZOMBIFIED_PLAYER).name());
		}
		return super.getLocalizedDeathMessage(living);
	}
}
