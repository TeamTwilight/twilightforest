package twilightforest.forge.network;

import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.world.entity.player.Player;

import java.util.concurrent.CompletableFuture;

/**
 * Le contexte de reception d'un paquet, sur le modele de NeoForge.
 *
 * <p>Forge fournit son propre contexte, dont l'expediteur n'est disponible que cote serveur. Les
 * trente classes de paquets du mod attendent la forme NeoForge : on la redonne ici plutot que de
 * les reecrire une par une.</p>
 */
public interface IPayloadContext {
	/** Repousse le travail sur le fil principal — un paquet arrive sur le fil reseau. */
	CompletableFuture<Void> enqueueWork(Runnable work);

	/** Le joueur au bout du fil : l'expediteur cote serveur, le joueur local cote client. */
	Player player();

	/** Le sens du paquet. Forge n'expose que le cote de reception, qui le determine. */
	PacketFlow flow();

	/** Recu par le client, donc envoye par le serveur. */
	default boolean isClientbound() {
		return this.flow() == PacketFlow.CLIENTBOUND;
	}

	/** Recu par le serveur, donc envoye par un client. */
	default boolean isServerbound() {
		return this.flow() == PacketFlow.SERVERBOUND;
	}
}
