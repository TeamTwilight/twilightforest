package twilightforest.events;

import net.minecraftforge.event.entity.living.LivingEvent;
import twilightforest.forge.attachment.Attachments;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.EntityTeleportEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.TickEvent;
import twilightforest.forge.beans.Component;
import twilightforest.forge.beans.PostConstruct;
import twilightforest.entity.IHostileMount;
import twilightforest.init.TFDamageTypes;
import twilightforest.init.TFDataAttachments;

@Component
public class HostileMountEvents {

	public static volatile boolean allowDismount = false;

	@PostConstruct
	private void setup() {
		MinecraftForge.EVENT_BUS.addListener(this::handleMountDamage);
		MinecraftForge.EVENT_BUS.addListener(this::preventTeleportingOffHostileMounts);
		MinecraftForge.EVENT_BUS.addListener(this::preventMountDismount);
		MinecraftForge.EVENT_BUS.addListener(this::preventHostilMountCrouching);
	}

	// Pas de @SubscribeEvent : la methode est branchee par addListener dans setup(), et le bus de
	// Forge rejette toute methode privee annotee des le chargement de la classe.
	private void handleMountDamage(LivingHurtEvent event) {
		LivingEntity living = event.getEntity();
		DamageSource damageSource = event.getSource();
		// lets not make the player take suffocation damage if riding something
		if (living instanceof Player && isRidingUnfriendly(living) && damageSource.is(DamageTypes.IN_WALL)) {
			event.setCanceled(true);
		}

		if (damageSource.is(DamageTypes.FALL) && Attachments.get(living, TFDataAttachments.YETI_THROWING).getThrown()) {
			float amount = event.getAmount();
			event.setCanceled(true);
			living.hurt(TFDamageTypes.getEntityDamageSource(living.level(), TFDamageTypes.YEETED, Attachments.get(living, TFDataAttachments.YETI_THROWING).getThrower()), amount);
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

	private void preventHostilMountCrouching(LivingEvent.LivingTickEvent event) {
		if (event.getEntity() instanceof IHostileMount)
			event.getEntity().getPassengers().forEach(e -> e.setShiftKeyDown(false));
	}

	public static boolean isRidingUnfriendly(LivingEntity entity) {
		return entity.isPassenger() && entity.getVehicle() instanceof IHostileMount;
	}
}
