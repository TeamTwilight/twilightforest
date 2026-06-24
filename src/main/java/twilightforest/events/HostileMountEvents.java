package twilightforest.events;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityMountEvent;
import net.neoforged.neoforge.event.entity.EntityTeleportEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import tamaized.beanification.Component;
import tamaized.beanification.PostConstruct;
import twilightforest.entity.IHostileMount;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFDataAttachments;

@Component
public class HostileMountEvents {

	public static volatile boolean allowDismount = false;

	@PostConstruct
	private void setup() {
		NeoForge.EVENT_BUS.addListener(this::handleMountDamage);
		NeoForge.EVENT_BUS.addListener(this::preventTeleportingOffHostileMounts);
		NeoForge.EVENT_BUS.addListener(this::preventMountDismount);
		NeoForge.EVENT_BUS.addListener(this::preventHostilMountCrouching);
	}

	@SubscribeEvent
	private void handleMountDamage(LivingIncomingDamageEvent event) {
		LivingEntity living = event.getEntity();
		DamageSource damageSource = event.getSource();
		// lets not make the player take suffocation damage if riding something
		if (living instanceof Player && isRidingUnfriendly(living) && damageSource.is(DamageTypes.IN_WALL)) {
			event.setCanceled(true);
		}

		if (damageSource.is(DamageTypes.FALL) && living.getData(TFDataAttachments.YETI_THROWING).getThrown()) {
			float amount = event.getAmount();
			event.setCanceled(true);
			living.hurt(TFDamageTypes.getEntityDamageSource(living.level(), TFDamageTypes.YEETED, living.getData(TFDataAttachments.YETI_THROWING).getThrower()), amount);
		}
	}

	private void preventTeleportingOffHostileMounts(EntityTeleportEvent event) {
		// if our grabbed target tries to teleport dont let them
		if (event.getEntity() instanceof LivingEntity living && isRidingUnfriendly(living)) {
			event.setCanceled(true);
		}
	}

	public static void hostileDismount(Entity rider) {
		HostileMountEvents.allowDismount = true;
		rider.stopRiding();
		HostileMountEvents.allowDismount = false;
	}

	private void preventMountDismount(EntityMountEvent event) {
		if (!event.getLevel().isClientSide() &&
			!event.isMounting() && event.getEntityBeingMounted().isAlive() &&
			event.getEntityMounting() instanceof Player player && player.isAlive() &&
			isRidingUnfriendly(player) && !allowDismount && !player.getAbilities().invulnerable)
			event.setCanceled(true);
	}

	private void preventHostilMountCrouching(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof IHostileMount)
			event.getEntity().getPassengers().forEach(e -> e.setShiftKeyDown(false));
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getVehicle() instanceof IHostileMount;
	}
}
