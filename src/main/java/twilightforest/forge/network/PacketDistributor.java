package twilightforest.forge.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import org.jetbrains.annotations.Nullable;

/**
 * L'envoi de paquets, sous la forme que le mod connait.
 *
 * <p>NeoForge offre des methodes statiques nommees par destinataire ; Forge designe une cible puis
 * la fait porter par le canal. La traduction est directe, et la faire ici evite de toucher aux
 * soixante-douze envois disperses dans le mod.</p>
 */
public final class PacketDistributor {
	private PacketDistributor() {}

	private static void send(CustomPacketPayload payload,
			net.minecraftforge.network.PacketDistributor.PacketTarget target) {
		TFNetwork.channel().send(payload, target);
	}

	public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
		send(payload, net.minecraftforge.network.PacketDistributor.PLAYER.with(player));
	}

	public static void sendToPlayersTrackingEntity(Entity entity, CustomPacketPayload payload) {
		send(payload, net.minecraftforge.network.PacketDistributor.TRACKING_ENTITY.with(entity));
	}

	public static void sendToPlayersTrackingEntityAndSelf(Entity entity, CustomPacketPayload payload) {
		send(payload, net.minecraftforge.network.PacketDistributor.TRACKING_ENTITY_AND_SELF.with(entity));
	}

	public static void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos pos, CustomPacketPayload payload) {
		send(payload, net.minecraftforge.network.PacketDistributor.TRACKING_CHUNK.with(level.getChunk(pos.x, pos.z)));
	}

	public static void sendToPlayersNear(ServerLevel level, @Nullable ServerPlayer excluded,
			double x, double y, double z, double radius, CustomPacketPayload payload) {
		send(payload, net.minecraftforge.network.PacketDistributor.NEAR.with(
				new net.minecraftforge.network.PacketDistributor.TargetPoint(
						excluded, x, y, z, radius, level.dimension())));
	}

	public static void sendToAllPlayers(CustomPacketPayload payload) {
		send(payload, net.minecraftforge.network.PacketDistributor.ALL.noArg());
	}

	public static void sendToServer(CustomPacketPayload payload) {
		send(payload, net.minecraftforge.network.PacketDistributor.SERVER.noArg());
	}
}
