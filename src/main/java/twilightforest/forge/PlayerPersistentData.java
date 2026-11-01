package twilightforest.forge;

/**
 * La cle du sous-arbre NBT qui survit a la mort du joueur.
 *
 * <p>NeoForge l'expose en constante sur {@code Player} ; Forge non, alors que le mecanisme existe
 * des deux cotes avec le meme nom de balise. On la redeclare donc, a l'identique.</p>
 */
public final class PlayerPersistentData {
	private PlayerPersistentData() {}

	public static final String PERSISTED_NBT_TAG = "PlayerPersisted";
}
