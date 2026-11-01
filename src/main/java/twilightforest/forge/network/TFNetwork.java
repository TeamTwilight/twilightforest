package twilightforest.forge.network;

import io.netty.buffer.Unpooled;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.ForgePayload;
import net.minecraftforge.network.payload.PayloadFlow;
import net.minecraftforge.network.payload.PayloadProtocol;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Le canal reseau du mod, et l'enregistrement de ses paquets.
 *
 * <p>Les deux chargeurs utilisent le systeme moderne de charges utiles, mais s'y prennent
 * differemment : NeoForge donne un « registrar » avec des methodes par sens
 * ({@code playToClient}...), Forge fait construire un canal par un enchainement
 * {@code protocole → sens → paquets}. Cette classe reprend la forme NeoForge par-dessus celle de
 * Forge, ce qui laisse les trente classes de paquets du mod intactes.</p>
 */
public final class TFNetwork {
	private static final int PROTOCOL_VERSION = 1;

	@Nullable
	private static Channel<CustomPacketPayload> channel;

	/** Codec de chaque paquet declare, pour {@link #toClient} qui encode hors du canal. */
	private static final Map<ResourceLocation, StreamCodec<RegistryFriendlyByteBuf, ?>> CODECS = new HashMap<>();

	private final PayloadProtocol<RegistryFriendlyByteBuf, CustomPacketPayload> play;

	@Nullable
	private PayloadFlow<RegistryFriendlyByteBuf, CustomPacketPayload> toClient;
	@Nullable
	private PayloadFlow<RegistryFriendlyByteBuf, CustomPacketPayload> toServer;
	@Nullable
	private PayloadFlow<RegistryFriendlyByteBuf, CustomPacketPayload> both;

	private TFNetwork(ResourceLocation id) {
		// « optional » laisse un client sans le mod se connecter a un serveur qui l'a, et
		// reciproquement : c'est ce que demandait la declaration d'origine.
		this.play = ChannelBuilder.named(id)
				.networkProtocolVersion(PROTOCOL_VERSION)
				.optional()
				.payloadChannel()
				.play();
	}

	public static TFNetwork registrar(String modId) {
		return new TFNetwork(ResourceLocation.fromNamespaceAndPath(modId, "main"));
	}

	public <T extends CustomPacketPayload> TFNetwork playToClient(
			CustomPacketPayload.Type<T> type,
			StreamCodec<RegistryFriendlyByteBuf, T> codec,
			BiConsumer<T, IPayloadContext> handler) {
		CODECS.put(type.id(), codec);
		this.toClient = (this.toClient == null ? this.play.clientbound() : this.toClient)
				.add(type, codec, adapt(handler));
		return this;
	}

	public <T extends CustomPacketPayload> TFNetwork playToServer(
			CustomPacketPayload.Type<T> type,
			StreamCodec<RegistryFriendlyByteBuf, T> codec,
			BiConsumer<T, IPayloadContext> handler) {
		CODECS.put(type.id(), codec);
		this.toServer = (this.toServer == null ? this.play.serverbound() : this.toServer)
				.add(type, codec, adapt(handler));
		return this;
	}

	public <T extends CustomPacketPayload> TFNetwork playBidirectional(
			CustomPacketPayload.Type<T> type,
			StreamCodec<RegistryFriendlyByteBuf, T> codec,
			BiConsumer<T, IPayloadContext> handler) {
		CODECS.put(type.id(), codec);
		this.both = (this.both == null ? this.play.bidirectional() : this.both)
				.add(type, codec, adapt(handler));
		return this;
	}

	/** Ferme la declaration et retient le canal, dont dependent tous les envois. */
	public void build() {
		PayloadFlow<RegistryFriendlyByteBuf, CustomPacketPayload> last =
				this.both != null ? this.both : (this.toServer != null ? this.toServer : this.toClient);

		if (last == null) {
			throw new IllegalStateException("Canal reseau construit sans aucun paquet");
		}

		channel = last.build();
	}

	static Channel<CustomPacketPayload> channel() {
		if (channel == null) {
			throw new IllegalStateException("Canal reseau pas encore construit");
		}

		return channel;
	}

	private static <T extends CustomPacketPayload> BiConsumer<T, CustomPayloadEvent.Context> adapt(
			BiConsumer<T, IPayloadContext> handler) {
		return (msg, ctx) -> {
			handler.accept(msg, new ForgeContext(ctx));
			// NeoForge considere tout paquet recu comme traite. Forge non : sans ce drapeau, il rend
			// le paquet au jeu, qui le reprogramme sur le fil principal et le fait decoder une
			// seconde fois dans un tampon deja lu (EOFException, puis plantage du client).
			ctx.setPacketHandled(true);
		};
	}

	private record ForgeContext(CustomPayloadEvent.Context ctx) implements IPayloadContext {
		@Override
		public CompletableFuture<Void> enqueueWork(Runnable work) {
			return this.ctx.enqueueWork(work);
		}

		@Override
		public PacketFlow flow() {
			// Recu cote client : le paquet venait du serveur, et reciproquement.
			return this.ctx.isClientSide() ? PacketFlow.CLIENTBOUND : PacketFlow.SERVERBOUND;
		}

		@Override
		public Player player() {
			ServerPlayer sender = this.ctx.getSender();

			if (sender != null) {
				return sender;
			}

			// Pas d'expediteur : le paquet a ete recu par le client, le joueur concerne est le sien.
			return FMLEnvironment.dist == Dist.CLIENT ? ClientPlayerAccess.player() : null;
		}
	}

	/**
	 * Un paquet vanilla pret a partir vers un client, pour les rares endroits ou le jeu attend un
	 * paquet plutot qu'un envoi (mise a jour des cartes).
	 *
	 * <p>NeoForge sait encoder une charge utile emballee telle quelle ; Forge non : il n'encode que
	 * ses propres {@code ForgePayload}, sinon il coupe la connexion (ClassCastException a l'envoi).
	 * La charge est donc encodee ici avec son codec, dans un tampon qui porte les registres du
	 * monde, comme le fait le canal.</p>
	 */
	@SuppressWarnings("unchecked")
	public static net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket toClient(
			CustomPacketPayload payload, RegistryAccess registries) {
		ResourceLocation id = payload.type().id();
		StreamCodec<RegistryFriendlyByteBuf, CustomPacketPayload> codec =
				(StreamCodec<RegistryFriendlyByteBuf, CustomPacketPayload>) CODECS.get(id);
		if (codec == null) {
			throw new IllegalArgumentException("Paquet non declare sur le canal : " + id);
		}
		RegistryFriendlyByteBuf buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), registries);
		codec.encode(buf, payload);
		return new net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket(ForgePayload.create(id, buf));
	}
}
