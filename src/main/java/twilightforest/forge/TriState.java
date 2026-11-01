package twilightforest.forge;

/**
 * Oui, non, ou « laisse decider le jeu ».
 *
 * <p>NeoForge fournit ce type ; Forge non. Il sert au mod pour repondre a des questions ou
 * l'absence de reponse est une reponse valable — un bloc qui ne se prononce pas sur la survie
 * d'une plante laisse le comportement vanilla s'appliquer.</p>
 */
public enum TriState {
	TRUE,
	FALSE,
	DEFAULT;

	public boolean isTrue() {
		return this == TRUE;
	}

	public boolean isFalse() {
		return this == FALSE;
	}

	public boolean isDefault() {
		return this == DEFAULT;
	}

	/** La valeur booleenne, en retombant sur {@code fallback} quand rien n'est decide. */
	public boolean toBoolean(boolean fallback) {
		return this == DEFAULT ? fallback : this == TRUE;
	}

	public static TriState of(boolean value) {
		return value ? TRUE : FALSE;
	}
}
