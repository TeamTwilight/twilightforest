package twilightforest.forge.network;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

/**
 * Le joueur local.
 *
 * <p>Classe a part, et jamais chargee ailleurs que sur un client : {@code Minecraft} n'existe pas
 * sur un serveur dedie, et le simple fait de la nommer dans une classe commune suffirait a le faire
 * planter au chargement.</p>
 */
final class ClientPlayerAccess {
	private ClientPlayerAccess() {}

	@Nullable
	static Player player() {
		return Minecraft.getInstance().player;
	}
}
