package twilightforest.forge;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Un joueur factice, pour les actions du jeu qui en exigent un sans qu'il y ait de vrai joueur.
 *
 * <p>Forge 1.21.1 n'a plus de {@code FakePlayer} ; NeoForge si. Le mod en a besoin a un seul
 * endroit : la terre feconde, qui applique de la poudre d'os toute seule et dont la methode vanilla
 * reclame un joueur. On en fabrique donc un par monde, mis en cache.</p>
 */
public class TFFakePlayer extends ServerPlayer {
	private static final GameProfile PROFILE = new GameProfile(
			UUID.fromString("6c1a0a9c-9b53-4a3c-8f27-2b0f2a4a11d2"), "[TwilightForest]");

	@Nullable
	private static TFFakePlayer cached;

	private TFFakePlayer(ServerLevel level) {
		super(level.getServer(), level, PROFILE, CommonListenerCookie.createInitial(PROFILE, false).clientInformation());
	}

	/**
	 * Le joueur factice du monde donne.
	 *
	 * <p>Il est reutilise d'un appel a l'autre, et redirige vers le bon monde au besoin : en creer un
	 * a chaque usage ferait grossir la liste des entites pour rien.</p>
	 */
	public static Player get(ServerLevel level) {
		if (cached == null || cached.level() != level) {
			cached = new TFFakePlayer(level);
		}

		return cached;
	}

	@Override
	public boolean isSpectator() {
		return false;
	}

	@Override
	public boolean isCreative() {
		return false;
	}
}
