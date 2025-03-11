package twilightforest.events;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import twilightforest.beans.Component;
import twilightforest.beans.PostConstruct;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDataComponents;

@Component
public class TravellersArmorTickEventHandler {

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(this::playerTickPre);
	}

	private void playerTickPre(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		Boolean hasDoubleJump = null;
		Boolean hasDoubleJumpAbility = player.getItemBySlot(EquipmentSlot.LEGS).get(TFDataComponents.HAS_DOUBLE_JUMP);
		if (!Boolean.TRUE.equals(hasDoubleJumpAbility)) {
			hasDoubleJump = false;
		} else if (player.onGround() || player.mayFly())
			hasDoubleJump = true;

		if (hasDoubleJump != null && hasDoubleJump != player.getData(TFDataAttachments.HAS_DOUBLE_JUMP)) {
			player.setData(TFDataAttachments.HAS_DOUBLE_JUMP, hasDoubleJump);
		}
	}

}
