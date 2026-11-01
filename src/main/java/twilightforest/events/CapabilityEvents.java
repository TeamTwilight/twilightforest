package twilightforest.events;

import twilightforest.forge.PlayerPersistentData;
import net.minecraftforge.event.entity.living.LivingEvent;
import twilightforest.forge.attachment.Attachments;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.TickEvent;
import twilightforest.forge.beans.Component;
import twilightforest.forge.beans.PostConstruct;
import twilightforest.components.entity.FortificationShieldAttachment;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDimension;
import twilightforest.world.NoReturnTeleporter;
import twilightforest.world.TFTeleporter;

@Component
public class CapabilityEvents {

	@PostConstruct
	private void setup() {
		MinecraftForge.EVENT_BUS.addListener(this::updateShields);
		MinecraftForge.EVENT_BUS.addListener(this::updatePlayerCaps);
		MinecraftForge.EVENT_BUS.addListener(this::absorbShieldHits);
		MinecraftForge.EVENT_BUS.addListener(this::spawnInTFIfNecessary);
		MinecraftForge.EVENT_BUS.addListener(this::playerLogsIn);
	}

	private void updateShields(LivingEvent.LivingTickEvent event) {
		if (event.getEntity() instanceof LivingEntity living && !living.level().isClientSide() && Attachments.has(living, TFDataAttachments.FORTIFICATION_SHIELDS)) {
			Attachments.get(event.getEntity(), TFDataAttachments.FORTIFICATION_SHIELDS).tick(living);
		}
	}

	private void updatePlayerCaps(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}

		if (Attachments.get(event.player, TFDataAttachments.FEATHER_FAN)) {
			event.player.setIgnoreFallDamageFromCurrentImpulse(true);
			event.player.currentImpulseImpactPos = event.player.position();

			if (event.player.onGround() || event.player.isSwimming() || event.player.isInWater()) {
				Attachments.set(event.player, TFDataAttachments.FEATHER_FAN, false);
			}
		}
		Attachments.get(event.player, TFDataAttachments.YETI_THROWING).tick(event.player);
		Attachments.get(event.player, TFDataAttachments.TF_PORTAL_COOLDOWN).tick(event.player);
	}

	private void absorbShieldHits(LivingHurtEvent event) {
		LivingEntity living = event.getEntity();
		// shields
		if (!living.level().isClientSide() && !event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)) {
            FortificationShieldAttachment attachment = Attachments.get(living, TFDataAttachments.FORTIFICATION_SHIELDS);
			if (attachment.shieldsLeft() > 0) {
				if (living.invulnerableTime <= 0) {
					attachment.breakShield(living, false);
					FortificationShieldAttachment.addShieldBreakParticles(event.getSource(), living);
					living.invulnerableTime = living.invulnerableDuration;
				}
				event.setCanceled(true);
			}
		}
	}

	private void spawnInTFIfNecessary(PlayerEvent.PlayerRespawnEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

		if (serverPlayer.getRespawnPosition() == null) {
			newSpawnInTwilightForest(serverPlayer);
		}
	}

	/**
	 * When player logs in, report conflict status, set progression status
	 */
	public void playerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity().level().isClientSide() || !(event.getEntity() instanceof ServerPlayer player))
			return;
		dataFixLegacyBanish(player);
		if (!Attachments.has(player, TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST))
			newSpawnInTwilightForest(player);
	}

	private static void newSpawnInTwilightForest(ServerPlayer player) {
		if (!TFConfig.newPlayersSpawnInTF)
			return;
		ServerLevel level = player.getServer().getLevel(TFDimension.DIMENSION_KEY);
		if (level == null)
			return;

		BlockPos newDefaultSpawn = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, player.blockPosition());

		player.changeDimension(TFConfig.portalForNewPlayerSpawn ?
			TFTeleporter.createTransition(player, level, newDefaultSpawn, true) :
			NoReturnTeleporter.createNoPortalTransition(level, player, newDefaultSpawn));
		player.setRespawnPosition(TFDimension.DIMENSION_KEY, newDefaultSpawn, player.getYRot(), true, false);

		Attachments.set(player, TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
	}

	private static void dataFixLegacyBanish(ServerPlayer player) {
		CompoundTag tagCompound = player.getPersistentData();
		if (!tagCompound.contains(PlayerPersistentData.PERSISTED_NBT_TAG))
			return;
		CompoundTag playerData = tagCompound.getCompound(PlayerPersistentData.PERSISTED_NBT_TAG);
		if (!playerData.contains("twilightforest_banished"))
			return;

		playerData.remove("twilightforest_banished");
		tagCompound.put(PlayerPersistentData.PERSISTED_NBT_TAG, playerData);

		if (Attachments.has(player, TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST))
			return;

		Attachments.set(player, TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
	}
}
