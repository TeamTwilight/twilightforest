package twilightforest.events;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Unit;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import twilightforest.TwilightForestMod;
import twilightforest.components.entity.FortificationShieldAttachment;
import twilightforest.config.TFConfig;
import twilightforest.init.TFDataAttachments;
import twilightforest.init.TFDimension;
import twilightforest.network.UpdateShieldPacket;
import twilightforest.world.NoReturnTeleporter;
import twilightforest.world.TFTeleporter;

@EventBusSubscriber(modid = TwilightForestMod.ID)
public class CapabilityEvents {

	@SubscribeEvent
	public static void updateShields(EntityTickEvent.Post event) {
		if (event.getEntity() instanceof LivingEntity living && !living.level().isClientSide() && living.hasData(TFDataAttachments.FORTIFICATION_SHIELDS)) {
			event.getEntity().getData(TFDataAttachments.FORTIFICATION_SHIELDS).tick(living);
		}
	}

	@SubscribeEvent
	public static void updatePlayerCaps(PlayerTickEvent.Post event) {
		if (event.getEntity().getData(TFDataAttachments.FEATHER_FAN)) {
			event.getEntity().setIgnoreFallDamageFromCurrentImpulse(true);
			event.getEntity().currentImpulseImpactPos = event.getEntity().position();

			if (event.getEntity().onGround() || event.getEntity().isSwimming() || event.getEntity().isInWater()) {
				event.getEntity().setData(TFDataAttachments.FEATHER_FAN, false);
			}
		}
		event.getEntity().getData(TFDataAttachments.YETI_THROWING).tick(event.getEntity());
		event.getEntity().getData(TFDataAttachments.TF_PORTAL_COOLDOWN).tick(event.getEntity());
	}

	@SubscribeEvent
	public static void livingAttack(LivingIncomingDamageEvent event) {
		LivingEntity living = event.getEntity();
		// shields
		if (!living.level().isClientSide() && !event.getSource().is(DamageTypeTags.BYPASSES_ARMOR)) {
            FortificationShieldAttachment attachment = living.getData(TFDataAttachments.FORTIFICATION_SHIELDS);
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

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (!(event.getEntity() instanceof ServerPlayer serverPlayer)) return;

		if (serverPlayer.getRespawnPosition() == null) {
			newSpawnInTwilightForest(serverPlayer);
		}
	}

	/**
	 * When player logs in, report conflict status, set progression status
	 */
	@SubscribeEvent
	public static void playerLogsIn(PlayerEvent.PlayerLoggedInEvent event) {
		if (event.getEntity().level().isClientSide() || !(event.getEntity() instanceof ServerPlayer player))
			return;
		updateCapabilities(player, event.getEntity());
		dataFixLegacyBanish(player);
		if (!player.hasData(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST))
			newSpawnInTwilightForest(player);
	}

	@SubscribeEvent
	public static void playerPortals(PlayerEvent.PlayerChangedDimensionEvent event) {
		if (!event.getEntity().level().isClientSide() && event.getEntity() instanceof ServerPlayer player) {
			updateCapabilities(player, event.getEntity());
		}
	}

	@SubscribeEvent
	public static void onStartTracking(PlayerEvent.StartTracking event) {
		updateCapabilities((ServerPlayer) event.getEntity(), event.getTarget());
	}

	// send any capabilities that are needed client-side
	private static void updateCapabilities(ServerPlayer clientTarget, Entity shielded) {
		var attachment = shielded.getData(TFDataAttachments.FORTIFICATION_SHIELDS);
		if (attachment.shieldsLeft() > 0) {
			PacketDistributor.sendToPlayer(clientTarget, new UpdateShieldPacket(shielded.getId(), attachment.temporaryShieldsLeft(), attachment.permanentShieldsLeft()));
		}
	}

	private static void newSpawnInTwilightForest(ServerPlayer player) {
		if (!TFConfig.newPlayersSpawnInTF)
			return;
		ServerLevel level = player.getServer().getLevel(TFDimension.DIMENSION_KEY);
		if (level == null)
			return;

		BlockPos newDefaultSpawn = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, player.blockPosition());

		player.teleport(TFConfig.portalForNewPlayerSpawn ?
			TFTeleporter.createTransition(player, level, newDefaultSpawn, true) :
			NoReturnTeleporter.createNoPortalTransition(level, player, newDefaultSpawn));
		player.setRespawnPosition(TFDimension.DIMENSION_KEY, newDefaultSpawn, player.getYRot(), true, false);

		player.setData(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
	}

	private static void dataFixLegacyBanish(ServerPlayer player) {
		CompoundTag tagCompound = player.getPersistentData();
		if (!tagCompound.contains(Player.PERSISTED_NBT_TAG))
			return;
		CompoundTag playerData = tagCompound.getCompound(Player.PERSISTED_NBT_TAG);
		if (!playerData.contains("twilightforest_banished"))
			return;

		playerData.remove("twilightforest_banished");
		tagCompound.put(Player.PERSISTED_NBT_TAG, playerData);

		if (player.hasData(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST))
			return;

		player.setData(TFDataAttachments.BANISHED_TO_TWILIGHT_FOREST, Unit.INSTANCE);
	}
}
