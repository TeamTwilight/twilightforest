package twilightforest.events;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import twilightforest.beans.Component;
import twilightforest.beans.PostConstruct;
import twilightforest.init.TFDataAttachments;
import twilightforest.item.travellers_gear.TravellersArmorItem;
import twilightforest.item.travellers_gear.modifiers.TravellersModifiers;

@Component
public class TravellersArmorTickEventHandler {

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(this::playerTickPre);
		NeoForge.EVENT_BUS.addListener(this::levelTickPost);
	}

	private void playerTickPre(PlayerTickEvent.Pre event) {
		Player player = event.getEntity();
		Boolean hasDoubleJump = null;
		if (!TravellersModifiers.DOUBLE_JUMP_MODIFIER.isActive(player.getItemBySlot(EquipmentSlot.LEGS))) {
			hasDoubleJump = false;
		} else if (player.onGround())
			hasDoubleJump = true;

		if (hasDoubleJump != null && hasDoubleJump != player.getData(TFDataAttachments.HAS_DOUBLE_JUMP)) {
			player.setData(TFDataAttachments.HAS_DOUBLE_JUMP, hasDoubleJump);
			player.setData(TFDataAttachments.DOUBLE_JUMP_VALIDATOR, 0);
		}

		TravellersArmorItem.travellersWingsSidestepCooldownSound(player);
	}

	public void levelTickPost(LevelTickEvent.Post event) {
		Level level = event.getLevel();
		if (level instanceof ServerLevel serverLevel) {
			serverLevel.getEntities().getAll().forEach(entity -> {
				if (!(entity instanceof LivingEntity livingEntity))
					return;
				TravellersArmorItem.travellersWingsControlledFall(livingEntity);
				TravellersArmorItem.travellersVestHaste(livingEntity);
				TravellersArmorItem.travellersWingsHighJump(livingEntity);
				TravellersArmorItem.travellersGearAutoRepair(livingEntity);
				TravellersArmorItem.travellersBootsForwardBoost(livingEntity);
			});
		}
	}
}
